package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms

class OccasionAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferencesManager(context)
        if (prefs.isOccasionReminderEnabled()) {
            val today = ReligiousAlarms.today()
            val offset = prefs.getHijriOffset()
            val occasions = ShiaCalendar.occasionsOn(today, offset)
            if (occasions.isNotEmpty()) {
                NotificationHelper.showNotification(
                    context, 6000, "مناسبة اليوم: ${ShiaCalendar.toHijri(today, offset)}",
                    occasions.joinToString("\n") { it.title }
                )
            }
        }
        ReligiousAlarms.scheduleOccasion(context)
    }
}
