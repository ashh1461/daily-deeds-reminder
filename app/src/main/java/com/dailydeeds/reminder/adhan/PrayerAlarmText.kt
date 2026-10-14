package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.Prayer

/** Title and body of a prayer notification. */
data class AlarmText(val title: String, val text: String)

/**
 * Wording of the prayer notifications, kept pure so that it is unit-tested. An alarm delivered long after its
 * time (Doze, inexact alarms) must neither announce "now" nor count down to a prayer time that has already come.
 */
object PrayerAlarmText {
    const val STALE_AFTER_MS = 10 * 60 * 1000L
    private const val MINUTE_MS = 60 * 1000L

    /** Id of a prayer's "time has come" notification; its pre-alarm uses this plus 100. */
    fun notificationId(prayer: Prayer) = 5000 + prayer.id

    fun isLate(scheduledAt: Long, now: Long) = scheduledAt > 0 && now - scheduledAt > STALE_AFTER_MS

    /**
     * The heads-up [preMinutes] before the prayer. Delivered late it counts the minutes really left, and once the
     * prayer time has come (the planner then skips that prayer's own alarm) it says that the time has come.
     */
    fun pre(prayer: Prayer, preMinutes: Int, scheduledAt: Long, now: Long): AlarmText {
        val elapsed = if (scheduledAt > 0) ((now - scheduledAt) / MINUTE_MS).coerceAtLeast(0) else 0L
        // Part of a minute left counts as a whole one, never "after 0 minutes".
        val remaining = preMinutes - elapsed
        return when {
            remaining <= 0 -> passed(prayer, (elapsed - preMinutes).coerceAtLeast(1))
            elapsed == 0L ->
                AlarmText("اقترب وقت صلاة ${prayer.nameArabic}", "بعد $preMinutes دقيقة يحين موعد أذان ${prayer.nameArabic}.")
            else ->
                AlarmText(
                    "اقترب وقت صلاة ${prayer.nameArabic}",
                    "بعد ${minutes(remaining, genitive = true)} يحين موعد أذان ${prayer.nameArabic}."
                )
        }
    }

    /** The prayer-time notification without sound. */
    fun silent(prayer: Prayer, scheduledAt: Long = 0L, now: Long = 0L, snooze: Boolean = false): AlarmText =
        late(prayer, scheduledAt, now, snooze)
            ?: AlarmText("حان وقت صلاة ${prayer.nameArabic}", "حان الآن موعد أذان ${prayer.nameArabic}.")

    /** The prayer-time notification with sound; also what is left behind when the adhan cannot be played. */
    fun reminder(prayer: Prayer, scheduledAt: Long = 0L, now: Long = 0L, snooze: Boolean = false): AlarmText =
        late(prayer, scheduledAt, now, snooze)
            ?: AlarmText("حان وقت صلاة ${prayer.nameArabic}", "حان الآن موعد أذان ${prayer.nameArabic} بحسب الفقه الجعفري.")

    /**
     * [n] minutes in Arabic: one and two have their own forms, 3 to 10 take the plural, 11 and more the singular.
     * Two is «دقيقتان» as a subject and «دقيقتين» after a preposition such as «بعد» ([genitive]).
     */
    fun minutes(n: Long, genitive: Boolean = false): String = when {
        n == 1L -> "دقيقة واحدة"
        n == 2L -> if (genitive) "دقيقتين" else "دقيقتان"
        n in 3L..10L -> "$n دقائق"
        else -> "$n دقيقة"
    }

    private fun late(prayer: Prayer, scheduledAt: Long, now: Long, snooze: Boolean): AlarmText? {
        if (!isLate(scheduledAt, now)) return null
        // A snooze is due some minutes after the adhan time, so how long ago the adhan was is not known.
        return if (snooze) AlarmText("دخل وقت صلاة ${prayer.nameArabic}", "مضى موعد أذان ${prayer.nameArabic}.")
        else passed(prayer, (now - scheduledAt) / MINUTE_MS)
    }

    /** The prayer time came [ago] minutes before. */
    private fun passed(prayer: Prayer, ago: Long): AlarmText =
        AlarmText("دخل وقت صلاة ${prayer.nameArabic}", "مضى على موعد أذان ${prayer.nameArabic} ${minutes(ago)}.")
}
