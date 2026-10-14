package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun chipsAreTheSevenMafatihSectionsAndExcludeTheSahifa() {
        val chips = repository.getCategories()
        assertEquals(
            listOf(
                MafatihCategoryType.ADIYAH, MafatihCategoryType.ZIYARAT, MafatihCategoryType.MUNAJAT,
                MafatihCategoryType.TAQIBAT, MafatihCategoryType.JUMUAH, MafatihCategoryType.AMAL,
                MafatihCategoryType.INDEX
            ),
            chips
        )
        for (c in chips) assertTrue("${c.id} is empty", repository.getItemsByCategory(c).isNotEmpty())
        assertFalse(chips.contains(MafatihCategoryType.SAHIFA))
    }

    @Test
    fun famousDuasAreCompleteNotAbridged() {
        val adiyah = repository.getItemsByCategory(MafatihCategoryType.ADIYAH)
        val kumayl = adiyah.find { it.title.contains("دعاء كميل") }
        assertNotNull(kumayl)
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
    fun weekdayRowsLiveOnlyInTheWeekdayScreensAndTheIndex() {
        val adiyah = repository.getItemsByCategory(MafatihCategoryType.ADIYAH)
        val ziyarat = repository.getItemsByCategory(MafatihCategoryType.ZIYARAT)
        assertTrue(adiyah.none { Regex("^دعاء يوم ").containsMatchIn(it.title) })
        assertTrue(ziyarat.none { it.title.contains("— يوم") })
        // ...but the index still lists them, so the book stays complete.
        val index = repository.getItemsByCategory(MafatihCategoryType.INDEX)
        assertTrue(index.count { Regex("^دعاء يوم ").containsMatchIn(it.title) } == 7)
        assertTrue(index.count { it.title.contains("— يوم") } >= 7)
    }

    @Test
    fun fridayDeedsHaveTheirOwnSectionAndMonthsAreNotMixedWithThem() {
        val jumuah = repository.getItemsByCategory(MafatihCategoryType.JUMUAH)
        val amal = repository.getItemsByCategory(MafatihCategoryType.AMAL)
        assertTrue(jumuah.isNotEmpty())
        assertTrue(jumuah.all { it.group.contains("ليلة الجمعة") })
        assertTrue(amal.none { it.group.contains("ليلة الجمعة") })
        assertTrue(amal.any { it.group.contains("شهر رمضان") })
    }

    @Test
    fun everyBookItemHasAChapterGroupAndIndexListsEveryItemOnce() {
        val index = repository.getItemsByCategory(MafatihCategoryType.INDEX)
        assertEquals(repository.getBookItems(), index)
        assertEquals(index.size, index.map { it.id }.toSet().size)
        assertTrue(index.all { it.group.isNotBlank() })
        assertTrue(index.map { it.group }.distinct().size >= 25)
        // Themed chips never repeat a row.
        for (c in MafatihCategoryType.mafatihChips.filter { it != MafatihCategoryType.INDEX }) {
            val items = repository.getItemsByCategory(c)
            assertEquals(c.id, items.size, items.map { it.id }.toSet().size)
        }
    }

    @Test
    fun sahifaIsNotPartOfAnyMafatihList() {
        val book = repository.getBookItems()
        assertTrue(book.none { it.id.startsWith("sj_") })
        assertTrue(repository.getSahifaItems().all { it.id.startsWith("sj_") && !it.fromMafatihBook })
        assertTrue(repository.searchMafatih("الحمد لله الاول بلا اول كان قبله").none { it.id.startsWith("sj_") })
        assertTrue(repository.searchSahifa("الحمد لله الاول بلا اول كان قبله").any { it.id == "sj_1" })
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
