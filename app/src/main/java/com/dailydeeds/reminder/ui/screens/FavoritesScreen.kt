package com.dailydeeds.reminder.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.Saver
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.FavoritesList
import com.dailydeeds.reminder.util.ArabicDigits
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.data.WeekdayRepository
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.time.DayOfWeek
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.appLocale
import androidx.compose.ui.platform.LocalContext
import com.dailydeeds.reminder.model.Surah

private data class FavoriteRow(val key: FavoriteKey, val title: String, val subtitle: String)

/** Selection and the undo list survive rotation, stored in the favorites' own text format. */
private val KeySetSaver = Saver<Set<FavoriteKey>, String>(
    save = { FavoritesList.serialize(it.toList()) },
    restore = { FavoritesList.parse(it).toSet() }
)

private const val SNAPSHOT_PREFIX = "S"

private val SnapshotSaver = Saver<List<FavoriteKey>?, String>(
    save = { list -> list?.let { SNAPSHOT_PREFIX + FavoritesList.serialize(it) } },
    restore = { FavoritesList.parse(it.removePrefix(SNAPSHOT_PREFIX)) }
)

/** Narrows the list by kind, so "select all" can clear one kind at once. */
private enum class FavoriteFilter(@StringRes val label: Int) {
    ALL(R.string.search_type_all), AYAH(R.string.favorites_ayahs), MAFATIH(R.string.tab_mafatih), SAHIFA(R.string.tab_sahifa),
    WEEKDAY(R.string.search_type_weekday);

    fun matches(key: FavoriteKey): Boolean = when (this) {
        ALL -> true
        AYAH -> key is FavoriteKey.Ayah
        MAFATIH -> key is FavoriteKey.Mafatih
        SAHIFA -> key is FavoriteKey.Sahifa
        WEEKDAY -> key is FavoriteKey.Weekday
    }
}

/**
 * Favorites with in-place removal: the trash button deletes one item, and «تحديد» (or a long press) selects several
 * to delete together. Every delete can be undone from the bar that appears at the top of the list.
 */
@Composable
fun FavoritesScreen(
    viewModel: ToolsViewModel,
    onNavigateBack: () -> Unit,
    onOpenAyah: (Int, Int) -> Unit,
    onOpenMafatih: (String) -> Unit,
    onOpenWeekday: (DayContentKind, DayOfWeek) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()
    val quran = remember { QuranRepository() }
    val mafatih = remember { MafatihRepository() }
    // Rows are built outside composition, so the strings they need are resolved here (the activity's context
    // carries the app language; changing it recreates the screen).
    val context = LocalContext.current
    val english = appLocale().language == "en"
    val sahifaLabel = stringResource(R.string.tab_sahifa_title)
    val rows = remember(favorites) {
        val ayahTitle = { s: Surah, n: Int ->
            context.getString(
                R.string.favorites_ayah_title, if (english) s.nameEnglish else s.nameArabic,
                if (english) n.toString() else ArabicDigits.format(n)
            )
        }
        favorites.mapNotNull { favoriteRow(it, quran, mafatih, ayahTitle, sahifaLabel) }
    }

    var chosenFilter by rememberSaveable { mutableStateOf(FavoriteFilter.ALL) }
    var selecting by rememberSaveable { mutableStateOf(false) }
    var selected by rememberSaveable(stateSaver = KeySetSaver) { mutableStateOf(emptySet()) }
    var confirmBulk by rememberSaveable { mutableStateOf(false) }
    var undoSnapshot by rememberSaveable(stateSaver = SnapshotSaver) { mutableStateOf(null) }

    // A kind emptied by a delete has no chip any more, so fall back to "all" instead of an empty list.
    val filter = if (rows.any { chosenFilter.matches(it.key) }) chosenFilter else FavoriteFilter.ALL
    val visible = rows.filter { filter.matches(it.key) }
    val visibleKeys = visible.map { it.key }.toSet()
    val selectedVisible = selected intersect visibleKeys

    fun endSelection() {
        selecting = false
        selected = emptySet()
    }

    fun delete(keys: Set<FavoriteKey>) {
        if (keys.isEmpty()) return
        // Keep the list from before the first delete while the undo bar shows, so «تراجع» restores every one.
        undoSnapshot = undoSnapshot ?: favorites
        viewModel.removeFavorites(keys)
        endSelection()
    }

    fun open(key: FavoriteKey) = when (key) {
        is FavoriteKey.Ayah -> onOpenAyah(key.surah, key.ayah)
        is FavoriteKey.Mafatih -> onOpenMafatih(key.itemId)
        is FavoriteKey.Sahifa -> onOpenMafatih(key.itemId)
        is FavoriteKey.Weekday -> onOpenWeekday(key.kind, key.day)
    }

    BackHandler(enabled = selecting) { endSelection() }

    ToolScaffold(stringResource(R.string.action_favorites), onNavigateBack) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            undoSnapshot?.let { snapshot ->
                item(key = "undo") {
                    UndoBar(
                        removed = FavoritesList.removedSince(snapshot, favorites).coerceAtLeast(1),
                        onUndo = { viewModel.restoreFavorites(snapshot); undoSnapshot = null },
                        onDismiss = { undoSnapshot = null }
                    )
                }
            }
            if (rows.isEmpty()) {
                item(key = "empty") {
                    Text(
                        stringResource(R.string.favorites_empty),
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                item(key = "filters") {
                    FilterChips(rows, filter, onSelect = { chosenFilter = it })
                }
                item(key = "actions") {
                    SelectionBar(
                        selecting = selecting,
                        visibleCount = visible.size,
                        selectedCount = selectedVisible.size,
                        onStart = { selecting = true },
                        onToggleAll = { selected = if (selectedVisible.size == visibleKeys.size) emptySet() else visibleKeys },
                        onDelete = { confirmBulk = true },
                        onCancel = { endSelection() }
                    )
                }
                items(visible, key = { it.key.encode() }) { row ->
                    FavoriteCard(
                        row = row,
                        selecting = selecting,
                        checked = row.key in selected,
                        onClick = {
                            if (selecting) selected = if (row.key in selected) selected - row.key else selected + row.key
                            else open(row.key)
                        },
                        onLongClick = { selecting = true; selected = selected + row.key },
                        onDelete = { delete(setOf(row.key)) }
                    )
                }
            }
        }
    }

    if (confirmBulk) {
        val count = selectedVisible.size
        AlertDialog(
            onDismissRequest = { confirmBulk = false },
            title = { Text(stringResource(R.string.favorites_delete_title, count.toString())) },
            text = { Text(stringResource(R.string.favorites_delete_text)) },
            confirmButton = { TextButton(onClick = { confirmBulk = false; delete(selectedVisible) }) { Text(stringResource(R.string.common_delete)) } },
            dismissButton = { TextButton(onClick = { confirmBulk = false }) { Text(stringResource(R.string.common_cancel)) } }
        )
    }
}

private fun favoriteRow(
    key: FavoriteKey,
    quran: QuranRepository,
    mafatih: MafatihRepository,
    ayahTitle: (Surah, Int) -> String,
    sahifaLabel: String
): FavoriteRow? = when (key) {
    is FavoriteKey.Ayah -> {
        val s = quran.getSurahByNumber(key.surah)
        val a = quran.getAyah(key.surah, key.ayah)
        if (s != null && a != null) {
            FavoriteRow(key, ayahTitle(s, key.ayah), a.textArabic)
        } else null
    }
    is FavoriteKey.Mafatih -> mafatih.getItemById(key.itemId)?.let { FavoriteRow(key, it.title, it.category.titleArabic) }
    is FavoriteKey.Sahifa -> mafatih.getItemById(key.itemId)?.let { FavoriteRow(key, it.title, sahifaLabel) }
    is FavoriteKey.Weekday -> {
        val c = WeekdayRepository.get(key.kind, key.day)
        FavoriteRow(key, c.title, key.kind.titleArabic)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterChips(rows: List<FavoriteRow>, current: FavoriteFilter, onSelect: (FavoriteFilter) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        FavoriteFilter.values().forEach { f ->
            val count = rows.count { f.matches(it.key) }
            if (f == FavoriteFilter.ALL || count > 0) {
                FilterChip(selected = f == current, onClick = { onSelect(f) }, label = { Text("${stringResource(f.label)} ($count)") })
            }
        }
    }
}

@Composable
private fun SelectionBar(
    selecting: Boolean,
    visibleCount: Int,
    selectedCount: Int,
    onStart: () -> Unit,
    onToggleAll: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if (!selecting) {
            Text(stringResource(R.string.favorites_count, visibleCount.toString()), Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
            TextButton(onClick = onStart, enabled = visibleCount > 0) { Text(stringResource(R.string.favorites_select)) }
        } else {
            Text(stringResource(R.string.favorites_selected, selectedCount.toString()), Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
            TextButton(onClick = onToggleAll) { Text(stringResource(if (selectedCount == visibleCount) R.string.favorites_unselect_all else R.string.favorites_select_all)) }
            TextButton(onClick = onDelete, enabled = selectedCount > 0) { Text(stringResource(R.string.common_delete)) }
            TextButton(onClick = onCancel) { Text(stringResource(R.string.common_cancel)) }
        }
    }
}

@Composable
private fun UndoBar(removed: Int, onUndo: () -> Unit, onDismiss: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Row(Modifier.fillMaxWidth().padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (removed == 1) stringResource(R.string.favorites_removed_one) else stringResource(R.string.favorites_removed_n, removed.toString()),
                Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            TextButton(onClick = onUndo) { Text(stringResource(R.string.action_undo)) }
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_hide)) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FavoriteCard(
    row: FavoriteRow,
    selecting: Boolean,
    checked: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(Modifier.fillMaxWidth().combinedClickable(onClick = onClick, onLongClick = onLongClick)) {
        Row(Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (selecting) Checkbox(checked = checked, onCheckedChange = { onClick() })
            Column(Modifier.weight(1f).padding(start = 8.dp)) {
                Text(row.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(
                    row.subtitle, maxLines = 2, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!selecting) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.favorite_remove), tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
