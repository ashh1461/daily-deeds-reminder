package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.RevelationType
import com.dailydeeds.reminder.util.ArabicNormalizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuranRepositoryTest {

    private lateinit var repository: QuranRepository

    @Before
    fun setUp() {
        repository = QuranRepository()
    }

    @Test
    fun testGetAllSurahsReturns114Surahs() {
        val surahs = repository.getAllSurahs()
        assertEquals(114, surahs.size)
        assertEquals(1, surahs.first().number)
        assertEquals("الفاتحة", surahs.first().nameArabic)
        assertEquals(114, surahs.last().number)
        assertEquals("الناس", surahs.last().nameArabic)
    }

    @Test
    fun testGetSurahByNumber() {
        val fatihah = repository.getSurahByNumber(1)
        assertNotNull(fatihah)
        assertEquals("الفاتحة", fatihah?.nameArabic)
        assertEquals(RevelationType.MAKKI, fatihah?.revelationType)
        assertEquals(7, fatihah?.ayahCount)

        val ikhlas = repository.getSurahByNumber(112)
        assertNotNull(ikhlas)
        assertEquals("الإخلاص", ikhlas?.nameArabic)
        assertEquals(4, ikhlas?.ayahCount)
    }

    @Test
    fun testGetAyahsForSurah() {
        val fatihahAyahs = repository.getAyahsForSurah(1)
        assertEquals(7, fatihahAyahs.size)
        assertEquals(1, fatihahAyahs[0].ayahNumber)
        assertTrue(ArabicNormalizer.contains(fatihahAyahs[0].textArabic, "بسم الله الرحمن الرحيم"))
        assertEquals(7, fatihahAyahs[6].ayahNumber)
        assertTrue(ArabicNormalizer.contains(fatihahAyahs[6].textArabic, "صراط الذين أنعمت عليهم"))
    }

    @Test
    fun testGetTafsirForAyah() {
        val tafsir = repository.getTafsirForAyah(1, 1)
        assertNotNull("Tafsir Al-Mizan commentary should exist for Surah Al-Fatihah Ayah 1", tafsir)
        assertTrue(tafsir!!.commentaryArabic.isNotBlank())
        assertTrue(tafsir.title.contains("الميزان") || tafsir.title.contains("تفسير"))
    }

    @Test
    fun testSearchSurahs() {
        val resultsArabic = repository.searchSurahs("الكهف")
        assertTrue(resultsArabic.any { it.number == 18 })

        val resultsEnglish = repository.searchSurahs("Yasin")
        assertTrue(resultsEnglish.any { it.number == 36 })
    }
}
