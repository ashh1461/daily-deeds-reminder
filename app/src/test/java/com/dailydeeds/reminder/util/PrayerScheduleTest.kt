package com.dailydeeds.reminder.util

import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PrayerScheduleTest {
    private val beirut = PlacePresets.default

    @Test
    fun nextAfterMidnightIsFajr() {
        val now = ZonedDateTime.of(2026, 10, 15, 1, 0, 0, 0, ZoneId.of("Asia/Beirut"))
        val n = PrayerSchedule.next(now, beirut)!!
        assertEquals(Prayer.FAJR, n.prayer)
        assertTrue(n.time.isAfter(now))
        assertEquals(LocalDate.of(2026, 10, 15), n.time.toLocalDate())
    }

    @Test
    fun nextAfterIshaRollsToTomorrowFajr() {
        val now = ZonedDateTime.of(2026, 10, 15, 23, 0, 0, 0, ZoneId.of("Asia/Beirut"))
        val n = PrayerSchedule.next(now, beirut)!!
        assertEquals(Prayer.FAJR, n.prayer)
        assertEquals(LocalDate.of(2026, 10, 16), n.time.toLocalDate())
    }

    @Test
    fun midDayGivesDhuhrOrLater() {
        val now = ZonedDateTime.of(2026, 10, 15, 12, 0, 0, 0, ZoneId.of("Asia/Beirut"))
        assertEquals(Prayer.DHUHR, PrayerSchedule.next(now, beirut)!!.prayer)
    }

    @Test
    fun deviceInDifferentZoneStillResolvesInPlaceZone() {
        val now = ZonedDateTime.of(2026, 10, 15, 0, 0, 0, 0, ZoneId.of("UTC"))
        assertNotNull(PrayerSchedule.next(now, beirut))
    }

    @Test
    fun everyPresetPlaceIsValidAndComputable() {
        for (p in PlacePresets.all) {
            val t = PrayerSchedule.timesFor(LocalDate.of(2026, 10, 15), p)
            assertNotNull(p.name, t.fajr)
            assertNotNull(p.name, t.isha)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidCoordinatesAreRejected() {
        Place("x", 123.0, 0.0, "UTC")
    }
}
