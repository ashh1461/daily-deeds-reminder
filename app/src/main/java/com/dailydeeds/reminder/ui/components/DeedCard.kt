package com.dailydeeds.reminder.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.Deed
import com.dailydeeds.reminder.model.DeedType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeedCard(
    deed: Deed,
    isCompleted: Boolean,
    currentCount: Int,
    onClick: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        label = "BorderColor"
    )
    val progress = if (deed.targetCount > 0) (currentCount.toFloat() / deed.targetCount.toFloat()).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "Progress")

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Interactive Status Badge Button
            FilledTonalIconToggleButton(
                checked = isCompleted,
                onCheckedChange = { onToggleCompleted() },
                modifier = Modifier.size(48.dp).semantics {
                    contentDescription = "إتمام ${deed.title}"
                    stateDescription = if (isCompleted) "مكتمل" else "غير مكتمل"
                }
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null)
                } else {
                    Text("${deed.id}", style = MaterialTheme.typography.titleMedium)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(deed.category.titleArabic, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Text(deed.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(deed.subtitle, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (deed.type != DeedType.READING) {
                    Text("$currentCount / ${deed.targetCount}", style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary)
                }
                if (deed.type != DeedType.READING && deed.targetCount > 1) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}
