package com.dailydeeds.reminder.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The app language picked in Settings, and how it resolves against the device language (v1.9.0). */
class AppLanguageTest {

    @Test
    fun anExplicitChoiceWinsOverTheDeviceLanguage() {
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve("ar", deviceLanguage = "en"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.resolve("en", deviceLanguage = "ar"))
        assertEquals(AppLanguage.PERSIAN, AppLanguage.resolve("fa", deviceLanguage = "en"))
    }

    @Test
    fun followingTheDeviceUsesItsLanguageWhenTheAppHasIt() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.resolve(AppLanguage.SYSTEM, deviceLanguage = "en"))
        assertEquals(AppLanguage.PERSIAN, AppLanguage.resolve(AppLanguage.SYSTEM, deviceLanguage = "fa"))
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve(AppLanguage.SYSTEM, deviceLanguage = "ar"))
    }

    @Test
    fun followingTheDeviceFallsBackToArabicForOtherLanguages() {
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve(AppLanguage.SYSTEM, deviceLanguage = "de"))
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve(AppLanguage.SYSTEM, deviceLanguage = ""))
    }

    @Test
    fun aMissingOrUnknownStoredValueKeepsTheAppArabic() {
        // Existing users have no stored value: the app must stay exactly as it was, in Arabic.
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve(null, deviceLanguage = "en"))
        assertEquals(AppLanguage.ARABIC, AppLanguage.resolve("xx", deviceLanguage = "en"))
        assertEquals(AppLanguage.ARABIC.tag, AppLanguage.DEFAULT_PREF)
    }

    @Test
    fun arabicAndPersianAreRightToLeftAndEnglishIsNot() {
        assertTrue(AppLanguage.ARABIC.rtl)
        assertTrue(AppLanguage.PERSIAN.rtl)
        assertFalse(AppLanguage.ENGLISH.rtl)
    }

    @Test
    fun tagsAreTheResourceQualifiers() {
        assertEquals(listOf("ar", "en", "fa"), AppLanguage.entries.map { it.tag })
    }

    @Test
    fun eachLanguageNamesItselfInItsOwnScript() {
        // The picker lists every choice in its own language, so a user who cannot read the current UI can still find theirs.
        assertEquals("العربية", AppLanguage.ARABIC.nativeName)
        assertEquals("English", AppLanguage.ENGLISH.nativeName)
        assertEquals("فارسی", AppLanguage.PERSIAN.nativeName)
    }
}
