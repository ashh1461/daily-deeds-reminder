package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.Prayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** What a fired prayer alarm says, on time and when delivered late. */
class PrayerAlarmTextTest {
    private val t0 = 1_800_000_000_000L
    private val minute = 60_000L

    @Test
    fun onTimeWordingIsUnchanged() {
        assertEquals(
            AlarmText("اقترب وقت صلاة الفجر", "بعد 10 دقيقة يحين موعد أذان الفجر."),
            PrayerAlarmText.pre(Prayer.FAJR, 10, t0, t0 + 30_000)
        )
        assertEquals(
            AlarmText("حان وقت صلاة الظهر", "حان الآن موعد أذان الظهر."),
            PrayerAlarmText.silent(Prayer.DHUHR, t0, t0 + 9 * minute)
        )
        assertEquals(
            AlarmText("حان وقت صلاة المغرب", "حان الآن موعد أذان المغرب بحسب الفقه الجعفري."),
            PrayerAlarmText.reminder(Prayer.MAGHRIB, t0, t0 + minute, snooze = true)
        )
        assertEquals(PrayerAlarmText.reminder(Prayer.ISHA, t0, t0), PrayerAlarmText.reminder(Prayer.ISHA))
    }

    @Test
    fun aPreAlarmDeliveredAfterThePrayerTimeSaysTheTimeHasCome() {
        // The planner skips that prayer's own alarm (it is in the past), so this is the only notification left.
        assertEquals(
            AlarmText("دخل وقت صلاة الفجر", "مضى على موعد أذان الفجر دقيقة واحدة."),
            PrayerAlarmText.pre(Prayer.FAJR, 10, t0, t0 + 11 * minute)
        )
        assertEquals("مضى على موعد أذان الفجر 35 دقيقة.", PrayerAlarmText.pre(Prayer.FAJR, 5, t0, t0 + 40 * minute).text)
        // Not "late" by the 10-minute rule, but a 5-minute heads-up 7 minutes late must not count down any more.
        assertEquals(
            AlarmText("دخل وقت صلاة العشاء", "مضى على موعد أذان العشاء دقيقتان."),
            PrayerAlarmText.pre(Prayer.ISHA, 5, t0, t0 + 7 * minute)
        )
    }

    @Test
    fun aPreAlarmAtExactlyThePrayerTimeNoLongerCountsDown() {
        assertEquals(
            AlarmText("دخل وقت صلاة الظهر", "مضى على موعد أذان الظهر دقيقة واحدة."),
            PrayerAlarmText.pre(Prayer.DHUHR, 10, t0, t0 + 10 * minute)
        )
        // A moment before, part of a minute left counts as a whole one, never "after 0 minutes".
        assertEquals(
            AlarmText("اقترب وقت صلاة الظهر", "بعد دقيقة واحدة يحين موعد أذان الظهر."),
            PrayerAlarmText.pre(Prayer.DHUHR, 10, t0, t0 + 10 * minute - 1)
        )
    }

    @Test
    fun aLatePreAlarmBeforeThePrayerTimeShowsTheMinutesReallyLeft() {
        assertEquals("بعد 18 دقيقة يحين موعد أذان الظهر.", PrayerAlarmText.pre(Prayer.DHUHR, 30, t0, t0 + 12 * minute).text)
        assertEquals(
            "بعد دقيقة واحدة يحين موعد أذان الظهر.",
            PrayerAlarmText.pre(Prayer.DHUHR, 30, t0, t0 + 29 * minute + 30_000).text
        )
        // Even a short delay, below the 10-minute rule, counts the minutes really left.
        assertEquals("بعد 3 دقائق يحين موعد أذان المغرب.", PrayerAlarmText.pre(Prayer.MAGHRIB, 5, t0, t0 + 2 * minute).text)
        // After «بعد» two minutes is genitive: «دقيقتين», not «دقيقتان».
        assertEquals("بعد دقيقتين يحين موعد أذان المغرب.", PrayerAlarmText.pre(Prayer.MAGHRIB, 5, t0, t0 + 3 * minute).text)
    }

    @Test
    fun anUnknownScheduledTimeKeepsTheOnTimeWording() {
        assertEquals("بعد 10 دقيقة يحين موعد أذان الفجر.", PrayerAlarmText.pre(Prayer.FAJR, 10, 0L, t0).text)
    }

    @Test
    fun minutesAreWordedAsArabicCountsThem() {
        assertEquals("دقيقة واحدة", PrayerAlarmText.minutes(1))
        assertEquals("دقيقتان", PrayerAlarmText.minutes(2))
        assertEquals("دقيقتين", PrayerAlarmText.minutes(2, genitive = true))
        assertEquals("3 دقائق", PrayerAlarmText.minutes(3, genitive = true))
        assertEquals("3 دقائق", PrayerAlarmText.minutes(3))
        assertEquals("10 دقائق", PrayerAlarmText.minutes(10))
        assertEquals("11 دقيقة", PrayerAlarmText.minutes(11))
        assertEquals("25 دقيقة", PrayerAlarmText.minutes(25))
    }

    @Test
    fun lateNotificationsDoNotClaimNow() {
        val silent = PrayerAlarmText.silent(Prayer.DHUHR, t0, t0 + 25 * minute)
        assertEquals(AlarmText("دخل وقت صلاة الظهر", "مضى على موعد أذان الظهر 25 دقيقة."), silent)
        val reminder = PrayerAlarmText.reminder(Prayer.ISHA, t0, t0 + 11 * minute)
        assertEquals(AlarmText("دخل وقت صلاة العشاء", "مضى على موعد أذان العشاء 11 دقيقة."), reminder)
        assertEquals("مضى على موعد أذان الفجر 10 دقائق.", PrayerAlarmText.silent(Prayer.FAJR, t0, t0 + 10 * minute + 1).text)
        // A late snooze does not know when the adhan itself was.
        val snoozed = PrayerAlarmText.reminder(Prayer.MAGHRIB, t0, t0 + 15 * minute, snooze = true)
        assertEquals("مضى موعد أذان المغرب.", snoozed.text)
        listOf(silent, reminder, snoozed).forEach { assertFalse(it.text, it.text.contains("الآن")) }
    }

    @Test
    fun lateMeansMoreThanTenMinutesAfterAKnownTime() {
        assertFalse(PrayerAlarmText.isLate(t0, t0 + 10 * minute))
        assertTrue(PrayerAlarmText.isLate(t0, t0 + 10 * minute + 1))
        assertFalse(PrayerAlarmText.isLate(0L, t0))
    }

    @Test
    fun theAdhanFallbackUsesTheReceiversNotificationId() {
        assertEquals(5001, PrayerAlarmText.notificationId(Prayer.FAJR))
        assertEquals(5004, PrayerAlarmText.notificationId(Prayer.ISHA))
    }
}
