package com.dailydeeds.reminder.adhan

/**
 * The optional fade-in of the adhan ([AdhanGlobalSettings.fadeIn]): the player's volume rises from [START_VOLUME] to
 * full over [DURATION_MS]. Loudness is heard on a log scale, so the volume follows a square curve, which sounds like
 * an even rise instead of a jump at the start. The stream volume the user set is never changed.
 */
object AdhanFade {
    const val DURATION_MS = 10_000L
    const val STEP_MS = 250L
    const val START_VOLUME = 0.05f

    /** The player volume (0..1) [elapsedMs] after playback started. */
    fun volumeAt(elapsedMs: Long): Float {
        val t = (elapsedMs.toFloat() / DURATION_MS).coerceIn(0f, 1f)
        return START_VOLUME + (1f - START_VOLUME) * t * t
    }
}
