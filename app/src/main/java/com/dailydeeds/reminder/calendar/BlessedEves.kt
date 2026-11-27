package com.dailydeeds.reminder.calendar

import com.dailydeeds.reminder.worship.DailyNote
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import com.dailydeeds.reminder.worship.NoteWords
import com.dailydeeds.reminder.worship.ArabicNoteWords

/**
 * The opt-in evening reminder before blessed nights: ليلة الجمعة every Thursday, and the eve of each night in
 * [ShiaCalendar.OCCASIONS] (mid-Sha'ban, the Laylat al-Qadr nights). A night begins at the sunset of the civil day
 * before it, so the reminder comes on that day, an hour before Maghrib. Pure, so it can be tested without Android.
 */
object BlessedEves {
    const val ID_NOTE = 6003
    const val FRIDAY_NIGHT = "ليلة الجمعة"
    private const val LEAD_MINUTES = 60L
    /** Used when Maghrib cannot be computed for the place that day (or is too early to subtract the lead from). */
    val FALLBACK_TIME: LocalTime = LocalTime.of(17, 0)
    /** Every Thursday is an eve, so the next one is always within a week. */
    private const val SEARCH_DAYS = 8L

    /** The blessed nights that begin at the sunset of [date]. */
    fun nightsBeginningOn(date: LocalDate, dayOffset: Int, fridayNight: String = FRIDAY_NIGHT): List<String> {
        val friday = if (date.dayOfWeek == DayOfWeek.THURSDAY) listOf(fridayNight) else emptyList()
        val listed = ShiaCalendar.occasionsOn(date, dayOffset).filter { it.kind == OccasionKind.NIGHT }.map { it.title }
        return friday + listed
    }

    /** An hour before [maghrib], or [FALLBACK_TIME] without one. */
    fun reminderTime(maghrib: LocalTime?): LocalTime {
        val earliest = LocalTime.MIDNIGHT.plusMinutes(LEAD_MINUTES)
        return if (maghrib != null && maghrib.isAfter(earliest)) maghrib.minusMinutes(LEAD_MINUTES) else FALLBACK_TIME
    }

    /**
     * The next reminder strictly after [now], in [now]'s zone (the place's). [maghribOn] gives Maghrib for a date at
     * that place, with the user's adjustment.
     */
    fun next(now: ZonedDateTime, dayOffset: Int, maghribOn: (LocalDate) -> LocalTime?): ZonedDateTime? {
        val today = now.toLocalDate()
        for (i in 0..SEARCH_DAYS) {
            val date = today.plusDays(i)
            if (nightsBeginningOn(date, dayOffset).isEmpty()) continue
            val at = date.atTime(reminderTime(maghribOn(date))).atZone(now.zone)
            if (at.isAfter(now)) return at
        }
        return null
    }

    /** The notification for the eve [date], or null when no blessed night begins that evening. */
    fun note(date: LocalDate, dayOffset: Int, words: NoteWords = ArabicNoteWords): DailyNote? {
        val nights = nightsBeginningOn(date, dayOffset, words.fridayNight)
        if (nights.isEmpty()) return null
        return DailyNote(ID_NOTE, nights.joinToString(" • "), words.eveBody)
    }
}
