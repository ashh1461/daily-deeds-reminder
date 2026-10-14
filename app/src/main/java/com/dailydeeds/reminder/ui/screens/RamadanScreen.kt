package com.dailydeeds.reminder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.ui.theme.GoldMain
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.Timetable
import com.dailydeeds.reminder.worship.TimetablePdf
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val CLOCK = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
private fun LocalTime?.clock() = this?.format(CLOCK) ?: "--:--"

/** Monthly Imsak / Iftar timetable (Ramadan by default) for the chosen city, with a PDF export. */
@Composable
fun RamadanScreen(tools: ToolsViewModel, worship: WorshipViewModel, onNavigateBack: () -> Unit) {
    val place by tools.place.collectAsState()
    val calc by tools.prayerContext.collectAsState()
    val offset by tools.hijriOffset.collectAsState()
    val imsakAlarm by worship.imsakAlarm.collectAsState()
    val today = remember { LocalDate.now() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var year by rememberSaveable { mutableIntStateOf(Timetable.nextRamadanYear(today, offset)) }
    var month by rememberSaveable { mutableIntStateOf(9) }
    var message by remember { mutableStateOf<String?>(null) }

    val rows = remember(year, month, place, calc, offset) {
        runCatching { Timetable.month(year, month, place, calc, offset) }.getOrDefault(emptyList())
    }
    val monthTitle = "${ShiaCalendar.MONTH_NAMES[month - 1]} $year هـ"

    val pdfLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        if (uri != null) {
            scope.launch {
                message = withContext(Dispatchers.IO) {
                    runCatching {
                        val out = context.contentResolver.openOutputStream(uri, "wt") ?: error("no stream")
                        out.use { TimetablePdf.write(context, rows, "جدول أوقات $monthTitle", "${place.name} — بحسب إعدادات الحساب في التطبيق", it) }
                        "تم حفظ الجدول بصيغة PDF"
                    }.getOrElse { "تعذّر حفظ الملف" }
                }
            }
        }
    }

    fun move(delta: Int) {
        var m = month + delta
        var y = year
        if (m < 1) { m = 12; y -= 1 }
        if (m > 12) { m = 1; y += 1 }
        if (y in Timetable.MIN_YEAR..Timetable.MAX_YEAR) { month = m; year = y }
    }

    ToolScaffold("رمضان والإمساك", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WorshipCard {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { move(-1) }) { Text("‹") }
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(monthTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(place.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OutlinedButton(onClick = { move(1) }) { Text("›") }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { year = Timetable.nextRamadanYear(today, offset); month = 9 }, modifier = Modifier.weight(1f)) {
                        Text("رمضان القادم")
                    }
                    Button(
                        onClick = { pdfLauncher.launch("timetable-$year-${month.toString().padStart(2, '0')}.pdf") },
                        enabled = rows.isNotEmpty(), modifier = Modifier.weight(1f)
                    ) { Text("حفظ PDF") }
                }
                message?.let { WorshipNote(it) }
            }

            WorshipCard {
                SwitchLine("تنبيه وقت الإمساك في أيام رمضان", imsakAlarm) { worship.setImsakAlarm(it) }
                WorshipNote(
                    "الإمساك قبل الفجر بعدد الدقائق المحدد في إعدادات الأذان (10 دقائق افتراضياً). " +
                        "للتنبيه عند الإفطار فعّل أذان المغرب أو تنبيهه من «إعدادات الأذان»."
                )
            }

            if (rows.isEmpty()) {
                WorshipNote("تعذّر إنشاء الجدول لهذا الشهر.")
            } else {
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small).padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("اليوم", "التاريخ", "الإمساك", "الفجر", "الشروق", "الظهر", "المغرب", "العشاء").forEach {
                        Cell(it, header = true)
                    }
                }
                rows.forEach { r ->
                    val tint = when {
                        r.qadrNight != null -> GoldMain.copy(alpha = 0.25f)
                        r.date == today -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                    Row(Modifier.fillMaxWidth().background(tint, MaterialTheme.shapes.extraSmall).padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Cell("${r.hijri.day} " + r.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ar")))
                        Cell("${r.date.dayOfMonth}/${r.date.monthValue}")
                        Cell(r.times.imsak.clock(), bold = true)
                        Cell(r.times.fajr.clock())
                        Cell(r.times.sunrise.clock())
                        Cell(r.times.dhuhr.clock())
                        Cell(r.times.maghrib.clock(), bold = true)
                        Cell(r.times.isha.clock())
                    }
                }
                if (rows.any { it.qadrNight != null }) {
                    WorshipNote("الأسطر الذهبية: اليوم الذي تبدأ عند مغربه ليلة من ليالي القدر (19 و21 و23).")
                }
                WorshipNote("الأوقات تقريبية بحسب الحساب الفلكي وإعدادات طريقة الحساب؛ احتط في الإمساك وأفطر عند تحقق المغرب.")
            }
        }
    }
}

@Composable
private fun RowScope.Cell(text: String, bold: Boolean = false, header: Boolean = false) {
    Text(
        text, Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, maxLines = 1,
        fontWeight = if (bold || header) FontWeight.Bold else FontWeight.Normal,
        color = if (header) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    )
}
