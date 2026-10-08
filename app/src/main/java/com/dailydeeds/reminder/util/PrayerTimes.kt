package com.dailydeeds.reminder.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

/** Daily prayer times for one place; any time that cannot be computed (extreme latitude) is null. */
data class PrayerTimesResult(
    val fajr: LocalTime?,
    val sunrise: LocalTime?,
    val dhuhr: LocalTime?,
    val sunset: LocalTime?,
    val maghrib: LocalTime?,
    val isha: LocalTime?,
    /** Legal midnight: halfway between sunset and the next day's Fajr. */
    val midnight: LocalTime?,
    /** Start of the fast: [PrayerParams.imsakMinutes] before Fajr. */
    val imsak: LocalTime? = null
)

/**
 * Calculation parameters. The two Jafari presets are the Leva Research Institute (Qom) method and the
 * University of Tehran method. A positive [maghribDelayMinutes] replaces the Maghrib angle by
 * "minutes after sunset".
 */
data class PrayerParams(
    val fajrAngle: Double = 16.0,
    val ishaAngle: Double = 14.0,
    val maghribAngle: Double = 4.0,
    val maghribDelayMinutes: Int = 0,
    val imsakMinutes: Int = 10
) {
    init {
        require(fajrAngle in 5.0..25.0 && ishaAngle in 5.0..25.0 && maghribAngle in 0.0..10.0)
        require(maghribDelayMinutes in 0..60 && imsakMinutes in 0..60)
    }

    companion object {
        val LEVA = PrayerParams()
        val TEHRAN = PrayerParams(fajrAngle = 17.7, ishaAngle = 14.0, maghribAngle = 4.5)
    }
}

/**
 * Jafari (Ithna Ashari) prayer-time calculation: Fajr at 16 degrees, Maghrib 4 degrees below the
 * horizon after sunset, Isha at 14 degrees. Based on the standard PrayTimes.org sun-position
 * algorithm. Pure Kotlin so it can be unit-tested against published timetables.
 */
object PrayerTimes {
    const val FAJR_ANGLE = 16.0
    const val MAGHRIB_ANGLE = 4.0
    const val ISHA_ANGLE = 14.0
    private const val RISE_SET_ANGLE = 0.833

    fun compute(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        zone: ZoneId,
        params: PrayerParams = PrayerParams.LEVA
    ): PrayerTimesResult {
        val tzHours = zone.rules.getOffset(LocalDateTime.of(date, LocalTime.NOON)).totalSeconds / 3600.0
        val today = raw(date, latitude, longitude, params)
        val tomorrow = raw(date.plusDays(1), latitude, longitude, params)

        fun toTime(hours: Double?): LocalTime? {
            if (hours == null || hours.isNaN()) return null
            val local = fixHour(hours + tzHours - longitude / 15.0)
            val minutes = Math.round(local * 60.0).toInt() % (24 * 60)
            return LocalTime.of(minutes / 60, minutes % 60)
        }

        val sunset = today.sunset
        val fajrNext = tomorrow.fajr
        val midnight = if (sunset != null && fajrNext != null) {
            // Both are expressed as hours of "solar" day; bring next Fajr past this sunset.
            var f = fajrNext + 24.0
            while (f < sunset) f += 24.0
            sunset + (f - sunset) / 2.0
        } else null

        return PrayerTimesResult(
            fajr = toTime(today.fajr), sunrise = toTime(today.sunrise), dhuhr = toTime(today.dhuhr),
            sunset = toTime(today.sunset), maghrib = toTime(today.maghrib), isha = toTime(today.isha),
            midnight = toTime(midnight),
            imsak = toTime(today.fajr?.minus(params.imsakMinutes / 60.0))
        )
    }

    private class Raw(
        val fajr: Double?, val sunrise: Double?, val dhuhr: Double,
        val sunset: Double?, val maghrib: Double?, val isha: Double?
    )

    private fun raw(date: LocalDate, lat: Double, lng: Double, params: PrayerParams): Raw {
        val jd = julian(date.year, date.monthValue, date.dayOfMonth) - lng / (15.0 * 24.0)

        fun midDay(t: Double): Double = fixHour(12.0 - sunPosition(jd + t).second)

        fun angleTime(angle: Double, t: Double, ccw: Boolean): Double? {
            val decl = sunPosition(jd + t).first
            val noon = midDay(t)
            val x = (-sin(rad(angle)) - sin(rad(decl)) * sin(rad(lat))) / (cos(rad(decl)) * cos(rad(lat)))
            if (x < -1.0 || x > 1.0) return null
            val dt = deg(acos(x)) / 15.0
            return noon + if (ccw) -dt else dt
        }

        // Two refinement passes, as in PrayTimes.org (day portions of the previous estimate).
        var fajr = 5.0; var sunrise = 6.0; var dhuhr = 12.0
        var sunset = 18.0; var maghrib = 18.0; var isha = 18.0
        var rFajr: Double? = null; var rSunrise: Double? = null; var rSunset: Double? = null
        var rMaghrib: Double? = null; var rIsha: Double? = null
        repeat(2) {
            rFajr = angleTime(params.fajrAngle, fajr / 24.0, true)
            rSunrise = angleTime(RISE_SET_ANGLE, sunrise / 24.0, true)
            dhuhr = midDay(dhuhr / 24.0)
            rSunset = angleTime(RISE_SET_ANGLE, sunset / 24.0, false)
            rMaghrib = angleTime(params.maghribAngle, maghrib / 24.0, false)
            rIsha = angleTime(params.ishaAngle, isha / 24.0, false)
            rFajr?.let { fajr = it }; rSunrise?.let { sunrise = it }
            rSunset?.let { sunset = it }; rMaghrib?.let { maghrib = it }; rIsha?.let { isha = it }
        }
        if (params.maghribDelayMinutes > 0) rMaghrib = rSunset?.plus(params.maghribDelayMinutes / 60.0)
        return Raw(rFajr, rSunrise, dhuhr, rSunset, rMaghrib, rIsha)
    }

    /** Returns (declination in degrees, equation of time in hours). */
    private fun sunPosition(jd: Double): Pair<Double, Double> {
        val d = jd - 2451544.5
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(rad(g)) + 0.020 * sin(rad(2 * g)))
        val e = 23.439 - 0.00000036 * d
        val ra = deg(atan2(cos(rad(e)) * sin(rad(l)), cos(rad(l)))) / 15.0
        val eqt = q / 15.0 - fixHour(ra)
        val decl = deg(asin(sin(rad(e)) * sin(rad(l))))
        return decl to eqt
    }

    private fun julian(year: Int, month: Int, day: Int): Double {
        var y = year; var m = month
        if (m <= 2) { y -= 1; m += 12 }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun rad(d: Double) = d * PI / 180.0
    private fun deg(r: Double) = r * 180.0 / PI
    private fun fixAngle(a: Double) = a - 360.0 * floor(a / 360.0)
    private fun fixHour(h: Double) = h - 24.0 * floor(h / 24.0)
}
