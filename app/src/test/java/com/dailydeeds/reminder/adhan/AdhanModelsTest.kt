package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.PrayerParams
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdhanModelsTest {

    @Test
    fun alarmConfigRoundTrips() {
        val c = PrayerAlarmConfig(AdhanMode.ADHAN, "own_k3j2", 15, -3)
        assertEquals(c, PrayerAlarmConfig.decode(c.encode()))
    }

    @Test
    fun malformedAlarmConfigsAreRejected() {
        assertNull(PrayerAlarmConfig.decode(null))
        assertNull(PrayerAlarmConfig.decode(""))
        assertNull(PrayerAlarmConfig.decode("adhan,default,15"))
        assertNull(PrayerAlarmConfig.decode("loud,default,0,0"))
        assertNull(PrayerAlarmConfig.decode("adhan,../../etc/passwd,0,0"))
        assertNull(PrayerAlarmConfig.decode("adhan,default,45,0"))
        assertNull(PrayerAlarmConfig.decode("adhan,default,0,99"))
        assertNull(PrayerAlarmConfig.decode("adhan,default,x,0"))
    }

    @Test
    fun globalSettingsRoundTripAndRejectGarbage() {
        val g = AdhanGlobalSettings(followRinger = false, vibrate = true, snoozeMinutes = 10, maxSeconds = 120)
        assertEquals(g, AdhanGlobalSettings.decode(g.encode()))
        assertNull(AdhanGlobalSettings.decode("yes,true,5,300"))
        assertNull(AdhanGlobalSettings.decode("true,true,0,300"))
        assertNull(AdhanGlobalSettings.decode("true,true,5,5"))
    }

    @Test
    fun methodsMapToTheRightAngles() {
        assertEquals(PrayerParams.LEVA, PrayerSettings(CalcMethod.LEVA).params())
        assertEquals(PrayerParams.TEHRAN, PrayerSettings(CalcMethod.TEHRAN).params())
        val custom = PrayerSettings(CalcMethod.CUSTOM, 15.0, 13.5, 3.0, maghribDelayMinutes = 5, imsakMinutes = 12).params()
        assertEquals(15.0, custom.fajrAngle, 0.0)
        assertEquals(13.5, custom.ishaAngle, 0.0)
        assertEquals(3.0, custom.maghribAngle, 0.0)
        assertEquals(5, custom.maghribDelayMinutes)
        assertEquals(12, custom.imsakMinutes)
    }

    @Test
    fun prayerSettingsRoundTripAndRejectInvalidAngles() {
        val s = PrayerSettings(CalcMethod.CUSTOM, 16.5, 14.0, 4.0, 2, 8)
        assertEquals(s, PrayerSettings.decode(s.encode()))
        assertNull(PrayerSettings.decode("custom,99.0,14.0,4.0,0,10"))
        assertNull(PrayerSettings.decode("custom,16.0,14.0,4.0,0"))
        assertTrue(PrayerSettings.decode("tehran,16.0,14.0,4.0,0,10") != null)
    }
}
