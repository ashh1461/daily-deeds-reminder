package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.SearchResultType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setUp() {
        searchRepository = SearchRepository()
    }

    @Test
    fun testEmptyQueryReturnsEmptyList() {
        val results = searchRepository.search("")
        assertTrue(results.isEmpty())
    }

    @Test
    fun testDiacriticNeutralSearchFindsQuranAyah() {
        val results = searchRepository.search("الرحمن الرحيم", SearchResultType.QURAN)
        assertFalse(results.isEmpty())
        assertTrue(results.any { it.surahNumber == 1 })
    }

    @Test
    fun testSearchInTafsirAlMizanReturnsRelevantCommentary() {
        val results = searchRepository.search("البسملة", SearchResultType.TAFSIR)
        assertFalse(results.isEmpty())
        assertTrue(results.any { it.title.contains("الميزان") })
    }

    @Test
    fun testSearchInMafatihAlJinanReturnsDuasAndZiyarat() {
        val results = searchRepository.search("كميل", SearchResultType.MAFATIH)
        assertFalse(results.isEmpty())
        assertTrue(results.any { it.title.contains("دعاء كميل") })

        val ashura = searchRepository.search("عاشوراء", SearchResultType.MAFATIH)
        assertTrue(ashura.any { it.title.contains("زيارة عاشوراء") })

        val sahifa = searchRepository.search("الحمد لله الاول بلا اول كان قبله", SearchResultType.SAHIFA)
        assertTrue(sahifa.any { it.title.contains("الدعاء 1") })
    }

    @Test
    fun testSearchFindsWeekdayDuasAndZiyarat() {
        val results = searchRepository.search("دعاء يوم الجمعة", SearchResultType.WEEKDAY)
        assertTrue(results.any { it.weekdayKind == com.dailydeeds.reminder.model.DayContentKind.DUA })
    }

    @Test
    fun testSearchFindsAyahInSurahThatWasPreviouslyPlaceholder() {
        val results = searchRepository.search("ذلك الكتاب لا ريب فيه", SearchResultType.QURAN)
        assertTrue(results.any { it.surahNumber == 2 })
        val kahf = searchRepository.search("الحمد لله الذي أنزل على عبده الكتاب", SearchResultType.QURAN)
        assertTrue(kahf.any { it.surahNumber == 18 })
    }

    @Test
    fun testGlobalSearchWithAllFilterReturnsCrossDomainResults() {
        val results = searchRepository.search("الله", SearchResultType.ALL)
        assertTrue(results.any { it.type == SearchResultType.QURAN })
        assertTrue(results.any { it.type == SearchResultType.TAFSIR })
        assertTrue(results.any { it.type == SearchResultType.MAFATIH })
    }

    @Test
    fun hugeHostileQueryIsBoundedAndFast() {
        val start = System.nanoTime()
        val results = searchRepository.search("ا".repeat(5_000_000) + "الله")
        val ms = (System.nanoTime() - start) / 1_000_000
        assertTrue("took $ms ms", ms < 5_000)
        assertTrue(results.size <= 6 * SearchRepository.MAX_RESULTS)
    }

    @Test
    fun dailyDeedsAreSearchable() {
        val results = searchRepository.search("آية الكرسي", SearchResultType.DEEDS)
        assertTrue(results.any { it.deedId != null })
        assertTrue(results.all { it.type == SearchResultType.DEEDS })
    }
}
