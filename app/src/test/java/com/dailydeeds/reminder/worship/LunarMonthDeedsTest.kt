package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.MafatihRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LunarMonthDeedsTest {

    private val book = MafatihRepository().getBookItems()
    private val sections = LunarMonthDeeds.sections(book)

    @Test
    fun everyHijriMonthHasItsMafatihChapter() {
        for (month in 1..12) {
            val s = LunarMonthDeeds.sectionFor(month, sections)
            assertTrue("month $month has no section", s != null && s.items.isNotEmpty())
        }
        assertNull(LunarMonthDeeds.sectionFor(13, sections))
    }

    @Test
    fun sectionsAreInHijriOrder() {
        assertEquals((1..12).toList(), sections.flatMap { it.months })
        assertEquals(sections[3], LunarMonthDeeds.sectionFor(5, sections))
        assertEquals(sections[3], LunarMonthDeeds.sectionFor(6, sections))
    }

    @Test
    fun theWholeSecondPartOfTheBookIsShownExactlyOnce() {
        val part = book.filter { it.group.startsWith("الباب الثاني") }.map { it.id }
        val shown = sections.flatMap { s -> s.items.map { it.id } } + LunarMonthDeeds.general(book).items.map { it.id }
        assertEquals(part.sorted(), shown.sorted())
        assertEquals(shown.size, shown.toSet().size)
    }

    @Test
    fun ramadanIncludesItsDuasAndRajabStartsWithItsVirtues() {
        val ramadan = LunarMonthDeeds.sectionFor(9, sections)!!.items.map { it.title }
        assertTrue(ramadan.any { it.contains("دعاء أبي حمزة الثمالي") })
        assertTrue(ramadan.any { it.contains("دعاء الافتتاح") })
        val rajab = LunarMonthDeeds.sectionFor(7, sections)!!
        assertTrue(rajab.items.first().title.contains("شهر رجب"))
    }

    @Test
    fun generalSectionHoldsTheIntroductionAndCommonDeeds() {
        val general = LunarMonthDeeds.general(book).items.map { it.title }
        assertEquals(2, general.size)
        assertTrue(general.any { it.contains("عامة الشهور") })
    }

    @Test
    fun theThreeOccasionsFromMafatihAreInTheCalendar() {
        val o = ShiaCalendar.OCCASIONS
        assertTrue(o.any { it.month == 12 && it.day == 7 && it.title.contains("الباقر") })
        assertTrue(o.any { it.month == 2 && it.day == 7 && it.title.contains("الكاظم") })
        assertTrue(o.any { it.month == 12 && it.day == 15 && it.title.contains("الهادي") })
        assertTrue(o.any { it.month == 2 && it.day == 3 && it.title.contains("الباقر") })
    }
}
