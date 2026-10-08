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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.data.WeekdayRepository
import com.dailydeeds.reminder.model.DayContentKind
import java.time.LocalDate
import com.dailydeeds.reminder.model.Deed
import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.ui.components.CircularProgressBar
import com.dailydeeds.reminder.ui.components.DeedCard
import com.dailydeeds.reminder.ui.components.FilterChipRow
import com.dailydeeds.reminder.ui.theme.DailyReminderTheme
import com.dailydeeds.reminder.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToDeed: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDuas: () -> Unit,
    onNavigateToZiyarat: () -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val completedMap by viewModel.completedMap.collectAsState()
    val countsMap by viewModel.countsMap.collectAsState()
    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val activeDate by viewModel.activeDate.collectAsState()
    val filteredDeeds = remember(selectedCategory, activeDate) {
        DeedsRepository.getDeedsByCategory(selectedCategory, activeDate)
    }

    HomeContent(
        selectedCategory, filteredDeeds, completedMap, countsMap, dailyProgress,
        viewModel::selectCategory, viewModel::resetToday, viewModel::toggleDeedCompleted,
        onNavigateToDeed, onNavigateToSettings, onNavigateToDuas, onNavigateToZiyarat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    selectedCategory: DeedCategory,
    deeds: List<Deed>,
    completedMap: Map<Int, Boolean>,
    countsMap: Map<Int, Int>,
    dailyProgress: Pair<Int, Int>,
    onCategorySelected: (DeedCategory) -> Unit,
    onResetToday: () -> Unit,
    onToggleCompleted: (Int) -> Unit,
    onNavigateToDeed: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDuas: () -> Unit,
    onNavigateToZiyarat: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الأعمال اليومية", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onResetToday) {
                        Icon(Icons.Default.Refresh, contentDescription = "إعادة ضبط اليوم")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "الإعدادات")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            item(key = "daily_progress", contentType = "summary") {
                DailyProgressCard(dailyProgress.first, dailyProgress.second,
                    modifier = Modifier.padding(horizontal = 16.dp))
            }
            item(key = "today_weekday", contentType = "weekday") {
                TodayWeekdayCard(onNavigateToDuas, onNavigateToZiyarat,
                    modifier = Modifier.padding(horizontal = 16.dp))
            }
            item(key = "categories", contentType = "filters") {
                FilterChipRow(selectedCategory, onCategorySelected)
            }
            item(key = "section_title", contentType = "heading") {
                Text(selectedCategory.titleArabic, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
            }
            items(deeds, key = { it.id }, contentType = { "deed" }) { deed ->
                DeedCard(
                    deed = deed,
                    isCompleted = completedMap[deed.id] ?: false,
                    currentCount = countsMap[deed.id] ?: 0,
                    onClick = { onNavigateToDeed(deed.id) },
                    onToggleCompleted = { onToggleCompleted(deed.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun TodayWeekdayCard(onDuas: () -> Unit, onZiyarat: () -> Unit, modifier: Modifier = Modifier) {
    val today = remember { LocalDate.now().dayOfWeek }
    val dua = remember(today) { WeekdayRepository.get(DayContentKind.DUA, today) }
    val ziyarah = remember(today) { WeekdayRepository.get(DayContentKind.ZIYARAT, today) }
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("اليوم: ${WeekdayRepository.dayNameArabic(today)} • ${dua.honoree}",
                style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            WeekdayShortcuts(onDuas, onZiyarat)
            Text("${dua.title} • ${ziyarah.title}", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DailyProgressCard(completed: Int, total: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("أوراد يومية وقراءات في أوقاتها", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("إنجاز الأوراد اليومية", style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold)
                    Text(
                        if (completed == total && total > 0) {
                            "ما شاء الله! أتممت جميع الأوراد المباركة لليوم 🌿"
                        } else {
                            "أنجزت $completed من أصل $total عملاً"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                CircularProgressBar(completed, total, size = 80.dp, strokeWidth = 6.dp)
            }
            Text("«أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ»", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(name = "Arabic light", locale = "ar", showBackground = true)
@Preview(name = "Arabic dark", locale = "ar", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Arabic large text", locale = "ar", fontScale = 1.5f, widthDp = 360)
@Composable
private fun HomePreview() {
    DailyReminderTheme {
        HomeContent(
            selectedCategory = DeedCategory.ALL,
            deeds = DeedsRepository.getDeedsByCategory(DeedCategory.ALL),
            completedMap = mapOf(1 to true), countsMap = mapOf(2 to 12), dailyProgress = 1 to 16,
            onCategorySelected = {}, onResetToday = {}, onToggleCompleted = {},
            onNavigateToDeed = {}, onNavigateToSettings = {},
            onNavigateToDuas = {}, onNavigateToZiyarat = {}
        )
    }
}
