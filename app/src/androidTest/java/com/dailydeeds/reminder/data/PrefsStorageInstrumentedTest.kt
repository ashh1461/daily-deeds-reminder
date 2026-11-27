package com.dailydeeds.reminder.data

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Runs on a phone or emulator (`connectedDebugAndroidTest`). Finding F-1: the single settings file is split once,
 * after the first unlock, into a device-protected boot store and a credential-protected user store. The test
 * covers an upgrade from the 1.8.2–1.8.4 layout (whole file already in the device store) and a direct upgrade from
 * pre-1.8.2 (whole file still in the credential store), as well as a fresh install.
 */
@RunWith(AndroidJUnit4::class)
class PrefsStorageInstrumentedTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val device: Context = context.createDeviceProtectedStorageContext()

    private fun deviceStore() = device.getSharedPreferences(PrefsStorage.DEVICE_NAME, Context.MODE_PRIVATE)
    private fun credentialDeviceNameStore() = context.getSharedPreferences(PrefsStorage.DEVICE_NAME, Context.MODE_PRIVATE)
    private fun userStore() = context.getSharedPreferences(PrefsStorage.USER_NAME, Context.MODE_PRIVATE)

    @Before
    fun clean() {
        device.deleteSharedPreferences(PrefsStorage.DEVICE_NAME)
        context.deleteSharedPreferences(PrefsStorage.DEVICE_NAME)
        context.deleteSharedPreferences(PrefsStorage.USER_NAME)
        PrefsStorage.resetSplitCheckForTest()
    }

    @After
    fun tearDown() = clean()

    /** 1.8.2–1.8.4: the whole file is in the device store. User keys move to the user store; boot keys stay. */
    @Test
    fun theDeviceOnlyFileIsSplitIntoTwoStores() {
        deviceStore().edit()
            .putString("pref_theme_mode", "dark")                 // user content
            .putString("worship_khums_draft", "{\"x\":1}")        // user content (the financial draft)
            .putString("pref_prayer_settings", "{\"m\":3}")       // boot key
            .putLong("pref_place_lat", 42L)                       // boot key
            .commit()

        val prefs = PreferencesManager(context)

        // User content is readable and lives only in the user store now.
        assertEquals("dark", prefs.getThemeMode())
        assertEquals("dark", userStore().getString("pref_theme_mode", null))
        assertNull(deviceStore().getString("pref_theme_mode", null))
        assertEquals("{\"x\":1}", userStore().getString("worship_khums_draft", null))
        assertNull(deviceStore().getString("worship_khums_draft", null))

        // Boot keys stay in the device store (readable before unlock).
        assertEquals("{\"m\":3}", deviceStore().getString("pref_prayer_settings", null))
        assertEquals(42L, deviceStore().getLong("pref_place_lat", 0L))
        assertNull(userStore().getString("pref_prayer_settings", null))
    }

    /** pre-1.8.2: the whole file never left credential storage. It is split, and the old credential file is removed. */
    @Test
    fun theLegacyCredentialFileIsSplitAndRemoved() {
        credentialDeviceNameStore().edit()
            .putString("pref_theme_mode", "light")
            .putString("pref_prayer_settings", "{\"m\":5}")
            .commit()

        val prefs = PreferencesManager(context)

        assertEquals("light", prefs.getThemeMode())
        assertEquals("light", userStore().getString("pref_theme_mode", null))
        // Boot keys brought into the device-protected store.
        assertEquals("{\"m\":5}", deviceStore().getString("pref_prayer_settings", null))
        // The old credential file of the same name is gone.
        assertNull(credentialDeviceNameStore().getString("pref_theme_mode", null))
        assertNull(credentialDeviceNameStore().getString("pref_prayer_settings", null))
    }

    /** A fresh install routes each key to its store directly, with no migration to do. */
    @Test
    fun aFreshInstallRoutesKeysToTheRightStore() {
        val prefs = PreferencesManager(context)
        prefs.setThemeMode("light")                                // user
        prefs.setPrayerSettings(com.dailydeeds.reminder.adhan.PrayerSettings()) // boot

        assertEquals("light", userStore().getString("pref_theme_mode", null))
        assertNull(deviceStore().getString("pref_theme_mode", null))
        assertNull(userStore().getString("pref_prayer_settings", null))
        // The boot key is present in the device store.
        assertEquals(true, deviceStore().contains("pref_prayer_settings"))
    }

    /** Backup round-trips keys regardless of which store they land in. */
    @Test
    fun backupRoundTripsAcrossBothStores() {
        val prefs = PreferencesManager(context)
        prefs.setThemeMode("dark")                                 // user store
        prefs.setHijriOffset(3)                                    // boot store
        val backup = prefs.exportForBackup()

        prefs.setThemeMode("light")
        prefs.setHijriOffset(0)
        val parsed = BackupCodec.parse(backup)
        assertEquals(true, parsed is BackupCodec.Import.Ok)
        prefs.importFromBackup((parsed as BackupCodec.Import.Ok).entries)

        assertEquals("dark", PreferencesManager(context).getThemeMode())
        assertEquals(3, PreferencesManager(context).getHijriOffset())
    }
}
