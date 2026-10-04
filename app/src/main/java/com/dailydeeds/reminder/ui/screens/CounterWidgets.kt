package com.dailydeeds.reminder.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.model.DeedType
import com.dailydeeds.reminder.model.Deed

@Composable
internal fun CounterStageRow(
    deed: Deed,
    currentStage: Int,
    isCompleted: Boolean
) {
    // Multi-Stage Pills (for Fatima's Tasbeeh)
    if (deed.type == DeedType.MULTI_STAGE_COUNTER && deed.stages != null) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "مراحل التسبيح المبارك (١٠٠ تسبيحة):",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    deed.stages.forEachIndexed { index, stage ->
                        val isCurrent = currentStage == index
                        val isDone = currentStage > index || isCompleted
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isDone -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                                        isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    }
                                )
                                .border(
                                    width = if (isCurrent) 1.5.dp else 1.dp,
                                    color = when {
                                        isDone -> MaterialTheme.colorScheme.tertiary
                                        isCurrent -> MaterialTheme.colorScheme.secondary
                                        else -> Color.Transparent
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stage.phrase,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.5.sp
                                    ),
                                    maxLines = 1,
                                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${stage.targetCount}x",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun CounterOrb(
    deed: Deed,
    currentCount: Int,
    isCompleted: Boolean,
    onIncrement: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val orbScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = tween(120),
        label = "OrbScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(240.dp)
            .scale(orbScale)
            .clip(CircleShape)
            .background(
                if (isCompleted) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.primary
            )
            .border(
                width = 5.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true, color = MaterialTheme.colorScheme.secondary)
            ) {
                onIncrement()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$currentCount",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 56.sp
                ),
                color = (if (isCompleted) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary)
            )
            Text(
                text = "من ${deed.targetCount}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = if (isCompleted) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary
            )
            if (!isCompleted) {
                Text(
                    text = "بقي: ${deed.targetCount - currentCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        color = (if (isCompleted) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary).copy(alpha = 0.75f)
                    )
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(18.dp))
}
