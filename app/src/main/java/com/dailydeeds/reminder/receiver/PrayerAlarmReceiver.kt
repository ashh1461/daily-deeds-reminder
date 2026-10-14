package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.adhan.AlarmText
import com.dailydeeds.reminder.adhan.PlannedAlarm
import com.dailydeeds.reminder.adhan.PrayerAlarmText
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.service.AdhanService
import com.dailydeeds.reminder.util.Prayer
import java.time.Instant
import java.time.ZoneId

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayer = Prayer.fromId(intent.getIntExtra(ReligiousAlarms.EXTRA_PRAYER_ID, -1))
        var last: PlannedAlarm? = null
        if (prayer != null) {
            val kind = runCatching { AlarmKind.valueOf(intent.getStringExtra(ReligiousAlarms.EXTRA_KIND).orEmpty()) }
                .getOrDefault(AlarmKind.MAIN)
            val scheduledAt = intent.getLongExtra(ReligiousAlarms.EXTRA_SCHEDULED_AT, 0L)
            val snooze = intent.getBooleanExtra(ReligiousAlarms.EXTRA_SNOOZE, false)
            handle(context, prayer, kind, scheduledAt, snooze)
            // An alarm due at the same instant (a 10-minute Fajr pre-alarm and Imsak) is handled now, not set for
            // a time that has already come; the next one is then planned strictly after the last handled.
            // A snooze is not part of the plan.
            if (!snooze && scheduledAt > 0) {
                val at = Instant.ofEpochMilli(scheduledAt).atZone(ZoneId.systemDefault())
                val fired = PlannedAlarm(prayer, kind, at, at)
                last = fired
                for (tied in ReligiousAlarms.dueWith(context, fired)) {
                    handle(context, tied.prayer, tied.kind, tied.triggerAt.toInstant().toEpochMilli(), snooze = false)
                    last = tied
                }
            }
        }
        ReligiousAlarms.schedulePrayer(context, last)
    }

    private fun handle(context: Context, prayer: Prayer, kind: AlarmKind, scheduledAt: Long, snooze: Boolean) {
        val now = System.currentTimeMillis()
        // An alarm delivered long after its time (Doze, or inexact alarms) must not announce "now".
        val late = PrayerAlarmText.isLate(scheduledAt, now)
        if (kind == AlarmKind.IMSAK) {
            if (!late) NotificationHelper.showNotification(
                context, 5200, "حان وقت الإمساك", "بدأ وقت الإمساك. تقبّل الله صيامكم.",
                channelId = NotificationHelper.CHANNEL_PRAYER_REMINDER
            )
            return
        }
        val config = PreferencesManager(context).getAlarmConfig(prayer)
        // Also drops a snooze still pending for a prayer that has since been switched off.
        if (config.mode == AdhanMode.OFF) return
        val id = PrayerAlarmText.notificationId(prayer)

        when {
            kind == AlarmKind.PRE -> show(
                context, id + 100, PrayerAlarmText.pre(prayer, config.preMinutes, scheduledAt, now),
                NotificationHelper.CHANNEL_PRAYER_REMINDER
            )
            config.mode == AdhanMode.SILENT -> show(
                context, id, PrayerAlarmText.silent(prayer, scheduledAt, now, snooze), NotificationHelper.CHANNEL_PRAYER_SILENT
            )
            (config.mode == AdhanMode.ADHAN || snooze) && !late -> {
                try {
                    AdhanService.play(context, prayer, config.voiceId)
                } catch (e: Exception) {
                    // Foreground-service start refused (background restrictions): fall back to a normal notification.
                    Log.w(TAG, "Could not start adhan service", e)
                    show(context, id, PrayerAlarmText.reminder(prayer), NotificationHelper.CHANNEL_PRAYER_REMINDER)
                }
            }
            else -> show(
                context, id, PrayerAlarmText.reminder(prayer, scheduledAt, now, snooze), NotificationHelper.CHANNEL_PRAYER_REMINDER
            )
        }
    }

    private fun show(context: Context, id: Int, text: AlarmText, channelId: String) =
        NotificationHelper.showNotification(context, id, text.title, text.text, channelId = channelId)

    private companion object {
        const val TAG = "PrayerAlarmReceiver"
    }
}
