package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.viewmodel.ToolsViewModel

/** Home shortcuts: continue reading, then calendar, prayer times, qibla and favorites. */
@Composable
fun HomeToolsCard(
    tools: ToolsViewModel,
    onOpenRoute: (String) -> Unit,
    onResumeSurah: (Int) -> Unit,
    onResumeMafatih: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastSurah by tools.lastSurah.collectAsState()
    val lastMafatihId by tools.lastMafatihId.collectAsState()
    val surahName = remember(lastSurah) { QuranRepository().getSurahByNumber(lastSurah)?.nameArabic }
    val mafatihTitle = remember(lastMafatihId) { lastMafatihId?.let { MafatihRepository().getItemById(it)?.title } }

    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
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
            Text("أدوات", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
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
