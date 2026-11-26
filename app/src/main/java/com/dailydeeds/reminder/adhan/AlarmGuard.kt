package com.dailydeeds.reminder.adhan

/**
 * Rules that keep prayer alarms from firing twice or getting lost. Pure logic, so it can be unit-tested; the
 * receivers and [com.dailydeeds.reminder.notification.ReligiousAlarms] apply it.
 */
object AlarmGuard {
    /**
     * How far back a re-delivered alarm counts as "already handled". A clock moved back is in practice the end of
     * summer time (one hour) or a small correction; a wider window would let a clock that was wrongly set ahead,
     * then corrected, silence real prayers.
     */
    const val REPEAT_WINDOW_MS = 3 * 60 * 60 * 1000L

    /**
     * True when a planned alarm for [scheduledAt] was already handled, because the clock was moved back after it
     * fired and the plan produced it again. [lastHandled] is the latest planned instant handled so far (0 if none).
     * A snooze is never a repeat.
     */
    fun isRepeat(scheduledAt: Long, lastHandled: Long, snooze: Boolean): Boolean =
        !snooze && scheduledAt > 0 && lastHandled > 0 &&
            scheduledAt <= lastHandled && lastHandled - scheduledAt < REPEAT_WINDOW_MS
}

/**
 * Snoozes waiting to play again, kept in preferences so a reboot does not lose them (alarms do not survive one).
 * Stored as `prayerId:epochMillis` pairs, e.g. `1:1767225600000,3:1767250800000`.
 */
object PendingSnoozes {
    fun decode(text: String?): Map<Int, Long> =
        text.orEmpty().split(',').mapNotNull { pair ->
            val parts = pair.split(':')
            if (parts.size != 2) return@mapNotNull null
            val id = parts[0].toIntOrNull() ?: return@mapNotNull null
            val at = parts[1].toLongOrNull()?.takeIf { it > 0 } ?: return@mapNotNull null
            id to at
        }.toMap()

    fun encode(snoozes: Map<Int, Long>): String =
        snoozes.entries.sortedBy { it.key }.joinToString(",") { "${it.key}:${it.value}" }

    /** The snoozes still to come at [now]; a snooze whose time passed while the phone was off is dropped. */
    fun stillDue(snoozes: Map<Int, Long>, now: Long): Map<Int, Long> = snoozes.filterValues { it > now }
}
