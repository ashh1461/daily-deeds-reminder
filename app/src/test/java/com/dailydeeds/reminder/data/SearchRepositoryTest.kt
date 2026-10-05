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
        assertTrue(results.any { it.mafatihItemId == "dua_kumayl" })

        val ashura = searchRepository.search("عاشوراء", SearchResultType.MAFATIH)
        assertTrue(ashura.any { it.mafatihItemId == "ziyarat_ashura" })
    }

    @Test
    fun testGlobalSearchWithAllFilterReturnsCrossDomainResults() {
        val results = searchRepository.search("الله", SearchResultType.ALL)
        assertTrue(results.any { it.type == SearchResultType.QURAN })
        assertTrue(results.any { it.type == SearchResultType.TAFSIR })
        assertTrue(results.any { it.type == SearchResultType.MAFATIH })
    }
}
