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

@Composable
fun CalendarScreen(viewModel: ToolsViewModel, onNavigateBack: () -> Unit) {
    val offset by viewModel.hijriOffset.collectAsState()
    val reminder by viewModel.occasionReminder.collectAsState()
    val today = remember { LocalDate.now() }
    val hijri = remember(offset) { ShiaCalendar.toHijri(today, offset) }
    val upcoming = remember(offset) { ShiaCalendar.upcoming(today, 120, offset) }

    ToolScaffold("التقويم والمناسبات", onNavigateBack) {
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
                        Text(hijri.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("الموافق $today", style = MaterialTheme.typography.bodyMedium)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("تعديل الرؤية:", style = MaterialTheme.typography.bodySmall)
                            OutlinedButton(onClick = { viewModel.setHijriOffset(offset - 1) }, enabled = offset > -ShiaCalendar.MAX_OFFSET) { Text("−") }
                            Text(if (offset > 0) "+$offset" else "$offset", fontWeight = FontWeight.Bold)
                            OutlinedButton(onClick = { viewModel.setHijriOffset(offset + 1) }, enabled = offset < ShiaCalendar.MAX_OFFSET) { Text("+") }
                        }
                        Text(
                            "يعتمد التاريخ على جداول أم القرى وقد يختلف يوماً عن رؤية الهلال في بلدك؛ عدّله هنا.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("تذكير صباحي بمناسبة اليوم", Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                    Switch(checked = reminder, onCheckedChange = viewModel::setOccasionReminder)
                }
            }
            item { Text("المناسبات القادمة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(upcoming, key = { "${it.date}_${it.occasion.title}" }) { u ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(u.occasion.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${u.hijri} • ${u.date} • " + when (u.daysAway) {
                                0 -> "اليوم"
                                1 -> "غداً"
                                else -> "بعد ${u.daysAway} يوماً"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            item {
                Text(
                    "تتفاوت بعض تواريخ الولادات والوفيات بين الأقوال والمراجع؛ يُرجى مراجعة مرجع التقليد.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
