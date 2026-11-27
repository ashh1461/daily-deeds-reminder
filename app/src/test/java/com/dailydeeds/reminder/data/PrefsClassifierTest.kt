package com.dailydeeds.reminder.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The boot/alarm path reads these keys before the first unlock, so they stay in device-protected storage; all
 * other user content moves to credential-protected storage ([PrefsStorage], finding F-1 of the APK audit).
 */
class PrefsClassifierTest {

    @Test
    fun theLockedBootPathKeysAreDeviceProtected() {
        val boot = listOf(
            "pref_prayer_settings",
            "pref_hijri_offset",
            "pref_adhan_global",
            "worship_imsak_enabled",
            "alarm_last_handled",
            "alarm_pending_snoozes",
            "pref_place_name", "pref_place_lat", "pref_place_lng", "pref_place_zone",
            "pref_alarm_cfg_1", "pref_alarm_cfg_2", "pref_alarm_cfg_3", "pref_alarm_cfg_4",
            "pref_prayer_alarm_1", "pref_prayer_alarm_4", // legacy per-prayer on/off, read as a fallback
            "pref_app_language" // the adhan notification is written in the chosen language while locked
        )
        boot.forEach { assertTrue(it, PrefsClassifier.isBootKey(it)) }
    }

    @Test
    fun userContentIsNotDeviceProtected() {
        val user = listOf(
            // The two keys the audit called out specifically.
            "worship_khums_draft", "worship_khums_year",
            // Other user content.
            "pref_favorites", "pref_last_surah", "pref_last_page", "pref_last_ayah",
            "pref_last_mafatih", "pref_last_sahifa", "pref_theme_mode", "pref_haptics_enabled",
            "pref_quran_pages_mode", "pref_mushaf_fit", "pref_font_quran", "pref_font_mushaf",
            "pref_morning_reminder_enabled", "pref_evening_hour", "pref_prayer_reminder_enabled",
            "pref_occasion_reminder", "pref_blessed_eve_enabled", "pref_asked_notifications",
            "pref_last_active_date", "pref_completed_days_streak",
            "worship_tasbih", "worship_qada", "worship_khums_enabled", "worship_eclipse_enabled",
            "deed_1_completed_2026-11-27", "deed_12_count_2026-11-27", "deed_3_stage_2026-11-27"
        )
        user.forEach { assertFalse(it, PrefsClassifier.isBootKey(it)) }
    }

    @Test
    fun theTwoPrefixesDoNotLeakIntoLookalikeKeys() {
        // Only pref_alarm_cfg_* / pref_prayer_alarm_* are boot keys; a different alarm key is not.
        assertTrue(PrefsClassifier.isBootKey("pref_alarm_cfg_1"))
        assertFalse(PrefsClassifier.isBootKey("pref_alarms_other"))
        assertFalse(PrefsClassifier.isBootKey("pref_last_page"))
    }
}
