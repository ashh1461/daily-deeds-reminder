package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.model.Surah

/**
 * The 604-page Madani mushaf layout, taken from the page column of the bundled text (`surah|ayah|juz|page|text`).
 * Pure logic, so it can be unit-tested.
 */
object QuranPages {
    const val COUNT = 604
    const val JUZ_COUNT = 30

    /** Consecutive ayahs of one surah on a page; a page holds one segment per surah it shows. */
    data class Segment(val surah: Surah, val ayahs: List<Ayah>) {
        /** The surah begins on this page, so its title (and basmala) is drawn before the first ayah. */
        val startsSurah: Boolean get() = ayahs.firstOrNull()?.ayahNumber == 1
    }

    data class Page(val number: Int, val juz: Int, val segments: List<Segment>) {
        val firstAyah: Ayah get() = segments.first().ayahs.first()

        fun contains(surah: Int, ayah: Int): Boolean =
            segments.any { s -> s.surah.number == surah && s.ayahs.any { it.ayahNumber == ayah } }

        fun showsSurah(surah: Int): Boolean = segments.any { it.surah.number == surah }
    }

    private val ayahsByPage: Map<Int, List<Ayah>> by lazy {
        QuranDataProvider.allAyahs()
            .sortedWith(compareBy({ it.surahNumber }, { it.ayahNumber }))
            .groupBy { it.page }
    }

    private val surahsByNumber: Map<Int, Surah> by lazy { QuranDataProvider.surahs.associateBy { it.number } }

    fun clamp(page: Int): Int = page.coerceIn(1, COUNT)

    fun page(number: Int): Page {
        val n = clamp(number)
        val ayahs = ayahsByPage[n].orEmpty()
        val segments = ayahs.groupBy { it.surahNumber }.mapNotNull { (surah, list) ->
            surahsByNumber[surah]?.let { Segment(it, list) }
        }
        return Page(n, ayahs.firstOrNull()?.juz ?: 1, segments)
    }

    /** The page that holds an ayah; an unknown ayah falls back to its surah's first page, then to page 1. */
    fun pageOf(surah: Int, ayah: Int): Int =
        QuranDataProvider.getAyah(surah, ayah)?.page ?: firstPageOfSurah(surah)

    fun firstPageOfSurah(surah: Int): Int = surahsByNumber[surah]?.pageNumber ?: 1

    /** The first page of a juz (1..30); out-of-range values are clamped. */
    fun firstPageOfJuz(juz: Int): Int {
        val j = juz.coerceIn(1, JUZ_COUNT)
        return ayahsByPage.entries.first { (_, ayahs) -> ayahs.any { it.juz == j } }.key
    }
}
