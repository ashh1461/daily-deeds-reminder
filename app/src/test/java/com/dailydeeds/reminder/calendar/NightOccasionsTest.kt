package com.dailydeeds.reminder.calendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/** A blessed night precedes its Hijri day, so it is announced on the civil date of its eve. */
class NightOccasionsTest {

    /** The civil date that Umm al-Qura maps to [month]/[day] of [year] AH. */
    private fun civilDate(year: Int, month: Int, day: Int): LocalDate {
        var d = LocalDate.of(2025, 1, 1)
        repeat(1200) {
            val h = ShiaCalendar.toHijri(d)
            if (h.year == year && h.month == month && h.day == day) return d
            d = d.plusDays(1)
        }
        error("$day/$month/$year not found")
    }

    @Test
    fun laylatAlQadrIsListedOnTheEve() {
        val day19 = civilDate(1447, 9, 19)
        val eve = ShiaCalendar.occasionsOn(day19.minusDays(1))
        assertTrue(eve.any { it.title == "ليلة القدر الأولى" })
        val onDay = ShiaCalendar.occasionsOn(day19)
        assertFalse(onDay.any { it.title == "ليلة القدر الأولى" })
        // The strike happened at dawn on the 19th itself.
        assertTrue(onDay.any { it.title.contains("ضربة") })
    }

    @Test
    fun everyNightIsOnTheEveOfItsDay() {
        for (o in ShiaCalendar.OCCASIONS.filter { it.kind == OccasionKind.NIGHT }) {
            val day = civilDate(1447, o.month, o.day)
            assertTrue(o.title, ShiaCalendar.occasionsOn(day.minusDays(1)).contains(o))
            assertFalse(o.title, ShiaCalendar.occasionsOn(day).contains(o))
        }
    }

    @Test
    fun midShabanBirthStaysOnTheDayAndTheNightMovesToTheEve() {
        val day15 = civilDate(1447, 8, 15)
        assertTrue(ShiaCalendar.occasionsOn(day15).any { it.title.contains("مولد الإمام المهدي") })
        assertTrue(ShiaCalendar.occasionsOn(day15.minusDays(1)).any { it.title == "ليلة النصف من شعبان" })
    }

    @Test
    fun nightsSayTheyBeginThisEvening() {
        val night = ShiaCalendar.OCCASIONS.first { it.kind == OccasionKind.NIGHT }
        assertTrue(night.displayTitle.contains("مغرب"))
        val day = ShiaCalendar.OCCASIONS.first { it.kind != OccasionKind.NIGHT }
        assertEquals(day.title, day.displayTitle)
    }

    @Test
    fun upcomingListsTheNightOnItsEve() {
        val day21 = civilDate(1447, 9, 21)
        val list = ShiaCalendar.upcoming(day21.minusDays(5), 10)
        val entry = list.first { it.occasion.title == "ليلة القدر الثانية" }
        assertEquals(day21.minusDays(1), entry.date)
    }
}
