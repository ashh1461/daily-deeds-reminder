package com.dailydeeds.reminder.data

/**
 * Where «تابع من حيث توقفت» reopens the Quran and what its tile says: the last mushaf page in pages mode, the last
 * ayah read in the list of ayahs otherwise. Pure, so it can be tested without Android.
 */
object QuranResume {

    enum class Kind { SURAH, PAGE, AYAH }

    /** What the tile names: the surah alone, or with its last page or ayah ([number]; 0 for [Kind.SURAH]). */
    data class Target(val kind: Kind, val number: Int)

    /**
     * What the tile resumes. [pageShowsSurah] is whether [lastPage] belongs to the surah being resumed (the surah may
     * have been opened from the list since that page was read). The UI words it in the app language.
     */
    fun target(pagesMode: Boolean, lastPage: Int, pageShowsSurah: Boolean, lastAyah: Int): Target = when {
        pagesMode && lastPage > 0 && pageShowsSurah -> Target(Kind.PAGE, lastPage)
        !pagesMode && lastAyah > 1 -> Target(Kind.AYAH, lastAyah)
        else -> Target(Kind.SURAH, 0)
    }

    /** The ayah at the top of the list reader: item 0 is the surah banner, so item n is ayah n. 0 when there are none. */
    fun ayahOfFirstVisible(index: Int, ayahCount: Int): Int =
        if (ayahCount <= 0) 0 else index.coerceIn(1, ayahCount)

    /** The ayah the list reader reopens [surah] at, or 0 for its top (another surah, or nothing past the first ayah). */
    fun resumeAyah(surah: Int, lastSurah: Int, lastAyah: Int): Int =
        if (surah == lastSurah && lastAyah > 1) lastAyah else 0
}
