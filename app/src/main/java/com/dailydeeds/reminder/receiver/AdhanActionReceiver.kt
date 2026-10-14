package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.service.AdhanService
import com.dailydeeds.reminder.util.Prayer

/** Handles the Stop and Snooze buttons of the adhan notification. Not exported. */
class AdhanActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AdhanService.stop(context)
        if (intent.action == ACTION_SNOOZE) {
            val prayer = Prayer.fromId(intent.getIntExtra(AdhanService.EXTRA_PRAYER_ID, -1)) ?: return
            val minutes = PreferencesManager(context).getAdhanGlobal().snoozeMinutes
            ReligiousAlarms.scheduleSnooze(context, prayer, minutes)
        }
    }

    companion object {
        const val ACTION_STOP = "com.dailydeeds.reminder.ADHAN_STOP"
        const val ACTION_SNOOZE = "com.dailydeeds.reminder.ADHAN_SNOOZE"
    }
}
