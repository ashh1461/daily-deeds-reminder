package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DayContentKind
import java.time.DayOfWeek
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeekdayRepositoryTest {

    private fun normalize(s: String): String =
        s.filterNot { it in '\u064B'..'\u0652' }
            .replace('أ', 'ا').replace('إ', 'ا').replace('آ', 'ا')

    @Test
    fun weekOrder_startsSaturdayAndCoversAllSevenDays() {
        assertEquals(DayOfWeek.SATURDAY, WeekdayRepository.weekOrder.first())
        assertEquals(DayOfWeek.values().toSet(), WeekdayRepository.weekOrder.toSet())
        assertEquals(7, WeekdayRepository.weekOrder.size)
    }

    @Test
    fun everyKindHasExactlyOneEntryPerDay() {
        DayContentKind.values().forEach { kind ->
            val entries = WeekdayRepository.all(kind)
            assertEquals(7, entries.size)
            assertEquals(WeekdayRepository.weekOrder.toSet(), entries.map { it.day }.toSet())
            entries.forEach { assertEquals(kind, it.kind) }
        }
    }

    @Test
    fun everyEntryHasNonBlankContentAndMatchingDayName() {
        DayContentKind.values().forEach { kind ->
            WeekdayRepository.all(kind).forEach { entry ->
                assertTrue("${entry.title} text", entry.text.isNotBlank())
                assertTrue("${entry.title} title", entry.title.isNotBlank())
                assertTrue("${entry.title} honoree", entry.honoree.isNotBlank())
                assertEquals(WeekdayRepository.dayNameArabic(entry.day), entry.dayNameArabic)
            }
        }
    }

    @Test
    fun ziyaratMentionsItsOwnDay() {
        WeekdayRepository.all(DayContentKind.ZIYARAT).forEach { entry ->
            assertTrue(
                "${entry.title} should mention ${entry.dayNameArabic}",
                normalize(entry.text).contains(normalize(entry.dayNameArabic))
            )
        }
    }

    @Test
    fun get_returnsEntryForRequestedDay() {
        val friday = WeekdayRepository.get(DayContentKind.DUA, DayOfWeek.FRIDAY)
        assertEquals(DayOfWeek.FRIDAY, friday.day)
        assertTrue(friday.isExcerpt)
        assertFalse(WeekdayRepository.get(DayContentKind.DUA, DayOfWeek.SATURDAY).isExcerpt)
    }
}
