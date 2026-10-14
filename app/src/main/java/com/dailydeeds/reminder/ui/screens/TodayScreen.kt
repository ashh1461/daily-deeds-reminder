package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.ui.components.CircularProgressBar
import com.dailydeeds.reminder.ui.components.DeedCard
import com.dailydeeds.reminder.ui.components.FilterChipRow
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.MainViewModel
import com.dailydeeds.reminder.viewmodel.ToolsViewModel

/**
 * The "اليوم" tab: hero, today's prayers, today's dua and ziyarah, resume tiles and then the daily deeds.
 * Everything that is not about today lives in the other tabs or behind the shared top bar.
 */
@Composable
fun TodayScreen(
    viewModel: MainViewModel,
    tools: ToolsViewModel,
    onOpenDeed: (Int) -> Unit,
    onOpenRoute: (String) -> Unit,
    onResumeSurah: (Int) -> Unit,
    onResumeMafatih: (String) -> Unit,
    onResumeSahifa: (String) -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val completedMap by viewModel.completedMap.collectAsState()
    val countsMap by viewModel.countsMap.collectAsState()
    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val activeDate by viewModel.activeDate.collectAsState()
    val deeds = remember(selectedCategory, activeDate) {
        DeedsRepository.getDeedsByCategory(selectedCategory, activeDate)
    }
    var menuOpen by remember { mutableStateOf(false) }
    var confirmReset by remember { mutableStateOf(false) }
    val side = Modifier.padding(horizontal = 16.dp)

    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "hero") { TodayHero(tools, side) }
        item(key = "prayers") { PrayerStrip(tools, onClick = { onOpenRoute("prayer") }, modifier = side) }
        item(key = "weekday") {
            TodayWeekdayTiles(onDua = { onOpenRoute("duas") }, onZiyarah = { onOpenRoute("ziyarat") }, modifier = side)
        }
        item(key = "resume") { ResumeTiles(tools, onResumeSurah, onResumeMafatih, onResumeSahifa, side) }
        item(key = "progress") { DailyProgressCard(dailyProgress.first, dailyProgress.second, side) }
        item(key = "categories") { FilterChipRow(selectedCategory, viewModel::selectCategory) }
        item(key = "deeds_header") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedCategory.titleArabic,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "خيارات الأعمال")
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("إعادة ضبط إنجاز اليوم") },
                        onClick = { menuOpen = false; confirmReset = true }
                    )
                }
            }
        }
        items(deeds, key = { it.id }, contentType = { "deed" }) { deed ->
            DeedCard(
                deed = deed,
                isCompleted = completedMap[deed.id] ?: false,
                currentCount = countsMap[deed.id] ?: 0,
                onClick = { onOpenDeed(deed.id) },
                onToggleCompleted = { viewModel.toggleDeedCompleted(deed.id) },
                modifier = side
            )
        }
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("تأكيد إعادة التصفير") },
            text = { Text("هل تود تصفير تقدم الأعمال والعدادات لليوم؟") },
            confirmButton = {
                TextButton(onClick = { viewModel.resetToday(); confirmReset = false }) { Text("نعم، صفّر الإنجاز") }
            },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("إلغاء") } }
        )
    }
}

@Composable
private fun DailyProgressCard(completed: Int, total: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("إنجاز الأوراد اليومية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    if (completed == total && total > 0) "ما شاء الله! أتممت جميع الأوراد المباركة لليوم"
                    else "أنجزت $completed من أصل $total عملاً",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            CircularProgressBar(completed, total, size = 64.dp, strokeWidth = 5.dp)
        }
    }
}
