package com.dailydeeds.reminder.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Reference values: aladhan.com method 7 (University of Tehran: 17.7 / 4.5 / 14) for 2026-10-15. */
class PrayerMethodsTest {

    private class Ref(
        val name: String, val lat: Double, val lng: Double, val zone: String,
        val imsak: String, val fajr: String, val maghrib: String, val isha: String
    )

    private val refs = listOf(
        Ref("Beirut", 33.8938, 35.5018, "Asia/Beirut", "05:11", "05:21", "18:22", "19:08"),
        Ref("Baghdad", 32.0, 44.3, "Asia/Baghdad", "04:36", "04:46", "17:48", "18:33"),
        Ref("Qom", 34.6401, 50.8764, "Asia/Tehran", "04:40", "04:50", "17:50", "18:37"),
        Ref("London", 51.5074, -0.1278, "Europe/London", "05:25", "05:35", "18:31", "19:32"),
        Ref("Detroit", 42.3223, -83.1763, "America/Detroit", "06:04", "06:14", "19:11", "20:02")
    )

    private fun minutes(t: LocalTime?) = t!!.hour * 60 + t.minute

    @Test
    fun universityOfTehranMatchesTheReferenceWithinTwoMinutes() {
        val date = LocalDate.of(2026, 10, 15)
        for (r in refs) {
            val t = PrayerTimes.compute(date, r.lat, r.lng, ZoneId.of(r.zone), PrayerParams.TEHRAN)
            for ((label, got, want) in listOf(
                Triple("imsak", t.imsak, r.imsak), Triple("fajr", t.fajr, r.fajr),
                Triple("maghrib", t.maghrib, r.maghrib), Triple("isha", t.isha, r.isha)
            )) {
                assertTrue("${r.name} $label got=$got want=$want", abs(minutes(got) - minutes(LocalTime.parse(want))) <= 2)
            }
        }
    }

    @Test
    fun imsakIsFajrMinusTheConfiguredMinutes() {
        val date = LocalDate.of(2026, 10, 15)
        val zone = ZoneId.of("Asia/Beirut")
        val t = PrayerTimes.compute(date, 33.8938, 35.5018, zone, PrayerParams.LEVA.copy(imsakMinutes = 20))
        assertEquals(minutes(t.fajr) - 20, minutes(t.imsak))
    }

    @Test
    fun maghribDelayReplacesTheAngleWithMinutesAfterSunset() {
        val date = LocalDate.of(2026, 10, 15)
        val zone = ZoneId.of("Asia/Beirut")
        val t = PrayerTimes.compute(date, 33.8938, 35.5018, zone, PrayerParams.LEVA.copy(maghribDelayMinutes = 15))
        assertTrue(abs(minutes(t.maghrib) - (minutes(t.sunset) + 15)) <= 1)
    }

    @Test
    fun manualOffsetsShiftOnlyTheChosenPrayer() {
        val date = LocalDate.of(2026, 10, 15)
        val place = com.dailydeeds.reminder.model.PlacePresets.default
        val base = PrayerSchedule.timesFor(date, place)
        val shifted = PrayerSchedule.timesFor(date, place, PrayerContext(offsets = mapOf(Prayer.DHUHR to -4, Prayer.ISHA to 3)))
        assertEquals(base.fajr, shifted.fajr)
        assertEquals(minutes(base.dhuhr) - 4, minutes(shifted.dhuhr))
        assertEquals(minutes(base.isha) + 3, minutes(shifted.isha))
        assertEquals(base.maghrib, shifted.maghrib)
    }

    @Test(expected = IllegalArgumentException::class)
    fun absurdAnglesAreRejected() {
        PrayerParams(fajrAngle = 40.0)
    }
}
