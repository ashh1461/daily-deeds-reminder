package com.dailydeeds.reminder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.receiver.DailyReminderReceiver
import java.util.Calendar

object AlarmScheduler {

    const val EXTRA_REMINDER_TYPE = "extra_reminder_type"
    const val TYPE_MORNING = 1
    const val TYPE_EVENING = 2

    fun scheduleAllReminders(context: Context) {
        val prefs = PreferencesManager(context)
        if (prefs.isMorningReminderEnabled()) {
            val (hour, minute) = prefs.getMorningReminderTime()
            scheduleReminder(context, TYPE_MORNING, hour, minute)
        } else {
            cancelReminder(context, TYPE_MORNING)
        }

        if (prefs.isEveningReminderEnabled()) {
            val (hour, minute) = prefs.getEveningReminderTime()
            scheduleReminder(context, TYPE_EVENING, hour, minute)
        } else {
            cancelReminder(context, TYPE_EVENING)
        }
    }

    fun scheduleReminder(context: Context, type: Int, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, DailyReminderReceiver::class.java).apply {
            putExtra(EXTRA_REMINDER_TYPE, type)
        }
        val requestCode = if (type == TYPE_MORNING) 2001 else 2002
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder(context: Context, type: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, DailyReminderReceiver::class.java)
        val requestCode = if (type == TYPE_MORNING) 2001 else 2002
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
