package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.data.WeekdayRepository
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.viewmodel.ToolsViewModel

private data class FavoriteRow(val key: FavoriteKey, val title: String, val subtitle: String)

@Composable
fun FavoritesScreen(
    viewModel: ToolsViewModel,
    onNavigateBack: () -> Unit,
    onOpenSurah: (Int) -> Unit,
    onOpenMafatih: (String) -> Unit,
    onOpenWeekday: (DayContentKind) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()
    val quran = remember { QuranRepository() }
    val mafatih = remember { MafatihRepository() }

    val rows = remember(favorites) {
        favorites.mapNotNull { key ->
            when (key) {
                is FavoriteKey.Ayah -> {
                    val s = quran.getSurahByNumber(key.surah)
                    val a = quran.getAyah(key.surah, key.ayah)
                    if (s != null && a != null) FavoriteRow(key, "سورة ${s.nameArabic} - الآية ${key.ayah}", a.textArabic) else null
                }
                is FavoriteKey.Mafatih -> mafatih.getItemById(key.itemId)
                    ?.let { FavoriteRow(key, it.title, it.category.titleArabic) }
                is FavoriteKey.Sahifa -> mafatih.getItemById(key.itemId)
                    ?.let { FavoriteRow(key, it.title, "الصحيفة السجادية") }
                is FavoriteKey.Weekday -> {
                    val c = WeekdayRepository.get(key.kind, key.day)
                    FavoriteRow(key, c.title, key.kind.titleArabic)
                }
            }
        }
    }

    ToolScaffold("المفضلة", onNavigateBack) {
        if (rows.isEmpty()) {
            Text(
                "لا توجد عناصر مفضلة بعد. اضغط على النجمة في أي آية أو دعاء أو زيارة لإضافتها هنا.",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(rows, key = { it.key.encode() }) { row ->
                    Card(
                        Modifier.fillMaxWidth().clickable {
                            when (val k = row.key) {
                                is FavoriteKey.Ayah -> onOpenSurah(k.surah)
                                is FavoriteKey.Mafatih -> onOpenMafatih(k.itemId)
                                is FavoriteKey.Sahifa -> onOpenMafatih(k.itemId)
                                is FavoriteKey.Weekday -> onOpenWeekday(k.kind)
                            }
                        }
                    ) {
                        Row(Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(row.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    row.subtitle, maxLines = 2, overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(row.key) }) {
                                Icon(Icons.Default.Star, contentDescription = "إزالة من المفضلة", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
