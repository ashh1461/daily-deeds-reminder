package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MafatihRepositoryTest {

    private lateinit var repository: MafatihRepository

    @Before
    fun setUp() {
        repository = MafatihRepository()
    }

    @Test
    fun testGetCategoriesReturnsAll6MajorCategories() {
        val categories = repository.getCategories()
        assertEquals(7, categories.size)
        assertTrue(categories.contains(MafatihCategoryType.ADIYAH))
        assertTrue(categories.contains(MafatihCategoryType.ZIYARAT))
        assertTrue(categories.contains(MafatihCategoryType.TAQIBAT))
        assertTrue(categories.contains(MafatihCategoryType.MUNAJAT))
        assertTrue(categories.contains(MafatihCategoryType.AMAL))
        assertTrue(categories.contains(MafatihCategoryType.BAQIYAT))
    }

    @Test
    fun testGetItemsByCategoryReturnsAdiyahLikeDuaKumayl() {
        val adiyah = repository.getItemsByCategory(MafatihCategoryType.ADIYAH)
        assertTrue(adiyah.isNotEmpty())
        val kumayl = adiyah.find { it.id == "dua_kumayl" }
        assertNotNull(kumayl)
        assertEquals("دعاء كميل بن زياد", kumayl?.title)
        assertTrue(kumayl?.arabicText?.contains("اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ") == true)
    }

    @Test
    fun testGetItemsByCategoryReturnsZiyaratLikeZiyaratAshura() {
        val ziyarat = repository.getItemsByCategory(MafatihCategoryType.ZIYARAT)
        val ashura = ziyarat.find { it.id == "ziyarat_ashura" }
        assertNotNull(ashura)
        assertTrue(ashura?.arabicText?.contains("السَّلَامُ عَلَيْكَ يَا أَبَا عَبْدِ اللَّهِ") == true)
    }

    @Test
    fun testSearchMafatihDiacriticNeutralMatching() {
        val results = repository.searchMafatih("عظم البلاء")
        assertTrue(results.any { it.id == "dua_faraj" })

        val tawassulResults = repository.searchMafatih("التوسل")
        assertTrue(tawassulResults.any { it.id == "dua_tawassul" })
    }
}
