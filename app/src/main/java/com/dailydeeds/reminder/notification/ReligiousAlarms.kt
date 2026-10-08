package com.dailydeeds.reminder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.receiver.OccasionAlarmReceiver
import com.dailydeeds.reminder.receiver.PrayerAlarmReceiver
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerSchedule
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Alarms for prayer times and Ahl al-Bayt occasions. Each alarm fires once, shows its notification,
 * and the receiver schedules the next one, so only a single alarm per kind is ever pending.
 */
object ReligiousAlarms {
    const val EXTRA_PRAYER_ID = "extra_prayer_id"
    private const val PRAYER_REQUEST_CODE = 3000
    private const val OCCASION_REQUEST_CODE = 3001
    const val OCCASION_HOUR = 8

    fun scheduleAll(context: Context) {
        schedulePrayer(context)
        scheduleOccasion(context)
    }

    /** Schedules the next enabled prayer alarm, or cancels the pending one if none are enabled. */
    fun schedulePrayer(context: Context) {
        val prefs = PreferencesManager(context)
        val enabled = Prayer.values().filter { prefs.isPrayerAlarmEnabled(it) }.toSet()
        if (enabled.isEmpty()) {
            cancel(context, PRAYER_REQUEST_CODE, PrayerAlarmReceiver::class.java)
            return
        }
        val place = prefs.getPlace()
        var cursor = ZonedDateTime.now()
        // Skip prayers the user has not enabled; bounded so a bad state can never loop forever.
        repeat(12) {
            val next = PrayerSchedule.next(cursor, place) ?: return
            if (next.prayer in enabled) {
                val intent = Intent(context, PrayerAlarmReceiver::class.java)
                    .putExtra(EXTRA_PRAYER_ID, next.prayer.id)
                set(context, PRAYER_REQUEST_CODE, next.time.toInstant().toEpochMilli(), intent)
                return
            }
            cursor = next.time
        }
    }

    fun scheduleOccasion(context: Context) {
        val prefs = PreferencesManager(context)
        if (!prefs.isOccasionReminderEnabled()) {
            cancel(context, OCCASION_REQUEST_CODE, OccasionAlarmReceiver::class.java)
            return
        }
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        var at = now.toLocalDate().atTime(OCCASION_HOUR, 0).atZone(zone)
        if (!at.isAfter(now)) at = at.plusDays(1)
        set(context, OCCASION_REQUEST_CODE, at.toInstant().toEpochMilli(), Intent(context, OccasionAlarmReceiver::class.java))
    }

    fun today(): LocalDate = LocalDate.now()

    private fun set(context: Context, requestCode: Int, triggerAt: Long, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pending = PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        }
    }

    private fun cancel(context: Context, requestCode: Int, receiver: Class<*>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pending = PendingIntent.getBroadcast(
            context, requestCode, Intent(context, receiver),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return
        alarmManager.cancel(pending)
        pending.cancel()
    }
}
