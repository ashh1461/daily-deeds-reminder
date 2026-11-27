package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import com.dailydeeds.reminder.ui.components.EmeraldBanner
import com.dailydeeds.reminder.ui.components.OrnamentDivider
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.ui.theme.GoldBright
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.LaunchedEffect
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import com.dailydeeds.reminder.data.QuranResume
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.dailydeeds.reminder.ui.components.KeepScreenOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.viewmodel.QuranViewModel
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.localNumber
import com.dailydeeds.reminder.ui.labelRes
import com.dailydeeds.reminder.ui.theme.RtlContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surahNumber: Int,
    viewModel: QuranViewModel,
    tools: ToolsViewModel,
    onOpenMizan: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit,
    initialAyah: Int = 0
) {
    val surah by viewModel.selectedSurah.collectAsState()
    val ayahs by viewModel.ayahs.collectAsState()
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()

    val favorites by tools.favorites.collectAsState()
    KeepScreenOn()
    val listState = rememberLazyListState()
    val currentSurah = surah ?: return
    LaunchedEffect(currentSurah.number) { tools.recordSurah(currentSurah.number) }
    // When a scroll by the reader ends, the ayah at the top is saved, so «تابع من حيث توقفت» reopens there. The first
    // value (no scroll yet) is skipped: opening the surah must not overwrite the saved ayah with its first one.
    val currentAyahs by rememberUpdatedState(ayahs)
    LaunchedEffect(currentSurah.number) {
        snapshotFlow { listState.isScrollInProgress }.drop(1).filter { !it }.collect {
            val list = currentAyahs
            if (list.isNotEmpty() && list.first().surahNumber == currentSurah.number) {
                tools.recordAyah(currentSurah.number, QuranResume.ayahOfFirstVisible(listState.firstVisibleItemIndex, list.size))
            }
        }
    }
    // Opened from search or favorites: bring that ayah into view once (item 0 is the surah banner, so ayah n is
    // item n). Saved, so coming back from Mizan or rotating keeps the reader where it was scrolled since.
    var jumped by rememberSaveable(currentSurah.number, initialAyah) { mutableStateOf(false) }
    LaunchedEffect(currentSurah.number, initialAyah, ayahs.size) {
        if (!jumped && initialAyah in 1..ayahs.size && ayahs.first().surahNumber == currentSurah.number) {
            listState.scrollToItem(initialAyah)
            jumped = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "سورة ${currentSurah.nameArabic}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(
                                R.string.quran_surah_meta, stringResource(currentSurah.revelationType.labelRes),
                                currentSurah.ayahCount.toString(), currentSurah.juzStart.toString()
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.increaseFontSize() }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = stringResource(R.string.font_larger))
                    }
                    IconButton(onClick = { viewModel.decreaseFontSize() }) {
                        Icon(Icons.Default.ZoomOut, contentDescription = stringResource(R.string.font_smaller))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues -> RtlContent {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    EmeraldBanner(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "سورة ${currentSurah.nameArabic}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = GoldBright,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringResource(
                                    R.string.quran_surah_meta, stringResource(currentSurah.revelationType.labelRes),
                                    currentSurah.ayahCount.toString(), currentSurah.juzStart.toString()
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            if (currentSurah.number != 1 && currentSurah.number != 9) {
                                OrnamentDivider(Modifier.padding(vertical = 10.dp), color = GoldBright)
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = (fontSizeSp + 2).sp),
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                items(ayahs) { ayah ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        border = cardBorder(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                val fav = FavoriteKey.Ayah(ayah.surahNumber, ayah.ayahNumber)
                                IconButton(onClick = { tools.toggleFavorite(fav) }) {
                                    Icon(
                                        if (fav in favorites) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = stringResource(R.string.action_favorites),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                OutlinedButton(
                                    onClick = { onOpenMizan(ayah.surahNumber, ayah.ayahNumber) }
                                ) {
                                    Icon(
                                        Icons.Default.MenuBook,
                                        contentDescription = stringResource(R.string.hub_mizan),
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                    Text(stringResource(R.string.hub_mizan), style = MaterialTheme.typography.labelMedium)
                                }
                                }

                                Box(
                                    modifier = Modifier
                                        .border(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f), CircleShape)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                                            shape = CircleShape
                                        )
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "﴿${ayah.ayahNumber}﴾",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = ayah.textArabic,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp * 1.9f).sp
                                ),
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
}
