package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
import org.junit.Test

/** The Today resume tile and where it reopens the Quran: the mushaf page in pages mode, the last ayah in list mode. */
class QuranResumeTest {

    @Test
    fun pagesModeShowsTheLastPageInArabicDigits() {
        assertEquals(
            "القرآن: سورة الكهف، صفحة ٣٠٢",
            QuranResume.label("الكهف", pagesMode = true, lastPage = 302, pageShowsSurah = true, lastAyah = 0)
        )
    }

    @Test
    fun pagesModeWithoutAMatchingPageShowsOnlyTheSurah() {
        // The last page belongs to another surah (the surah was opened from the list since), or no page was read yet.
        assertEquals("القرآن: سورة الكهف", QuranResume.label("الكهف", true, 302, pageShowsSurah = false, lastAyah = 0))
        assertEquals("القرآن: سورة الكهف", QuranResume.label("الكهف", true, 0, pageShowsSurah = true, lastAyah = 0))
    }

    @Test
    fun listModeShowsTheLastAyahPastTheFirst() {
        assertEquals("القرآن: سورة الكهف، الآية ٤٥", QuranResume.label("الكهف", false, 302, true, lastAyah = 45))
        assertEquals("القرآن: سورة الكهف", QuranResume.label("الكهف", false, 302, true, lastAyah = 1))
        assertEquals("القرآن: سورة الكهف", QuranResume.label("الكهف", false, 302, true, lastAyah = 0))
    }

    @Test
    fun theFirstVisibleItemGivesTheAyah() {
        // Item 0 is the surah banner, so item n is ayah n.
        assertEquals(1, QuranResume.ayahOfFirstVisible(0, 110))
        assertEquals(1, QuranResume.ayahOfFirstVisible(1, 110))
        assertEquals(45, QuranResume.ayahOfFirstVisible(45, 110))
        assertEquals(110, QuranResume.ayahOfFirstVisible(500, 110))
        assertEquals(0, QuranResume.ayahOfFirstVisible(3, 0))
    }

    @Test
    fun theReaderReopensAtTheLastAyahOfTheSameSurahOnly() {
        assertEquals(45, QuranResume.resumeAyah(surah = 18, lastSurah = 18, lastAyah = 45))
        assertEquals(0, QuranResume.resumeAyah(surah = 19, lastSurah = 18, lastAyah = 45))
        assertEquals(0, QuranResume.resumeAyah(surah = 18, lastSurah = 18, lastAyah = 1))
    }
}
