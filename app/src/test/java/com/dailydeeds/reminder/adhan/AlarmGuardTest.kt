package com.dailydeeds.reminder.adhan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AlarmGuardTest {
    private val hour = 60 * 60 * 1000L
    private val maghrib = 1_790_000_000_000L

    /** The clock went back an hour (end of summer time) after Maghrib played: the re-planned Maghrib is skipped. */
    @Test
    fun anAlarmAlreadyHandledIsARepeat() {
        assertTrue(AlarmGuard.isRepeat(maghrib, lastHandled = maghrib, snooze = false))
        assertTrue(AlarmGuard.isRepeat(maghrib - hour, lastHandled = maghrib, snooze = false))
    }

    @Test
    fun laterAlarmsAndSnoozesAreNeverRepeats() {
        assertFalse(AlarmGuard.isRepeat(maghrib + 1, lastHandled = maghrib, snooze = false))
        assertFalse(AlarmGuard.isRepeat(maghrib, lastHandled = maghrib, snooze = true))
        assertFalse(AlarmGuard.isRepeat(maghrib, lastHandled = 0, snooze = false))
        assertFalse(AlarmGuard.isRepeat(0, lastHandled = maghrib, snooze = false))
    }

    /** A clock that was wrongly set ahead and then corrected must not silence real prayers for long. */
    @Test
    fun theGuardOnlyLooksBackAFewHours() {
        assertFalse(AlarmGuard.isRepeat(maghrib - AlarmGuard.REPEAT_WINDOW_MS, lastHandled = maghrib, snooze = false))
        assertFalse(AlarmGuard.isRepeat(maghrib - 24 * hour, lastHandled = maghrib, snooze = false))
        assertTrue(AlarmGuard.REPEAT_WINDOW_MS <= 3 * hour)
    }
}

class PendingSnoozesTest {

    @Test
    fun encodingRoundTrips() {
        val snoozes = mapOf(3 to 1_790_000_600_000L, 1 to 1_790_000_000_000L)
        val text = PendingSnoozes.encode(snoozes)
        assertEquals("1:1790000000000,3:1790000600000", text)
        assertEquals(snoozes, PendingSnoozes.decode(text))
    }

    @Test
    fun badEntriesAreIgnored() {
        assertEquals(emptyMap<Int, Long>(), PendingSnoozes.decode(null))
        assertEquals(emptyMap<Int, Long>(), PendingSnoozes.decode(""))
        assertEquals(mapOf(2 to 5L), PendingSnoozes.decode("x:1,2:5,3:-4,4,5:y,:7"))
    }

    /** After a restart only the snoozes still to come are set again. */
    @Test
    fun onlyFutureSnoozesAreRestored() {
        val now = 1_790_000_000_000L
        val snoozes = mapOf(1 to now - 1, 2 to now, 3 to now + 60_000)
        assertEquals(mapOf(3 to now + 60_000), PendingSnoozes.stillDue(snoozes, now))
    }
}
