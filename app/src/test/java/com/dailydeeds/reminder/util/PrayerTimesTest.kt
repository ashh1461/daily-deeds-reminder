package com.dailydeeds.reminder.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Reference values: aladhan.com method 0 (Shia Ithna-Ashari, 16/4/14) for 2026-10-15. */
class PrayerTimesTest {

    private class Ref(
        val name: String, val lat: Double, val lng: Double, val zone: String,
        val fajr: String, val sunrise: String, val dhuhr: String,
        val sunset: String, val maghrib: String, val isha: String
    )

    private val refs = listOf(
        Ref("Beirut", 33.8938, 35.5018, "Asia/Beirut", "05:29", "06:43", "12:24", "18:04", "18:20", "19:08"),
        Ref("Baghdad", 32.0, 44.3, "Asia/Baghdad", "04:54", "06:06", "11:49", "17:31", "17:46", "18:33"),
        Ref("Qom", 34.6401, 50.8764, "Asia/Tehran", "04:58", "06:12", "11:52", "17:32", "17:48", "18:37"),
        Ref("London", 51.5074, -0.1278, "Europe/London", "05:46", "07:24", "12:46", "18:07", "18:28", "19:32"),
        Ref("Detroit", 42.3223, -83.1763, "America/Detroit", "06:23", "07:46", "13:18", "18:51", "19:08", "20:02")
    )

    private fun minutes(t: LocalTime?) = t!!.hour * 60 + t.minute

    @Test
    fun matchesPublishedJafariTimesWithinTwoMinutes() {
        val date = LocalDate.of(2026, 10, 15)
        for (r in refs) {
            val t = PrayerTimes.compute(date, r.lat, r.lng, ZoneId.of(r.zone))
            val pairs = listOf(
                "fajr" to (t.fajr to r.fajr), "sunrise" to (t.sunrise to r.sunrise),
                "dhuhr" to (t.dhuhr to r.dhuhr), "sunset" to (t.sunset to r.sunset),
                "maghrib" to (t.maghrib to r.maghrib), "isha" to (t.isha to r.isha)
            )
            for ((label, p) in pairs) {
                val diff = abs(minutes(p.first) - minutes(LocalTime.parse(p.second)))
                assertTrue("${r.name} $label computed=${p.first} expected=${p.second}", diff <= 2)
            }
        }
    }

    @Test
    fun orderingIsConsistent() {
        val t = PrayerTimes.compute(LocalDate.of(2026, 3, 21), 32.0, 44.3, ZoneId.of("Asia/Baghdad"))
        assertTrue(minutes(t.fajr) < minutes(t.sunrise))
        assertTrue(minutes(t.sunrise) < minutes(t.dhuhr))
        assertTrue(minutes(t.dhuhr) < minutes(t.sunset))
        assertTrue(minutes(t.sunset) < minutes(t.maghrib))
        assertTrue(minutes(t.maghrib) < minutes(t.isha))
        assertNotNull(t.midnight)
    }

    @Test
    fun midnightIsHalfwayBetweenSunsetAndNextFajr() {
        val date = LocalDate.of(2026, 10, 15)
        val zone = ZoneId.of("Asia/Beirut")
        val today = PrayerTimes.compute(date, 33.8938, 35.5018, zone)
        val next = PrayerTimes.compute(date.plusDays(1), 33.8938, 35.5018, zone)
        val span = (24 * 60 + minutes(next.fajr)) - minutes(today.sunset)
        val expected = (minutes(today.sunset) + span / 2) % (24 * 60)
        assertTrue(abs(minutes(today.midnight) - expected) <= 2)
    }

    @Test
    fun polarSummerReturnsNullInsteadOfGarbage() {
        val t = PrayerTimes.compute(LocalDate.of(2026, 6, 21), 69.65, 18.96, ZoneId.of("Europe/Oslo"))
        assertNull(t.isha)
    }

    @Test
    fun qiblaMatchesReference() {
        assertEquals(161.88, Qibla.bearing(33.8938, 35.5018), 0.3)
        assertEquals(201.74, Qibla.bearing(32.0, 44.3), 0.3)
        assertEquals(219.19, Qibla.bearing(34.6401, 50.8764), 0.3)
        assertEquals(118.99, Qibla.bearing(51.5074, -0.1278), 0.3)
        assertEquals(51.93, Qibla.bearing(42.3223, -83.1763), 0.3)
    }
}
