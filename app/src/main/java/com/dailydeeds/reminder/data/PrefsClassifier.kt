package com.dailydeeds.reminder.data

/**
 * Which settings keys the boot / alarm path reads *before the first unlock* after a restart, and which are
 * ordinary user content that is only ever touched once the phone is unlocked.
 *
 * Direct-boot (device-protected) storage is encrypted with the device key, not the screen lock, so it can be
 * read before the first unlock. That is exactly what the prayer-alarm path needs ([ReligiousAlarms.schedulePrayer],
 * [PrayerAlarmReceiver], [AdhanService], [AdhanActionReceiver] all run while the phone is locked). Everything else —
 * favorites, reading positions, deed history, the Khums financial draft, tasbih and make-up counters, theme and
 * reminder times — is not needed before unlock and is kept in credential-protected storage, which only decrypts
 * after the user unlocks. See [PrefsStorage] for the two stores and the one-time split migration.
 *
 * The location (`pref_place_*`) is a **boot** key on purpose: the pre-unlock path recomputes prayer times with
 * [com.dailydeeds.reminder.adhan.AdhanPlanner], which needs latitude/longitude/time-zone. It is documented in
 * `docs/PRIVACY.md`.
 *
 * Pure logic, unit-tested, so the store split can be reasoned about without Android.
 */
object PrefsClassifier {

    /** Fixed keys the locked boot/alarm path reads before the first unlock. */
    private val BOOT_KEYS = setOf(
        "pref_prayer_settings",   // calculation method + per-prayer offsets (PrayerContext)
        "pref_hijri_offset",      // Ramadan check for the Imsak alarm
        "pref_adhan_global",      // which voice/ringer behaviour plays the adhan while locked
        "worship_imsak_enabled",  // whether the Imsak alarm is planned
        "alarm_last_handled",     // guards against replaying an alarm after the clock moves back
        "alarm_pending_snoozes",  // snoozes restored after a reboot
        "pref_place_name",        // location for prayer-time calculation (see class note / PRIVACY.md)
        "pref_place_lat",
        "pref_place_lng",
        "pref_place_zone",
        "pref_app_language"       // UI language: the locked adhan notification is written in it (v1.9.0)
    )

    /** Per-prayer alarm configuration, current (`pref_alarm_cfg_1..4`) and legacy (`pref_prayer_alarm_1..4`). */
    private val BOOT_PREFIXES = listOf("pref_alarm_cfg_", "pref_prayer_alarm_")

    /**
     * True when [key] must live in device-protected storage so the boot/alarm path can read it before the first
     * unlock. False for ordinary user content, which belongs in credential-protected storage.
     */
    fun isBootKey(key: String): Boolean =
        key in BOOT_KEYS || BOOT_PREFIXES.any { key.startsWith(it) }
}
