package com.dailydeeds.reminder.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update

/**
 * Announces that a backup was imported, so view models that keep settings in memory read them again: the tasbih,
 * make-up prayers, khums, the reader text sizes and fit mode (since 1.8.3); favourites, the place, the Hijri offset,
 * the adhan and prayer settings, the occasion and eve reminders, the theme, the last positions, the pages mode, the
 * daily reminders and haptics (since 1.8.4). Without it, the next tap would write the old values over the imported
 * ones, and the app had to be restarted.
 */
object DataReloads {
    private val _imports = MutableStateFlow(0)

    /** Counts imports since the process started; a collector skips the count it saw when it was created. */
    val imports: StateFlow<Int> = _imports.asStateFlow()

    fun backupImported() = _imports.update { it + 1 }

    /**
     * The imports counted after [seen], the value a view model read when it was created: imports before that are
     * already in what it read from the preferences.
     */
    fun importsAfter(seen: Int): Flow<Int> = imports.filter { it != seen }
}
