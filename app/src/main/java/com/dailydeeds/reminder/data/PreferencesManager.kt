package com.dailydeeds.reminder.data

import android.content.Context
import android.content.SharedPreferences
import com.dailydeeds.reminder.adhan.AdhanGlobalSettings
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.PendingSnoozes
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.adhan.PrayerSettings
import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.model.ReminderSettings
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.worship.KhumsDraft
import com.dailydeeds.reminder.worship.KhumsYear
import com.dailydeeds.reminder.worship.QadaState
import com.dailydeeds.reminder.worship.TasbihState
import java.time.LocalDate
import java.time.ZoneId

class PreferencesManager(context: Context) {

    private val appContext: Context = context.applicationContext ?: context

    /**
     * Two stores (finding F-1, see [PrefsStorage]). [boot] is device-protected and holds only the keys the
     * boot/alarm path reads before the first unlock ([PrefsClassifier.isBootKey]); it is always safe to open.
     * [user] is credential-protected and holds everything else; it is opened lazily so the locked boot path (which
     * reads only boot keys) never touches credential storage before the first unlock. Each getter/setter uses the
     * store its key belongs in.
     */
    private val boot: SharedPreferences = PrefsStorage.openDevice(appContext)
    private val user: SharedPreferences by lazy { PrefsStorage.openUser(appContext) }

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
        const val KEY_COMPLETED_DAYS_STREAK = "pref_completed_days_streak"
        private const val KEY_FAVORITES = "pref_favorites"
        private const val KEY_LAST_SURAH = "pref_last_surah"
        private const val KEY_LAST_PAGE = "pref_last_page"
        private const val KEY_LAST_AYAH = "pref_last_ayah"
        private const val KEY_QURAN_PAGES_MODE = "pref_quran_pages_mode"
        private const val KEY_LAST_MAFATIH = "pref_last_mafatih"
        private const val KEY_LAST_SAHIFA = "pref_last_sahifa"
        private const val KEY_THEME_MODE = "pref_theme_mode"
        private const val KEY_ADHAN_GLOBAL = "pref_adhan_global"
        private const val KEY_PRAYER_SETTINGS = "pref_prayer_settings"
        private const val KEY_ASKED_NOTIFICATIONS = "pref_asked_notifications"
        private const val KEY_PLACE_NAME = "pref_place_name"
        private const val KEY_PLACE_LAT = "pref_place_lat"
        private const val KEY_PLACE_LNG = "pref_place_lng"
        private const val KEY_PLACE_ZONE = "pref_place_zone"
        private const val KEY_HIJRI_OFFSET = "pref_hijri_offset"
        private const val KEY_OCCASION_REMINDER = "pref_occasion_reminder"
        private const val KEY_BLESSED_EVE = "pref_blessed_eve_enabled"
        private const val KEY_TASBIH = "worship_tasbih"
        private const val KEY_QADA = "worship_qada"
        private const val KEY_KHUMS_YEAR = "worship_khums_year"
        private const val KEY_KHUMS_DRAFT = "worship_khums_draft"
        private const val KEY_IMSAK_ALARM = "worship_imsak_enabled"
        private const val KEY_KHUMS_REMINDER = "worship_khums_enabled"
        private const val KEY_ECLIPSE_REMINDER = "worship_eclipse_enabled"
        private const val KEY_LAST_HANDLED_ALARM = "alarm_last_handled"
        private const val KEY_PENDING_SNOOZES = "alarm_pending_snoozes"

        /** Days of per-day deed state kept before old keys are pruned. */
        const val RETENTION_DAYS = 90L
        private val DEED_DAY_KEY = Regex("""^deed_\d+_(?:completed|count|stage)_(\d{4}-\d{2}-\d{2})$""")

        fun getTodayDateString(): String = LocalDate.now().toString()
    }

    fun isDeedCompleted(deedId: Int, date: String = getTodayDateString()): Boolean {
        return user.getBoolean("deed_${deedId}_completed_$date", false)
    }

    fun setDeedCompleted(deedId: Int, completed: Boolean, date: String = getTodayDateString()) {
        user.edit().putBoolean("deed_${deedId}_completed_$date", completed).apply()
    }

    fun getDeedCount(deedId: Int, date: String = getTodayDateString()): Int {
        return user.getInt("deed_${deedId}_count_$date", 0)
    }

    fun setDeedCount(deedId: Int, count: Int, date: String = getTodayDateString()) {
        user.edit().putInt("deed_${deedId}_count_$date", count).apply()
    }

    fun getDeedStage(deedId: Int, date: String = getTodayDateString()): Int {
        return user.getInt("deed_${deedId}_stage_$date", 0)
    }

    fun setDeedStage(deedId: Int, stage: Int, date: String = getTodayDateString()) {
        user.edit().putInt("deed_${deedId}_stage_$date", stage).apply()
    }

    fun getLastActiveDate(): String {
        return user.getString(KEY_LAST_ACTIVE_DATE, "") ?: ""
    }

    fun setLastActiveDate(date: String) {
        user.edit().putString(KEY_LAST_ACTIVE_DATE, date).apply()
    }

    /** Removes per-day deed keys older than [RETENTION_DAYS] so the preferences file stays bounded. */
    fun pruneOldDeedState(today: String = getTodayDateString()) {
        val cutoff = LocalDate.parse(today).minusDays(RETENTION_DAYS)
        val stale = user.all.keys.filter { key ->
            val date = DEED_DAY_KEY.matchEntire(key)?.groupValues?.get(1) ?: return@filter false
            runCatching { LocalDate.parse(date).isBefore(cutoff) }.getOrDefault(false)
        }
        if (stale.isEmpty()) return
        val editor = user.edit()
        stale.forEach(editor::remove)
        editor.apply()
    }

    fun checkAndResetDaily(today: String = getTodayDateString()): Boolean {
        val lastDate = getLastActiveDate()
        if (lastDate.isEmpty()) {
            setLastActiveDate(today)
            return false
        }
        if (lastDate != today) {
            setLastActiveDate(today)
            pruneOldDeedState(today)
            return true
        }
        return false
    }

    fun getDailyProgress(date: String = getTodayDateString()): Pair<Int, Int> {
        val allDeeds = DeedsRepository.getDailyDeeds(LocalDate.parse(date))
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
        val editor = user.edit()
        val allDeeds = DeedsRepository.getAllDeeds()
        for (deed in allDeeds) {
            editor.remove("deed_${deed.id}_completed_$today")
            editor.remove("deed_${deed.id}_count_$today")
            editor.remove("deed_${deed.id}_stage_$today")
        }
        editor.apply()
    }

    fun getReminder(type: ReminderType): ReminderSettings = ReminderSettings(
        enabled = user.getBoolean("pref_${type.preferenceKey}_reminder_enabled", true),
        hour = user.getInt("pref_${type.preferenceKey}_hour", type.defaultHour).coerceIn(0, 23),
        minute = user.getInt("pref_${type.preferenceKey}_minute", 0).coerceIn(0, 59)
    )

    fun setReminder(type: ReminderType, settings: ReminderSettings) {
        require(settings.hour in 0..23 && settings.minute in 0..59)
        user.edit()
            .putBoolean("pref_${type.preferenceKey}_reminder_enabled", settings.enabled)
            .putInt("pref_${type.preferenceKey}_hour", settings.hour)
            .putInt("pref_${type.preferenceKey}_minute", settings.minute)
            .apply()
    }

    fun isMorningReminderEnabled(): Boolean = user.getBoolean(KEY_MORNING_ENABLED, true)
    fun setMorningReminderEnabled(enabled: Boolean) =
        user.edit().putBoolean(KEY_MORNING_ENABLED, enabled).apply()

    fun getMorningReminderTime(): Pair<Int, Int> {
        val hour = user.getInt(KEY_MORNING_HOUR, 7)
        val minute = user.getInt(KEY_MORNING_MINUTE, 0)
        return Pair(hour, minute)
    }

    fun setMorningReminderTime(hour: Int, minute: Int) {
        user.edit().putInt(KEY_MORNING_HOUR, hour).putInt(KEY_MORNING_MINUTE, minute).apply()
    }

    fun isEveningReminderEnabled(): Boolean = user.getBoolean(KEY_EVENING_ENABLED, true)
    fun setEveningReminderEnabled(enabled: Boolean) =
        user.edit().putBoolean(KEY_EVENING_ENABLED, enabled).apply()

    fun getEveningReminderTime(): Pair<Int, Int> {
        val hour = user.getInt(KEY_EVENING_HOUR, 20)
        val minute = user.getInt(KEY_EVENING_MINUTE, 0)
        return Pair(hour, minute)
    }

    fun setEveningReminderTime(hour: Int, minute: Int) {
        user.edit().putInt(KEY_EVENING_HOUR, hour).putInt(KEY_EVENING_MINUTE, minute).apply()
    }

    fun isPrayerReminderEnabled(): Boolean = user.getBoolean(KEY_PRAYER_ENABLED, true)
    fun setPrayerReminderEnabled(enabled: Boolean) =
        user.edit().putBoolean(KEY_PRAYER_ENABLED, enabled).apply()

    fun isHapticsEnabled(): Boolean = user.getBoolean(KEY_HAPTICS_ENABLED, true)
    fun setHapticsEnabled(enabled: Boolean) =
        user.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply()


    // --- Favorites and reading position ---------------------------------------------------

    fun getFavorites(): List<FavoriteKey> = FavoritesList.parse(user.getString(KEY_FAVORITES, null))

    fun toggleFavorite(key: FavoriteKey): List<FavoriteKey> {
        val updated = FavoritesList.toggle(getFavorites(), key)
        user.edit().putString(KEY_FAVORITES, FavoritesList.serialize(updated)).apply()
        return updated
    }

    fun removeFavorites(keys: Set<FavoriteKey>): List<FavoriteKey> {
        val updated = FavoritesList.removeAll(getFavorites(), keys)
        setFavorites(updated)
        return updated
    }

    /** Replaces the whole list; used to undo a delete. */
    fun setFavorites(items: List<FavoriteKey>) =
        user.edit().putString(KEY_FAVORITES, FavoritesList.serialize(items)).apply()

    fun getLastSurah(): Int = user.getInt(KEY_LAST_SURAH, 0)
    fun setLastSurah(surah: Int) = user.edit().putInt(KEY_LAST_SURAH, surah).apply()

    /** The ayah at the top of the list reader when it was last scrolled, in the last surah; 0 when none. */
    fun getLastAyah(): Int = user.getInt(KEY_LAST_AYAH, 0)
    fun setLastAyah(ayah: Int) = user.edit().putInt(KEY_LAST_AYAH, ayah).apply()

    /** Last mushaf page read (1..604), 0 when none. */
    fun getLastQuranPage(): Int = user.getInt(KEY_LAST_PAGE, 0)
    fun setLastQuranPage(page: Int) = user.edit().putInt(KEY_LAST_PAGE, page).apply()

    /** The Quran tab opens the mushaf page by page (default) or as a scrolling list of ayahs. */
    fun getQuranPagesMode(): Boolean = user.getBoolean(KEY_QURAN_PAGES_MODE, true)
    fun setQuranPagesMode(pages: Boolean) = user.edit().putBoolean(KEY_QURAN_PAGES_MODE, pages).apply()

    /** A reader's saved text size ([ReaderFonts]); the reader's default until it is first changed. */
    fun getReaderFontSp(key: String, range: ReaderFonts.Range): Float =
        if (user.contains(key)) ReaderFonts.fromStored(user.getInt(key, 0), range) else range.defaultSp

    fun setReaderFontSp(key: String, sp: Float) = user.edit().putInt(key, ReaderFonts.toStored(sp)).apply()

    /** The latest planned prayer alarm handled (epoch ms), so a clock moved back does not play it again. Not backed up. */
    fun getLastHandledAlarm(): Long = boot.getLong(KEY_LAST_HANDLED_ALARM, 0L)
    fun setLastHandledAlarm(at: Long) = boot.edit().putLong(KEY_LAST_HANDLED_ALARM, at).apply()

    /** Snoozes waiting to play ([PendingSnoozes]), restored after a reboot. Not backed up. */
    fun getPendingSnoozes(): Map<Int, Long> = PendingSnoozes.decode(boot.getString(KEY_PENDING_SNOOZES, null))
    fun setPendingSnoozes(snoozes: Map<Int, Long>) =
        boot.edit().putString(KEY_PENDING_SNOOZES, PendingSnoozes.encode(snoozes)).apply()

    /** Whether each mushaf page picks the largest text size that fits the screen (the default). */
    fun getMushafFit(): Boolean = user.getBoolean(ReaderFonts.KEY_MUSHAF_FIT, true)
    fun setMushafFit(fit: Boolean) = user.edit().putBoolean(ReaderFonts.KEY_MUSHAF_FIT, fit).apply()

    fun getLastMafatihId(): String? = user.getString(KEY_LAST_MAFATIH, null)
    fun setLastMafatihId(id: String) = user.edit().putString(KEY_LAST_MAFATIH, id).apply()

    // --- Place, calendar offset and religious reminders ------------------------------------

    fun getPlace(): Place {
        val name = boot.getString(KEY_PLACE_NAME, null) ?: return PlacePresets.default
        return runCatching {
            val lat = Double.fromBits(boot.getLong(KEY_PLACE_LAT, 0L))
            val lng = Double.fromBits(boot.getLong(KEY_PLACE_LNG, 0L))
            val zone = boot.getString(KEY_PLACE_ZONE, "UTC") ?: "UTC"
            require(lat.isFinite() && lng.isFinite() && lat in -90.0..90.0 && lng in -180.0..180.0)
            ZoneId.of(zone)
            Place(name, lat, lng, zone)
        }.getOrDefault(PlacePresets.default)
    }

    fun setPlace(place: Place) {
        boot.edit()
            .putString(KEY_PLACE_NAME, place.name)
            .putLong(KEY_PLACE_LAT, place.latitude.toRawBits())
            .putLong(KEY_PLACE_LNG, place.longitude.toRawBits())
            .putString(KEY_PLACE_ZONE, place.zoneId)
            .apply()
    }

    fun getHijriOffset(): Int = boot.getInt(KEY_HIJRI_OFFSET, 0).coerceIn(-ShiaCalendar.MAX_OFFSET, ShiaCalendar.MAX_OFFSET)
    fun setHijriOffset(days: Int) =
        boot.edit().putInt(KEY_HIJRI_OFFSET, days.coerceIn(-ShiaCalendar.MAX_OFFSET, ShiaCalendar.MAX_OFFSET)).apply()

    /**
     * Alarm configuration of one prayer. Installs from before the adhan feature stored a single on/off
     * switch; "on" becomes the notification mode so nobody is silently switched to audio.
     */
    fun getAlarmConfig(prayer: Prayer): PrayerAlarmConfig {
        PrayerAlarmConfig.decode(boot.getString("pref_alarm_cfg_${prayer.id}", null))?.let { return it }
        val legacyOn = boot.getBoolean("pref_prayer_alarm_${prayer.id}", false)
        return PrayerAlarmConfig(mode = if (legacyOn) AdhanMode.NOTIFICATION else AdhanMode.OFF)
    }

    fun setAlarmConfig(prayer: Prayer, config: PrayerAlarmConfig) =
        boot.edit().putString("pref_alarm_cfg_${prayer.id}", config.encode()).apply()

    fun getAdhanGlobal(): AdhanGlobalSettings = AdhanGlobalSettings.decode(boot.getString(KEY_ADHAN_GLOBAL, null)) ?: AdhanGlobalSettings()
    fun setAdhanGlobal(settings: AdhanGlobalSettings) = boot.edit().putString(KEY_ADHAN_GLOBAL, settings.encode()).apply()

    fun getPrayerSettings(): PrayerSettings = PrayerSettings.decode(boot.getString(KEY_PRAYER_SETTINGS, null)) ?: PrayerSettings()
    fun setPrayerSettings(settings: PrayerSettings) = boot.edit().putString(KEY_PRAYER_SETTINGS, settings.encode()).apply()

    fun isOccasionReminderEnabled(): Boolean = user.getBoolean(KEY_OCCASION_REMINDER, false)
    fun setOccasionReminderEnabled(enabled: Boolean) =
        user.edit().putBoolean(KEY_OCCASION_REMINDER, enabled).apply()

    /** The evening reminder before blessed nights and ليلة الجمعة (opt-in). */
    fun isBlessedEveReminderEnabled(): Boolean = user.getBoolean(KEY_BLESSED_EVE, false)
    fun setBlessedEveReminderEnabled(enabled: Boolean) = user.edit().putBoolean(KEY_BLESSED_EVE, enabled).apply()

    // --- Worship tools --------------------------------------------------------------------

    fun getTasbih(): TasbihState = TasbihState.decode(user.getString(KEY_TASBIH, null))
    fun setTasbih(state: TasbihState) = user.edit().putString(KEY_TASBIH, state.encode()).apply()

    fun getQada(): QadaState = QadaState.decode(user.getString(KEY_QADA, null))
    fun setQada(state: QadaState) = user.edit().putString(KEY_QADA, state.encode()).apply()

    fun getKhumsYear(): KhumsYear? = KhumsYear.decode(user.getString(KEY_KHUMS_YEAR, null))
    fun setKhumsYear(year: KhumsYear?) {
        if (year == null) user.edit().remove(KEY_KHUMS_YEAR).apply()
        else user.edit().putString(KEY_KHUMS_YEAR, year.encode()).apply()
    }

    fun getKhumsDraft(): KhumsDraft = KhumsDraft.decode(user.getString(KEY_KHUMS_DRAFT, null))
    fun setKhumsDraft(draft: KhumsDraft) {
        if (draft.isEmpty) user.edit().remove(KEY_KHUMS_DRAFT).apply()
        else user.edit().putString(KEY_KHUMS_DRAFT, draft.encode()).apply()
    }

    fun isImsakAlarmEnabled(): Boolean = boot.getBoolean(KEY_IMSAK_ALARM, false)
    fun setImsakAlarmEnabled(enabled: Boolean) = boot.edit().putBoolean(KEY_IMSAK_ALARM, enabled).apply()

    fun isKhumsReminderEnabled(): Boolean = user.getBoolean(KEY_KHUMS_REMINDER, false)
    fun setKhumsReminderEnabled(enabled: Boolean) = user.edit().putBoolean(KEY_KHUMS_REMINDER, enabled).apply()

    fun isEclipseReminderEnabled(): Boolean = user.getBoolean(KEY_ECLIPSE_REMINDER, false)
    fun setEclipseReminderEnabled(enabled: Boolean) = user.edit().putBoolean(KEY_ECLIPSE_REMINDER, enabled).apply()

    // --- Backup ---------------------------------------------------------------------------

    /**
     * Everything that belongs in a backup file (see [BackupCodec]). Backed-up keys live in both stores — place and
     * alarm configs in the device store, the rest in the user store — so the backup reads the union of the two.
     */
    fun exportForBackup(): String = BackupCodec.export(boot.all + user.all, java.time.Instant.now().toString())

    /**
     * Replaces the backed-up settings with [entries], which [BackupCodec.parse] has already validated. Each key is
     * written to the store it belongs in, so a restored key round-trips regardless of which store holds it.
     */
    fun importFromBackup(entries: Map<String, Any>) {
        val bootEditor = boot.edit()
        val userEditor = user.edit()
        boot.all.keys.filter { BackupCodec.isAllowedKey(it) }.forEach(bootEditor::remove)
        user.all.keys.filter { BackupCodec.isAllowedKey(it) }.forEach(userEditor::remove)
        entries.forEach { (key, value) ->
            val editor = if (PrefsClassifier.isBootKey(key)) bootEditor else userEditor
            when (value) {
                is Boolean -> editor.putBoolean(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is String -> editor.putString(key, value)
            }
        }
        bootEditor.commit()
        userEditor.commit()
    }

    fun getLastSahifaId(): String? = user.getString(KEY_LAST_SAHIFA, null)
    fun setLastSahifaId(id: String) = user.edit().putString(KEY_LAST_SAHIFA, id).apply()

    /** "system", "light" or "dark". */
    fun getThemeMode(): String = user.getString(KEY_THEME_MODE, "system") ?: "system"
    fun setThemeMode(mode: String) = user.edit().putString(KEY_THEME_MODE, mode).apply()

    fun hasAskedNotificationPermission(): Boolean = user.getBoolean(KEY_ASKED_NOTIFICATIONS, false)
    fun setAskedNotificationPermission() = user.edit().putBoolean(KEY_ASKED_NOTIFICATIONS, true).apply()
}
