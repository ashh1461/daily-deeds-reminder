package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.model.DeedType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DeedsRepositoryTest {

    @Test
    fun testAllDeedsCountIsEleven() {
        val deeds = DeedsRepository.getAllDeeds()
        assertEquals(11, deeds.size)
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
        val allDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.ALL)
        assertEquals(11, allDeeds.size)

        val quranDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.QURAN)
        assertEquals(4, quranDeeds.size)

        val tasbeehDeeds = DeedsRepository.getDeedsByCategory(DeedCategory.TASBEEH)
        assertEquals(3, tasbeehDeeds.size)
    }
}
