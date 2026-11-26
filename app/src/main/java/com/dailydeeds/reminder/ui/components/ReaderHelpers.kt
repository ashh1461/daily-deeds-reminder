package com.dailydeeds.reminder.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import java.util.WeakHashMap

/**
 * Keeps the screen on while the calling screen is shown (the tasbih and the readers). Counted per window, so during a
 * navigation animation the screen leaving cannot switch it off under the screen arriving.
 */
@Composable
fun KeepScreenOn() {
    val view = LocalView.current
    DisposableEffect(view) {
        ScreenOnHolds.acquire(view)
        onDispose { ScreenOnHolds.release(view) }
    }
}

private object ScreenOnHolds {
    private val counts = WeakHashMap<View, Int>()

    fun acquire(view: View) {
        counts[view] = (counts[view] ?: 0) + 1
        view.keepScreenOn = true
    }

    fun release(view: View) {
        val left = (counts[view] ?: 1) - 1
        if (left <= 0) {
            counts.remove(view)
            view.keepScreenOn = false
        } else {
            counts[view] = left
        }
    }
}

/** A strong double buzz, felt without looking at the phone (the end of each tasbih stage, a reached target). */
@Composable
fun rememberStrongBuzz(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        val vibrator = vibratorOf(context)
        val buzz: () -> Unit = {
            if (vibrator?.hasVibrator() == true) {
                vibrator.vibrate(VibrationEffect.createWaveform(STRONG_BUZZ_PATTERN, -1))
            }
        }
        buzz
    }
}

/** Off, on, pause, on (milliseconds): longer and stronger than the tick of an ordinary tap. */
private val STRONG_BUZZ_PATTERN = longArrayOf(0L, 180L, 90L, 180L)

private fun vibratorOf(context: Context): Vibrator? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
