package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.service.AdhanService
import com.dailydeeds.reminder.util.Prayer

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayer = Prayer.fromId(intent.getIntExtra(ReligiousAlarms.EXTRA_PRAYER_ID, -1))
        if (prayer != null) {
            val kind = runCatching { AlarmKind.valueOf(intent.getStringExtra(ReligiousAlarms.EXTRA_KIND).orEmpty()) }
                .getOrDefault(AlarmKind.MAIN)
            val scheduledAt = intent.getLongExtra(ReligiousAlarms.EXTRA_SCHEDULED_AT, 0L)
            val snooze = intent.getBooleanExtra(ReligiousAlarms.EXTRA_SNOOZE, false)
            handle(context, prayer, kind, scheduledAt, snooze)
        }
        ReligiousAlarms.schedulePrayer(context)
    }

    private fun handle(context: Context, prayer: Prayer, kind: AlarmKind, scheduledAt: Long, snooze: Boolean) {
        // An alarm delivered long after its time (Doze, or inexact alarms) must not announce "now".
        val late = scheduledAt > 0 && System.currentTimeMillis() - scheduledAt > STALE_AFTER_MS
        if (kind == AlarmKind.IMSAK) {
            if (!late) NotificationHelper.showNotification(
                context, 5200, "حان وقت الإمساك", "بدأ وقت الإمساك. تقبّل الله صيامكم.",
                channelId = NotificationHelper.CHANNEL_PRAYER_REMINDER
            )
            return
        }
        val config = PreferencesManager(context).getAlarmConfig(prayer)
        if (config.mode == AdhanMode.OFF && !snooze) return
        val id = 5000 + prayer.id

        when {
            kind == AlarmKind.PRE -> NotificationHelper.showNotification(
                context, id + 100, "اقترب وقت صلاة ${prayer.nameArabic}",
                "بعد ${config.preMinutes} دقيقة يحين موعد أذان ${prayer.nameArabic}.",
                channelId = NotificationHelper.CHANNEL_PRAYER_REMINDER
            )
            config.mode == AdhanMode.SILENT -> NotificationHelper.showNotification(
                context, id, "حان وقت صلاة ${prayer.nameArabic}", "حان الآن موعد أذان ${prayer.nameArabic}.",
                channelId = NotificationHelper.CHANNEL_PRAYER_SILENT
            )
            (config.mode == AdhanMode.ADHAN || snooze) && !late -> {
                try {
                    AdhanService.play(context, prayer, config.voiceId)
                } catch (e: Exception) {
                    // Foreground-service start refused (background restrictions): fall back to a normal notification.
                    Log.w(TAG, "Could not start adhan service", e)
                    reminder(context, prayer, id)
                }
            }
            else -> reminder(context, prayer, id)
        }
    }

    private fun reminder(context: Context, prayer: Prayer, id: Int) = NotificationHelper.showNotification(
        context, id, "حان وقت صلاة ${prayer.nameArabic}", "حان الآن موعد أذان ${prayer.nameArabic} بحسب الفقه الجعفري.",
        channelId = NotificationHelper.CHANNEL_PRAYER_REMINDER
    )

    private companion object {
        const val TAG = "PrayerAlarmReceiver"
        const val STALE_AFTER_MS = 10 * 60 * 1000L
    }
}
