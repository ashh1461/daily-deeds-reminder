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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.ui.components.rememberNow
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.util.TimeFormat
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.labelRes
import com.dailydeeds.reminder.ui.countdownText

private val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun PrayerTimesScreen(
    viewModel: ToolsViewModel,
    onNavigateBack: () -> Unit,
    onOpenQibla: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val place by viewModel.place.collectAsState()
    val calc by viewModel.prayerContext.collectAsState()
    val configs by viewModel.alarmConfigs.collectAsState()

    // Refreshes every 30 s, so the highlighted "next prayer" and the date roll over without reopening the screen.
    val now by rememberNow()
    // "Today" is the place's date, not the phone's: they differ when the place is in another time zone.
    val today = now.withZoneSameInstant(place.zone).toLocalDate()
    val times = remember(place, today, calc) { PrayerSchedule.timesFor(today, place, calc) }
    val next = remember(place, now, calc) { PrayerSchedule.next(now, place, calc) }

    val rows: List<Triple<String, LocalTime?, Prayer?>> = listOf(
        Triple(stringResource(R.string.time_imsak), times.imsak, null),
        Triple(stringResource(R.string.prayer_fajr), times.fajr, Prayer.FAJR),
        Triple(stringResource(R.string.time_sunrise), times.sunrise, null),
        Triple(stringResource(R.string.prayer_dhuhr), times.dhuhr, Prayer.DHUHR),
        Triple(stringResource(R.string.time_sunset), times.sunset, null),
        Triple(stringResource(R.string.prayer_maghrib), times.maghrib, Prayer.MAGHRIB),
        Triple(stringResource(R.string.prayer_isha), times.isha, Prayer.ISHA),
        Triple(stringResource(R.string.time_midnight), times.midnight, null)
    )

    ToolScaffold(stringResource(R.string.prayer_times_title), onNavigateBack) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item { PermissionHealthCard() }
            item {
                PlacePickerCard(place, onPick = viewModel::setPlace) {
                    next?.let {
                        Text(
                            stringResource(
                                R.string.prayer_next_at, stringResource(it.prayer.labelRes), it.time.format(TIME_FORMAT),
                                countdownText(TimeFormat.countdown(now, it.time))
                            )
                        )
                    }
                }
            }
            items(rows) { (label, time, prayer) ->
                Card(
                    Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    border = cardBorder(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            val estimated = prayer != null && prayer in times.estimated
                            Text(if (estimated) stringResource(R.string.prayer_estimated, label) else label, style = MaterialTheme.typography.titleSmall)
                            if (prayer != null) {
                                val mode = configs[prayer]?.mode ?: AdhanMode.OFF
                                Text(
                                    stringResource(R.string.prayer_alert_mode, stringResource(mode.labelRes)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(time?.format(TIME_FORMAT) ?: "—", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            if (times.estimated.isNotEmpty()) {
                item {
                    Text(
                        stringResource(R.string.prayer_estimated_note),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.prayer_settings_title)) }
            }
            item {
                OutlinedButton(onClick = onOpenQibla, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.qibla_title)) }
            }
            item {
                Text(
                    stringResource(R.string.prayer_method_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
