package com.dailydeeds.reminder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.receiver.DailyReminderReceiver
import java.time.ZonedDateTime

object AlarmScheduler {
    const val EXTRA_REMINDER_TYPE = "extra_reminder_type"

    fun scheduleAllReminders(context: Context) {
        ReminderType.values().forEach { scheduleReminder(context, it) }
    }

    fun scheduleReminder(context: Context, type: ReminderType) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val settings = PreferencesManager(context).getReminder(type)
        if (!settings.enabled) {
            cancelReminder(context, type)
            return
        }
        val intent = Intent(context, DailyReminderReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_TYPE, type.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 2000 + type.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAt = ReminderTimeCalculator.nextOccurrence(
            ZonedDateTime.now(), settings.hour, settings.minute, type.dayOfWeek
        ).toInstant().toEpochMilli()
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    fun cancelReminder(context: Context, type: ReminderType) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pendingIntent = PendingIntent.getBroadcast(
            context, 2000 + type.id, Intent(context, DailyReminderReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
