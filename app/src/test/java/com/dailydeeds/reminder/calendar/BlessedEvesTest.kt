package com.dailydeeds.reminder.calendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/** The evening reminder before blessed nights: every Thursday for ليلة الجمعة, and the eve of each listed night. */
class BlessedEvesTest {
    private val zone = ZoneId.of("Asia/Beirut")
    private val maghrib = LocalTime.of(16, 40)
    private val maghribOf: (LocalDate) -> LocalTime? = { maghrib }

    @Test
    fun everyThursdayIsTheEveOfLaylatAlJumua() {
        val thursday = LocalDate.of(2026, 12, 10)
        assertEquals(DayOfWeek.THURSDAY, thursday.dayOfWeek)
        assertTrue("ليلة الجمعة" in BlessedEves.nightsBeginningOn(thursday, 0))
        assertTrue(BlessedEves.nightsBeginningOn(thursday.plusDays(1), 0).none { it == "ليلة الجمعة" })
    }

    @Test
    fun theEveOfMidShabanIsListed() {
        // The civil day whose sunset begins the night of 15 Sha'ban.
        val eve = (0L..400L).map { LocalDate.of(2027, 1, 1).plusDays(it) }
            .first { ShiaCalendar.toHijri(it.plusDays(1)).let { h -> h.month == 8 && h.day == 15 } }
        assertTrue("ليلة النصف من شعبان" in BlessedEves.nightsBeginningOn(eve, 0))
        assertTrue(BlessedEves.nightsBeginningOn(eve.plusDays(1), 0).none { it == "ليلة النصف من شعبان" })
    }

    @Test
    fun theReminderIsAnHourBeforeMaghrib() {
        assertEquals(LocalTime.of(15, 40), BlessedEves.reminderTime(maghrib))
        assertEquals(BlessedEves.FALLBACK_TIME, BlessedEves.reminderTime(null))
    }

    @Test
    fun onAThursdayBeforeTheTimeItIsToday() {
        val now = ZonedDateTime.of(2026, 12, 10, 9, 0, 0, 0, zone)
        assertEquals(ZonedDateTime.of(2026, 12, 10, 15, 40, 0, 0, zone), BlessedEves.next(now, 0, maghribOf))
    }

    @Test
    fun afterTheTimeItIsTheNextEve() {
        val monday = LocalDate.of(2026, 12, 7)
        // No listed night falls in this week, so the next eve is Thursday.
        (0L..10L).forEach { i -> assertTrue(ShiaCalendar.occasionsOn(monday.plusDays(i)).none { it.kind == OccasionKind.NIGHT }) }
        val thursdayEvening = ZonedDateTime.of(2026, 12, 10, 15, 40, 0, 0, zone)
        assertEquals(thursdayEvening.plusDays(7), BlessedEves.next(thursdayEvening, 0, maghribOf))
        assertEquals(thursdayEvening, BlessedEves.next(ZonedDateTime.of(2026, 12, 7, 20, 0, 0, 0, zone), 0, maghribOf))
    }

    @Test
    fun theNoteNamesTheNightsOrNothing() {
        val note = BlessedEves.note(LocalDate.of(2026, 12, 10), 0)!!
        assertEquals(BlessedEves.ID_NOTE, note.id)
        assertTrue(note.title.contains("ليلة الجمعة"))
        assertTrue(note.body.contains("المغرب"))
        assertNull(BlessedEves.note(LocalDate.of(2026, 12, 8), 0))
    }
}
