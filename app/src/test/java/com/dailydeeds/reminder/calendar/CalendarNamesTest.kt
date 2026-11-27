package com.dailydeeds.reminder.calendar

import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.util.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.DayOfWeek

/** Hijri months and dates, occasions, weekdays and suggested cities in each UI language (v1.9.0). */
class CalendarNamesTest {
    private val ar = AppLanguage.ARABIC
    private val en = AppLanguage.ENGLISH
    private val fa = AppLanguage.PERSIAN
    private val arabicScript = Regex("[؀-ۿ]")

    @Test
    fun arabicIsExactlyWhatTheAppShowedBefore() {
        val day = HijriDay(1448, 6, 17)
        assertEquals(day.toString(), CalendarNames.date(day, ar))
        ShiaCalendar.OCCASIONS.forEach {
            assertEquals(it.title, CalendarNames.occasion(it, ar))
            assertEquals(it.displayTitle, CalendarNames.occasionDisplay(it, ar))
        }
        (1..12).forEach { assertEquals(ShiaCalendar.MONTH_NAMES[it - 1], CalendarNames.month(it, ar)) }
        PlacePresets.all.forEach { assertEquals(it.name, CalendarNames.place(it, ar)) }
    }

    @Test
    fun everyOccasionHasAnEnglishAndAPersianTitle() {
        ShiaCalendar.OCCASIONS.forEach { o ->
            val english = CalendarNames.occasion(o, en)
            assertFalse("no English title for ${o.title}: $english", arabicScript.containsMatchIn(english))
            assertNotEquals("no Persian title for ${o.title}", o.title, CalendarNames.occasion(o, fa))
        }
    }

    @Test
    fun hijriDatesInEnglishAndPersian() {
        val day = HijriDay(1448, 6, 17)
        assertEquals("17 Jumada al-Akhirah 1448 AH", CalendarNames.date(day, en))
        assertEquals("۱۷ جمادی‌الثانی ۱۴۴۸ ق", CalendarNames.date(day, fa))
        assertEquals("Ramadan", CalendarNames.month(9, en))
    }

    @Test
    fun nightsSayTheyBeginThisEvening() {
        val night = ShiaCalendar.OCCASIONS.first { it.kind == OccasionKind.NIGHT }
        assertEquals("${CalendarNames.occasion(night, en)} (begins at Maghrib today)", CalendarNames.occasionDisplay(night, en))
    }

    @Test
    fun everySuggestedCityHasAnEnglishNameAndOtherPlacesKeepTheirs() {
        PlacePresets.all.forEach {
            assertFalse("no English name for ${it.name}", arabicScript.containsMatchIn(CalendarNames.place(it, en)))
        }
        val custom = Place("مكان ما", 1.0, 2.0, "UTC")
        assertEquals("مكان ما", CalendarNames.place(custom, en))
    }

    @Test
    fun weekdays() {
        assertEquals("السبت", CalendarNames.weekday(DayOfWeek.SATURDAY, ar))
        assertEquals("Saturday", CalendarNames.weekday(DayOfWeek.SATURDAY, en))
        assertEquals("شنبه", CalendarNames.weekday(DayOfWeek.SATURDAY, fa))
    }
}
