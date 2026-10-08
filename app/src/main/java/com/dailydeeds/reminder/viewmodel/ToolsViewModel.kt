package com.dailydeeds.reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dailydeeds.reminder.adhan.AdhanGlobalSettings
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.adhan.PrayerSettings
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _lastSurah = MutableStateFlow(prefs.getLastSurah())
    val lastSurah: StateFlow<Int> = _lastSurah.asStateFlow()

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

    fun setPlace(place: Place) {
        prefs.setPlace(place)
        _place.value = place
        ReligiousAlarms.schedulePrayer(getApplication())
    }

    fun setHijriOffset(days: Int) {
        prefs.setHijriOffset(days)
        _hijriOffset.value = prefs.getHijriOffset()
    }

    fun setAlarmConfig(prayer: Prayer, config: PrayerAlarmConfig) {
        prefs.setAlarmConfig(prayer, config)
        _alarmConfigs.value = _alarmConfigs.value + (prayer to config)
        _prayerContext.value = buildContext()
        ReligiousAlarms.schedulePrayer(getApplication())
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
    }

    fun setOccasionReminder(enabled: Boolean) {
        prefs.setOccasionReminderEnabled(enabled)
        _occasionReminder.value = enabled
        ReligiousAlarms.scheduleOccasion(getApplication())
    }

    fun recordSurah(surah: Int) {
        if (surah == _lastSurah.value) return
        prefs.setLastSurah(surah)
        _lastSurah.value = surah
    }

    fun recordMafatih(id: String) {
        if (id == _lastMafatihId.value) return
        prefs.setLastMafatihId(id)
        _lastMafatihId.value = id
    }
}
