package com.dailydeeds.reminder.util

import java.time.Duration
import java.time.ZonedDateTime

object TimeFormat {
    /** Time left until a moment, in whole hours and minutes. The UI words it (ui/Labels.kt countdownText). */
    data class Countdown(val hours: Long, val minutes: Long) {
        val isNow: Boolean get() = hours == 0L && minutes == 0L
    }

    /** Never negative: a moment already past is "now". */
    fun countdown(now: ZonedDateTime, target: ZonedDateTime): Countdown {
        val total = Duration.between(now, target).toMinutes().coerceAtLeast(0)
        return Countdown(total / 60, total % 60)
    }
}
