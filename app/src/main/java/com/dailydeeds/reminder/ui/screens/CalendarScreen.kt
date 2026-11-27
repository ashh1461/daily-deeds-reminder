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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.daysAwayText
import com.dailydeeds.reminder.calendar.CalendarNames
import com.dailydeeds.reminder.ui.appLanguage

@Composable
fun CalendarScreen(viewModel: ToolsViewModel, onNavigateBack: () -> Unit) {
    val offset by viewModel.hijriOffset.collectAsState()
    val reminder by viewModel.occasionReminder.collectAsState()
    val eveReminder by viewModel.blessedEveReminder.collectAsState()
    val today = remember { LocalDate.now() }
    val hijri = remember(offset) { ShiaCalendar.toHijri(today, offset) }
    val upcoming = remember(offset) { ShiaCalendar.upcoming(today, 120, offset) }

    ToolScaffold(stringResource(R.string.settings_calendar_open), onNavigateBack) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(CalendarNames.date(hijri, appLanguage()), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(stringResource(R.string.calendar_corresponds, today.toString()), style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.calendar_adjust), style = MaterialTheme.typography.bodySmall)
                            OutlinedButton(onClick = { viewModel.setHijriOffset(offset - 1) }, enabled = offset > -ShiaCalendar.MAX_OFFSET) { Text("−") }
                            Text(if (offset > 0) "+$offset" else "$offset", fontWeight = FontWeight.Bold)
                            OutlinedButton(onClick = { viewModel.setHijriOffset(offset + 1) }, enabled = offset < ShiaCalendar.MAX_OFFSET) { Text("+") }
                        }
                        Text(
                            stringResource(R.string.calendar_adjust_note),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.calendar_morning_reminder), Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                    Switch(checked = reminder, onCheckedChange = viewModel::setOccasionReminder)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.calendar_eve_reminder), style = MaterialTheme.typography.titleSmall)
                        Text(
                            stringResource(R.string.calendar_eve_note),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(checked = eveReminder, onCheckedChange = viewModel::setBlessedEveReminder)
                }
            }
            item { Text(stringResource(R.string.calendar_upcoming), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(upcoming, key = { "${it.date}_${it.occasion.title}" }) { u ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(CalendarNames.occasionDisplay(u.occasion, appLanguage()), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${CalendarNames.date(u.hijri, appLanguage())} • ${u.date} • " + daysAwayText(u.daysAway.toLong()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            item {
                Text(
                    stringResource(R.string.calendar_dates_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
