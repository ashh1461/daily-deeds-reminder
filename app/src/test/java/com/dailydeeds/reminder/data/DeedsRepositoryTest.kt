package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.model.DeedType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DeedsRepositoryTest {

    @Test
    fun testAllDeedsCountIsSixteen() {
        val deeds = DeedsRepository.getAllDeeds()
        assertEquals(16, deeds.size)
        assertEquals((1..16).toList(), deeds.map { it.id })
    }

    @Test
    fun testEveryDeedHasArabicContentAndTitle() {
        val deeds = DeedsRepository.getAllDeeds()
        for (deed in deeds) {
            assertTrue("Deed #${deed.id} title should not be blank", deed.title.isNotBlank())
            assertTrue("Deed #${deed.id} content should not be blank", deed.content.isNotBlank())
            assertTrue("Deed #${deed.id} targetCount must be >= 1", deed.targetCount >= 1)
        }
    }

    @Test
    fun testTasbeehFatimaHasThreeStagesTotalling100() {
        val deed = DeedsRepository.getDeedById(2)
        assertNotNull(deed)
        assertEquals(DeedType.MULTI_STAGE_COUNTER, deed!!.type)
        assertEquals(100, deed.targetCount)
        assertNotNull(deed.stages)
        assertEquals(3, deed.stages!!.size)

        assertEquals("اللَّهُ أَكْبَرُ", deed.stages!![0].phrase)
        assertEquals(34, deed.stages!![0].targetCount)

        assertEquals("الْحَمْدُ لِلَّهِ", deed.stages!![1].phrase)
        assertEquals(33, deed.stages!![1].targetCount)

        assertEquals("سُبْحَانَ اللَّهِ", deed.stages!![2].phrase)
        assertEquals(33, deed.stages!![2].targetCount)
    }

    @Test
    fun testCounterDeedsTargets() {
        val bismillah = DeedsRepository.getDeedById(3)
        assertNotNull(bismillah)
        assertEquals(19, bismillah!!.targetCount)

        val istighfar = DeedsRepository.getDeedById(10)
        assertNotNull(istighfar)
        assertEquals(70, istighfar!!.targetCount)

        val salawat = DeedsRepository.getDeedById(11)
        assertNotNull(salawat)
        assertEquals(100, salawat!!.targetCount)
    }

    @Test
    fun testCategoryFiltering() {
        val allDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.ALL, LocalDate.of(2026, 10, 8))
        assertEquals(16, allDeeds.size)

        val quranDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.QURAN)
        assertEquals(4, quranDeeds.size)

        val tasbeehDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.TASBEEH)
        assertEquals(3, tasbeehDeeds.size)
    }

    @Test
    fun suppliedTextsHaveTheirStatedTimesAndCounts() {
        assertEquals(listOf(12, 13), DeedsRepository.getDeedsByCategory(DeedCategory.ANYTIME).map { it.id })
        assertEquals(listOf(14), DeedsRepository.getDeedsByCategory(DeedCategory.BEDTIME).map { it.id })
        assertEquals(listOf(15), DeedsRepository.getDeedsByCategory(DeedCategory.NIGHT).map { it.id })
        assertEquals(listOf(16), DeedsRepository.getDeedsByCategory(DeedCategory.THURSDAY).map { it.id })
        for (id in listOf(13, 14)) {
            assertEquals(DeedType.COUNTER, DeedsRepository.getDeedById(id)!!.type)
            assertEquals(3, DeedsRepository.getDeedById(id)!!.targetCount)
        }
        assertTrue(DeedsRepository.getDeedById(12)!!.instructions!!.contains("قلبك"))
        assertTrue(DeedsRepository.getDeedById(16)!!.content.contains("سورة القدر"))
        assertTrue(DeedsRepository.getDeedById(16)!!.content.contains("سورة الفاتحة"))
    }

    @Test
    fun thursdayReadingOnlyContributesToThursdayProgress() {
        val thursday = LocalDate.of(2026, 10, 8)
        assertEquals(16, DeedsRepository.getDailyDeeds(thursday).size)
        for (days in 1L..6L) {
            val date = thursday.plusDays(days)
            assertEquals(15, DeedsRepository.getDailyDeeds(date).size)
            assertTrue(DeedsRepository.getDailyDeeds(date).none { it.id == 16 })
            assertEquals(15, DeedsRepository.getDeedsByCategory(DeedCategory.ALL, date).size)
            assertEquals(16, DeedsRepository.getDeedsByCategory(DeedCategory.THURSDAY, date).single().id)
        }
    }
}
