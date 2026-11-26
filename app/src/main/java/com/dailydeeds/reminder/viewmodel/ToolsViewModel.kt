package com.dailydeeds.reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.adhan.AdhanGlobalSettings
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.adhan.PrayerSettings
import com.dailydeeds.reminder.data.DataReloads
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.FavoritesList
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** State for the calendar, prayer times, qibla and favorites screens. */
class ToolsViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = PreferencesManager(app)

    private val _favorites = MutableStateFlow(prefs.getFavorites())
    val favorites: StateFlow<List<FavoriteKey>> = _favorites.asStateFlow()

    private val _place = MutableStateFlow(prefs.getPlace())
    val place: StateFlow<Place> = _place.asStateFlow()

    private val _hijriOffset = MutableStateFlow(prefs.getHijriOffset())
    val hijriOffset: StateFlow<Int> = _hijriOffset.asStateFlow()

    private val _alarmConfigs = MutableStateFlow(Prayer.values().associateWith { prefs.getAlarmConfig(it) })
    val alarmConfigs: StateFlow<Map<Prayer, PrayerAlarmConfig>> = _alarmConfigs.asStateFlow()

    private val _adhanGlobal = MutableStateFlow(prefs.getAdhanGlobal())
    val adhanGlobal: StateFlow<AdhanGlobalSettings> = _adhanGlobal.asStateFlow()

    private val _prayerSettings = MutableStateFlow(prefs.getPrayerSettings())
    val prayerSettings: StateFlow<PrayerSettings> = _prayerSettings.asStateFlow()

    /** Calculation parameters plus manual offsets; what every screen uses to compute prayer times. */
    private val _prayerContext = MutableStateFlow(buildContext())
    val prayerContext: StateFlow<PrayerContext> = _prayerContext.asStateFlow()

    private fun buildContext() = PrayerContext(
        _prayerSettings.value.params(),
        _alarmConfigs.value.mapValues { it.value.offsetMinutes }
    )

    private val _occasionReminder = MutableStateFlow(prefs.isOccasionReminderEnabled())
    val occasionReminder: StateFlow<Boolean> = _occasionReminder.asStateFlow()

    private val _blessedEveReminder = MutableStateFlow(prefs.isBlessedEveReminderEnabled())
    val blessedEveReminder: StateFlow<Boolean> = _blessedEveReminder.asStateFlow()

    private val _lastSurah = MutableStateFlow(prefs.getLastSurah())
    val lastSurah: StateFlow<Int> = _lastSurah.asStateFlow()

    private val _lastAyah = MutableStateFlow(prefs.getLastAyah())
    val lastAyah: StateFlow<Int> = _lastAyah.asStateFlow()

    private val _lastMafatihId = MutableStateFlow(prefs.getLastMafatihId())
    val lastMafatihId: StateFlow<String?> = _lastMafatihId.asStateFlow()

    private val _lastSahifaId = MutableStateFlow(prefs.getLastSahifaId())
    val lastSahifaId: StateFlow<String?> = _lastSahifaId.asStateFlow()

    private val _themeMode = MutableStateFlow(prefs.getThemeMode())
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun setThemeMode(mode: String) {
        prefs.setThemeMode(mode)
        _themeMode.value = mode
    }

    fun recordSahifa(id: String) {
        if (id == _lastSahifaId.value) return
        prefs.setLastSahifaId(id)
        _lastSahifaId.value = id
    }

    fun toggleFavorite(key: FavoriteKey) {
        _favorites.value = prefs.toggleFavorite(key)
    }

    /** Deletes several favorites at once, from the favorites screen. */
    fun removeFavorites(keys: Set<FavoriteKey>) {
        if (keys.isEmpty()) return
        _favorites.value = prefs.removeFavorites(keys)
    }

    /**
     * The undo of the favorites screen: puts back what [snapshot] (saved before the delete) has and the list now
     * lacks. A star added elsewhere since the delete is kept.
     */
    fun restoreFavorites(snapshot: List<FavoriteKey>) {
        prefs.setFavorites(FavoritesList.restoreRemoved(snapshot, prefs.getFavorites()))
        _favorites.value = prefs.getFavorites()
    }

    private val _quranPagesMode = MutableStateFlow(prefs.getQuranPagesMode())
    val quranPagesMode: StateFlow<Boolean> = _quranPagesMode.asStateFlow()

    fun setQuranPagesMode(pages: Boolean) {
        prefs.setQuranPagesMode(pages)
        _quranPagesMode.value = pages
    }

    private val _lastQuranPage = MutableStateFlow(prefs.getLastQuranPage())
    val lastQuranPage: StateFlow<Int> = _lastQuranPage.asStateFlow()

    /** Called when a mushaf page settles; also keeps the "resume" surah in step. */
    fun recordQuranPage(page: Int, surah: Int) {
        if (page != _lastQuranPage.value) {
            prefs.setLastQuranPage(page)
            _lastQuranPage.value = page
            // Reading went on in the mushaf: the ayah saved by the list is no longer where the reader is.
            if (_lastAyah.value != 0) {
                prefs.setLastAyah(0)
                _lastAyah.value = 0
            }
        }
        recordSurah(surah)
    }

    fun setPlace(place: Place) {
        prefs.setPlace(place)
        _place.value = place
        ReligiousAlarms.schedulePrayer(getApplication())
        // The eve reminder is an hour before Maghrib at the place, on the Hijri dates: it moves with them.
        ReligiousAlarms.scheduleBlessedEve(getApplication())
    }

    fun setHijriOffset(days: Int) {
        prefs.setHijriOffset(days)
        _hijriOffset.value = prefs.getHijriOffset()
        // The offset decides which days are in Ramadan (Imsak alarm) and the widget's Hijri date.
        ReligiousAlarms.schedulePrayer(getApplication())
        // The eve reminder is an hour before Maghrib at the place, on the Hijri dates: it moves with them.
        ReligiousAlarms.scheduleBlessedEve(getApplication())
    }

    fun setAlarmConfig(prayer: Prayer, config: PrayerAlarmConfig) {
        prefs.setAlarmConfig(prayer, config)
        _alarmConfigs.value = _alarmConfigs.value + (prayer to config)
        _prayerContext.value = buildContext()
        if (config.mode == AdhanMode.OFF) ReligiousAlarms.cancelSnooze(getApplication(), prayer)
        ReligiousAlarms.schedulePrayer(getApplication())
        // The eve reminder is an hour before Maghrib at the place, on the Hijri dates: it moves with them.
        ReligiousAlarms.scheduleBlessedEve(getApplication())
    }

    fun setAdhanGlobal(settings: AdhanGlobalSettings) {
        prefs.setAdhanGlobal(settings)
        _adhanGlobal.value = settings
    }

    fun setPrayerSettings(settings: PrayerSettings) {
        prefs.setPrayerSettings(settings)
        _prayerSettings.value = settings
        _prayerContext.value = buildContext()
        ReligiousAlarms.schedulePrayer(getApplication())
        // The eve reminder is an hour before Maghrib at the place, on the Hijri dates: it moves with them.
        ReligiousAlarms.scheduleBlessedEve(getApplication())
    }

    fun setOccasionReminder(enabled: Boolean) {
        prefs.setOccasionReminderEnabled(enabled)
        _occasionReminder.value = enabled
        ReligiousAlarms.scheduleOccasion(getApplication())
    }

    fun setBlessedEveReminder(enabled: Boolean) {
        prefs.setBlessedEveReminderEnabled(enabled)
        _blessedEveReminder.value = enabled
        ReligiousAlarms.scheduleBlessedEve(getApplication())
    }

    fun recordSurah(surah: Int) {
        if (surah == _lastSurah.value) return
        prefs.setLastSurah(surah)
        _lastSurah.value = surah
        // The saved ayah belonged to the previous surah.
        if (_lastAyah.value != 0) {
            prefs.setLastAyah(0)
            _lastAyah.value = 0
        }
    }

    /** Called when the list reader stops scrolling, with the ayah at its top. */
    fun recordAyah(surah: Int, ayah: Int) {
        recordSurah(surah)
        if (ayah == _lastAyah.value) return
        prefs.setLastAyah(ayah)
        _lastAyah.value = ayah
    }

    fun recordMafatih(id: String) {
        if (id == _lastMafatihId.value) return
        prefs.setLastMafatihId(id)
        _lastMafatihId.value = id
    }

    /** Reads everything this view model keeps in memory again, after a backup import replaced it. */
    private fun reloadFromPrefs() {
        _favorites.value = prefs.getFavorites()
        _place.value = prefs.getPlace()
        _hijriOffset.value = prefs.getHijriOffset()
        _alarmConfigs.value = Prayer.values().associateWith { prefs.getAlarmConfig(it) }
        _adhanGlobal.value = prefs.getAdhanGlobal()
        _prayerSettings.value = prefs.getPrayerSettings()
        _prayerContext.value = buildContext()
        _occasionReminder.value = prefs.isOccasionReminderEnabled()
        _blessedEveReminder.value = prefs.isBlessedEveReminderEnabled()
        _lastSurah.value = prefs.getLastSurah()
        _lastAyah.value = prefs.getLastAyah()
        _lastMafatihId.value = prefs.getLastMafatihId()
        _lastSahifaId.value = prefs.getLastSahifaId()
        _themeMode.value = prefs.getThemeMode()
        _quranPagesMode.value = prefs.getQuranPagesMode()
        _lastQuranPage.value = prefs.getLastQuranPage()
    }

    init {
        // Declared last so every flow above exists before the first reload. The import itself reschedules the alarms.
        val seen = DataReloads.imports.value
        viewModelScope.launch { DataReloads.importsAfter(seen).collect { reloadFromPrefs() } }
    }
}
