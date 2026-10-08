package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.viewmodel.MafatihViewModel

/** The Mafatih al-Jinan tab: thematic chips over the complete book, plus the chapter index. */
@Composable
fun MafatihScreen(
    viewModel: MafatihViewModel,
    onNavigateToItem: (String) -> Unit,
    onNavigateToDuas: () -> Unit,
    onNavigateToZiyarat: () -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val items by viewModel.items.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val rows = remember(items) { buildRows(items) }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
        SearchField(searchQuery, "ابحث في ${selectedCategory.titleArabic}...", viewModel::onSearchQueryChanged)

        Spacer(Modifier.height(10.dp))

        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.categories) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { viewModel.selectCategory(category) },
                    label = {
                        Text(
                            text = category.titleArabic,
                            fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (searchQuery.isBlank()) {
                when (selectedCategory) {
                    MafatihCategoryType.ADIYAH -> item(key = "pin_duas") {
                        ItemRow("أدعية الأيام السبعة", "دعاء لكل يوم من أيام الأسبوع", onNavigateToDuas)
                    }
                    MafatihCategoryType.ZIYARAT -> item(key = "pin_ziyarat") {
                        ItemRow("زيارات أيام الأسبوع", "زيارة لكل يوم من أيام الأسبوع", onNavigateToZiyarat)
                    }
                    else -> Unit
                }
            }
            mafatihRows(rows) { id ->
                viewModel.selectItem(id)
                onNavigateToItem(id)
            }
            item(key = "end") { Spacer(Modifier.height(16.dp)) }
        }
    }
}
