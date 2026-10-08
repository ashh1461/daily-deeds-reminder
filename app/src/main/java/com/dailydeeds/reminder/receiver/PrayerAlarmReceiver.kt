package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.util.Prayer

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayer = Prayer.fromId(intent.getIntExtra(ReligiousAlarms.EXTRA_PRAYER_ID, -1))
        if (prayer != null && PreferencesManager(context).isPrayerAlarmEnabled(prayer)) {
            NotificationHelper.showNotification(
                context, 5000 + prayer.id, "حان وقت صلاة ${prayer.nameArabic}",
                "حان الآن موعد أذان ${prayer.nameArabic} بحسب الفقه الجعفري."
            )
        }
        ReligiousAlarms.schedulePrayer(context)
    }
}
