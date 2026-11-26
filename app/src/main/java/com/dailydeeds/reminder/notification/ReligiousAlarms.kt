package com.dailydeeds.reminder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dailydeeds.reminder.MainActivity
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.AdhanPlanner
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.adhan.PendingSnoozes
import com.dailydeeds.reminder.data.PrefsStorage
import com.dailydeeds.reminder.adhan.PlannedAlarm
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.calendar.BlessedEves
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.receiver.BlessedEveReceiver
import com.dailydeeds.reminder.receiver.OccasionAlarmReceiver
import com.dailydeeds.reminder.receiver.PrayerAlarmReceiver
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.util.PrayerSchedule
import com.dailydeeds.reminder.widget.WidgetUpdater
import com.dailydeeds.reminder.worship.DailyNotesConfig
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Alarms for prayer times and Ahl al-Bayt occasions. Each alarm fires once, the receiver handles it (with any
 * other due at the same instant) and schedules the next, so at most one prayer alarm (plus one snooze per
 * prayer) is ever pending.
 */
object ReligiousAlarms {
    const val EXTRA_PRAYER_ID = "extra_prayer_id"
    const val EXTRA_KIND = "extra_alarm_kind"
    const val EXTRA_SCHEDULED_AT = "extra_scheduled_at"
    const val EXTRA_SNOOZE = "extra_snooze"
    /** The instant (epoch millis) a blessed-eve reminder was set for. */
    const val EXTRA_EVE_AT = "extra_eve_at"
    private const val PRAYER_REQUEST_CODE = 3000
    private const val OCCASION_REQUEST_CODE = 3001
    private const val BLESSED_EVE_REQUEST_CODE = 3002
    /** Plus the prayer id, so that one prayer's snooze can be cancelled without touching another's. */
    private const val SNOOZE_REQUEST_CODE = 3010
    /** The app screen the system opens from its "next alarm" entry. */
    private const val SHOW_REQUEST_CODE = 3100
    const val OCCASION_HOUR = 8
    private const val RAMADAN = 9

    fun scheduleAll(context: Context) {
        schedulePrayer(context)
        scheduleOccasion(context)
        scheduleBlessedEve(context)
    }

    /**
     * Schedules the next prayer or pre-adhan alarm from the saved configuration, or cancels it if none is on.
     * [after] is the last alarm the receiver has just handled (together with those of [dueWith]), so that the
     * next one is planned strictly after it and never for a time that has already come.
     */
    fun schedulePrayer(context: Context, after: PlannedAlarm? = null) {
        val planned = withSavedConfig(context) { place, calc, configs, imsakOn ->
            AdhanPlanner.next(ZonedDateTime.now(), place, calc, configs, after, imsakOn)
        }
        // The widget is not direct-boot aware: before the first unlock only the alarm is set.
        if (PrefsStorage.isUnlocked(context)) WidgetUpdater.refresh(context)
        if (planned == null) {
            cancel(context, PRAYER_REQUEST_CODE, PrayerAlarmReceiver::class.java)
            return
        }
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
            .putExtra(EXTRA_PRAYER_ID, planned.prayer.id)
            .putExtra(EXTRA_KIND, planned.kind.name)
            .putExtra(EXTRA_SCHEDULED_AT, planned.triggerAt.toInstant().toEpochMilli())
        // An adhan is an alarm clock to the system: it shows in the status bar and is the least likely to be deferred.
        val adhan = planned.kind == AlarmKind.MAIN &&
            PreferencesManager(context).getAlarmConfig(planned.prayer).mode == AdhanMode.ADHAN
        set(context, PRAYER_REQUEST_CODE, planned.triggerAt.toInstant().toEpochMilli(), intent, alarmClock = adhan)
    }

    /** The other alarms due at the same instant as [fired], for the receiver to handle in the same run. */
    fun dueWith(context: Context, fired: PlannedAlarm): List<PlannedAlarm> =
        withSavedConfig(context) { place, calc, configs, imsakOn ->
            AdhanPlanner.dueWith(fired, place, calc, configs, imsakOn)
        }

    /** Reads the saved alarm configuration for [plan], so that [schedulePrayer] and [dueWith] see the same inputs. */
    private fun <T> withSavedConfig(
        context: Context,
        plan: (Place, PrayerContext, Map<Prayer, PrayerAlarmConfig>, (LocalDate) -> Boolean) -> T
    ): T {
        val prefs = PreferencesManager(context)
        val configs = Prayer.values().associateWith { prefs.getAlarmConfig(it) }
        val calc = PrayerContext(prefs.getPrayerSettings().params(), configs.mapValues { it.value.offsetMinutes })
        val offset = prefs.getHijriOffset()
        val imsakOn = prefs.isImsakAlarmEnabled()
        val imsakFor: (LocalDate) -> Boolean = { date -> imsakOn && ShiaCalendar.toHijri(date, offset).month == RAMADAN }
        return plan(prefs.getPlace(), calc, configs, imsakFor)
    }

    /** One-off "play the adhan again in [minutes]" alarm; it is saved so that a reboot does not lose it. */
    fun scheduleSnooze(context: Context, prayer: Prayer, minutes: Int) {
        val at = System.currentTimeMillis() + minutes * 60_000L
        val prefs = PreferencesManager(context)
        prefs.setPendingSnoozes(prefs.getPendingSnoozes() + (prayer.id to at))
        setSnooze(context, prayer, at)
    }

    private fun setSnooze(context: Context, prayer: Prayer, at: Long) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
            .putExtra(EXTRA_PRAYER_ID, prayer.id)
            .putExtra(EXTRA_KIND, AlarmKind.MAIN.name)
            .putExtra(EXTRA_SCHEDULED_AT, at)
            .putExtra(EXTRA_SNOOZE, true)
        set(context, SNOOZE_REQUEST_CODE + prayer.id, at, intent, alarmClock = true)
    }

    /** Drops a pending snooze of [prayer], e.g. once its alarm is switched off. */
    fun cancelSnooze(context: Context, prayer: Prayer) {
        snoozeHandled(context, prayer)
        cancel(context, SNOOZE_REQUEST_CODE + prayer.id, PrayerAlarmReceiver::class.java)
    }

    /** The snooze of [prayer] fired or was dropped: it no longer needs restoring after a reboot. */
    fun snoozeHandled(context: Context, prayer: Prayer) {
        val prefs = PreferencesManager(context)
        val pending = prefs.getPendingSnoozes()
        if (prayer.id in pending) prefs.setPendingSnoozes(pending - prayer.id)
    }

    /** After a reboot: sets again the snoozes still to come; those whose time passed while the phone was off are dropped. */
    fun restoreSnoozes(context: Context) {
        val prefs = PreferencesManager(context)
        val pending = prefs.getPendingSnoozes()
        val due = PendingSnoozes.stillDue(pending, System.currentTimeMillis())
        if (due != pending) prefs.setPendingSnoozes(due)
        due.forEach { (id, at) -> Prayer.fromId(id)?.let { setSnooze(context, it, at) } }
    }

    fun scheduleOccasion(context: Context) {
        val prefs = PreferencesManager(context)
        if (!notesConfig(prefs).any) {
            cancel(context, OCCASION_REQUEST_CODE, OccasionAlarmReceiver::class.java)
            return
        }
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        var at = now.toLocalDate().atTime(OCCASION_HOUR, 0).atZone(zone)
        if (!at.isAfter(now)) at = at.plusDays(1)
        set(context, OCCASION_REQUEST_CODE, at.toInstant().toEpochMilli(), Intent(context, OccasionAlarmReceiver::class.java))
    }

    /**
     * The evening reminder before the next blessed night, an hour before that day's Maghrib at the saved place.
     * [afterMillis] is the reminder that has just fired: the next one is planned strictly after it, so an alarm
     * delivered a little early does not set the same reminder again.
     */
    fun scheduleBlessedEve(context: Context, afterMillis: Long = 0L) {
        val prefs = PreferencesManager(context)
        if (!prefs.isBlessedEveReminderEnabled()) {
            cancel(context, BLESSED_EVE_REQUEST_CODE, BlessedEveReceiver::class.java)
            return
        }
        val place = prefs.getPlace()
        val configs = Prayer.values().associateWith { prefs.getAlarmConfig(it) }
        val calc = PrayerContext(prefs.getPrayerSettings().params(), configs.mapValues { it.value.offsetMinutes })
        val from = maxOf(System.currentTimeMillis(), afterMillis)
        val now = ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(from), place.zone)
        val at = BlessedEves.next(now, prefs.getHijriOffset()) { date ->
            PrayerSchedule.timesFor(date, place, calc).maghrib
        } ?: return
        val millis = at.toInstant().toEpochMilli()
        set(context, BLESSED_EVE_REQUEST_CODE, millis, Intent(context, BlessedEveReceiver::class.java).putExtra(EXTRA_EVE_AT, millis))
    }

    fun today(): LocalDate = LocalDate.now()

    /** Which of the morning notes (occasion, khums year, lunar eclipse) the user has switched on. */
    fun notesConfig(prefs: PreferencesManager) = DailyNotesConfig(
        occasions = prefs.isOccasionReminderEnabled(),
        khumsYear = if (prefs.isKhumsReminderEnabled()) prefs.getKhumsYear() else null,
        lunarEclipse = prefs.isEclipseReminderEnabled()
    )

    private fun set(context: Context, requestCode: Int, triggerAt: Long, intent: Intent, alarmClock: Boolean = false) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pending = PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        try {
            val exactAllowed = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
            if (exactAllowed && alarmClock) {
                val show = PendingIntent.getActivity(
                    context, SHOW_REQUEST_CODE, Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerAt, show), pending)
            } else if (exactAllowed) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
            }
        } catch (_: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        }
    }

    private fun cancel(context: Context, requestCode: Int, receiver: Class<*>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pending = PendingIntent.getBroadcast(
            context, requestCode, Intent(context, receiver),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) ?: return
        alarmManager.cancel(pending)
        pending.cancel()
    }
}
