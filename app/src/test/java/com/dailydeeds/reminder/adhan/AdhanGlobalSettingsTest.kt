package com.dailydeeds.reminder.adhan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdhanGlobalSettingsTest {

    @Test
    fun hardwareStopRoundTrips() {
        val off = AdhanGlobalSettings(hardwareStop = false)
        assertEquals(off, AdhanGlobalSettings.decode(off.encode()))
        assertTrue(AdhanGlobalSettings().hardwareStop)
    }

    @Test
    fun settingsSavedBeforeTheFlagDefaultToHardwareStopOn() {
        val old = AdhanGlobalSettings.decode("false,true,10,120")
        assertEquals(AdhanGlobalSettings(followRinger = false, vibrate = true, snoozeMinutes = 10, maxSeconds = 120), old)
        assertTrue(old!!.hardwareStop)
        assertFalse(AdhanGlobalSettings.decode("true,true,5,300,false")!!.hardwareStop)
    }

    @Test
    fun malformedFlagIsRejected() {
        assertNull(AdhanGlobalSettings.decode("true,true,5,300,maybe"))
        assertNull(AdhanGlobalSettings.decode("true,true,5,300,true,extra"))
    }
}
