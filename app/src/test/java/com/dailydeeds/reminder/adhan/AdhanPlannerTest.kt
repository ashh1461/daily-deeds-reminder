package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdhanPlannerTest {
    private val beirut = PlacePresets.default
    private val zone = ZoneId.of("Asia/Beirut")
    private val calc = PrayerContext()

    private fun at(h: Int, m: Int = 0, day: Int = 15) = ZonedDateTime.of(2026, 10, day, h, m, 0, 0, zone)
    private fun allOn(mode: AdhanMode = AdhanMode.ADHAN, pre: Int = 0) =
        Prayer.values().associateWith { PrayerAlarmConfig(mode = mode, preMinutes = pre) }

    @Test
    fun nothingIsPlannedWhenEveryPrayerIsOff() {
        assertNull(AdhanPlanner.next(at(1), beirut, calc, allOn(AdhanMode.OFF)))
        assertNull(AdhanPlanner.next(at(1), beirut, calc, emptyMap()))
    }

    @Test
    fun picksTheNextEnabledPrayerAfterNow() {
        val p = AdhanPlanner.next(at(1), beirut, calc, allOn())!!
        assertEquals(Prayer.FAJR, p.prayer)
        assertEquals(AlarmKind.MAIN, p.kind)
        assertTrue(p.triggerAt.isAfter(at(1)))
        assertEquals(p.triggerAt, p.prayerAt)
    }

    @Test
    fun skipsDisabledPrayers() {
        val configs = allOn().toMutableMap()
        configs[Prayer.FAJR] = PrayerAlarmConfig(mode = AdhanMode.OFF)
        configs[Prayer.DHUHR] = PrayerAlarmConfig(mode = AdhanMode.OFF)
        val p = AdhanPlanner.next(at(1), beirut, calc, configs)!!
        assertEquals(Prayer.MAGHRIB, p.prayer)
    }

    @Test
    fun rollsToTomorrowAfterTheLastPrayer() {
        val p = AdhanPlanner.next(at(23, 30), beirut, calc, allOn())!!
        assertEquals(Prayer.FAJR, p.prayer)
        assertEquals(LocalDate.of(2026, 10, 16), p.prayerAt.toLocalDate())
    }

    @Test
    fun preAlarmComesBeforeTheMainAlarm() {
        val configs = allOn(pre = 15)
        val first = AdhanPlanner.next(at(1), beirut, calc, configs)!!
        assertEquals(AlarmKind.PRE, first.kind)
        assertEquals(first.prayerAt.minusMinutes(15), first.triggerAt)
        // Once the pre-alarm time has passed, the main alarm of the same prayer is next.
        val second = AdhanPlanner.next(first.triggerAt.plusSeconds(1), beirut, calc, configs)!!
        assertEquals(AlarmKind.MAIN, second.kind)
        assertEquals(first.prayer, second.prayer)
    }

    @Test
    fun silentModeNeverGetsAPreAlarm() {
        val p = AdhanPlanner.next(at(1), beirut, calc, allOn(AdhanMode.SILENT, pre = 15))!!
        assertEquals(AlarmKind.MAIN, p.kind)
    }

    @Test
    fun manualOffsetMovesTheAlarm() {
        val base = AdhanPlanner.next(at(1), beirut, calc, allOn())!!
        val shifted = AdhanPlanner.next(
            at(1), beirut, PrayerContext(offsets = mapOf(Prayer.FAJR to 5)),
            allOn().toMutableMap().apply { put(Prayer.FAJR, PrayerAlarmConfig(mode = AdhanMode.ADHAN, offsetMinutes = 5)) }
        )!!
        assertEquals(base.triggerAt.plusMinutes(5), shifted.triggerAt)
    }

    @Test
    fun worksAcrossTheSpringForwardDayInLondon() {
        val london = com.dailydeeds.reminder.model.Place("London", 51.5074, -0.1278, "Europe/London")
        val before = ZonedDateTime.of(2026, 3, 28, 22, 0, 0, 0, ZoneId.of("Europe/London"))
        val p = AdhanPlanner.next(before, london, calc, allOn())!!
        assertEquals(Prayer.FAJR, p.prayer)
        assertEquals(LocalDate.of(2026, 3, 29), p.prayerAt.toLocalDate())
        assertNotNull(p.triggerAt.zone)
        // Dhuhr on 29 March (BST) is 13:05 in the reference timetable; the planner must agree within 2 minutes.
        val dhuhr = AdhanPlanner.next(ZonedDateTime.of(2026, 3, 29, 9, 0, 0, 0, ZoneId.of("Europe/London")), london, calc, allOn())!!
        assertEquals(Prayer.DHUHR, dhuhr.prayer)
        val minutes = dhuhr.prayerAt.hour * 60 + dhuhr.prayerAt.minute
        assertTrue("got ${dhuhr.prayerAt}", kotlin.math.abs(minutes - (13 * 60 + 5)) <= 2)
    }

    @Test
    fun deviceInAnotherTimeZoneStillUsesThePlaceZone() {
        val utc = ZonedDateTime.of(2026, 10, 15, 0, 0, 0, 0, ZoneId.of("UTC"))
        val p = AdhanPlanner.next(utc, beirut, calc, allOn())!!
        assertEquals(zone, p.prayerAt.zone)
    }
}
