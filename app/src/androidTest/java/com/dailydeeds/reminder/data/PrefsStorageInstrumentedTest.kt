package com.dailydeeds.reminder.data

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Runs on a phone or emulator (`connectedDebugAndroidTest`): settings saved by 1.8.1.x in credential storage are
 * moved to device-protected storage on the first unlocked start of 1.8.2, and read from there.
 */
@RunWith(AndroidJUnit4::class)
class PrefsStorageInstrumentedTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val device: Context = context.createDeviceProtectedStorageContext()

    @Before
    fun clean() {
        device.deleteSharedPreferences(PrefsStorage.NAME)
        context.deleteSharedPreferences(PrefsStorage.NAME)
        PrefsStorage.resetMoveCheckForTest()
    }

    @After
    fun tearDown() = clean()

    @Test
    fun settingsSavedBeforeTheUpdateAreMovedAndStillRead() {
        context.getSharedPreferences(PrefsStorage.NAME, Context.MODE_PRIVATE)
            .edit().putString("pref_theme_mode", "dark").commit()

        val prefs = PrefsStorage.open(context)

        assertEquals("dark", prefs.getString("pref_theme_mode", null))
        assertEquals("dark", PreferencesManager(context).getThemeMode())
        assertEquals(true, PrefsStorage.isReadable(context))
    }

    @Test
    fun aFreshInstallStartsInDeviceStorage() {
        PreferencesManager(context).setThemeMode("light")
        assertEquals("light", device.getSharedPreferences(PrefsStorage.NAME, Context.MODE_PRIVATE).getString("pref_theme_mode", null))
    }
}
