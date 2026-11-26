package com.dailydeeds.reminder.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Announces that a backup was imported, so view models that keep settings in memory (the tasbih, make-up prayers,
 * khums, the reader text sizes and fit mode) read them again. Without it, the next tap would write the old values
 * over the imported ones.
 */
object DataReloads {
    private val _imports = MutableStateFlow(0)

    /** Counts imports since the process started; a collector skips the count it saw when it was created. */
    val imports: StateFlow<Int> = _imports.asStateFlow()

    fun backupImported() = _imports.update { it + 1 }
}
