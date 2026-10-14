package com.dailydeeds.reminder.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent

/** Home-screen widget with the next prayer, the Hijri date and the occasion of the day. */
class PrayerWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        WidgetUpdater.refresh(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == WidgetUpdater.ACTION_REFRESH) WidgetUpdater.refresh(context) else super.onReceive(context, intent)
    }

    override fun onDisabled(context: Context) {
        WidgetUpdater.cancelRefresh(context)
    }
}
