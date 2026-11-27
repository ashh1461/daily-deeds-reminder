package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.calendar.BlessedEves
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.notification.ReligiousAlarms
import java.time.Instant
import com.dailydeeds.reminder.util.LocaleSupport
import com.dailydeeds.reminder.worship.NoteWords

/** The evening reminder before a blessed night (ليلة الجمعة, mid-Sha'ban, the Laylat al-Qadr nights). */
class BlessedEveReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferencesManager(context)
        val now = System.currentTimeMillis()
        val scheduledAt = intent.getLongExtra(ReligiousAlarms.EXTRA_EVE_AT, now)
        // Delivered hours late (the phone was off or in deep sleep): the night has begun or passed, so say nothing.
        if (prefs.isBlessedEveReminderEnabled() && now - scheduledAt <= MAX_LATE_MILLIS) {
            // The eve the alarm was set for, not the date when it arrived.
            val eve = Instant.ofEpochMilli(scheduledAt).atZone(prefs.getPlace().zone).toLocalDate()
            BlessedEves.note(eve, prefs.getHijriOffset(), NoteWords.of(LocaleSupport.current(context)))?.let { note ->
                NotificationHelper.showNotification(context, note.id, note.title, note.body)
            }
        }
        ReligiousAlarms.scheduleBlessedEve(context, afterMillis = scheduledAt)
    }

    private companion object {
        const val MAX_LATE_MILLIS = 3 * 60 * 60 * 1000L
    }
}
