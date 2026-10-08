package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.HijriDay
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.util.PrayerTimesResult
import java.time.LocalDate
import java.time.chrono.HijrahDate

/**
 * One day of a monthly timetable. [qadrNight] is 19, 21 or 23 when the night that begins at this day's
 * Maghrib is one of the nights of Qadr (Ramadan only).
 */
data class TimetableRow(
    val date: LocalDate,
    val hijri: HijriDay,
    val times: PrayerTimesResult,
    val qadrNight: Int?
)

/** Monthly prayer timetable for a Hijri month, used for the Imsak and Iftar timetable of Ramadan. */
object Timetable {
    const val MIN_YEAR = 1350
    const val MAX_YEAR = 1500
    private val QADR_NIGHTS = setOf(19, 21, 23)

    /** Gregorian date of the first day of a Hijri month, taking the user's day adjustment into account. */
    fun firstDay(year: Int, month: Int, dayOffset: Int): LocalDate =
        LocalDate.from(HijrahDate.of(year, month, 1)).minusDays(dayOffset.toLong())

    fun monthLength(year: Int, month: Int): Int = HijrahDate.of(year, month, 1).lengthOfMonth()

    fun month(year: Int, month: Int, place: Place, context: PrayerContext, dayOffset: Int): List<TimetableRow> {
        require(year in MIN_YEAR..MAX_YEAR && month in 1..12)
        val start = firstDay(year, month, dayOffset)
        return (0 until monthLength(year, month)).map { i ->
            val date = start.plusDays(i.toLong())
            val day = i + 1
            TimetableRow(
                date = date,
                hijri = HijriDay(year, month, day),
                times = PrayerSchedule.timesFor(date, place, context),
                qadrNight = if (month == 9 && (day + 1) in QADR_NIGHTS) day + 1 else null
            )
        }
    }

    /** The Hijri year of the Ramadan that is under way or comes next. */
    fun nextRamadanYear(today: LocalDate, dayOffset: Int): Int {
        val h = ShiaCalendar.toHijri(today, dayOffset)
        return (if (h.month <= 9) h.year else h.year + 1).coerceIn(MIN_YEAR, MAX_YEAR)
    }
}
