package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.NotificationHelper

class DailyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getIntExtra(AlarmScheduler.EXTRA_REMINDER_TYPE, AlarmScheduler.TYPE_MORNING)

        val (notificationId, title, message) = if (type == AlarmScheduler.TYPE_MORNING) {
            Triple(
                NotificationHelper.NOTIFICATION_ID_MORNING,
                "أذكار الصباح المباركة ☀️",
                "حان وقت الأوراد الصباحية: ١٩ مرة البسملة، دعاء الدرع الحصينة، الفاتحة، وآية الكرسي."
            )
        } else {
            Triple(
                NotificationHelper.NOTIFICATION_ID_EVENING,
                "أذكار المساء المباركة 🌙",
                "حان وقت الأوراد المسائية: ١٩ مرة البسملة، دعاء الدرع الحصينة، الاستغفار، والصلوات المحمدية."
            )
        }

        NotificationHelper.showNotification(context, notificationId, title, message)

        AlarmScheduler.scheduleAllReminders(context)
    }
}
