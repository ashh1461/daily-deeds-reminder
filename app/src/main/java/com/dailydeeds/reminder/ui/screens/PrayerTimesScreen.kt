package com.dailydeeds.reminder.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.ui.components.rememberNow
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.util.TimeFormat
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@SuppressLint("MissingPermission")
internal fun lastKnownPlace(context: Context): Place? {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ) return null
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
    val loc = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER, LocationManager.GPS_PROVIDER)
        .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
        .maxByOrNull { it.time } ?: return null
    return Place("موقعي الحالي", loc.latitude, loc.longitude, ZoneId.systemDefault().id)
}

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
    val context = LocalContext.current
    var menuOpen by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showCoordinates by remember { mutableStateOf(false) }

    fun useDeviceLocation() {
        val p = lastKnownPlace(context)
        if (p == null) {
            Toast.makeText(context, "تعذّر تحديد الموقع؛ ابحث عن مدينتك أو اخترها من القائمة.", Toast.LENGTH_LONG).show()
        } else viewModel.setPlace(p)
    }

    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) useDeviceLocation()
    }

    // Refreshes every 30 s, so the highlighted "next prayer" and the date roll over without reopening the screen.
    val now by rememberNow()
    val today = now.toLocalDate()
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
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("المكان: ${place.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        next?.let {
                            Text("الصلاة القادمة: ${it.prayer.nameArabic} عند ${it.time.format(TIME_FORMAT)} • ${TimeFormat.countdown(now, it.time)}")
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box {
                                OutlinedButton(onClick = { menuOpen = true }) { Text("مدن مقترحة") }
                                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                    PlacePresets.all.forEach { p ->
                                        DropdownMenuItem(text = { Text(p.name) }, onClick = {
                                            viewModel.setPlace(p); menuOpen = false
                                        })
                                    }
                                }
                            }
                            OutlinedButton(onClick = { showSearch = true }) { Text("بحث") }
                            OutlinedButton(onClick = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED
                                ) useDeviceLocation()
                                else permission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                            }) { Text("موقعي") }
                        }
                        OutlinedButton(onClick = { showCoordinates = true }) { Text("إدخال الإحداثيات") }
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

    if (showSearch) {
        CitySearchDialog(onDismiss = { showSearch = false }, onPick = { viewModel.setPlace(it); showSearch = false })
    }
    if (showCoordinates) {
        CoordinatesDialog(onDismiss = { showCoordinates = false }, onPick = { viewModel.setPlace(it); showCoordinates = false })
    }
}
