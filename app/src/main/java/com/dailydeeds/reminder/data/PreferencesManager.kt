package com.dailydeeds.reminder.data

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("daily_deeds_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_LAST_ACTIVE_DATE = "pref_last_active_date"
        const val KEY_MORNING_ENABLED = "pref_morning_reminder_enabled"
        const val KEY_MORNING_HOUR = "pref_morning_hour"
        const val KEY_MORNING_MINUTE = "pref_morning_minute"
        const val KEY_EVENING_ENABLED = "pref_evening_reminder_enabled"
        const val KEY_EVENING_HOUR = "pref_evening_hour"
        const val KEY_EVENING_MINUTE = "pref_evening_minute"
        const val KEY_PRAYER_ENABLED = "pref_prayer_reminder_enabled"
        const val KEY_HAPTICS_ENABLED = "pref_haptics_enabled"
        const val KEY_SOUND_ENABLED = "pref_sound_enabled"
        const val KEY_COMPLETED_DAYS_STREAK = "pref_completed_days_streak"

        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(Date())
        }
    }

    fun isDeedCompleted(deedId: Int, date: String = getTodayDateString()): Boolean {
        return prefs.getBoolean("deed_${deedId}_completed_$date", false)
    }

    fun setDeedCompleted(deedId: Int, completed: Boolean, date: String = getTodayDateString()) {
        prefs.edit().putBoolean("deed_${deedId}_completed_$date", completed).apply()
    }

    fun getDeedCount(deedId: Int, date: String = getTodayDateString()): Int {
        return prefs.getInt("deed_${deedId}_count_$date", 0)
    }

    fun setDeedCount(deedId: Int, count: Int, date: String = getTodayDateString()) {
        prefs.edit().putInt("deed_${deedId}_count_$date", count).apply()
    }

    fun getDeedStage(deedId: Int, date: String = getTodayDateString()): Int {
        return prefs.getInt("deed_${deedId}_stage_$date", 0)
    }

    fun setDeedStage(deedId: Int, stage: Int, date: String = getTodayDateString()) {
        prefs.edit().putInt("deed_${deedId}_stage_$date", stage).apply()
    }

    fun getLastActiveDate(): String {
        return prefs.getString(KEY_LAST_ACTIVE_DATE, "") ?: ""
    }

    fun setLastActiveDate(date: String) {
        prefs.edit().putString(KEY_LAST_ACTIVE_DATE, date).apply()
    }

    fun checkAndResetDaily(today: String = getTodayDateString()): Boolean {
        val lastDate = getLastActiveDate()
        if (lastDate.isEmpty()) {
            setLastActiveDate(today)
            return false
        }
        if (lastDate != today) {
            setLastActiveDate(today)
            return true
        }
        return false
    }

    fun getDailyProgress(date: String = getTodayDateString()): Pair<Int, Int> {
        val allDeeds = DeedsRepository.getAllDeeds()
        val total = allDeeds.size
        var completedCount = 0
        for (deed in allDeeds) {
            if (isDeedCompleted(deed.id, date)) {
                completedCount++
            }
        }
        return Pair(completedCount, total)
    }

    fun resetToday(today: String = getTodayDateString()) {
        val editor = prefs.edit()
        val allDeeds = DeedsRepository.getAllDeeds()
        for (deed in allDeeds) {
            editor.remove("deed_${deed.id}_completed_$today")
            editor.remove("deed_${deed.id}_count_$today")
            editor.remove("deed_${deed.id}_stage_$today")
        }
        editor.apply()
    }

    fun isMorningReminderEnabled(): Boolean = prefs.getBoolean(KEY_MORNING_ENABLED, true)
    fun setMorningReminderEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(KEY_MORNING_ENABLED, enabled).apply()

    fun getMorningReminderTime(): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_MORNING_HOUR, 7)
        val minute = prefs.getInt(KEY_MORNING_MINUTE, 0)
        return Pair(hour, minute)
    }

    fun setMorningReminderTime(hour: Int, minute: Int) {
        prefs.edit().putInt(KEY_MORNING_HOUR, hour).putInt(KEY_MORNING_MINUTE, minute).apply()
    }

    fun isEveningReminderEnabled(): Boolean = prefs.getBoolean(KEY_EVENING_ENABLED, true)
    fun setEveningReminderEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(KEY_EVENING_ENABLED, enabled).apply()

    fun getEveningReminderTime(): Pair<Int, Int> {
        val hour = prefs.getInt(KEY_EVENING_HOUR, 20)
        val minute = prefs.getInt(KEY_EVENING_MINUTE, 0)
        return Pair(hour, minute)
    }

    fun setEveningReminderTime(hour: Int, minute: Int) {
        prefs.edit().putInt(KEY_EVENING_HOUR, hour).putInt(KEY_EVENING_MINUTE, minute).apply()
    }

    fun isPrayerReminderEnabled(): Boolean = prefs.getBoolean(KEY_PRAYER_ENABLED, true)
    fun setPrayerReminderEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(KEY_PRAYER_ENABLED, enabled).apply()

    fun isHapticsEnabled(): Boolean = prefs.getBoolean(KEY_HAPTICS_ENABLED, true)
    fun setHapticsEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply()

    fun isSoundEnabled(): Boolean = prefs.getBoolean(KEY_SOUND_ENABLED, true)
    fun setSoundEnabled(enabled: Boolean) =
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
}
