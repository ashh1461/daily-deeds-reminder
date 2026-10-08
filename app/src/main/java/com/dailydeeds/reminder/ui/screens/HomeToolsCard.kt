package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.ui.components.EmeraldBanner
import com.dailydeeds.reminder.ui.components.OrnamentDivider
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.ui.theme.GoldBright
import com.dailydeeds.reminder.ui.theme.ReadingFamily
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val HOUR_MINUTE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

/** Home hero: Hijri date, today's occasion and the next prayer, with quick access to the tools. */
@Composable
fun HomeToolsCard(
    tools: ToolsViewModel,
    onOpenRoute: (String) -> Unit,
    onResumeSurah: (Int) -> Unit,
    onResumeMafatih: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val place by tools.place.collectAsState()
    val offset by tools.hijriOffset.collectAsState()
    val lastSurah by tools.lastSurah.collectAsState()
    val lastMafatihId by tools.lastMafatihId.collectAsState()

    val today = remember { LocalDate.now() }
    val hijri = remember(offset) { ShiaCalendar.toHijri(today, offset) }
    val occasions = remember(offset) { ShiaCalendar.occasionsOn(today, offset) }
    val next = remember(place) { PrayerSchedule.next(ZonedDateTime.now(), place) }
    val surahName = remember(lastSurah) { QuranRepository().getSurahByNumber(lastSurah)?.nameArabic }
    val mafatihTitle = remember(lastMafatihId) { lastMafatihId?.let { MafatihRepository().getItemById(it)?.title } }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        EmeraldBanner(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text(
                    "بسم الله الرحمن الرحيم",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = ReadingFamily,
                    color = GoldBright,
                    textAlign = TextAlign.Center
                )
                Text(
                    hijri.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    "$today",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                OrnamentDivider(Modifier.padding(vertical = 10.dp), color = GoldBright)
                if (occasions.isNotEmpty()) {
                    Text(
                        occasions.first().title,
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldBright,
                        textAlign = TextAlign.Center
                    )
                }
                if (next != null) {
                    Text(
                        "الصلاة القادمة: ${next.prayer.nameArabic} • ${next.time.format(HOUR_MINUTE)} (${place.name})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            border = cardBorder(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (surahName != null || mafatihTitle != null) {
                    Text("تابع من حيث توقفت", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (surahName != null) {
                            OutlinedButton(onClick = { onResumeSurah(lastSurah) }, modifier = Modifier.weight(1f)) { Text("سورة $surahName") }
                        }
                        if (mafatihTitle != null) {
                            OutlinedButton(onClick = { onResumeMafatih(lastMafatihId!!) }, modifier = Modifier.weight(1f)) {
                                Text(mafatihTitle, maxLines = 1)
                            }
                        }
                    }
                }
                Text("الأدوات", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = { onOpenRoute("calendar") }, modifier = Modifier.weight(1f)) { Text("التقويم") }
                    FilledTonalButton(onClick = { onOpenRoute("prayer") }, modifier = Modifier.weight(1f)) { Text("الصلاة") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(onClick = { onOpenRoute("qibla") }, modifier = Modifier.weight(1f)) { Text("القبلة") }
                    FilledTonalButton(onClick = { onOpenRoute("favorites") }, modifier = Modifier.weight(1f)) { Text("المفضلة") }
                }
            }
        }
    }
}
