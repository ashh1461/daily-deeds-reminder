package com.dailydeeds.reminder.worship

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The strong vibration points of tasbih al-Zahra and the rules that decide when clearing asks first. */
class TasbihSafetyTest {

    @Test
    fun stageEndsAreTheLastTakbirTahmidAndTasbih() {
        assertEquals(listOf(34, 67, 100), (0..100).filter(ZahraStages::isStageEnd))
    }

    @Test
    fun pickingTheSameTargetAgainKeepsTheCount() {
        val s = TasbihState(freeCount = 12, freeTarget = 33)
        assertEquals(s, s.withTarget(33))
    }

    @Test
    fun pickingAnotherTargetClearsTheCount() {
        assertEquals(TasbihState(freeCount = 0, freeTarget = 99), TasbihState(freeCount = 12, freeTarget = 33).withTarget(99))
    }

    @Test
    fun clearingAsksOnlyWhenACountWouldBeLost() {
        val s = TasbihState(zahraCount = 5, freeCount = 0, freeTarget = 33)
        assertTrue(s.resetLosesCount(zahra = true))
        assertFalse(s.resetLosesCount(zahra = false))
        assertFalse(s.targetChangeLosesCount(99))
        val counted = s.copy(freeCount = 3)
        assertTrue(counted.targetChangeLosesCount(99))
        assertFalse(counted.targetChangeLosesCount(33))
    }

    @Test
    fun freeTargetIsReachedOnTheTapThatReachesIt() {
        val before = TasbihState(freeCount = 32, freeTarget = 33)
        assertFalse(before.freeDone)
        assertTrue(before.tapFree().freeDone)
    }
}
