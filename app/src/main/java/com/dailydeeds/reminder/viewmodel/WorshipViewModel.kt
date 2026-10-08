package com.dailydeeds.reminder.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.BackupCodec
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.worship.KhumsYear
import com.dailydeeds.reminder.worship.QadaKind
import com.dailydeeds.reminder.worship.QadaState
import com.dailydeeds.reminder.worship.TasbihState
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Outcome of a backup export or import, shown once on the settings screen. */
data class BackupStatus(val message: String, val ok: Boolean, val restartNeeded: Boolean = false)

/** State of the tasbih, make-up prayers, khums and Ramadan features plus backup of the user's data. */
class WorshipViewModel(app: Application) : AndroidViewModel(app) {
    private val prefs = PreferencesManager(app)

    // --- Tasbih ---------------------------------------------------------------------------
    private val _tasbih = MutableStateFlow(prefs.getTasbih())
    val tasbih: StateFlow<TasbihState> = _tasbih.asStateFlow()

    private fun updateTasbih(change: (TasbihState) -> TasbihState) {
        val next = change(_tasbih.value)
        if (next == _tasbih.value) return
        _tasbih.value = next
        prefs.setTasbih(next)
    }

    fun tapZahra() = updateTasbih { it.tapZahra() }
    fun tapFree() = updateTasbih { it.tapFree() }
    fun resetZahra() = updateTasbih { it.resetZahra() }
    fun resetFree() = updateTasbih { it.resetFree() }
    fun setFreeTarget(target: Int) = updateTasbih { it.withTarget(target) }

    // --- Make-up prayers and fasts -----------------------------------------------------------
    private val _qada = MutableStateFlow(prefs.getQada())
    val qada: StateFlow<QadaState> = _qada.asStateFlow()

    private fun updateQada(change: (QadaState) -> QadaState) {
        val next = change(_qada.value)
        if (next == _qada.value) return
        _qada.value = next
        prefs.setQada(next)
    }

    private fun today() = LocalDate.now().toEpochDay()
    fun addQada(kind: QadaKind, count: Int) = updateQada { it.add(kind, count, today()) }
    fun markQada(kind: QadaKind, count: Int = 1) = updateQada { it.markMade(kind, count, today()) }
    fun addQadaDays(days: Int) = updateQada { it.addDaysOfPrayers(days, today()) }
    fun undoQada() = updateQada { it.undoLast() }

    // --- Khums, eclipse reminder, Imsak alarm ----------------------------------------------------
    private val _khumsYear = MutableStateFlow(prefs.getKhumsYear())
    val khumsYear: StateFlow<KhumsYear?> = _khumsYear.asStateFlow()

    private val _khumsReminder = MutableStateFlow(prefs.isKhumsReminderEnabled())
    val khumsReminder: StateFlow<Boolean> = _khumsReminder.asStateFlow()

    private val _eclipseReminder = MutableStateFlow(prefs.isEclipseReminderEnabled())
    val eclipseReminder: StateFlow<Boolean> = _eclipseReminder.asStateFlow()

    private val _imsakAlarm = MutableStateFlow(prefs.isImsakAlarmEnabled())
    val imsakAlarm: StateFlow<Boolean> = _imsakAlarm.asStateFlow()

    fun setKhumsYear(year: KhumsYear?) {
        prefs.setKhumsYear(year)
        _khumsYear.value = year
        ReligiousAlarms.scheduleOccasion(getApplication())
    }

    fun setKhumsReminder(enabled: Boolean) {
        prefs.setKhumsReminderEnabled(enabled)
        _khumsReminder.value = enabled
        ReligiousAlarms.scheduleOccasion(getApplication())
    }

    fun setEclipseReminder(enabled: Boolean) {
        prefs.setEclipseReminderEnabled(enabled)
        _eclipseReminder.value = enabled
        ReligiousAlarms.scheduleOccasion(getApplication())
    }

    fun setImsakAlarm(enabled: Boolean) {
        prefs.setImsakAlarmEnabled(enabled)
        _imsakAlarm.value = enabled
        ReligiousAlarms.schedulePrayer(getApplication())
    }

    // --- Backup ---------------------------------------------------------------------------
    private val _backup = MutableStateFlow<BackupStatus?>(null)
    val backup: StateFlow<BackupStatus?> = _backup.asStateFlow()

    fun clearBackupStatus() {
        _backup.value = null
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            _backup.value = withContext(Dispatchers.IO) {
                runCatching {
                    val bytes = prefs.exportForBackup().toByteArray(Charsets.UTF_8)
                    val out = getApplication<Application>().contentResolver.openOutputStream(uri, "wt")
                        ?: return@runCatching BackupStatus("تعذّر فتح الملف للكتابة", false)
                    out.use { it.write(bytes) }
                    BackupStatus("تم حفظ النسخة الاحتياطية", true)
                }.getOrElse { BackupStatus("تعذّر حفظ النسخة الاحتياطية", false) }
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            _backup.value = withContext(Dispatchers.IO) {
                runCatching {
                    val input = getApplication<Application>().contentResolver.openInputStream(uri)
                        ?: return@runCatching BackupStatus("تعذّر فتح الملف", false)
                    val text = input.use { readCapped(it) }
                        ?: return@runCatching BackupStatus("الملف أكبر من الحد المسموح", false)
                    when (val result = BackupCodec.parse(text)) {
                        is BackupCodec.Import.Error -> BackupStatus(result.message, false)
                        is BackupCodec.Import.Ok -> {
                            prefs.importFromBackup(result.entries)
                            AlarmScheduler.scheduleAllReminders(getApplication())
                            val skipped = if (result.skipped > 0) " (تم تجاهل ${result.skipped} عنصر غير صالح)" else ""
                            BackupStatus("تم استيراد ${result.entries.size} عنصر$skipped. أعد تشغيل التطبيق لتطبيق البيانات.", true, restartNeeded = true)
                        }
                    }
                }.getOrElse { BackupStatus("تعذّر قراءة النسخة الاحتياطية", false) }
            }
        }
    }

    private fun readCapped(input: InputStream): String? {
        val buffer = ByteArrayOutputStream()
        val chunk = ByteArray(8192)
        var total = 0
        while (true) {
            val n = input.read(chunk)
            if (n < 0) break
            total += n
            if (total > BackupCodec.MAX_BYTES) return null
            buffer.write(chunk, 0, n)
        }
        return buffer.toString(Charsets.UTF_8.name())
    }
}
