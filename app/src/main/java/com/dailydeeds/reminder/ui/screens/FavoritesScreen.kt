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
private enum class FavoriteFilter(val label: String) {
    ALL("الكل"), AYAH("الآيات"), MAFATIH("المفاتيح"), SAHIFA("الصحيفة"), WEEKDAY("أدعية وزيارات الأيام");

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
    onOpenWeekday: (DayContentKind) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()
    val quran = remember { QuranRepository() }
    val mafatih = remember { MafatihRepository() }
    val rows = remember(favorites) { favorites.mapNotNull { favoriteRow(it, quran, mafatih) } }

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
        is FavoriteKey.Weekday -> onOpenWeekday(key.kind)
    }

    BackHandler(enabled = selecting) { endSelection() }

    ToolScaffold("المفضلة", onNavigateBack) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            undoSnapshot?.let { snapshot ->
                item(key = "undo") {
                    UndoBar(
                        removed = (snapshot.size - favorites.size).coerceAtLeast(1),
                        onUndo = { viewModel.restoreFavorites(snapshot); undoSnapshot = null },
                        onDismiss = { undoSnapshot = null }
                    )
                }
            }
            if (rows.isEmpty()) {
                item(key = "empty") {
                    Text(
                        "لا توجد عناصر مفضلة بعد. اضغط على النجمة في أي آية أو دعاء أو زيارة لإضافتها هنا.",
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
            title = { Text("حذف $count من المفضلة؟") },
            text = { Text("تُزال العناصر المحددة من المفضلة فقط، ولا يُحذف أي نص. يمكنك التراجع بعد الحذف مباشرة.") },
            confirmButton = { TextButton(onClick = { confirmBulk = false; delete(selectedVisible) }) { Text("حذف") } },
            dismissButton = { TextButton(onClick = { confirmBulk = false }) { Text("إلغاء") } }
        )
    }
}

private fun favoriteRow(key: FavoriteKey, quran: QuranRepository, mafatih: MafatihRepository): FavoriteRow? = when (key) {
    is FavoriteKey.Ayah -> {
        val s = quran.getSurahByNumber(key.surah)
        val a = quran.getAyah(key.surah, key.ayah)
        if (s != null && a != null) {
            FavoriteRow(key, "سورة ${s.nameArabic} - الآية ${ArabicDigits.format(key.ayah)}", a.textArabic)
        } else null
    }
    is FavoriteKey.Mafatih -> mafatih.getItemById(key.itemId)?.let { FavoriteRow(key, it.title, it.category.titleArabic) }
    is FavoriteKey.Sahifa -> mafatih.getItemById(key.itemId)?.let { FavoriteRow(key, it.title, "الصحيفة السجادية") }
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
                FilterChip(selected = f == current, onClick = { onSelect(f) }, label = { Text("${f.label} ($count)") })
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
            Text("$visibleCount عنصر", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
            TextButton(onClick = onStart, enabled = visibleCount > 0) { Text("تحديد") }
        } else {
            Text("تم تحديد $selectedCount", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
            TextButton(onClick = onToggleAll) { Text(if (selectedCount == visibleCount) "إلغاء الكل" else "تحديد الكل") }
            TextButton(onClick = onDelete, enabled = selectedCount > 0) { Text("حذف") }
            TextButton(onClick = onCancel) { Text("إلغاء") }
        }
    }
}

@Composable
private fun UndoBar(removed: Int, onUndo: () -> Unit, onDismiss: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Row(Modifier.fillMaxWidth().padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (removed == 1) "حُذف عنصر من المفضلة" else "حُذف $removed من المفضلة",
                Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            TextButton(onClick = onUndo) { Text("تراجع") }
            TextButton(onClick = onDismiss) { Text("إخفاء") }
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
                    Icon(Icons.Default.Delete, contentDescription = "حذف من المفضلة", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
