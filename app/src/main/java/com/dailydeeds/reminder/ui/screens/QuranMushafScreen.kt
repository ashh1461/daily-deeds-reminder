package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
                            Text("صفحة ${ArabicDigits.format(current.number)}", fontWeight = FontWeight.Bold)
                            Text(
                                current.segments.joinToString(" • ") { "سورة ${it.surah.nameArabic}" } +
                                    " • الجزء ${ArabicDigits.format(current.juz)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showGoTo = true }) {
                            Icon(Icons.Default.FindInPage, contentDescription = "الانتقال إلى صفحة أو جزء")
                        }
                        val onScreenSp = if (fit) fittedSizes[current.number] ?: fontSizeSp else fontSizeSp
                        IconButton(onClick = { viewModel.setMushafFit(!fit) }) {
                            Icon(
                                Icons.Default.AspectRatio,
                                contentDescription = if (fit) "ملاءمة الصفحة للشاشة: مفعّلة" else "ملاءمة الصفحة للشاشة",
                                tint = if (fit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { viewModel.increaseMushafFont(onScreenSp) }) {
                            Icon(Icons.Default.ZoomIn, contentDescription = "تكبير الخط")
                        }
                        IconButton(onClick = { viewModel.decreaseMushafFont(onScreenSp) }) {
                            Icon(Icons.Default.ZoomOut, contentDescription = "تصغير الخط")
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
    onFitted: (Float) -> Unit,
    onAyahTap: (Ayah) -> Unit
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val measurer = rememberTextMeasurer()
        val density = LocalDensity.current
        val bodyStyle = MaterialTheme.typography.bodyLarge
        val titleStyle = MaterialTheme.typography.titleLarge
        val footerStyle = MaterialTheme.typography.labelLarge
        val maxW = constraints.maxWidth
        val maxH = constraints.maxHeight
        val fitted = if (!fit) null else remember(page.number, favorites, maxW, maxH, density, bodyStyle, titleStyle) {
            fitMushafFontSize(page, favorites, measurer, density, bodyStyle, titleStyle, footerStyle, maxW, maxH)
        }
        fitted?.let { LaunchedEffect(page.number, it) { onFitted(it) } }
        val size = fitted ?: fontSizeSp
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MushafLayout.HorizontalPadding, vertical = MushafLayout.VerticalPadding),
            verticalArrangement = Arrangement.spacedBy(MushafLayout.Spacing)
        ) {
            page.segments.forEach { segment ->
                if (segment.startsSurah) SurahTitle(segment.surah.number, segment.surah.nameArabic, size)
                AyahRun(segment.ayahs, size, favorites, highlight, onAyahTap)
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

/** One surah's ayahs on the page as continuous justified text, each closed by its number. */
@Composable
private fun AyahRun(
    ayahs: List<Ayah>,
    fontSizeSp: Float,
    favorites: Set<FavoriteKey>,
    highlight: FavoriteKey.Ayah?,
    onAyahTap: (Ayah) -> Unit
) {
    val marker = MaterialTheme.colorScheme.primary
    val favoriteMarker = MaterialTheme.colorScheme.tertiary
    val highlightBackground = MaterialTheme.colorScheme.primaryContainer
    val text = remember(ayahs, favorites, highlight, marker, favoriteMarker, highlightBackground) {
        ayahText(ayahs, favorites, highlight, marker, favoriteMarker, highlightBackground)
    }
    ClickableText(
        text = text,
        style = MushafLayout.ayahStyle(MaterialTheme.typography.bodyLarge, fontSizeSp, MaterialTheme.colorScheme.onSurface),
        modifier = Modifier.fillMaxWidth()
    ) { offset ->
        val id = text.getStringAnnotations(AYAH_TAG, offset, offset).firstOrNull()?.item?.toIntOrNull()
        ayahs.firstOrNull { it.ayahNumber == id }?.let(onAyahTap)
    }
}

internal fun ayahText(
    ayahs: List<Ayah>,
    favorites: Set<FavoriteKey>,
    highlight: FavoriteKey.Ayah?,
    marker: Color,
    favoriteMarker: Color,
    highlightBackground: Color
): AnnotatedString = buildAnnotatedString {
    ayahs.forEachIndexed { i, ayah ->
        val start = length
        val highlighted = highlight?.surah == ayah.surahNumber && highlight.ayah == ayah.ayahNumber
        if (highlighted) withStyle(SpanStyle(background = highlightBackground)) { append(ayah.textArabic) }
        else append(ayah.textArabic)
        val favorite = FavoriteKey.Ayah(ayah.surahNumber, ayah.ayahNumber) in favorites
        append(' ')
        withStyle(SpanStyle(color = if (favorite) favoriteMarker else marker, fontWeight = FontWeight.Bold)) {
            if (favorite) append("★")
            append("﴿${ArabicDigits.format(ayah.ayahNumber)}﴾")
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("سورة $surahName - الآية ${ArabicDigits.format(ayah.ayahNumber)}") },
        text = { Text(ayah.textArabic, maxLines = 6, overflow = TextOverflow.Ellipsis) },
        confirmButton = {
            TextButton(onClick = { onToggleFavorite(); onDismiss() }) {
                Text(if (isFavorite) "إزالة من المفضلة" else "إضافة إلى المفضلة")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onDismiss(); onOpenMizan() }) { Text("تفسير الميزان") }
                TextButton(onClick = onDismiss) { Text("إغلاق") }
            }
        }
    )
}

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
        title = { Text("الانتقال في المصحف") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = pageText, onValueChange = { pageText = it; if (it.isNotEmpty()) juzText = "" },
                    singleLine = true, label = { Text("رقم الصفحة (١ - ٦٠٤)") },
                    isError = pageText.isNotBlank() && page == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = juzText, onValueChange = { juzText = it; if (it.isNotEmpty()) pageText = "" },
                    singleLine = true, label = { Text("أو رقم الجزء (١ - ٣٠)") },
                    isError = juzText.isNotBlank() && juz == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = { TextButton(enabled = target != null, onClick = { target?.let(onGo) }) { Text("انتقال") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }
    )
}
