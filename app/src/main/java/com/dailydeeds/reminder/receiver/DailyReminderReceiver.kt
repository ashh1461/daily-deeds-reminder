package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.NotificationHelper

class DailyReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = ReminderType.fromId(intent.getIntExtra(AlarmScheduler.EXTRA_REMINDER_TYPE, -1)) ?: return
        if (PreferencesManager(context).getReminder(type).enabled) {
            NotificationHelper.showNotification(context, 1000 + type.id, type.title, type.message, type.category)
        }
        // Reschedule only this reminder so simultaneous alarms do not cancel each other.
        AlarmScheduler.scheduleReminder(context, type)
    }
}
