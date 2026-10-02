package com.dailydeeds.reminder.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.model.DeedType
import com.dailydeeds.reminder.ui.theme.GoldDark
import com.dailydeeds.reminder.ui.theme.GoldLight
import com.dailydeeds.reminder.ui.theme.GoldPrimary
import com.dailydeeds.reminder.ui.theme.NavyDark
import com.dailydeeds.reminder.ui.theme.NavyPrimary
import com.dailydeeds.reminder.ui.theme.NavySecondary
import com.dailydeeds.reminder.ui.theme.SuccessGreen
import com.dailydeeds.reminder.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderCounterScreen(
    deedId: Int,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val deed = DeedsRepository.getDeedById(deedId) ?: run {
        onNavigateBack()
        return
    }

    val completedMap by viewModel.completedMap.collectAsState()
    val countsMap by viewModel.countsMap.collectAsState()
    val stagesMap by viewModel.stagesMap.collectAsState()

    val isCompleted = completedMap[deedId] ?: false
    val currentCount = countsMap[deedId] ?: 0
    val currentStage = stagesMap[deedId] ?: 0

    var readerFontSize by remember { mutableFloatStateOf(21f) }
    var useAlternativeVariant by remember { mutableStateOf(false) }

    val activeContent = if (useAlternativeVariant && deed.alternativeContent != null) {
        deed.alternativeContent
    } else {
        deed.content
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = deed.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            maxLines = 1,
                            color = Color.White
                        )
                        Text(
                            text = deed.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = GoldLight.copy(alpha = 0.9f)
                            ),
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleDeedCompleted(deedId) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = if (isCompleted) "مكتمل (اضغط للإلغاء)" else "تحديد كـ مكتمل",
                            tint = if (isCompleted) GoldPrimary else Color.White
                        )
                    }
                    if (deed.type != DeedType.READING) {
                        IconButton(onClick = { viewModel.resetDeedCount(deedId) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "إعادة ضبط العداد",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (deed.type == DeedType.READING) {
            // ==========================================
            // READING MODE (Quranic Verses & Sacred Duas)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Font Size & Actions Toolbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyPrimary.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = deed.category.titleArabic,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                    }

                    // Font Size Adjusters
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "حجم الخط: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = { if (readerFontSize > 16f) readerFontSize -= 2f },
                            modifier = Modifier.padding(horizontal = 3.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(text = "A-", style = MaterialTheme.typography.labelSmall)
                        }
                        OutlinedButton(
                            onClick = { if (readerFontSize < 34f) readerFontSize += 2f },
                            modifier = Modifier.padding(horizontal = 3.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(text = "A+", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Alternative Narration Switcher (e.g. Deed 9: لا يؤاخذ vs لا يأخذ)
                if (deed.alternativeContent != null) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = !useAlternativeVariant,
                                onClick = { useAlternativeVariant = false },
                                label = { Text("الرواية المشهورة الأولى") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                border = if (!useAlternativeVariant) {
                                    FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = true,
                                        selectedBorderColor = GoldPrimary
                                    )
                                } else null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = useAlternativeVariant,
                                onClick = { useAlternativeVariant = true },
                                label = { Text(deed.alternativeLabel ?: "الرواية الأخرى") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                border = if (useAlternativeVariant) {
                                    FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = true,
                                        selectedBorderColor = GoldPrimary
                                    )
                                } else null
                            )
                        }
                    }
                }

                // Ornate Islamic Frame Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.5.dp,
                            brush = Brush.verticalGradient(
                                listOf(GoldPrimary, GoldLight, GoldDark, GoldPrimary)
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header Ornament
                        Text(
                            text = deed.headerOrnament ?: "۞ بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۞",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = GoldPrimary
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Decorative Divider
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(GoldPrimary.copy(alpha = 0.35f))
                            )
                            Text(
                                text = " ✤ ✦ ✤ ",
                                color = GoldPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(1.dp)
                                    .background(GoldPrimary.copy(alpha = 0.35f))
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Sacred Text Presentation
                        Text(
                            text = activeContent,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = readerFontSize.sp,
                                lineHeight = (readerFontSize * 1.9f).sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Closing Divider
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(GoldPrimary.copy(alpha = 0.4f))
                        )
                    }
                }

                // Reference Source & Virtue Card
                if (!deed.instructions.isNullOrBlank() || !deed.referenceSource.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = GoldPrimary.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "المصدر والفضل المأثور",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    ),
                                    color = GoldDark
                                )
                            }

                            if (!deed.instructions.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = deed.instructions,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.5.sp,
                                        lineHeight = 22.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (!deed.referenceSource.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "المصادر المعتمدة: ${deed.referenceSource}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = NavyPrimary.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Direct Completion Button
                Button(
                    onClick = { viewModel.toggleDeedCompleted(deedId) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) SuccessGreen else NavyPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isCompleted) "أُنجزت القراءة بحمد الله ✓ (اضغط للإلغاء)" else "إتمام القراءة وتسجيل الإنجاز",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = Color.White
                    )
                }
            }
        } else {
            // ==========================================
            // COUNTER MODE (Digital Tasbeeh & Prayers)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
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
                            .border(1.dp, GoldPrimary.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "مراحل التسبيح المبارك (١٠٠ تسبيحة):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldDark
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
                                                    isDone -> SuccessGreen.copy(alpha = 0.15f)
                                                    isCurrent -> NavyPrimary.copy(alpha = 0.12f)
                                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                                }
                                            )
                                            .border(
                                                width = if (isCurrent) 1.5.dp else 1.dp,
                                                color = when {
                                                    isDone -> SuccessGreen
                                                    isCurrent -> GoldPrimary
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
                                                color = if (isCurrent) NavyPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${stage.targetCount}x",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = GoldDark
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Active Phrase Header
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = deed.headerOrnament ?: "۞ اَلذِّكْرُ الْمُبَارَكُ ۞",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (deed.type == DeedType.MULTI_STAGE_COUNTER && deed.stages != null) {
                            val stageInfo = deed.stages.getOrNull(currentStage) ?: deed.stages.last()
                            Text(
                                text = stageInfo.phrase,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 32.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = deed.content,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    lineHeight = 36.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Tactile Glowing Tasbeeh Orb
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
                            Brush.radialGradient(
                                colors = if (isCompleted) {
                                    listOf(SuccessGreen, SuccessGreen.copy(alpha = 0.85f), Color(0xFF1B5E20))
                                } else {
                                    listOf(NavySecondary, NavyPrimary, NavyDark)
                                }
                            )
                        )
                        .border(
                            width = 5.dp,
                            brush = Brush.sweepGradient(
                                listOf(GoldPrimary, GoldLight, GoldDark, GoldLight, GoldPrimary)
                            ),
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = rememberRipple(bounded = true, color = GoldLight)
                        ) {
                            viewModel.incrementDeedCount(deedId)
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
                            color = Color.White
                        )
                        Text(
                            text = "من ${deed.targetCount}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = GoldLight
                        )
                        if (!isCompleted) {
                            Text(
                                text = "بقي: ${deed.targetCount - currentCount}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Status & Quick Action Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isCompleted) {
                        Text(
                            text = "تم إكمال هذا العمل المبارك لليوم بنجاح تقبل الله منا ومنكم 🌿",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            ),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "المس الدائرة للعدّ، أو اضغط الزر أدناه لتحديده كمكتمل فوراً",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.toggleDeedCompleted(deedId) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) SuccessGreen else NavyPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCompleted) "أُنجز بحمد الله ✓ (اضغط لإلغاء التحديد)" else "تحديد كـ مكتمل مباشرة دون نقر",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.resetDeedCount(deedId) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "تصفير العداد والبدء من جديد")
                    }
                }
            }
        }
    }
}
