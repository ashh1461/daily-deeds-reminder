package com.dailydeeds.reminder.util

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/** Great-circle qibla bearing towards the Kaaba, in degrees clockwise from true north (0..360). */
object Qibla {
    const val KAABA_LAT = 21.422487
    const val KAABA_LNG = 39.826206

    fun bearing(latitude: Double, longitude: Double): Double {
        val phi1 = latitude * PI / 180.0
        val phi2 = KAABA_LAT * PI / 180.0
        val dLambda = (KAABA_LNG - longitude) * PI / 180.0
        val y = sin(dLambda)
        val x = cos(phi1) * tan(phi2) - sin(phi1) * cos(dLambda)
        val deg = atan2(y, x) * 180.0 / PI
        return (deg % 360.0 + 360.0) % 360.0
    }
}
