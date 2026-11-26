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

    /** The settings move once: unlocked, still in credential storage, and not yet in device storage. */
    @Test
    fun theSettingsMoveOnlyOnce() {
        assertTrue(DirectBoot.shouldMove(unlocked = true, deviceCopyExists = false, credentialCopyExists = true))
        assertFalse(DirectBoot.shouldMove(unlocked = true, deviceCopyExists = true, credentialCopyExists = true))
        assertFalse(DirectBoot.shouldMove(unlocked = true, deviceCopyExists = false, credentialCopyExists = false))
        assertFalse(DirectBoot.shouldMove(unlocked = false, deviceCopyExists = false, credentialCopyExists = true))
    }
}
