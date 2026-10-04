package com.dailydeeds.reminder.notification

import java.time.DayOfWeek
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

object ReminderTimeCalculator {
    fun nextOccurrence(
        now: ZonedDateTime,
        hour: Int,
        minute: Int,
        dayOfWeek: DayOfWeek? = null
    ): ZonedDateTime {
        require(hour in 0..23 && minute in 0..59)
        var date = now.toLocalDate()
        if (dayOfWeek != null) date = date.with(TemporalAdjusters.nextOrSame(dayOfWeek))
        var next = date.atTime(hour, minute).atZone(now.zone)
        if (!next.isAfter(now)) {
            date = date.plusDays(if (dayOfWeek == null) 1 else 7)
            next = date.atTime(hour, minute).atZone(now.zone)
        }
        return next
    }
}
