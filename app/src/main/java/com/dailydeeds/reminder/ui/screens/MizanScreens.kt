package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.data.MizanData
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.model.MizanEntry
import com.dailydeeds.reminder.model.MizanHeader
import com.dailydeeds.reminder.model.MizanKind
import com.dailydeeds.reminder.model.MizanParagraph
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.MizanState
import com.dailydeeds.reminder.viewmodel.MizanViewModel

private fun rangeLabel(h: MizanHeader) = if (h.from == h.to) "الآية ${h.from}" else "الآيات ${h.from} – ${h.to}"

private fun entryTitle(h: MizanHeader, surahName: (Int) -> String): String =
    if (h.kind == MizanKind.SECTION) "سورة ${surahName(h.surah)} · ${rangeLabel(h)}" else h.title

@Composable
private fun StatusBox(state: MizanState, onRetry: () -> Unit, content: @Composable (MizanData) -> Unit) {
    when (state) {
        MizanState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CircularProgressIndicator()
                Text("جارٍ تجهيز الكتاب (في المرة الأولى فقط)…", style = MaterialTheme.typography.bodyMedium)
            }
        }
        is MizanState.Failed -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("تعذّر فتح تفسير الميزان: ${state.message}", textAlign = TextAlign.Center)
                Button(onClick = onRetry) { Text("إعادة المحاولة") }
            }
        }
        is MizanState.Ready -> content(state.data)
    }
}

/** The book: search, the surahs and the front matter and indices. */
@Composable
fun MizanHomeScreen(
    viewModel: MizanViewModel,
    onOpenSurah: (Int) -> Unit,
    onOpenEntry: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    ToolScaffold("تفسير الميزان", onNavigateBack) {
        StatusBox(state, viewModel::load) { data -> MizanHomeContent(data, viewModel, onOpenSurah, onOpenEntry) }
    }
}

@Composable
private fun MizanHomeContent(
    data: MizanData,
    viewModel: MizanViewModel,
    onOpenSurah: (Int) -> Unit,
    onOpenEntry: (Int, Int) -> Unit
) {
    val surahs = remember { QuranRepository().getAllSurahs() }
    val names = remember(surahs) { surahs.associate { it.number to it.nameArabic } }
    val search by viewModel.search.collectAsState()
    var text by rememberSaveable { mutableStateOf(search.query) }

    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("ابحث في نص الميزان كاملاً") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = { text = ""; viewModel.clearSearch() }) { Icon(Icons.Default.Clear, contentDescription = "مسح") }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.search(text) })
            )
        }
        if (search.query.isNotEmpty()) {
            if (search.running) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LinearProgressIndicator(progress = { search.progress }, modifier = Modifier.fillMaxWidth())
                        Text("جارٍ البحث في الكتاب…", style = MaterialTheme.typography.bodySmall)
                    }
                }
            } else if (search.hits.isEmpty()) {
                item { Text("لا نتائج لـ «${search.query}».", style = MaterialTheme.typography.bodyMedium) }
            } else {
                item {
                    Text(
                        "النتائج (حتى ${search.hits.size})",
                        style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                    )
                }
                itemsIndexed(search.hits) { _, hit ->
                    val header = data.header(hit.entryId)
                    MizanCard(
                        title = header?.let { entryTitle(it, { n -> names[n].orEmpty() }) } ?: "",
                        subtitle = hit.snippet,
                        onClick = { onOpenEntry(hit.entryId, hit.paragraphIndex) }
                    )
                }
            }
        } else {
            item {
                Text(
                    "الميزان في تفسير القرآن للعلامة الطباطبائي: النص الكامل بلا اختصار، موزّع على ${data.headers.count { it.kind == MizanKind.SECTION }} موضعاً من آيات القرآن، مع المقدمة وفهارس الأجزاء.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            data.front?.let { front ->
                item { MizanCard(title = front.title, subtitle = "مقدمة المؤلف وصفحات الكتاب الأولى", onClick = { onOpenEntry(front.id, 0) }) }
            }
            items(surahs, key = { it.number }) { surah ->
                val count = data.sectionsOf(surah.number).size
                MizanCard(
                    title = "${surah.number}. سورة ${surah.nameArabic}",
                    subtitle = "$count موضعاً من التفسير",
                    onClick = { onOpenSurah(surah.number) }
                )
            }
            item {
                Text(
                    "فهارس الأجزاء", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(data.indexes, key = { it.id }) { h ->
                MizanCard(title = h.title, subtitle = null, onClick = { onOpenEntry(h.id, 0) })
            }
        }
    }
}

@Composable
private fun MizanCard(title: String, subtitle: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 4)
            }
        }
    }
}

/** The sections of one surah. */
@Composable
fun MizanSurahScreen(
    surah: Int,
    viewModel: MizanViewModel,
    onOpenEntry: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val name = remember(surah) { QuranRepository().getSurahByNumber(surah)?.nameArabic.orEmpty() }
    ToolScaffold("الميزان · سورة $name", onNavigateBack) {
        StatusBox(state, viewModel::load) { data ->
            val sections = remember(data, surah) { data.sectionsOf(surah) }
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sections, key = { it.id }) { h ->
                    MizanCard(title = rangeLabel(h), subtitle = null, onClick = { onOpenEntry(h.id, 0) })
                }
            }
        }
    }
}

/** Finds the section that comments on one ayah and hands its id on. */
@Composable
fun MizanAyahScreen(
    surah: Int,
    ayah: Int,
    viewModel: MizanViewModel,
    onResolved: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val id = (state as? MizanState.Ready)?.data?.sectionIdFor(surah, ayah)
    LaunchedEffect(id) { id?.let(onResolved) }
    ToolScaffold("تفسير الميزان", onNavigateBack) {
        StatusBox(state, viewModel::load) {
            if (id == null) Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("لا يوجد قسم في الميزان لهذه الآية.", textAlign = TextAlign.Center)
            }
        }
    }
}

private fun quoted(text: String, color: Color): AnnotatedString = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        val open = text.indexOf('﴿', i)
        if (open < 0) {
            append(text.substring(i))
            break
        }
        append(text.substring(i, open))
        val close = text.indexOf('﴾', open)
        val end = if (close < 0) text.length else close + 1
        withStyle(SpanStyle(color = color, fontWeight = FontWeight.Bold)) { append(text.substring(open, end)) }
        i = end
    }
}

@Composable
private fun ParagraphView(p: MizanParagraph, fontSp: Float) {
    val level = p.headingLevel
    when {
        p.isPage -> {
            val parts = p.text.split(':')
            Column(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    "الجزء ${parts.getOrNull(0)} · الصفحة ${parts.getOrNull(1)}",
                    style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        level > 0 -> Text(
            p.text,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = (fontSp + (when (level) { 1 -> 6; 2 -> 4; 3 -> 2; else -> 0 })).sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        else -> {
            val color = MaterialTheme.colorScheme.secondary
            val styled = remember(p.text, color) { quoted(p.text, color) }
            Text(
                styled,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSp.sp, lineHeight = (fontSp * 1.8f).sp)
            )
        }
    }
}

/** One entry of the book, paragraph by paragraph, with the previous and next entry one tap away. */
@Composable
fun MizanReaderScreen(
    entryId: Int,
    paragraph: Int,
    viewModel: MizanViewModel,
    onOpenEntry: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val fontSp by viewModel.fontSp.collectAsState()
    val names = remember { QuranRepository().getAllSurahs().associate { it.number to it.nameArabic } }
    val data = (state as? MizanState.Ready)?.data
    val title = data?.header(entryId)?.let { entryTitle(it, { n -> names[n].orEmpty() }) } ?: "تفسير الميزان"

    ToolScaffold(title, onNavigateBack) {
        StatusBox(state, viewModel::load) { book ->
            var entry by remember(entryId) { mutableStateOf<MizanEntry?>(null) }
            LaunchedEffect(entryId, book) { entry = viewModel.readEntry(book, entryId) }
            val loaded = entry
            Column(Modifier.fillMaxSize()) {
                if (loaded == null) {
                    Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                } else {
                    val listState = rememberLazyListState()
                    LaunchedEffect(loaded) { if (paragraph > 0) listState.scrollToItem(paragraph.coerceIn(0, loaded.paragraphs.size - 1)) }
                    LazyColumn(
                        Modifier.weight(1f).fillMaxWidth(),
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(loaded.paragraphs) { _, p -> ParagraphView(p, fontSp) }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { onOpenEntry(entryId - 1) }, enabled = entryId > 0) { Text("السابق") }
                    Text("${entryId + 1} / ${book.headers.size}", Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium)
                    IconButton(onClick = viewModel::decreaseFont) { Icon(Icons.Default.ZoomOut, contentDescription = "تصغير الخط") }
                    IconButton(onClick = viewModel::increaseFont) { Icon(Icons.Default.ZoomIn, contentDescription = "تكبير الخط") }
                    OutlinedButton(onClick = { onOpenEntry(entryId + 1) }, enabled = entryId + 1 < book.headers.size) { Text("التالي") }
                }
            }
        }
    }
}
