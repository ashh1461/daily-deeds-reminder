package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.worship.DailyNotes
import java.time.ZonedDateTime

/** The single morning alarm: occasion of the day, khums-year reminder and lunar-eclipse heads-up. */
class OccasionAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferencesManager(context)
        val config = ReligiousAlarms.notesConfig(prefs)
        if (config.any) {
            DailyNotes.build(ZonedDateTime.now(), prefs.getPlace(), prefs.getHijriOffset(), config).forEach { note ->
                NotificationHelper.showNotification(context, note.id, note.title, note.body)
            }
        }
        ReligiousAlarms.scheduleOccasion(context)
    }
}
