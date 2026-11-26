package com.dailydeeds.reminder.adhan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The optional fade-in of the adhan volume. */
class AdhanFadeTest {

    @Test
    fun startsQuietAndEndsAtFullVolume() {
        assertEquals(AdhanFade.START_VOLUME, AdhanFade.volumeAt(0), 0.0001f)
        assertEquals(1f, AdhanFade.volumeAt(AdhanFade.DURATION_MS), 0.0001f)
        assertEquals(1f, AdhanFade.volumeAt(AdhanFade.DURATION_MS * 3), 0.0001f)
        assertEquals(AdhanFade.START_VOLUME, AdhanFade.volumeAt(-500), 0.0001f)
    }

    @Test
    fun risesSteadilyAndSlowlyAtFirst() {
        val steps = (0..AdhanFade.DURATION_MS step AdhanFade.STEP_MS).map { AdhanFade.volumeAt(it) }
        steps.zipWithNext().forEach { (a, b) -> assertTrue(b >= a) }
        // Loudness is heard on a log scale: a curve below the straight line sounds even.
        val half = AdhanFade.volumeAt(AdhanFade.DURATION_MS / 2)
        assertTrue(half < (AdhanFade.START_VOLUME + 1f) / 2)
    }

    @Test
    fun theOptionIsOffByDefaultAndRoundTrips() {
        assertFalse(AdhanGlobalSettings().fadeIn)
        val on = AdhanGlobalSettings(fadeIn = true)
        assertEquals(on, AdhanGlobalSettings.decode(on.encode()))
        // Saved before 1.8.4 (five fields): off.
        assertFalse(AdhanGlobalSettings.decode("true,true,5,300,false")!!.fadeIn)
    }
}
