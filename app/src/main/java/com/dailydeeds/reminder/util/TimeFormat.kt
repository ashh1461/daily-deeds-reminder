package com.dailydeeds.reminder.util

import java.time.Duration
import java.time.ZonedDateTime

object TimeFormat {
    /** "بعد ٣ ساعات و١٠ دقائق" style countdown; never negative. */
    fun countdown(now: ZonedDateTime, target: ZonedDateTime): String {
        val total = Duration.between(now, target).toMinutes().coerceAtLeast(0)
        val hours = total / 60
        val minutes = total % 60
        return when {
            hours == 0L && minutes == 0L -> "الآن"
            hours == 0L -> "بعد $minutes دقيقة"
            minutes == 0L -> "بعد $hours ساعة"
            else -> "بعد $hours ساعة و$minutes دقيقة"
        }
    }
}
