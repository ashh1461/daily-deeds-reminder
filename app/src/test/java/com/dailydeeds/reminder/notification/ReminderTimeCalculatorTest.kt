package com.dailydeeds.reminder.notification

import com.dailydeeds.reminder.model.ReminderType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.DayOfWeek
import java.time.ZonedDateTime

class ReminderTimeCalculatorTest {
    @Test fun dailyReminderUsesTodayBeforeTime() {
        val now = ZonedDateTime.parse("2026-10-03T20:00:00+03:00[Asia/Beirut]")
        assertEquals(now.withHour(21), ReminderTimeCalculator.nextOccurrence(now, 21, 0))
    }

    @Test fun dailyReminderAtExactTimeMovesToTomorrow() {
        val now = ZonedDateTime.parse("2026-10-03T22:00:00+03:00[Asia/Beirut]")
        assertEquals(now.plusDays(1), ReminderTimeCalculator.nextOccurrence(now, 22, 0))
    }

    @Test fun weeklyReminderUsesNextThursday() {
        val now = ZonedDateTime.parse("2026-10-03T10:00:00+03:00[Asia/Beirut]")
        val expected = ZonedDateTime.parse("2026-10-08T07:00:00+03:00[Asia/Beirut]")
        assertEquals(expected, ReminderTimeCalculator.nextOccurrence(now, 7, 0, DayOfWeek.THURSDAY))
    }

    @Test fun thursdayBeforeTimeUsesSameDay() {
        val now = ZonedDateTime.parse("2026-10-08T06:30:00+03:00[Asia/Beirut]")
        assertEquals(now.withHour(7).withMinute(0), ReminderTimeCalculator.nextOccurrence(now, 7, 0, DayOfWeek.THURSDAY))
    }

    @Test fun thursdayAtOrAfterTimeMovesToNextWeek() {
        val now = ZonedDateTime.parse("2026-10-08T07:00:00+03:00[Asia/Beirut]")
        val expected = now.plusWeeks(1)
        assertEquals(expected, ReminderTimeCalculator.nextOccurrence(now, 7, 0, DayOfWeek.THURSDAY))
        assertEquals(expected, ReminderTimeCalculator.nextOccurrence(now.plusHours(1), 7, 0, DayOfWeek.THURSDAY))
    }

    @Test fun dailyTimeStaysLocalAcrossDaylightSaving() {
        val now = ZonedDateTime.parse("2026-10-24T22:30:00+02:00[Europe/Berlin]")
        val expected = ZonedDateTime.parse("2026-10-25T22:00:00+01:00[Europe/Berlin]")
        assertEquals(expected, ReminderTimeCalculator.nextOccurrence(now, 22, 0))
    }

    @Test fun springGapProducesAFutureAlarm() {
        val now = ZonedDateTime.parse("2026-03-28T23:00:00+01:00[Europe/Berlin]")
        val expected = ZonedDateTime.parse("2026-03-29T03:30:00+02:00[Europe/Berlin]")
        assertEquals(expected, ReminderTimeCalculator.nextOccurrence(now, 2, 30))
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidHoursAreRejected() {
        ReminderTimeCalculator.nextOccurrence(ZonedDateTime.now(), 24, 0)
    }

    @Test fun reminderIdsStayDistinctAndOldPreferencesKeepTheirNames() {
        assertEquals(listOf(1, 2, 3, 4, 5), ReminderType.values().map { it.id })
        assertEquals("morning", ReminderType.MORNING.preferenceKey)
        assertEquals("evening", ReminderType.EVENING.preferenceKey)
        assertNull(ReminderType.fromId(0))
        assertNull(ReminderType.fromId(99))
        assertEquals(DayOfWeek.THURSDAY, ReminderType.THURSDAY.dayOfWeek)
    }
}
