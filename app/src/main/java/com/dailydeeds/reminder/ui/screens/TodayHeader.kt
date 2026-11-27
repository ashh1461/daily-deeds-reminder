package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.QuranPages
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.data.QuranResume
import com.dailydeeds.reminder.data.WeekdayRepository
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.ui.components.EmeraldBanner
import com.dailydeeds.reminder.ui.components.OrnamentDivider
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.ui.components.rememberNow
import com.dailydeeds.reminder.ui.theme.GoldBright
import com.dailydeeds.reminder.ui.theme.ReadingFamily
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.util.TimeFormat
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.labelRes
import com.dailydeeds.reminder.ui.countdownText
import com.dailydeeds.reminder.ui.localNumber
import com.dailydeeds.reminder.ui.surahDisplayName

private val HOUR_MINUTE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

/** Compact hero: Hijri date, today's occasion and a live countdown to the next prayer. */
@Composable
fun TodayHero(tools: ToolsViewModel, modifier: Modifier = Modifier) {
    val place by tools.place.collectAsState()
    val calc by tools.prayerContext.collectAsState()
    val offset by tools.hijriOffset.collectAsState()
    val now by rememberNow()
    val today = now.toLocalDate()
    val hijri = remember(today, offset) { ShiaCalendar.toHijri(today, offset) }
    val occasions = remember(today, offset) { ShiaCalendar.occasionsOn(today, offset) }
    val next = remember(place, now, calc) { PrayerSchedule.next(now, place, calc) }

    EmeraldBanner(modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "بسم الله الرحمن الرحيم",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = ReadingFamily,
                color = GoldBright,
                textAlign = TextAlign.Center
            )
            Text(
                hijri.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text("$today", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            OrnamentDivider(Modifier.padding(vertical = 8.dp), color = GoldBright)
            occasions.firstOrNull()?.let {
                Text(it.displayTitle, style = MaterialTheme.typography.titleSmall, color = GoldBright, textAlign = TextAlign.Center)
            }
            if (next != null) {
                Text(
                    "${stringResource(next.prayer.labelRes)} ${next.time.format(HOUR_MINUTE)} • ${countdownText(TimeFormat.countdown(now, next.time))}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/** Today's prayer times in one row; the next prayer is highlighted. Tapping opens the prayer hub. */
@Composable
fun PrayerStrip(tools: ToolsViewModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val place by tools.place.collectAsState()
    val calc by tools.prayerContext.collectAsState()
    val now by rememberNow()
    val times = remember(place, now.toLocalDate(), calc) { PrayerSchedule.timesFor(now.toLocalDate(), place, calc) }
    val next = remember(place, now, calc) { PrayerSchedule.next(now, place, calc) }
    val cells = listOf(
        Prayer.FAJR to times.fajr, Prayer.DHUHR to times.dhuhr,
        Prayer.MAGHRIB to times.maghrib, Prayer.ISHA to times.isha
    )
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            cells.forEach { (prayer, time) ->
                val highlighted = next?.prayer == prayer
                Column(
                    Modifier
                        .weight(1f)
                        .background(
                            if (highlighted) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(prayer.nameArabic, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        time?.format(HOUR_MINUTE) ?: "—",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (highlighted) FontWeight.Bold else FontWeight.Medium,
                        color = if (highlighted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/** Two tiles: today's weekday dua and ziyarah (single source: [WeekdayRepository]). */
@Composable
fun TodayWeekdayTiles(onDua: () -> Unit, onZiyarah: () -> Unit, modifier: Modifier = Modifier) {
    val now by rememberNow(periodMs = 60_000L)
    val day = now.dayOfWeek
    val dua = remember(day) { WeekdayRepository.get(DayContentKind.DUA, day) }
    val ziyarah = remember(day) { WeekdayRepository.get(DayContentKind.ZIYARAT, day) }
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        listOf(
            Triple(stringResource(R.string.today_dua_of_day), dua.title, onDua),
            Triple(stringResource(R.string.today_ziyarah_of_day), ziyarah.title, onZiyarah)
        ).forEach { (label, title, click) ->
            Card(
                modifier = Modifier.weight(1f).clickable(onClick = click),
                shape = MaterialTheme.shapes.large,
                border = cardBorder(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 2)
                    Text(
                        dua.honoree,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/** "Continue where you stopped" tiles for the Quran, Mafatih and Sahifa. Renders nothing if there is nothing to resume. */
@Composable
fun ResumeTiles(
    tools: ToolsViewModel,
    onSurah: (Int) -> Unit,
    onMafatih: (String) -> Unit,
    onSahifa: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastSurah by tools.lastSurah.collectAsState()
    val lastMafatihId by tools.lastMafatihId.collectAsState()
    val lastSahifaId by tools.lastSahifaId.collectAsState()
    val pagesMode by tools.quranPagesMode.collectAsState()
    val lastPage by tools.lastQuranPage.collectAsState()
    val lastAyah by tools.lastAyah.collectAsState()
    val surah = remember(lastSurah) { QuranRepository().getSurahByNumber(lastSurah) }
    val surahName = surah?.nameArabic
    val mafatihTitle = remember(lastMafatihId) { lastMafatihId?.let { MafatihRepository().getItemById(it)?.title } }
    val sahifaTitle = remember(lastSahifaId) { lastSahifaId?.let { MafatihRepository().getItemById(it)?.title } }
    if (surahName == null && mafatihTitle == null && sahifaTitle == null) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(R.string.today_resume_title), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            if (surahName != null) {
                // In pages mode the tile reopens (and names) the last mushaf page; in the list, the last ayah.
                val target = remember(lastSurah, pagesMode, lastPage, lastAyah) {
                    val pageShowsSurah = lastPage in 1..QuranPages.COUNT && QuranPages.page(lastPage).showsSurah(lastSurah)
                    QuranResume.target(pagesMode, lastPage, pageShowsSurah, lastAyah)
                }
                val name = surah?.let { surahDisplayName(it) } ?: surahName
                val label = when (target.kind) {
                    QuranResume.Kind.SURAH -> stringResource(R.string.today_resume_quran, name)
                    QuranResume.Kind.PAGE -> stringResource(R.string.today_resume_quran_page, name, localNumber(target.number))
                    QuranResume.Kind.AYAH -> stringResource(R.string.today_resume_quran_ayah, name, localNumber(target.number))
                }
                OutlinedButton(onClick = { onSurah(lastSurah) }, modifier = Modifier.fillMaxWidth()) { Text(label) }
            }
            if (mafatihTitle != null) {
                OutlinedButton(onClick = { onMafatih(lastMafatihId!!) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.today_resume_mafatih, mafatihTitle), maxLines = 1)
                }
            }
            if (sahifaTitle != null) {
                OutlinedButton(onClick = { onSahifa(lastSahifaId!!) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.today_resume_sahifa, sahifaTitle), maxLines = 1)
                }
            }
        }
    }
}
