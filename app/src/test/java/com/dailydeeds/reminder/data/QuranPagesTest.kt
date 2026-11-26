package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranPagesTest {

    @Test
    fun everyAyahIsOnExactlyOnePageInMushafOrder() {
        val all = (1..QuranPages.COUNT).flatMap { n -> QuranPages.page(n).segments.flatMap { it.ayahs } }
        assertEquals(6236, all.size)
        assertEquals(all.size, all.map { it.surahNumber to it.ayahNumber }.distinct().size)
        val ordered = all.zipWithNext().all { (a, b) ->
            (a.surahNumber + 1 == b.surahNumber && b.ayahNumber == 1) ||
                (a.surahNumber == b.surahNumber && a.ayahNumber + 1 == b.ayahNumber)
        }
        assertTrue("ayahs must run continuously across pages", ordered)
    }

    @Test
    fun noPageIsEmpty() {
        (1..QuranPages.COUNT).forEach { n ->
            assertTrue("page $n is empty", QuranPages.page(n).segments.isNotEmpty())
        }
    }

    @Test
    fun firstPagesFollowTheMadaniMushaf() {
        val fatiha = QuranPages.page(1)
        assertEquals(listOf(1), fatiha.segments.map { it.surah.number })
        assertEquals(7, fatiha.segments.single().ayahs.size)
        assertTrue(fatiha.segments.single().startsSurah)

        val second = QuranPages.page(2).segments.single()
        assertEquals(2, second.surah.number)
        assertEquals((1..5).toList(), second.ayahs.map { it.ayahNumber })

        val third = QuranPages.page(3)
        assertEquals(2 to 6, third.firstAyah.surahNumber to third.firstAyah.ayahNumber)
        assertFalse(third.segments.single().startsSurah)
    }

    @Test
    fun lastPageHoldsTheThreeLastSurahsEachFromItsStart() {
        val last = QuranPages.page(604)
        assertEquals(listOf(112, 113, 114), last.segments.map { it.surah.number })
        assertTrue(last.segments.all { it.startsSurah })
        assertEquals(30, last.juz)
    }

    @Test
    fun pageOfFindsTheAyahAndFallsBackForUnknownOnes() {
        assertEquals(42, QuranPages.pageOf(2, 255))
        assertTrue(QuranPages.page(42).contains(2, 255))
        assertEquals(604, QuranPages.pageOf(114, 6))
        // Unknown ayah: the surah's first page; unknown surah: page 1.
        assertEquals(2, QuranPages.pageOf(2, 999))
        assertEquals(1, QuranPages.pageOf(200, 1))
    }

    @Test
    fun surahStartPagesAgreeWithTheText() {
        QuranDataProvider.surahs.forEach { s ->
            assertEquals("surah ${s.number}", QuranPages.pageOf(s.number, 1), QuranPages.firstPageOfSurah(s.number))
            assertTrue(QuranPages.page(s.pageNumber).showsSurah(s.number))
        }
    }

    @Test
    fun juzStartPages() {
        assertEquals(1, QuranPages.firstPageOfJuz(1))
        assertEquals(22, QuranPages.firstPageOfJuz(2))
        assertEquals(582, QuranPages.firstPageOfJuz(30))
        assertEquals(1, QuranPages.firstPageOfJuz(0))
        assertEquals(582, QuranPages.firstPageOfJuz(99))
    }

    @Test
    fun outOfRangePagesAreClamped() {
        assertEquals(1, QuranPages.page(0).number)
        assertEquals(604, QuranPages.page(10_000).number)
    }
}
