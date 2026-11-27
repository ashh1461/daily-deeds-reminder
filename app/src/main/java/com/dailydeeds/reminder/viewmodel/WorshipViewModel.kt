package com.dailydeeds.reminder.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.BackupCodec
import com.dailydeeds.reminder.data.DataReloads
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.ReligiousAlarms
import com.dailydeeds.reminder.worship.KhumsDraft
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.annotation.StringRes
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.util.LocaleSupport

/** Outcome of a backup export or import, shown once on the settings screen. */
data class BackupStatus(val message: String, val ok: Boolean)

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
    fun markQadaFullDay() = updateQada { it.markFullDay(today()) }
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

    /** The figures typed into the khums calculator, kept until they are cleared. */
    private val _khumsDraft = MutableStateFlow(prefs.getKhumsDraft())
    val khumsDraft: StateFlow<KhumsDraft> = _khumsDraft.asStateFlow()

    fun setKhumsDraft(draft: KhumsDraft) {
        if (draft == _khumsDraft.value) return
        _khumsDraft.value = draft
        prefs.setKhumsDraft(draft)
    }

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

    private fun text(@StringRes id: Int, vararg args: Any): String =
        LocaleSupport.localized(getApplication()).getString(id, *args)

    @StringRes
    private fun reasonText(reason: BackupCodec.Reason): Int = when (reason) {
        BackupCodec.Reason.TOO_LARGE -> R.string.backup_error_too_large
        BackupCodec.Reason.NOT_A_BACKUP -> R.string.backup_error_not_backup
        BackupCodec.Reason.OTHER_APP -> R.string.backup_error_other_app
        BackupCodec.Reason.UNSUPPORTED_VERSION -> R.string.backup_error_version
        BackupCodec.Reason.EMPTY -> R.string.backup_error_empty
        BackupCodec.Reason.TOO_MANY_ENTRIES -> R.string.backup_error_too_many
        BackupCodec.Reason.NO_VALID_DATA -> R.string.backup_error_no_data
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            _backup.value = withContext(Dispatchers.IO) {
                runCatching {
                    val bytes = prefs.exportForBackup().toByteArray(Charsets.UTF_8)
                    val out = getApplication<Application>().contentResolver.openOutputStream(uri, "wt")
                        ?: return@runCatching BackupStatus(text(R.string.backup_open_write_failed), false)
                    out.use { it.write(bytes) }
                    BackupStatus(text(R.string.backup_saved), true)
                }.getOrElse { BackupStatus(text(R.string.backup_save_failed), false) }
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            _backup.value = withContext(Dispatchers.IO) {
                runCatching {
                    val input = getApplication<Application>().contentResolver.openInputStream(uri)
                        ?: return@runCatching BackupStatus(text(R.string.backup_open_failed), false)
                    val json = input.use { readCapped(it) }
                        ?: return@runCatching BackupStatus(text(R.string.backup_error_too_large), false)
                    when (val result = BackupCodec.parse(json)) {
                        is BackupCodec.Import.Error -> BackupStatus(text(reasonText(result.reason)), false)
                        is BackupCodec.Import.Ok -> {
                            prefs.importFromBackup(result.entries)
                            DataReloads.backupImported()
                            AlarmScheduler.scheduleAllReminders(getApplication())
                            val skipped = if (result.skipped > 0) " " + text(R.string.backup_skipped, result.skipped.toString()) else ""
                            // Every screen reads the imported values again (DataReloads) and the alarms are set
                            // from them above, so no restart is needed any more.
                            BackupStatus(text(R.string.backup_imported, result.entries.size.toString()) + skipped, true)
                        }
                    }
                }.getOrElse { BackupStatus(text(R.string.backup_read_failed), false) }
            }
        }
    }

    /** Reads everything this view model keeps in memory again, after a backup import replaced it. */
    private fun reloadFromPrefs() {
        _tasbih.value = prefs.getTasbih()
        _qada.value = prefs.getQada()
        _khumsYear.value = prefs.getKhumsYear()
        _khumsDraft.value = prefs.getKhumsDraft()
        _khumsReminder.value = prefs.isKhumsReminderEnabled()
        _eclipseReminder.value = prefs.isEclipseReminderEnabled()
        _imsakAlarm.value = prefs.isImsakAlarmEnabled()
    }

    init {
        // Declared last so every flow above exists before the first reload. Imports counted before this view model
        // existed are already in what it read from the preferences.
        val seen = DataReloads.imports.value
        viewModelScope.launch { DataReloads.imports.filter { it != seen }.collect { reloadFromPrefs() } }
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
