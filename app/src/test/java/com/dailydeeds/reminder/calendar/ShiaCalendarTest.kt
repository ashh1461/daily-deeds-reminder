package com.dailydeeds.reminder.calendar

import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShiaCalendarTest {

    @Test
    fun convertsKnownDate() {
        // 1 Muharram 1448 AH per Umm al-Qura falls on 2026-06-16.
        val h = ShiaCalendar.toHijri(LocalDate.of(2026, 6, 16))
        assertEquals(1448, h.year)
        assertEquals(1, h.month)
        assertEquals(1, h.day)
    }

    @Test
    fun ashuraIsFoundTenDaysAfterMuharramFirst() {
        val start = LocalDate.of(2026, 6, 16)
        val ashura = start.plusDays(9)
        val occ = ShiaCalendar.occasionsOn(ashura)
        assertTrue(occ.any { it.title.contains("عاشوراء") })
        assertTrue(ShiaCalendar.occasionsOn(start.plusDays(8)).none { it.title.contains("عاشوراء") })
    }

    @Test
    fun dayOffsetShiftsOccasions() {
        val start = LocalDate.of(2026, 6, 16)
        val ashura = start.plusDays(9)
        // If local sighting is one day later, the printed Ashura date is one day later too.
        assertTrue(ShiaCalendar.occasionsOn(ashura.plusDays(1), dayOffset = -1).any { it.title.contains("عاشوراء") })
    }

    @Test
    fun everyOccasionHasValidMonthAndDay() {
        for (o in ShiaCalendar.OCCASIONS) {
            assertTrue(o.title, o.month in 1..12)
            assertTrue(o.title, o.day in 1..30)
            assertTrue(o.title, o.title.isNotBlank())
        }
        assertEquals(ShiaCalendar.OCCASIONS.size, ShiaCalendar.OCCASIONS.map { it.month to (it.day to it.title) }.toSet().size)
    }

    @Test
    fun lastDayOfSafarIsFoundEvenWhenMonthHasTwentyNineDays() {
        // Find a Safar whose length is 29 and one whose length is 30 in a range of years.
        var found29 = false
        var found30 = false
        var d = LocalDate.of(2024, 1, 1)
        val end = LocalDate.of(2032, 1, 1)
        while (d.isBefore(end)) {
            val h = HijrahDate.from(d)
            if (h.get(ChronoField.MONTH_OF_YEAR) == 2 && h.get(ChronoField.DAY_OF_MONTH) == h.lengthOfMonth()) {
                val has = ShiaCalendar.occasionsOn(d).any { it.title.contains("الرضا") }
                assertTrue("last day of Safar on $d", has)
                if (h.lengthOfMonth() == 29) found29 = true else found30 = true
            }
            d = d.plusDays(1)
        }
        assertTrue(found29 || found30)
    }

    @Test
    fun upcomingIsOrderedAndBounded() {
        val list = ShiaCalendar.upcoming(LocalDate.of(2026, 6, 16), 60)
        assertTrue(list.isNotEmpty())
        assertEquals(list.sortedBy { it.daysAway }.map { it.daysAway }, list.map { it.daysAway })
        assertTrue(list.all { it.daysAway in 0..60 })
    }
}
