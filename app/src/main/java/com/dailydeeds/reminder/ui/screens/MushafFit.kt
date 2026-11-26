package com.dailydeeds.reminder.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.QuranPages
import com.dailydeeds.reminder.data.ReaderFonts
import com.dailydeeds.reminder.util.ArabicDigits
import kotlin.math.floor

/** Layout constants shared by the page view and the fitting below, so the two cannot drift apart. */
internal object MushafLayout {
    val HorizontalPadding = 14.dp
    val VerticalPadding = 8.dp
    val Spacing = 8.dp
    val FooterTop = 8.dp
    val FooterBottom = 16.dp
    /** EmeraldBanner pads its content by 20 dp on each side. */
    val BannerPadding = 20.dp
    /** OrnamentDivider (14 dp) plus its 6 dp padding above and below. */
    val DividerBlock = 26.dp
    const val LINE_HEIGHT = 1.9f
    /** The zoom buttons share this minimum, so «تصغير الخط» from a fitted page never enlarges it. */
    const val FIT_MIN_SP = ReaderFonts.MUSHAF_MIN_SP
    const val FIT_MAX_SP = 30f
    /** Leaves a sliver so rounding never tips a page into scrolling. */
    const val FIT_SAFETY = 0.98f

    fun ayahStyle(base: TextStyle, sp: Float, color: Color): TextStyle = base.copy(
        fontSize = sp.sp,
        lineHeight = (sp * LINE_HEIGHT).sp,
        textAlign = TextAlign.Justify,
        color = color
    )

    /** The basmala under a surah title, measured and drawn with the same style so fitting stays exact. */
    fun basmalaStyle(base: TextStyle, sp: Float): TextStyle = base.copy(
        fontSize = sp.sp,
        lineHeight = ReaderFonts.basmalaLineHeightSp(sp).sp
    )
}

/**
 * What a fitted size depends on: the page, the favourites starred on that page (each adds a ★), the space available
 * and the text scale. Favourites elsewhere are left out, so starring an ayah re-fits only its own page.
 * The theme's text styles are constant inside the app, so they are not part of the key.
 */
internal data class MushafFitKey(
    val page: Int,
    val favorites: Set<FavoriteKey.Ayah>,
    val width: Int,
    val height: Int,
    val density: Float,
    val fontScale: Float
)

internal fun mushafFitKey(
    page: QuranPages.Page,
    favorites: Set<FavoriteKey>,
    width: Int,
    height: Int,
    density: Float,
    fontScale: Float
): MushafFitKey {
    val onPage = page.segments.asSequence()
        .flatMap { it.ayahs.asSequence() }
        .map { FavoriteKey.Ayah(it.surahNumber, it.ayahNumber) }
        .filter { it in favorites }
        .toSet()
    return MushafFitKey(page.number, onPage, width, height, density, fontScale)
}

/** Fitted sizes of recently shown pages, most recently used kept, so swiping back costs nothing. Thread-safe. */
internal class MushafFitCache(private val capacity: Int = DEFAULT_CAPACITY) {
    private val sizes = object : LinkedHashMap<MushafFitKey, Float>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<MushafFitKey, Float>?): Boolean = size > capacity
    }

    @Synchronized
    fun get(key: MushafFitKey): Float? = sizes[key]

    @Synchronized
    fun put(key: MushafFitKey, sp: Float) {
        sizes[key] = sp
    }

    companion object {
        const val DEFAULT_CAPACITY = 48
    }
}

/** One cache for the whole process, so reopening the mushaf does not fit the same pages again. */
internal val mushafFits = MushafFitCache()

/**
 * The largest text size (in 0.5 sp steps, between [MushafLayout.FIT_MIN_SP] and [MushafLayout.FIT_MAX_SP]) at which the
 * whole page fits in [maxWidth] x [maxHeight] pixels. A page that does not fit even at the minimum gets the minimum
 * and scrolls.
 */
internal fun fitMushafFontSize(
    page: QuranPages.Page,
    favorites: Set<FavoriteKey>,
    measurer: TextMeasurer,
    density: Density,
    bodyStyle: TextStyle,
    titleStyle: TextStyle,
    footerStyle: TextStyle,
    maxWidth: Int,
    maxHeight: Int
): Float = with(density) {
    val width = (maxWidth - (MushafLayout.HorizontalPadding * 2).roundToPx()).coerceAtLeast(1)
    val bannerInner = (width - (MushafLayout.BannerPadding * 2).roundToPx()).coerceAtLeast(1)
    val banners = page.segments.filter { it.startsSurah }
    val children = page.segments.size + banners.size + 1
    val footer = measurer.measure(ArabicDigits.format(page.number), footerStyle).size.height +
        (MushafLayout.FooterTop + MushafLayout.FooterBottom).toPx()
    val fixed = (MushafLayout.VerticalPadding * 2).toPx() + MushafLayout.Spacing.toPx() * (children - 1) + footer
    val titles = banners.sumOf { seg ->
        measurer.measure("سورة ${seg.surah.nameArabic}", titleStyle, constraints = Constraints(maxWidth = bannerInner))
            .size.height.toDouble() + (MushafLayout.BannerPadding * 2).toPx()
    }.toFloat()
    val budget = maxHeight * MushafLayout.FIT_SAFETY

    fun height(sp: Float): Float {
        var h = fixed + titles
        banners.forEach { seg ->
            if (seg.surah.number != 1 && seg.surah.number != 9) {
                h += MushafLayout.DividerBlock.toPx() + measurer.measure(
                    MUSHAF_BASMALA, MushafLayout.basmalaStyle(titleStyle, sp), constraints = Constraints(maxWidth = bannerInner)
                ).size.height
            }
        }
        page.segments.forEach { seg ->
            val text = ayahText(seg.ayahs, favorites, null, Color.Unspecified, Color.Unspecified, Color.Unspecified)
            h += measurer.measure(
                text, MushafLayout.ayahStyle(bodyStyle, sp, Color.Unspecified), constraints = Constraints(maxWidth = width)
            ).size.height
        }
        return h
    }

    var lo = MushafLayout.FIT_MIN_SP
    var hi = MushafLayout.FIT_MAX_SP
    if (height(hi) <= budget) return hi
    if (height(lo) > budget) return lo
    repeat(7) {
        val mid = (lo + hi) / 2f
        if (height(mid) <= budget) lo = mid else hi = mid
    }
    floor(lo * 2f) / 2f
}
