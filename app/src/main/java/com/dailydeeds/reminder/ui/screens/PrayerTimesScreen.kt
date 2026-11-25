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
        Triple("الإمساك", times.imsak, null),
        Triple("الفجر", times.fajr, Prayer.FAJR),
        Triple("الشروق", times.sunrise, null),
        Triple("الظهر", times.dhuhr, Prayer.DHUHR),
        Triple("الغروب", times.sunset, null),
        Triple("المغرب", times.maghrib, Prayer.MAGHRIB),
        Triple("العشاء", times.isha, Prayer.ISHA),
        Triple("منتصف الليل الشرعي", times.midnight, null)
    )

    ToolScaffold("أوقات الصلاة والأذان", onNavigateBack) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item { PermissionHealthCard() }
            item {
                PlacePickerCard(place, onPick = viewModel::setPlace) {
                    next?.let {
                        Text("الصلاة القادمة: ${it.prayer.nameArabic} عند ${it.time.format(TIME_FORMAT)} • ${TimeFormat.countdown(now, it.time)}")
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
                            Text(label, style = MaterialTheme.typography.titleSmall)
                            if (prayer != null) {
                                val mode = configs[prayer]?.mode ?: AdhanMode.OFF
                                Text(
                                    "التنبيه: ${mode.labelArabic}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(time?.format(TIME_FORMAT) ?: "—", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
            item {
                Button(onClick = onOpenSettings, modifier = Modifier.fillMaxWidth()) { Text("إعدادات الأذان وطريقة الحساب") }
            }
            item {
                OutlinedButton(onClick = onOpenQibla, modifier = Modifier.fillMaxWidth()) { Text("اتجاه القبلة") }
            }
            item {
                Text(
                    "الحساب بالفقه الجعفري، وطريقته قابلة للتغيير من الإعدادات. قارن بجدول مرجعك المحلي واحتط عند الحاجة.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
