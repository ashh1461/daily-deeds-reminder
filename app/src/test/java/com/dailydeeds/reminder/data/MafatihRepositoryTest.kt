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
    fun everyCategoryHasItems() {
        val categories = repository.getCategories()
        assertEquals(MafatihCategoryType.values().toList(), categories)
        for (c in categories) {
            assertTrue("${c.id} is empty", repository.getItemsByCategory(c).isNotEmpty())
        }
    }

    @Test
    fun famousDuasAreCompleteNotAbridged() {
        val adiyah = repository.getItemsByCategory(MafatihCategoryType.ADIYAH)
        val kumayl = adiyah.find { it.title.contains("دعاء كميل") }
        assertNotNull(kumayl)
        // The full supplication runs to thousands of characters, not a few lines with an ellipsis.
        assertTrue(kumayl!!.arabicText.length > 5000)
        assertTrue(com.dailydeeds.reminder.util.ArabicNormalizer.contains(kumayl.arabicText, "اللهم اني اسالك برحمتك التي وسعت كل شيء"))
        assertTrue(adiyah.any { it.title.contains("دعاء الجوشن الكبير") && it.arabicText.length > 15000 })
        assertTrue(adiyah.any { it.title.contains("دعاء أبي حمزة") && it.arabicText.length > 15000 })
    }

    @Test
    fun ashuraAndArbaeenAreInTheZiyaratCategory() {
        val ziyarat = repository.getItemsByCategory(MafatihCategoryType.ZIYARAT)
        assertTrue(ziyarat.any { it.title.startsWith("زيارة عاشوراء") })
        assertTrue(ziyarat.any { it.title.startsWith("زيارة الأربعين") })
        assertTrue(ziyarat.any { it.title.startsWith("الزيارات الجامعة") })
    }

    @Test
    fun allFifteenMunajatArePresent() {
        val munajat = repository.getItemsByCategory(MafatihCategoryType.MUNAJAT)
        assertTrue(munajat.count { it.title.startsWith("المناجاة ") } >= 15)
    }

    @Test
    fun searchIsDiacriticNeutralAndScopedToCategory() {
        val faraj = repository.searchMafatih("عظم البلاء")
        assertTrue(faraj.any { it.title.contains("إلهي عظم البلاء") })

        val inMunajat = repository.searchMafatih("الهي", MafatihCategoryType.MUNAJAT)
        assertTrue(inMunajat.isNotEmpty())
        assertTrue(inMunajat.all { it.category == MafatihCategoryType.MUNAJAT })

        assertTrue(repository.searchMafatih("   ").isNotEmpty())
    }
}
