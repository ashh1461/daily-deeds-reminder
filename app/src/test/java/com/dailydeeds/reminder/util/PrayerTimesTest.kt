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
        assertNull(t.fajr)
    }

    /** London at midsummer: the sun only sinks about 15° below the horizon, short of the 16° Fajr angle. */
    @Test
    fun highLatitudeFillsAMissingFajrFromAPortionOfTheNight() {
        val t = PrayerTimes.compute(LocalDate.of(2026, 6, 21), 51.5074, -0.1278, ZoneId.of("Europe/London"))
        assertEquals(setOf(Prayer.FAJR), t.estimated)
        val fajr = minutes(t.fajr)
        val sunrise = minutes(t.sunrise)
        val sunset = minutes(t.sunset)
        val night = (24 * 60 + sunrise) - sunset
        // 16/60 of the night before sunrise, within the rounding of the times.
        assertTrue("fajr=${t.fajr} sunrise=${t.sunrise}", abs((sunrise - fajr) - night * 16 / 60) <= 2)
        assertNotNull(t.imsak)
        assertEquals(fajr - 10, minutes(t.imsak))
        assertNotNull(t.midnight)
        // Isha (14°) is still reached, so it is computed, not estimated.
        assertNotNull(t.isha)
    }

    /** Oslo at midsummer: Maghrib (4°) is reached, Isha (14°) is not. */
    @Test
    fun highLatitudeFillsAMissingIshaAfterSunset() {
        val t = PrayerTimes.compute(LocalDate.of(2026, 6, 21), 59.9139, 10.7522, ZoneId.of("Europe/Oslo"))
        assertTrue(Prayer.ISHA in t.estimated)
        assertTrue(Prayer.MAGHRIB !in t.estimated)
        val sunset = minutes(t.sunset)
        val isha = minutes(t.isha).let { if (it < sunset) it + 24 * 60 else it }
        assertTrue(isha > minutes(t.maghrib))
        val night = (24 * 60 + minutes(t.sunrise)) - sunset
        assertTrue("isha=${t.isha}", abs((isha - sunset) - night * 14 / 60) <= 2)
    }

    @Test
    fun normalLatitudesAreNotEstimated() {
        val t = PrayerTimes.compute(LocalDate.of(2026, 6, 21), 33.8938, 35.5018, ZoneId.of("Asia/Beirut"))
        assertTrue(t.estimated.isEmpty())
        val winter = PrayerTimes.compute(LocalDate.of(2026, 12, 21), 51.5074, -0.1278, ZoneId.of("Europe/London"))
        assertTrue(winter.estimated.isEmpty())
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
