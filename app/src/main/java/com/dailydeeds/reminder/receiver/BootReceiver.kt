package com.dailydeeds.reminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dailydeeds.reminder.data.DirectBoot
import com.dailydeeds.reminder.data.PrefsStorage
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.ReligiousAlarms

/**
 * Sets the alarms again after a restart, a clock or time-zone change, an app update, or a change of the exact-alarm
 * permission. Direct-boot aware: right after a restart, before the first unlock, it restores the prayer alarms
 * (their receiver and the adhan service can run while locked); the rest follows on the unlock broadcast.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action !in ACTIONS) return
        val restart = intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == QUICKBOOT_POWERON
        when (PrefsStorage.bootWork(context)) {
            DirectBoot.BootWork.NONE -> return
            DirectBoot.BootWork.PRAYER_ONLY -> ReligiousAlarms.schedulePrayer(context)
            DirectBoot.BootWork.ALL -> AlarmScheduler.scheduleAllReminders(context)
        }
        // Alarms do not survive a restart; a snooze still to come is set again (setting it twice replaces the same
        // alarm, so the locked and the unlocked boot broadcasts can both do it).
        if (restart) ReligiousAlarms.restoreSnoozes(context)
    }

    private companion object {
        const val QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON"
        val ACTIONS = setOf(
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_BOOT_COMPLETED,
            QUICKBOOT_POWERON,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
        )
    }
}
