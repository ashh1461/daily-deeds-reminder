package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.model.MafatihItem
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.worship.LunarMonthDeeds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R

private class LunarMonthsData(val sections: List<LunarMonthDeeds.Section>, val general: LunarMonthDeeds.Section)

/**
 * «أعمال الأشهر القمرية»: the month-by-month deeds of Mafatih al-Jinan. The current Hijri month (with the user's
 * sighting offset) comes first, then the deeds common to every month, then all twelve months in order. Each row
 * opens the full section in the Mafatih reader.
 */
@Composable
fun LunarMonthsScreen(tools: ToolsViewModel, onNavigateBack: () -> Unit, onOpenItem: (String) -> Unit) {
    val offset by tools.hijriOffset.collectAsState()
    val hijri = remember(offset) { ShiaCalendar.toHijri(LocalDate.now(), offset) }
    // The book is 1.5 MB of text: load it off the main thread.
    val data by produceState<LunarMonthsData?>(initialValue = null) {
        value = withContext(Dispatchers.Default) {
            val book = MafatihRepository().getBookItems()
            LunarMonthsData(LunarMonthDeeds.sections(book), LunarMonthDeeds.general(book))
        }
    }

    ToolScaffold(stringResource(R.string.lunar_title), onNavigateBack) {
        val loaded = data
        if (loaded == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            MonthsList(loaded, hijri.month, hijri.monthName, hijri.toString(), onOpenItem)
        }
    }
}

@Composable
private fun MonthsList(data: LunarMonthsData, month: Int, monthName: String, today: String, onOpenItem: (String) -> Unit) {
    val current = LunarMonthDeeds.sectionFor(month, data.sections)
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (current != null) {
            item(key = "current") {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(stringResource(R.string.lunar_current_month, monthName), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.lunar_today, today.toString()), style = MaterialTheme.typography.bodySmall)
                        current.items.forEach { DeedRow(it, onOpenItem) }
                    }
                }
            }
        }
        section(data.general, onOpenItem, "general")
        data.sections.forEach { s -> section(s, onOpenItem, "m${s.months.first()}") }
        item(key = "source") {
            Text(
                stringResource(R.string.lunar_source),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun LazyListScope.section(section: LunarMonthDeeds.Section, onOpenItem: (String) -> Unit, key: String) {
    item(key = "h_$key") {
        Text(
            section.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
    items(section.items, key = { "${key}_${it.id}" }) { DeedRow(it, onOpenItem) }
}

@Composable
private fun DeedRow(item: MafatihItem, onOpenItem: (String) -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable { onOpenItem(item.id) },
        shape = MaterialTheme.shapes.medium,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(item.title, Modifier.padding(horizontal = 14.dp, vertical = 12.dp), style = MaterialTheme.typography.bodyMedium)
    }
}
