package com.dailydeeds.reminder.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.dailydeeds.reminder.MainActivity
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import java.time.ZoneId
import java.time.ZonedDateTime

/** Draws the home-screen widget and keeps one inexact alarm pending so it moves on at each prayer. */
object WidgetUpdater {
    const val ACTION_REFRESH = "com.dailydeeds.reminder.widget.REFRESH"
    private const val REFRESH_REQUEST_CODE = 3100
    private const val OPEN_REQUEST_CODE = 3101

    private fun widgetIds(context: Context): IntArray =
        AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, PrayerWidgetProvider::class.java))

    /** Redraws every placed widget and schedules the next refresh; does nothing when there is no widget. */
    fun refresh(context: Context) {
        val app = context.applicationContext
        val ids = widgetIds(app)
        if (ids.isEmpty()) {
            cancelRefresh(app)
            return
        }
        val prefs = PreferencesManager(app)
        val configs = Prayer.values().associateWith { prefs.getAlarmConfig(it) }
        val calc = PrayerContext(prefs.getPrayerSettings().params(), configs.mapValues { it.value.offsetMinutes })
        val model = WidgetModel.build(ZonedDateTime.now(), prefs.getPlace(), calc, prefs.getHijriOffset())

        val views = RemoteViews(app.packageName, R.layout.widget_prayer)
        views.setTextViewText(R.id.widget_place, model.placeName)
        views.setTextViewText(R.id.widget_hijri, model.hijriDate)
        views.setTextViewText(R.id.widget_next_name, "الصلاة القادمة: ${model.nextPrayer}")
        views.setTextViewText(R.id.widget_next_time, model.nextTime)
        if (model.occasion != null) {
            views.setTextViewText(R.id.widget_occasion, model.occasion)
            views.setViewVisibility(R.id.widget_occasion, View.VISIBLE)
        } else {
            views.setViewVisibility(R.id.widget_occasion, View.GONE)
        }
        val open = PendingIntent.getActivity(
            app, OPEN_REQUEST_CODE, Intent(app, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_root, open)
        AppWidgetManager.getInstance(app).updateAppWidget(ids, views)

        scheduleRefresh(app, model.validUntil)
    }

    private fun scheduleRefresh(context: Context, validUntil: ZonedDateTime?) {
        val alarms = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val zone = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zone)
        val midnight = now.toLocalDate().plusDays(1).atStartOfDay(zone).plusSeconds(5)
        val at = listOfNotNull(validUntil?.plusSeconds(30)?.toInstant(), midnight.toInstant())
            .filter { it.isAfter(now.toInstant()) }.minOrNull() ?: midnight.toInstant()
        // An inexact alarm is enough for a widget; it needs no special permission.
        alarms.setAndAllowWhileIdle(AlarmManager.RTC, at.toEpochMilli(), refreshIntent(context, PendingIntent.FLAG_UPDATE_CURRENT) ?: return)
    }

    fun cancelRefresh(context: Context) {
        val alarms = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val pending = refreshIntent(context, PendingIntent.FLAG_NO_CREATE) ?: return
        alarms.cancel(pending)
        pending.cancel()
    }

    private fun refreshIntent(context: Context, flag: Int): PendingIntent? {
        val intent = Intent(context, PrayerWidgetProvider::class.java).setAction(ACTION_REFRESH)
        return PendingIntent.getBroadcast(context, REFRESH_REQUEST_CODE, intent, flag or PendingIntent.FLAG_IMMUTABLE)
    }
}
