package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.rememberUpdatedState
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.dailydeeds.reminder.ui.components.KeepScreenOn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.QuranPages
import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.ui.components.EmeraldBanner
import com.dailydeeds.reminder.ui.components.OrnamentDivider
import com.dailydeeds.reminder.ui.theme.GoldBright
import com.dailydeeds.reminder.util.ArabicDigits
import com.dailydeeds.reminder.viewmodel.QuranViewModel
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.localNumber
import com.dailydeeds.reminder.ui.surahDisplayName

private const val AYAH_TAG = "ayah"
internal const val MUSHAF_BASMALA = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"

/**
 * The Quran as the 604 pages of the Madani mushaf, turned sideways like a book (right to left). Tapping an ayah
 * offers the favorite star and Tafsir al-Mizan. [highlight] marks the ayah that was opened from search or favorites.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuranMushafScreen(
    initialPage: Int,
    highlight: FavoriteKey.Ayah?,
    viewModel: QuranViewModel,
    tools: ToolsViewModel,
    onOpenMizan: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val fontSizeSp by viewModel.mushafFontSp.collectAsState()
    val fit by viewModel.mushafFit.collectAsState()
    // Size each page chose in fit mode, so zooming starts from what is on screen.
    val fittedSizes = remember { mutableStateMapOf<Int, Float>() }
    val favorites by tools.favorites.collectAsState()
    val favoriteSet = remember(favorites) { favorites.toSet() }
    val pagerState = rememberPagerState(initialPage = QuranPages.clamp(initialPage) - 1) { QuranPages.COUNT }
    val scope = rememberCoroutineScope()
    var tapped by remember { mutableStateOf<Ayah?>(null) }
    var showGoTo by rememberSaveable { mutableStateOf(false) }
    val current = remember(pagerState.currentPage) { QuranPages.page(pagerState.currentPage + 1) }
    // The page that was opened scrolls to its highlighted ayah once, not again each time it is swiped back into view.
    var highlightShown by rememberSaveable { mutableStateOf(false) }
    KeepScreenOn()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { index ->
            val page = QuranPages.page(index + 1)
            tools.recordQuranPage(page.number, page.firstAyah.surahNumber)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(stringResource(R.string.mushaf_page, localNumber(current.number)), fontWeight = FontWeight.Bold)
                            Text(
                                (current.segments.map { stringResource(R.string.surah_title, surahDisplayName(it.surah)) } +
                                    stringResource(R.string.juz_n, localNumber(current.juz))).joinToString(" • "),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                        }
                    },
                    actions = {
                        IconButton(onClick = { showGoTo = true }) {
                            Icon(Icons.Default.FindInPage, contentDescription = stringResource(R.string.mushaf_go_to))
                        }
                        val onScreenSp = if (fit) fittedSizes[current.number] ?: fontSizeSp else fontSizeSp
                        IconButton(onClick = { viewModel.setMushafFit(!fit) }) {
                            Icon(
                                Icons.Default.AspectRatio,
                                contentDescription = stringResource(if (fit) R.string.mushaf_fit_on else R.string.mushaf_fit),
                                tint = if (fit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { viewModel.increaseMushafFont(onScreenSp) }) {
                            Icon(Icons.Default.ZoomIn, contentDescription = stringResource(R.string.font_larger))
                        }
                        IconButton(onClick = { viewModel.decreaseMushafFont(onScreenSp) }) {
                            Icon(Icons.Default.ZoomOut, contentDescription = stringResource(R.string.font_smaller))
                        }
                    }
                )
            }
        ) { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(padding),
                beyondViewportPageCount = 1,
                key = { it }
            ) { index ->
                val page = remember(index) { QuranPages.page(index + 1) }
                MushafPage(
                    page, fontSizeSp, fit, favoriteSet, highlight,
                    scrollToHighlight = !highlightShown,
                    onHighlightShown = { highlightShown = true },
                    onFitted = { fittedSizes[page.number] = it },
                    onAyahTap = { tapped = it }
                )
            }
        }
    }

    tapped?.let { ayah ->
        AyahActionsDialog(
            ayah = ayah,
            isFavorite = FavoriteKey.Ayah(ayah.surahNumber, ayah.ayahNumber) in favoriteSet,
            onToggleFavorite = { tools.toggleFavorite(FavoriteKey.Ayah(ayah.surahNumber, ayah.ayahNumber)) },
            onOpenMizan = { onOpenMizan(ayah.surahNumber, ayah.ayahNumber) },
            onDismiss = { tapped = null }
        )
    }
    if (showGoTo) {
        GoToPageDialog(
            onDismiss = { showGoTo = false },
            onGo = { page ->
                showGoTo = false
                scope.launch { pagerState.scrollToPage(QuranPages.clamp(page) - 1) }
            }
        )
    }
}

@Composable
private fun MushafPage(
    page: QuranPages.Page,
    fontSizeSp: Float,
    fit: Boolean,
    favorites: Set<FavoriteKey>,
    highlight: FavoriteKey.Ayah?,
    scrollToHighlight: Boolean,
    onHighlightShown: () -> Unit,
    onFitted: (Float) -> Unit,
    onAyahTap: (Ayah) -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val fontFamilyResolver = LocalFontFamilyResolver.current
        val layoutDirection = LocalLayoutDirection.current
        val bodyStyle = MaterialTheme.typography.bodyLarge
        val titleStyle = MaterialTheme.typography.titleLarge
        val footerStyle = MaterialTheme.typography.labelLarge
        val maxW = constraints.maxWidth
        val maxH = constraints.maxHeight
        // Fitting measures the page about nine times, so it runs off the main thread, outside composition, and its
        // result is cached by (page, size, that page's favourites): swiping back or starring elsewhere costs nothing.
        val key = remember(page.number, favorites, maxW, maxH, density) {
            mushafFitKey(page, favorites, maxW, maxH, density.density, density.fontScale)
        }
        // Kept per page and screen size, not per key: when a star on this page changes the key, the page keeps its
        // current size (and its scroll position) until the new fit is ready, instead of going blank.
        var fitted by remember(page.number, maxW, maxH, density) { mutableStateOf(mushafFits.get(key)) }
        if (fit) {
            LaunchedEffect(key) {
                fitted = mushafFits.get(key) ?: withContext(Dispatchers.Default) {
                    val measurer = TextMeasurer(fontFamilyResolver, density, layoutDirection, cacheSize = 0)
                    fitMushafFontSize(page, key.favorites, measurer, density, bodyStyle, titleStyle, footerStyle, maxW, maxH)
                }.also { mushafFits.put(key, it) }
            }
        }
        val fittedSize = if (fit) fitted else null
        fittedSize?.let { LaunchedEffect(page.number, it) { onFitted(it) } }
        val scrollState = rememberScrollState()
        // Only a page's first fit leaves it blank for that moment, rather than jumping from one size to another.
        if (fit && fittedSize == null) {
            Box(Modifier.fillMaxSize())
            return@BoxWithConstraints
        }
        val size = fittedSize ?: fontSizeSp

        val highlightOnPage = highlight != null && page.segments.any { seg ->
            seg.surah.number == highlight.surah && seg.ayahs.any { it.ayahNumber == highlight.ayah }
        }
        // Where the highlighted ayah's line starts inside the page, once its run has been laid out.
        var highlightY by remember(page.number, size) { mutableStateOf<Float?>(null) }
        if (scrollToHighlight && highlightOnPage) {
            LaunchedEffect(highlightY) {
                val y = highlightY ?: return@LaunchedEffect
                val margin = with(density) { (MushafLayout.Spacing * 4).toPx() }
                scrollState.animateScrollTo((y - margin).toInt().coerceAtLeast(0))
                onHighlightShown()
            }
        }
        val topPadding = with(density) { MushafLayout.VerticalPadding.toPx() }
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = MushafLayout.HorizontalPadding, vertical = MushafLayout.VerticalPadding),
            verticalArrangement = Arrangement.spacedBy(MushafLayout.Spacing)
        ) {
            page.segments.forEach { segment ->
                if (segment.startsSurah) SurahTitle(segment.surah.number, segment.surah.nameArabic, size)
                val runHighlight = highlight?.takeIf { it.surah == segment.surah.number }
                AyahRun(
                    segment.ayahs, size, favorites, runHighlight, onAyahTap,
                    onHighlightLaidOut = if (runHighlight == null) null else { top -> highlightY = topPadding + top }
                )
            }
            Text(
                ArabicDigits.format(page.number),
                modifier = Modifier.fillMaxWidth().padding(top = MushafLayout.FooterTop, bottom = MushafLayout.FooterBottom),
                textAlign = TextAlign.Center,
                style = footerStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SurahTitle(number: Int, name: String, fontSizeSp: Float) {
    EmeraldBanner(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("سورة $name", style = MaterialTheme.typography.titleLarge, color = GoldBright, textAlign = TextAlign.Center)
            // Al-Fatiha's basmala is its first ayah, and al-Tawba has none.
            if (number != 1 && number != 9) {
                OrnamentDivider(Modifier.padding(vertical = 6.dp), color = GoldBright)
                Text(
                    MUSHAF_BASMALA,
                    style = MushafLayout.basmalaStyle(MaterialTheme.typography.titleLarge, fontSizeSp),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * One surah's ayahs on the page as continuous justified text, each closed by its number. Each ayah is a link, so a tap
 * or TalkBack's link navigation opens its actions, and each also has a TalkBack custom action.
 */
@Composable
private fun AyahRun(
    ayahs: List<Ayah>,
    fontSizeSp: Float,
    favorites: Set<FavoriteKey>,
    highlight: FavoriteKey.Ayah?,
    onAyahTap: (Ayah) -> Unit,
    onHighlightLaidOut: ((Float) -> Unit)? = null
) {
    val marker = MaterialTheme.colorScheme.primary
    val favoriteMarker = MaterialTheme.colorScheme.tertiary
    val highlightBackground = MaterialTheme.colorScheme.primaryContainer
    val currentTap by rememberUpdatedState(onAyahTap)
    val text = remember(ayahs, favorites, highlight, marker, favoriteMarker, highlightBackground) {
        ayahText(ayahs, favorites, highlight, marker, favoriteMarker, highlightBackground) { currentTap(it) }
    }
    val actions = remember(ayahs) {
        ayahs.map { ayah ->
            CustomAccessibilityAction("الآية ${ArabicDigits.format(ayah.ayahNumber)}: المفضلة والتفسير") {
                currentTap(ayah)
                true
            }
        }
    }
    val highlightStart = remember(text, highlight) {
        highlight?.let { h -> text.getStringAnnotations(AYAH_TAG, 0, text.length).firstOrNull { it.item == h.ayah.toString() }?.start }
    }
    var runTop by remember { mutableStateOf<Float?>(null) }
    var lineTop by remember { mutableStateOf<Float?>(null) }
    if (onHighlightLaidOut != null) {
        val top = runTop
        val line = lineTop
        if (top != null && line != null) LaunchedEffect(top, line) { onHighlightLaidOut(top + line) }
    }
    Text(
        text = text,
        style = MushafLayout.ayahStyle(MaterialTheme.typography.bodyLarge, fontSizeSp, MaterialTheme.colorScheme.onSurface),
        onTextLayout = { layout ->
            lineTop = highlightStart?.let { layout.getLineTop(layout.getLineForOffset(it)) }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { runTop = it.positionInParent().y }
            .semantics { customActions = actions }
    )
}

/**
 * The run's text. Each ayah carries an [AYAH_TAG] annotation with its number; with [onTap] it is also a link to its
 * actions (fitting measures the text without links).
 */
internal fun ayahText(
    ayahs: List<Ayah>,
    favorites: Set<FavoriteKey>,
    highlight: FavoriteKey.Ayah?,
    marker: Color,
    favoriteMarker: Color,
    highlightBackground: Color,
    onTap: ((Ayah) -> Unit)? = null
): AnnotatedString = buildAnnotatedString {
    ayahs.forEachIndexed { i, ayah ->
        val start = length
        val write: AnnotatedString.Builder.() -> Unit = {
            val highlighted = highlight?.surah == ayah.surahNumber && highlight.ayah == ayah.ayahNumber
            if (highlighted) withStyle(SpanStyle(background = highlightBackground)) { append(ayah.textArabic) }
            else append(ayah.textArabic)
            val favorite = FavoriteKey.Ayah(ayah.surahNumber, ayah.ayahNumber) in favorites
            append(' ')
            withStyle(SpanStyle(color = if (favorite) favoriteMarker else marker, fontWeight = FontWeight.Bold)) {
                if (favorite) append("★")
                append("﴿${ArabicDigits.format(ayah.ayahNumber)}﴾")
            }
        }
        if (onTap == null) write() else {
            withLink(LinkAnnotation.Clickable(tag = "$AYAH_TAG:${ayah.ayahNumber}") { onTap(ayah) }) { write() }
        }
        addStringAnnotation(AYAH_TAG, ayah.ayahNumber.toString(), start, length)
        if (i < ayahs.lastIndex) append(' ')
    }
}

@Composable
private fun AyahActionsDialog(
    ayah: Ayah,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenMizan: () -> Unit,
    onDismiss: () -> Unit
) {
    val surahName = remember(ayah.surahNumber, ayah.page) {
        QuranPages.page(ayah.page).segments.firstOrNull { it.surah.number == ayah.surahNumber }?.surah?.nameArabic.orEmpty()
    }
    val context = LocalContext.current
    val shareText = remember(ayah, surahName) { ayahShareText(ayah, surahName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.favorites_ayah_title, surahName, localNumber(ayah.ayahNumber))) },
        text = {
            Column {
                // The whole ayah, scrolling when it is long, instead of cutting it at six lines.
                Text(ayah.textArabic, modifier = Modifier.heightIn(max = 320.dp).verticalScroll(rememberScrollState()))
                Row {
                    TextButton(onClick = {
                        context.getSystemService(ClipboardManager::class.java)
                            ?.setPrimaryClip(ClipData.newPlainText("آية", shareText))
                        // Android 13 and later confirm a copy on their own.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            Toast.makeText(context, context.getString(R.string.ayah_copied), Toast.LENGTH_SHORT).show()
                        }
                    }) { Text(stringResource(R.string.action_copy)) }
                    TextButton(onClick = {
                        val send = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, shareText)
                        context.startActivity(Intent.createChooser(send, context.getString(R.string.ayah_share)))
                    }) { Text(stringResource(R.string.action_share)) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onToggleFavorite(); onDismiss() }) {
                Text(stringResource(if (isFavorite) R.string.favorite_remove else R.string.favorite_add))
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDismiss(); onOpenMizan() }) { Text(stringResource(R.string.hub_mizan)) }
                TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_close)) }
            }
        }
    )
}

/** The ayah with its number and its place, as copied or shared. */
internal fun ayahShareText(ayah: Ayah, surahName: String): String =
    "${ayah.textArabic} ﴿${ArabicDigits.format(ayah.ayahNumber)}﴾\n(سورة $surahName، الآية ${ArabicDigits.format(ayah.ayahNumber)})"

/** Jump by page number (1-604) or juz (1-30); Arabic-Indic digits are accepted. */
@Composable
internal fun GoToPageDialog(onDismiss: () -> Unit, onGo: (Int) -> Unit) {
    var pageText by remember { mutableStateOf("") }
    var juzText by remember { mutableStateOf("") }
    val page = ArabicDigits.parsePositive(pageText)?.takeIf { it <= QuranPages.COUNT }
    val juz = ArabicDigits.parsePositive(juzText)?.takeIf { it <= QuranPages.JUZ_COUNT }
    val target = page ?: juz?.let { QuranPages.firstPageOfJuz(it) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.mushaf_go_to_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = pageText, onValueChange = { pageText = it; if (it.isNotEmpty()) juzText = "" },
                    singleLine = true, label = { Text(stringResource(R.string.mushaf_page_number, localNumber(1), localNumber(604))) },
                    isError = pageText.isNotBlank() && page == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = juzText, onValueChange = { juzText = it; if (it.isNotEmpty()) pageText = "" },
                    singleLine = true, label = { Text(stringResource(R.string.mushaf_juz_number, localNumber(1), localNumber(30))) },
                    isError = juzText.isNotBlank() && juz == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = { TextButton(enabled = target != null, onClick = { target?.let(onGo) }) { Text(stringResource(R.string.mushaf_go)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_cancel)) } }
    )
}
