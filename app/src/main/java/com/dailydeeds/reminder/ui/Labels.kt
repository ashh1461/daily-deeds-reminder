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
import androidx.compose.ui.platform.LocalConfiguration
import com.dailydeeds.reminder.worship.QadaKind
import com.dailydeeds.reminder.worship.EclipseType
import com.dailydeeds.reminder.worship.Visibility
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
import com.dailydeeds.reminder.model.Surah
import com.dailydeeds.reminder.model.RevelationType
import com.dailydeeds.reminder.model.SearchResultType
import com.dailydeeds.reminder.util.ArabicDigits
import com.dailydeeds.reminder.util.AppLanguage

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

@get:StringRes
val QadaKind.labelRes: Int
    get() = when (this) {
        QadaKind.FAJR -> R.string.qada_kind_fajr
        QadaKind.DHUHR -> R.string.prayer_dhuhr
        QadaKind.ASR -> R.string.qada_kind_asr
        QadaKind.MAGHRIB -> R.string.prayer_maghrib
        QadaKind.ISHA -> R.string.prayer_isha
        QadaKind.FAST -> R.string.qada_kind_fast
    }

/** "Make-up fasts" or "Make-up Fajr prayer". */
@Composable
fun qadaTitle(kind: QadaKind): String =
    if (kind.isFast) stringResource(R.string.qada_fast_title)
    else stringResource(R.string.qada_prayer_title, stringResource(kind.labelRes))

@get:StringRes
val EclipseType.labelRes: Int
    get() = when (this) {
        EclipseType.TOTAL -> R.string.eclipse_type_total
        EclipseType.PARTIAL -> R.string.eclipse_type_partial
        EclipseType.PENUMBRAL -> R.string.eclipse_type_penumbral
        EclipseType.ANNULAR -> R.string.eclipse_type_annular
        EclipseType.HYBRID -> R.string.eclipse_type_hybrid
    }

@get:StringRes
val Visibility.labelRes: Int
    get() = when (this) {
        Visibility.VISIBLE -> R.string.eclipse_visible
        Visibility.NEAR_HORIZON -> R.string.eclipse_near_horizon
        Visibility.BELOW_HORIZON -> R.string.eclipse_below_horizon
        Visibility.CHECK_LOCALLY -> R.string.eclipse_check_locally
    }

/** The locale of the app language, for dates and weekday names. */
@Composable
fun appLocale(): Locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()

/** The app language, as set on the activity's configuration (LocaleSupport). */
@Composable
fun appLanguage(): AppLanguage = AppLanguage.fromTag(appLocale().language) ?: AppLanguage.ARABIC

/** A Gregorian month name (1-12) in the app language. */
@Composable
fun gregorianMonthName(month: Int): String = Month.of(month).getDisplayName(TextStyle.FULL, appLocale())

/** A number in the app language's digits: Western in English, Arabic-Indic otherwise (as before v1.9.0). */
@Composable
fun localNumber(n: Int): String = if (appLocale().language == "en") n.toString() else ArabicDigits.format(n)

/** The surah's name for UI chrome: its English name in the English UI, the Arabic name otherwise. */
@Composable
fun surahDisplayName(surah: Surah): String = if (appLocale().language == "en") surah.nameEnglish else surah.nameArabic

@get:StringRes
val RevelationType.labelRes: Int
    get() = when (this) {
        RevelationType.MAKKI -> R.string.revelation_makki
        RevelationType.MADANI -> R.string.revelation_madani
    }

@get:StringRes
val SearchResultType.labelRes: Int
    get() = when (this) {
        SearchResultType.ALL -> R.string.search_type_all
        SearchResultType.QURAN -> R.string.tab_quran_title
        SearchResultType.MAFATIH -> R.string.tab_mafatih_title
        SearchResultType.SAHIFA -> R.string.tab_sahifa_title
        SearchResultType.WEEKDAY -> R.string.search_type_weekday
        SearchResultType.DEEDS -> R.string.search_type_deeds
    }

/** "today", "tomorrow" or "in N days". */
@Composable
fun daysAwayText(days: Long): String = when (days) {
    0L -> stringResource(R.string.days_today)
    1L -> stringResource(R.string.days_tomorrow)
    else -> stringResource(R.string.days_in_n, days.toString())
}
