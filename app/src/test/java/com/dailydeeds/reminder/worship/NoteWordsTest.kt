package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.BlessedEves
import com.dailydeeds.reminder.util.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/** The morning notes and the blessed-eve reminder in each UI language (v1.9.0); Arabic stays the default. */
class NoteWordsTest {
    private val thursday = LocalDate.of(2026, 12, 10)

    @Test
    fun arabicIsTheDefault() {
        assertSame(ArabicNoteWords, NoteWords.of(AppLanguage.ARABIC))
        assertEquals(BlessedEves.note(thursday, 0), BlessedEves.note(thursday, 0, ArabicNoteWords))
    }

    @Test
    fun theFridayEveReminderInEnglishAndPersian() {
        val en = BlessedEves.note(thursday, 0, NoteWords.of(AppLanguage.ENGLISH))!!
        assertTrue(en.title, "Eve of Friday" in en.title)
        assertEquals("The blessed night begins with the Maghrib adhan this evening.", en.body)
        val fa = BlessedEves.note(thursday, 0, NoteWords.of(AppLanguage.PERSIAN))!!
        assertTrue(fa.title, "شب جمعه" in fa.title)
    }

    @Test
    fun eclipseTitlesNameTheType() {
        assertEquals("Total lunar eclipse", EnglishNoteWords.eclipseTitle(EclipseType.TOTAL))
        assertEquals("خسوف کلی ماه", PersianNoteWords.eclipseTitle(EclipseType.TOTAL))
        assertEquals("خسوف القمر كلي", ArabicNoteWords.eclipseTitle(EclipseType.TOTAL))
    }
}
