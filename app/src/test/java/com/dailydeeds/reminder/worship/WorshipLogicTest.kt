package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.widget.WidgetModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.HijrahDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TasbihTest {
    @Test
    fun zahraStagesFollow34_33_33() {
        assertEquals(100, ZahraStages.STAGES.sumOf { it.target })
        assertEquals(listOf(0, 0, 1, 1, 2, 2), listOf(1, 34, 35, 67, 68, 100).map(ZahraStages::stageIndex))
        assertEquals(listOf(34, 1, 33, 1, 33), listOf(34, 35, 67, 68, 100).map(ZahraStages::doneInStage))
        assertEquals(0, ZahraStages.doneInStage(0))
    }

    @Test
    fun zahraCompletesOnceAndThenWaitsForReset() {
        var s = TasbihState()
        repeat(150) { s = s.tapZahra() }
        assertEquals(100, s.zahraCount)
        assertEquals(1, s.zahraCompletions)
        assertEquals(100L, s.lifetime)
        assertTrue(s.zahraDone)
        s = s.resetZahra().tapZahra()
        assertEquals(1, s.zahraCount)
        assertEquals(1, s.zahraCompletions)
    }

    @Test
    fun freeCounterStopsAtItsTarget() {
        var s = TasbihState().withTarget(33)
        repeat(40) { s = s.tapFree() }
        assertEquals(33, s.freeCount)
        assertTrue(s.freeDone)
        s = s.withTarget(0)
        repeat(5) { s = s.tapFree() }
        assertEquals(5, s.freeCount)
        assertFalse(s.freeDone)
    }

    @Test
    fun stateSurvivesEncodingAndGarbage() {
        val s = TasbihState(zahraCount = 40, zahraCompletions = 7, freeCount = 12, freeTarget = 99, lifetime = 12345)
        assertEquals(s, TasbihState.decode(s.encode()))
        assertEquals(TasbihState(), TasbihState.decode(null))
        assertEquals(TasbihState(), TasbihState.decode("1,2,3"))
        assertEquals(TasbihState(), TasbihState.decode("a,b,c,d,e"))
        val clamped = TasbihState.decode("500,1,50,10,-5")
        assertEquals(100, clamped.zahraCount)
        assertEquals(10, clamped.freeCount)
        assertEquals(0L, clamped.lifetime)
    }
}

class QadaTest {
    @Test
    fun addAndMarkMadeNeverGoBelowZero() {
        var s = QadaState().add(QadaKind.FAJR, 5, 100).add(QadaKind.FAST, 3, 100)
        assertEquals(5, s.owed(QadaKind.FAJR))
        assertEquals(5, s.totalPrayers)
        assertEquals(3, s.totalFasts)
        s = s.markMade(QadaKind.FAJR, 2, 101)
        assertEquals(3, s.owed(QadaKind.FAJR))
        s = s.markMade(QadaKind.FAJR, 50, 101)
        assertEquals(0, s.owed(QadaKind.FAJR))
        assertEquals(s, s.markMade(QadaKind.FAJR, 1, 102))
        assertEquals(s, s.add(QadaKind.DHUHR, -4, 102))
    }

    @Test
    fun bulkAddCoversTheFiveDailyPrayersButNotFasts() {
        val s = QadaState().addDaysOfPrayers(30, 1)
        assertEquals(150, s.totalPrayers)
        assertEquals(0, s.totalFasts)
        assertEquals(30, s.owed(QadaKind.ASR))
    }

    @Test
    fun undoRevertsTheLatestChange() {
        var s = QadaState().add(QadaKind.ISHA, 4, 1).markMade(QadaKind.ISHA, 1, 2)
        assertEquals(3, s.owed(QadaKind.ISHA))
        s = s.undoLast()
        assertEquals(4, s.owed(QadaKind.ISHA))
        s = s.undoLast()
        assertEquals(0, s.owed(QadaKind.ISHA))
        assertTrue(s.history.isEmpty())
        assertEquals(s, s.undoLast())
    }

    @Test
    fun historyIsBounded() {
        var s = QadaState()
        repeat(250) { s = s.add(QadaKind.MAGHRIB, 1, it.toLong()) }
        assertEquals(QadaState.MAX_HISTORY, s.history.size)
        assertEquals(250, s.owed(QadaKind.MAGHRIB))
    }

    @Test
    fun encodingRoundTripsAndToleratesGarbage() {
        val s = QadaState().add(QadaKind.FAJR, 3, 19000).add(QadaKind.FAST, 10, 19001).markMade(QadaKind.FAST, 4, 19002)
        assertEquals(s, QadaState.decode(s.encode()))
        assertEquals(QadaState(), QadaState.decode(null))
        assertEquals(QadaState(), QadaState.decode("???"))
        val partial = QadaState.decode("fajr=2,bogus=5,dhuhr=x,isha=-3|1:fajr:2;2:zzz:1;3:fajr:0;bad")
        assertEquals(2, partial.owed(QadaKind.FAJR))
        assertEquals(0, partial.owed(QadaKind.ISHA))
        assertEquals(1, partial.history.size)
    }
}

class KhumsTest {
    private fun d(s: String) = BigDecimal(s)

    @Test
    fun khumsIsOneFifthOfTheSurplus() {
        val r = Khums.calculate(KhumsInput(d("100000"), d("60000"), d("10000"), d("1000")))
        assertEquals(d("30000.00"), r.surplus)
        assertEquals(d("6000.00"), r.khums)
        assertEquals(d("5000.00"), r.remaining)
        assertEquals(d("2500.00"), r.shareOfImam)
        assertEquals(d("2500.00"), r.shareOfSadat)
    }

    @Test
    fun noSurplusMeansNoKhums() {
        val r = Khums.calculate(KhumsInput(d("1000"), d("5000"), d("0"), d("0")))
        assertEquals(0, r.surplus.signum())
        assertEquals(0, r.khums.signum())
        assertEquals(0, r.remaining.signum())
    }

    @Test
    fun overpaymentLeavesNothingRemaining() {
        val r = Khums.calculate(KhumsInput(d("10000"), d("0"), d("0"), d("5000")))
        assertEquals(d("2000.00"), r.khums)
        assertEquals(0, r.remaining.signum())
    }

    @Test
    fun oddCentsAreSplitWithoutLosingAny() {
        val r = Khums.calculate(KhumsInput(d("0.15"), d("0"), d("0"), d("0")))
        assertEquals(r.remaining, r.shareOfImam + r.shareOfSadat)
    }

    @Test
    fun amountsAcceptArabicDigitsAndSeparators() {
        assertEquals(0, d("12345.50").compareTo(Khums.parseAmount("١٢٬٣٤٥٫٥٠")!!))
        assertEquals(0, d("1234567").compareTo(Khums.parseAmount("1,234,567")!!))
        assertEquals(0, d("42").compareTo(Khums.parseAmount(" ۴۲ ")!!))
        listOf("", "abc", "-5", "1.2.3", "12e3", "9".repeat(30), "1 000 000 000 000 000 000").forEach {
            assertNull(it, Khums.parseAmount(it))
        }
    }

    @Test
    fun khumsYearFindsTheNextAnniversary() {
        val today = LocalDate.of(2026, 10, 8)
        val h = ShiaCalendar.toHijri(today)
        assertEquals(today, Khums.nextAnniversary(today, KhumsYear(h.month, h.day)))
        val yesterdayNextYear = Khums.nextAnniversary(today, KhumsYear(h.month, h.day - 1))!!
        val days = java.time.temporal.ChronoUnit.DAYS.between(today, yesterdayNextYear)
        assertTrue("days=$days", days in 340..360)
        assertNotNull(Khums.nextAnniversary(today, KhumsYear(1, 30)))
    }

    @Test
    fun khumsYearEncodingIsStrict() {
        assertEquals(KhumsYear(9, 1), KhumsYear.decode(KhumsYear(9, 1).encode()))
        listOf(null, "", "9", "13,1", "0,5", "9,31", "a,b", "9,1,2").forEach { assertNull(it, KhumsYear.decode(it)) }
    }
}

class TimetableTest {
    private val beirut = PlacePresets.default

    @Test
    fun ramadanTimetableCoversTheWholeMonth() {
        val year = Timetable.nextRamadanYear(LocalDate.of(2026, 10, 8), 0)
        val rows = Timetable.month(year, 9, beirut, PrayerContext(), 0)
        assertTrue(rows.size in 29..30)
        assertEquals(1, rows.first().hijri.day)
        assertTrue(rows.zipWithNext().all { (a, b) -> b.date == a.date.plusDays(1) })
        assertTrue(rows.all { ShiaCalendar.toHijri(it.date).let { h -> h.month == 9 && h.year == year } })
        for (r in rows) {
            val t = r.times
            assertTrue(t.imsak!! < t.fajr!! && t.fajr!! < t.sunrise!! && t.sunrise!! < t.dhuhr!! && t.dhuhr!! < t.maghrib!! && t.maghrib!! < t.isha!!)
        }
    }

    @Test
    fun theNightsOfQadrStartAtMaghribOfTheDayBefore() {
        val rows = Timetable.month(1448, 9, beirut, PrayerContext(), 0)
        assertEquals(listOf(18 to 19, 20 to 21, 22 to 23), rows.filter { it.qadrNight != null }.map { it.hijri.day to it.qadrNight!! })
        assertTrue(Timetable.month(1448, 10, beirut, PrayerContext(), 0).all { it.qadrNight == null })
    }

    @Test
    fun dayAdjustmentShiftsTheWholeMonth() {
        val a = Timetable.firstDay(1448, 9, 0)
        assertEquals(a.minusDays(1), Timetable.firstDay(1448, 9, 1))
        assertEquals(a.plusDays(1), Timetable.firstDay(1448, 9, -1))
        assertEquals(LocalDate.from(HijrahDate.of(1448, 9, 1)), a)
    }

    @Test
    fun nextRamadanIsThisYearUntilTheMonthEnds() {
        val h = ShiaCalendar.toHijri(LocalDate.of(2026, 10, 8))
        assertEquals(h.year, Timetable.nextRamadanYear(LocalDate.of(2026, 10, 8), 0))
        val afterRamadan = Timetable.firstDay(1448, 10, 0).plusDays(3)
        assertEquals(1449, Timetable.nextRamadanYear(afterRamadan, 0))
        val duringRamadan = Timetable.firstDay(1448, 9, 0).plusDays(5)
        assertEquals(1448, Timetable.nextRamadanYear(duringRamadan, 0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun absurdYearsAreRejected() {
        Timetable.month(5000, 9, beirut, PrayerContext(), 0)
    }
}

class DailyNotesTest {
    private val tokyo = Place("طوكيو", 35.6762, 139.6503, "Asia/Tokyo")
    private val beirut = PlacePresets.default
    private val zone = ZoneId.of("Asia/Beirut")

    @Test
    fun anOccasionNoteAppearsOnlyWhenSwitchedOn() {
        val ashura = LocalDate.from(HijrahDate.of(1448, 1, 10))
        val now = ashura.atTime(8, 0).atZone(zone)
        val on = DailyNotes.build(now, beirut, 0, DailyNotesConfig(occasions = true), emptyList())
        assertEquals(listOf(DailyNotes.ID_OCCASION), on.map { it.id })
        assertTrue(on.single().body.contains("عاشوراء"))
        assertTrue(DailyNotes.build(now, beirut, 0, DailyNotesConfig(), emptyList()).isEmpty())
    }

    @Test
    fun khumsReminderComesOnTheDayAndAWeekBefore() {
        val anniversary = LocalDate.of(2027, 1, 20)
        val h = ShiaCalendar.toHijri(anniversary)
        val config = DailyNotesConfig(khumsYear = KhumsYear(h.month, h.day))
        val onDay = DailyNotes.build(anniversary.atTime(8, 0).atZone(zone), beirut, 0, config, emptyList())
        assertEquals("رأس السنة الخمسية", onDay.single().title)
        val weekBefore = DailyNotes.build(anniversary.minusDays(7).atTime(8, 0).atZone(zone), beirut, 0, config, emptyList())
        assertEquals("اقتراب رأس السنة الخمسية", weekBefore.single().title)
        assertTrue(DailyNotes.build(anniversary.minusDays(3).atTime(8, 0).atZone(zone), beirut, 0, config, emptyList()).isEmpty())
    }

    @Test
    fun lunarEclipseNoteNeedsAVisibleEclipseWithinADay() {
        val march = Eclipses.all.first { it.greatest.toString().startsWith("2026-03-03") && it.kind == EclipseKind.LUNAR }
        val config = DailyNotesConfig(lunarEclipse = true)
        val morning = ZonedDateTime.of(2026, 3, 3, 8, 0, 0, 0, ZoneId.of("Asia/Tokyo"))
        val seen = DailyNotes.build(morning, tokyo, 0, config, listOf(march))
        assertEquals(listOf(DailyNotes.ID_ECLIPSE), seen.map { it.id })
        assertTrue(seen.single().body.contains("20:3"))
        // In Beirut the Moon is below the horizon at that moment.
        assertTrue(DailyNotes.build(ZonedDateTime.of(2026, 3, 3, 8, 0, 0, 0, zone), beirut, 0, config, listOf(march)).isEmpty())
        // More than a day away: nothing yet.
        assertTrue(DailyNotes.build(morning.minusDays(2), tokyo, 0, config, listOf(march)).isEmpty())
        // Penumbral eclipses need no prayer.
        val penumbral = Eclipses.all.first { it.type == EclipseType.PENUMBRAL }
        val atPenumbral = penumbral.greatest.atZone(tokyo.zone).minusHours(3)
        assertTrue(DailyNotes.build(atPenumbral, tokyo, 0, config, listOf(penumbral)).isEmpty())
    }
}

class WidgetModelTest {
    private val beirut = PlacePresets.default
    private val zone = ZoneId.of("Asia/Beirut")

    @Test
    fun showsTheNextPrayerAndWhenTheWidgetGoesStale() {
        val m = WidgetModel.build(ZonedDateTime.of(2026, 10, 15, 10, 0, 0, 0, zone), beirut, PrayerContext(), 0)
        assertEquals("الظهر", m.nextPrayer)
        assertTrue(Regex("\\d\\d:\\d\\d").matches(m.nextTime))
        assertEquals(m.nextTime, m.validUntil!!.toLocalTime().toString().take(5))
        assertEquals(beirut.name, m.placeName)
        assertTrue(m.hijriDate.contains("هـ"))
    }

    @Test
    fun rollsToFajrAfterIsha() {
        val m = WidgetModel.build(ZonedDateTime.of(2026, 10, 15, 23, 30, 0, 0, zone), beirut, PrayerContext(), 0)
        assertEquals("الفجر", m.nextPrayer)
        assertEquals(16, m.validUntil!!.dayOfMonth)
    }

    @Test
    fun carriesTheOccasionOfTheDay() {
        val ashura = LocalDate.from(HijrahDate.of(1448, 1, 10))
        val m = WidgetModel.build(ashura.atTime(9, 0).atZone(zone), beirut, PrayerContext(), 0)
        assertTrue(m.occasion!!.contains("عاشوراء"))
        val plain = WidgetModel.build(ashura.plusDays(3).atTime(9, 0).atZone(zone), beirut, PrayerContext(), 0)
        assertTrue(plain.occasion == null || !plain.occasion!!.contains("عاشوراء"))
    }
}
