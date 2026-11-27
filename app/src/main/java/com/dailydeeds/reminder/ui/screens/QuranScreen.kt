package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.dailydeeds.reminder.util.ArabicDigits
import com.dailydeeds.reminder.model.RevelationType
import com.dailydeeds.reminder.viewmodel.QuranViewModel
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.localNumber
import com.dailydeeds.reminder.ui.labelRes

@Composable
fun QuranScreen(
    viewModel: QuranViewModel,
    pagesMode: Boolean,
    lastPage: Int,
    onPagesModeChange: (Boolean) -> Unit,
    onNavigateToSurah: (Int) -> Unit,
    onOpenPage: (Int) -> Unit
) {
    val surahs by viewModel.surahs.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showGoTo by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        QuranModeBar(pagesMode, lastPage, onPagesModeChange, onOpenPage, onGoTo = { showGoTo = true })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.quran_search_hint), textAlign = TextAlign.Start) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.action_search)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.action_clear))
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(surahs, key = { it.number }) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.selectSurah(surah.number)
                            onNavigateToSurah(surah.number)
                        },
                    shape = MaterialTheme.shapes.medium,
                    border = com.dailydeeds.reminder.ui.components.cardBorder(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (surah.revelationType == RevelationType.MAKKI)
                                            MaterialTheme.colorScheme.tertiaryContainer
                                        else
                                            MaterialTheme.colorScheme.secondaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = stringResource(surah.revelationType.labelRes),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(R.string.quran_ayah_count, surah.ayahCount.toString()),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "سورة ${surah.nameArabic}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.quran_english_juz, surah.nameEnglish, surah.juzStart.toString()),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${surah.number}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showGoTo) {
        GoToPageDialog(onDismiss = { showGoTo = false }, onGo = { showGoTo = false; onOpenPage(it) })
    }
}

/** Chooses how the Quran opens: the mushaf page by page, or one surah as a scrolling list of ayahs. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuranModeBar(
    pagesMode: Boolean,
    lastPage: Int,
    onPagesModeChange: (Boolean) -> Unit,
    onOpenPage: (Int) -> Unit,
    onGoTo: () -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FilterChip(selected = pagesMode, onClick = { onPagesModeChange(true) }, label = { Text(stringResource(R.string.quran_mode_pages)) })
        FilterChip(selected = !pagesMode, onClick = { onPagesModeChange(false) }, label = { Text(stringResource(R.string.quran_mode_list)) })
        if (pagesMode) {
            OutlinedButton(onClick = { onOpenPage(if (lastPage > 0) lastPage else 1) }) {
                Text(if (lastPage > 0) stringResource(R.string.quran_continue_page, localNumber(lastPage)) else stringResource(R.string.quran_open_mushaf))
            }
            OutlinedButton(onClick = onGoTo) { Text(stringResource(R.string.quran_page_or_juz)) }
        }
    }
}
