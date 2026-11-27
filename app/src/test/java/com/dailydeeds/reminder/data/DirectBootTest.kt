package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.data.DirectBoot.BootWork
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DirectBootTest {

    /** Right after a restart, before the first unlock, the moved settings are enough to restore the prayer alarms. */
    @Test
    fun beforeUnlockOnlyPrayerAlarmsAreRestoredOnceTheSettingsWereMoved() {
        assertEquals(BootWork.PRAYER_ONLY, DirectBoot.bootWork(unlocked = false, deviceCopyExists = true))
    }

    /** Never plan with default settings: before the move, a locked phone waits for the unlock broadcast. */
    @Test
    fun beforeTheMoveALockedPhoneDoesNothing() {
        assertEquals(BootWork.NONE, DirectBoot.bootWork(unlocked = false, deviceCopyExists = false))
        assertFalse(DirectBoot.canReadSettings(unlocked = false, deviceCopyExists = false))
    }

    @Test
    fun onceUnlockedEverythingIsScheduled() {
        assertEquals(BootWork.ALL, DirectBoot.bootWork(unlocked = true, deviceCopyExists = true))
        assertEquals(BootWork.ALL, DirectBoot.bootWork(unlocked = true, deviceCopyExists = false))
        assertTrue(DirectBoot.canReadSettings(unlocked = true, deviceCopyExists = false))
    }

    /** The store split runs once, after the first unlock; never while locked, and never a second time. */
    @Test
    fun theStoreSplitsOnlyOnceAfterUnlock() {
        assertTrue(DirectBoot.shouldSplit(unlocked = true, splitDone = false))
        assertFalse(DirectBoot.shouldSplit(unlocked = true, splitDone = true))
        assertFalse(DirectBoot.shouldSplit(unlocked = false, splitDone = false))
        assertFalse(DirectBoot.shouldSplit(unlocked = false, splitDone = true))
    }
}
