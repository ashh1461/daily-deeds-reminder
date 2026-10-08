package com.dailydeeds.reminder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dailydeeds.reminder.adhan.AdhanPlanner
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.receiver.OccasionAlarmReceiver
import com.dailydeeds.reminder.receiver.PrayerAlarmReceiver
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Alarms for prayer times and Ahl al-Bayt occasions. Each alarm fires once, the receiver handles it and
 * schedules the next, so at most one prayer alarm (plus one snooze) is ever pending.
 */
object ReligiousAlarms {
    const val EXTRA_PRAYER_ID = "extra_prayer_id"
    const val EXTRA_KIND = "extra_alarm_kind"
    const val EXTRA_SCHEDULED_AT = "extra_scheduled_at"
    const val EXTRA_SNOOZE = "extra_snooze"
    private const val PRAYER_REQUEST_CODE = 3000
    private const val OCCASION_REQUEST_CODE = 3001
    private const val SNOOZE_REQUEST_CODE = 3002
    const val OCCASION_HOUR = 8

    fun scheduleAll(context: Context) {
        schedulePrayer(context)
        scheduleOccasion(context)
    }

    /** Schedules the next prayer or pre-adhan alarm from the saved configuration, or cancels it if none is on. */
    fun schedulePrayer(context: Context) {
        val prefs = PreferencesManager(context)
        val configs = Prayer.values().associateWith { prefs.getAlarmConfig(it) }
        val calc = PrayerContext(prefs.getPrayerSettings().params(), configs.mapValues { it.value.offsetMinutes })
        val planned = AdhanPlanner.next(ZonedDateTime.now(), prefs.getPlace(), calc, configs)
        if (planned == null) {
            cancel(context, PRAYER_REQUEST_CODE, PrayerAlarmReceiver::class.java)
            return
        }
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
            .putExtra(EXTRA_PRAYER_ID, planned.prayer.id)
            .putExtra(EXTRA_KIND, planned.kind.name)
            .putExtra(EXTRA_SCHEDULED_AT, planned.triggerAt.toInstant().toEpochMilli())
        set(context, PRAYER_REQUEST_CODE, planned.triggerAt.toInstant().toEpochMilli(), intent)
    }

    /** One-off "play the adhan again in [minutes]" alarm. */
    fun scheduleSnooze(context: Context, prayer: Prayer, minutes: Int) {
        val at = System.currentTimeMillis() + minutes * 60_000L
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
            .putExtra(EXTRA_PRAYER_ID, prayer.id)
            .putExtra(EXTRA_KIND, AlarmKind.MAIN.name)
            .putExtra(EXTRA_SCHEDULED_AT, at)
            .putExtra(EXTRA_SNOOZE, true)
        set(context, SNOOZE_REQUEST_CODE, at, intent)
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
