package com.dailydeeds.reminder.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.CalcMethod
import com.dailydeeds.reminder.util.CoordinateInput
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.TimeFormat

/*
 * UI wording of model values in the app language (v1.9.0). The models stay free of Android resources so they keep
 * their plain unit tests; the screens read these instead of the models' Arabic labels.
 */

@get:StringRes
val Prayer.labelRes: Int
    get() = when (this) {
        Prayer.FAJR -> R.string.prayer_fajr
        Prayer.DHUHR -> R.string.prayer_dhuhr
        Prayer.MAGHRIB -> R.string.prayer_maghrib
        Prayer.ISHA -> R.string.prayer_isha
    }

@get:StringRes
val AdhanMode.labelRes: Int
    get() = when (this) {
        AdhanMode.OFF -> R.string.adhan_mode_off
        AdhanMode.SILENT -> R.string.adhan_mode_silent
        AdhanMode.NOTIFICATION -> R.string.adhan_mode_notification
        AdhanMode.ADHAN -> R.string.adhan_mode_adhan
    }

@get:StringRes
val CalcMethod.labelRes: Int
    get() = when (this) {
        CalcMethod.LEVA -> R.string.calc_method_leva
        CalcMethod.TEHRAN -> R.string.calc_method_tehran
        CalcMethod.CUSTOM -> R.string.calc_method_custom
    }

/** "In 2 hours and 30 minutes", "now", ... */
@Composable
fun countdownText(c: TimeFormat.Countdown): String = when {
    c.isNow -> stringResource(R.string.countdown_now)
    c.hours == 0L -> stringResource(R.string.countdown_minutes, c.minutes.toString())
    c.minutes == 0L -> stringResource(R.string.countdown_hours, c.hours.toString())
    else -> stringResource(R.string.countdown_hours_minutes, c.hours.toString(), c.minutes.toString())
}

/** Why a typed latitude or longitude was rejected. */
@Composable
fun coordinateErrorText(e: CoordinateInput.Result.Invalid): String {
    val axis = stringResource(
        if (e.axis == CoordinateInput.Axis.LATITUDE) R.string.coord_latitude else R.string.coord_longitude
    )
    return when (e.reason) {
        CoordinateInput.Reason.NOT_A_NUMBER -> stringResource(R.string.coord_error_not_number, axis)
        CoordinateInput.Reason.OUT_OF_RANGE ->
            stringResource(R.string.coord_error_range, axis, e.axis.limit.toInt().toString())
    }
}
