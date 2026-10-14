package com.dailydeeds.reminder.worship

import java.time.Instant
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Low-precision positions of the Sun and Moon (about 0.3 degrees). That is plenty for "is the Moon above
 * the horizon at this time", which decides whether a lunar eclipse can be seen from a place.
 */
object Astro {
    data class Equatorial(val raDeg: Double, val decDeg: Double, val distanceKm: Double)
    data class Ecliptic(val lonDeg: Double, val latDeg: Double, val distanceKm: Double)

    private const val J2000 = 2451545.0
    private const val EARTH_RADIUS_KM = 6378.14

    fun julianDay(instant: Instant): Double = instant.toEpochMilli() / 86_400_000.0 + 2440587.5

    private fun rad(deg: Double) = Math.toRadians(deg)
    private fun norm(deg: Double): Double = ((deg % 360.0) + 360.0) % 360.0

    fun sunEcliptic(jd: Double): Ecliptic {
        val d = jd - J2000
        val g = rad(norm(357.529 + 0.98560028 * d))
        val l = norm(280.459 + 0.98564736 * d)
        val lon = norm(l + 1.915 * sin(g) + 0.020 * sin(2 * g))
        val distanceAu = 1.00014 - 0.01671 * cos(g) - 0.00014 * cos(2 * g)
        return Ecliptic(lon, 0.0, distanceAu * 149_597_870.7)
    }

    fun moonEcliptic(jd: Double): Ecliptic {
        val d = jd - J2000
        val lp = norm(218.316 + 13.176396 * d)
        val mp = rad(norm(134.963 + 13.064993 * d))
        val dd = rad(norm(297.850 + 12.190749 * d))
        val f = rad(norm(93.272 + 13.229350 * d))
        val m = rad(norm(357.529 + 0.98560028 * d))
        val lon = lp + 6.289 * sin(mp) + 1.274 * sin(2 * dd - mp) + 0.658 * sin(2 * dd) + 0.214 * sin(2 * mp) -
            0.186 * sin(m) - 0.114 * sin(2 * f) + 0.059 * sin(2 * dd - 2 * mp) + 0.057 * sin(2 * dd - m - mp) +
            0.053 * sin(2 * dd + mp) + 0.046 * sin(2 * dd - m) + 0.041 * sin(mp - m) - 0.035 * sin(dd) -
            0.031 * sin(mp + m)
        val lat = 5.128 * sin(f) + 0.281 * sin(mp + f) + 0.278 * sin(mp - f) + 0.173 * sin(2 * dd - f) +
            0.055 * sin(2 * dd - mp + f) + 0.046 * sin(2 * dd - mp - f) + 0.033 * sin(2 * dd + f) + 0.017 * sin(2 * mp + f)
        val dist = 385001.0 - 20905.0 * cos(mp) - 3699.0 * cos(2 * dd - mp) - 2956.0 * cos(2 * dd) -
            570.0 * cos(2 * mp) + 246.0 * cos(2 * mp - 2 * dd) - 205.0 * cos(m - 2 * dd) -
            171.0 * cos(mp + 2 * dd) - 152.0 * cos(mp + m - 2 * dd)
        return Ecliptic(norm(lon), lat, dist)
    }

    fun toEquatorial(e: Ecliptic, jd: Double): Equatorial {
        val eps = rad(23.439 - 0.0000004 * (jd - J2000))
        val lon = rad(e.lonDeg)
        val lat = rad(e.latDeg)
        val ra = atan2(sin(lon) * cos(eps) - tan(lat) * sin(eps), cos(lon))
        val dec = asin(sin(lat) * cos(eps) + cos(lat) * sin(eps) * sin(lon))
        return Equatorial(norm(Math.toDegrees(ra)), Math.toDegrees(dec), e.distanceKm)
    }

    /** Angle between the Sun and the Moon as seen from the Earth's centre, in degrees (180 at full moon). */
    fun elongationDeg(jd: Double): Double {
        val s = sunEcliptic(jd)
        val m = moonEcliptic(jd)
        val c = cos(rad(m.latDeg)) * cos(rad(m.lonDeg - s.lonDeg))
        return Math.toDegrees(acos(c.coerceIn(-1.0, 1.0)))
    }

    private fun altitudeDeg(eq: Equatorial, jd: Double, latDeg: Double, lngDeg: Double): Double {
        val gmst = norm(280.46061837 + 360.98564736629 * (jd - J2000))
        val hourAngle = rad(gmst + lngDeg - eq.raDeg)
        val lat = rad(latDeg)
        val dec = rad(eq.decDeg)
        val sinAlt = sin(lat) * sin(dec) + cos(lat) * cos(dec) * cos(hourAngle)
        return Math.toDegrees(asin(sinAlt.coerceIn(-1.0, 1.0)))
    }

    /** Altitude of the Moon's centre above the horizon (degrees), corrected for parallax. */
    fun moonAltitudeDeg(instant: Instant, latDeg: Double, lngDeg: Double): Double {
        val jd = julianDay(instant)
        val eq = toEquatorial(moonEcliptic(jd), jd)
        val geocentric = altitudeDeg(eq, jd, latDeg, lngDeg)
        val parallax = Math.toDegrees(asin(EARTH_RADIUS_KM / eq.distanceKm))
        return geocentric - parallax * cos(rad(geocentric))
    }
}
