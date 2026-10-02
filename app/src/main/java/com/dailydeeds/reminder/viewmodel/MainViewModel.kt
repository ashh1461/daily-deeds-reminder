package com.dailydeeds.reminder.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.model.DeedType
import com.dailydeeds.reminder.notification.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)

    private val _selectedCategory = MutableStateFlow(DeedCategory.ALL)
    val selectedCategory: StateFlow<DeedCategory> = _selectedCategory.asStateFlow()

    private val _completedMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val completedMap: StateFlow<Map<Int, Boolean>> = _completedMap.asStateFlow()

    private val _countsMap = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val countsMap: StateFlow<Map<Int, Int>> = _countsMap.asStateFlow()

    private val _stagesMap = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val stagesMap: StateFlow<Map<Int, Int>> = _stagesMap.asStateFlow()

    private val _dailyProgress = MutableStateFlow(Pair(0, 11))
    val dailyProgress: StateFlow<Pair<Int, Int>> = _dailyProgress.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(true)
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _morningEnabled = MutableStateFlow(true)
    val morningEnabled: StateFlow<Boolean> = _morningEnabled.asStateFlow()

    private val _morningTime = MutableStateFlow(Pair(7, 0))
    val morningTime: StateFlow<Pair<Int, Int>> = _morningTime.asStateFlow()

    private val _eveningEnabled = MutableStateFlow(true)
    val eveningEnabled: StateFlow<Boolean> = _eveningEnabled.asStateFlow()

    private val _eveningTime = MutableStateFlow(Pair(20, 0))
    val eveningTime: StateFlow<Pair<Int, Int>> = _eveningTime.asStateFlow()

    init {
        loadDailyState()
    }

    fun loadDailyState() {
        prefs.checkAndResetDaily()

        val allDeeds = DeedsRepository.getAllDeeds()
        val completed = mutableMapOf<Int, Boolean>()
        val counts = mutableMapOf<Int, Int>()
        val stages = mutableMapOf<Int, Int>()

        for (deed in allDeeds) {
            completed[deed.id] = prefs.isDeedCompleted(deed.id)
            counts[deed.id] = prefs.getDeedCount(deed.id)
            stages[deed.id] = prefs.getDeedStage(deed.id)
        }

        _completedMap.value = completed
        _countsMap.value = counts
        _stagesMap.value = stages
        _dailyProgress.value = prefs.getDailyProgress()

        _hapticsEnabled.value = prefs.isHapticsEnabled()
        _soundEnabled.value = prefs.isSoundEnabled()
        _morningEnabled.value = prefs.isMorningReminderEnabled()
        _morningTime.value = prefs.getMorningReminderTime()
        _eveningEnabled.value = prefs.isEveningReminderEnabled()
        _eveningTime.value = prefs.getEveningReminderTime()
    }

    fun selectCategory(category: DeedCategory) {
        _selectedCategory.value = category
    }

    fun toggleDeedCompleted(deedId: Int) {
        val current = _completedMap.value[deedId] ?: false
        val newStatus = !current
        val deed = DeedsRepository.getDeedById(deedId)

        prefs.setDeedCompleted(deedId, newStatus)
        _completedMap.value = _completedMap.value.toMutableMap().apply { put(deedId, newStatus) }

        if (deed != null && deed.type != DeedType.READING) {
            val count = if (newStatus) deed.targetCount else 0
            val stage = if (newStatus && deed.stages != null) deed.stages.size - 1 else 0
            prefs.setDeedCount(deedId, count)
            prefs.setDeedStage(deedId, stage)
            _countsMap.value = _countsMap.value.toMutableMap().apply { put(deedId, count) }
            _stagesMap.value = _stagesMap.value.toMutableMap().apply { put(deedId, stage) }
        }

        _dailyProgress.value = prefs.getDailyProgress()

        if (newStatus && _hapticsEnabled.value) {
            triggerCompletionVibration()
        }
    }

    fun incrementDeedCount(deedId: Int) {
        val deed = DeedsRepository.getDeedById(deedId) ?: return
        val currentCount = _countsMap.value[deedId] ?: 0

        if (currentCount >= deed.targetCount) {
            return
        }

        val newCount = currentCount + 1
        prefs.setDeedCount(deedId, newCount)
        _countsMap.value = _countsMap.value.toMutableMap().apply { put(deedId, newCount) }

        if (deed.type == DeedType.MULTI_STAGE_COUNTER && deed.stages != null) {
            val newStage = when {
                newCount <= 34 -> 0
                newCount <= 67 -> 1
                else -> 2
            }
            prefs.setDeedStage(deedId, newStage)
            _stagesMap.value = _stagesMap.value.toMutableMap().apply { put(deedId, newStage) }
        }

        if (_hapticsEnabled.value) {
            triggerTapVibration()
        }

        if (newCount >= deed.targetCount) {
            prefs.setDeedCompleted(deedId, true)
            _completedMap.value = _completedMap.value.toMutableMap().apply { put(deedId, true) }
            _dailyProgress.value = prefs.getDailyProgress()

            if (_hapticsEnabled.value) {
                triggerCompletionVibration()
            }
        }
    }

    fun resetDeedCount(deedId: Int) {
        prefs.setDeedCount(deedId, 0)
        prefs.setDeedStage(deedId, 0)
        prefs.setDeedCompleted(deedId, false)

        _countsMap.value = _countsMap.value.toMutableMap().apply { put(deedId, 0) }
        _stagesMap.value = _stagesMap.value.toMutableMap().apply { put(deedId, 0) }
        _completedMap.value = _completedMap.value.toMutableMap().apply { put(deedId, false) }
        _dailyProgress.value = prefs.getDailyProgress()
    }

    fun resetToday() {
        prefs.resetToday()
        loadDailyState()
    }

    fun setMorningReminder(enabled: Boolean, hour: Int, minute: Int) {
        prefs.setMorningReminderEnabled(enabled)
        prefs.setMorningReminderTime(hour, minute)
        _morningEnabled.value = enabled
        _morningTime.value = Pair(hour, minute)
        AlarmScheduler.scheduleAllReminders(getApplication())
    }

    fun setEveningReminder(enabled: Boolean, hour: Int, minute: Int) {
        prefs.setEveningReminderEnabled(enabled)
        prefs.setEveningReminderTime(hour, minute)
        _eveningEnabled.value = enabled
        _eveningTime.value = Pair(hour, minute)
        AlarmScheduler.scheduleAllReminders(getApplication())
    }

    fun toggleHaptics(enabled: Boolean) {
        prefs.setHapticsEnabled(enabled)
        _hapticsEnabled.value = enabled
    }

    fun toggleSound(enabled: Boolean) {
        prefs.setSoundEnabled(enabled)
        _soundEnabled.value = enabled
    }

    private fun getVibrator(): Vibrator? {
        val context = getApplication<Application>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun triggerTapVibration() {
        val vibrator = getVibrator() ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(35)
        }
    }

    private fun triggerCompletionVibration() {
        val vibrator = getVibrator() ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 100, 80, 150, 80, 250)
            val amplitudes = intArrayOf(0, 200, 0, 220, 0, 255)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 100, 80, 150, 80, 250), -1)
        }
    }
}
