package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.model.Place
import java.time.Instant

enum class EclipseKind { LUNAR, SOLAR }

enum class EclipseType(val labelArabic: String) {
    TOTAL("كلي"),
    PARTIAL("جزئي"),
    PENUMBRAL("شبه ظلّي"),
    ANNULAR("حلقي"),
    HYBRID("هجين")
}

/** One eclipse from the NASA catalogue: [greatest] is the instant of greatest eclipse in UT. */
data class Eclipse(
    val kind: EclipseKind,
    val greatest: Instant,
    val type: EclipseType,
    val magnitude: Double,
    /** Duration of the umbral phase in minutes (lunar eclipses only, 0 otherwise). */
    val umbralMinutes: Int,
    val regionArabic: String
) {
    /** A penumbral eclipse cannot be seen with the eye, so the prayer of signs is not due for it. */
    val needsPrayer: Boolean get() = type != EclipseType.PENUMBRAL
}

enum class Visibility(val labelArabic: String) {
    VISIBLE("القمر فوق الأفق في مدينتك"),
    NEAR_HORIZON("القمر قريب من الأفق في مدينتك"),
    BELOW_HORIZON("القمر تحت الأفق في مدينتك وقت ذروة الخسوف"),
    CHECK_LOCALLY("تحقق من الرؤية المحلية")
}

/**
 * Eclipses from the bundled NASA/GSFC five-millennium catalogue decade tables (F. Espenak, public domain),
 * 2021-2040. Lunar eclipse visibility is computed for a place; solar eclipse visibility is not (it needs
 * the Besselian elements), so those entries show the regions and ask the user to verify locally.
 */
object Eclipses {
    const val RESOURCE_PATH = "worship/eclipses.txt"

    /** Last year the bundled table covers. */
    const val LAST_YEAR = 2040

    val all: List<Eclipse> by lazy {
        val stream = Eclipses::class.java.classLoader?.getResourceAsStream(RESOURCE_PATH)
            ?: error("Missing bundled resource: $RESOURCE_PATH")
        parse(stream.bufferedReader(Charsets.UTF_8).readText())
    }

    fun parse(text: String): List<Eclipse> = text.lineSequence()
        .filter { it.isNotBlank() }
        .map { line ->
            val f = line.split('|', limit = 6)
            Eclipse(
                kind = if (f[0] == "L") EclipseKind.LUNAR else EclipseKind.SOLAR,
                greatest = Instant.parse(f[1]),
                type = EclipseType.valueOf(f[2]),
                magnitude = f[3].toDouble(),
                umbralMinutes = f[4].toInt(),
                regionArabic = f[5]
            )
        }
        .sortedBy { it.greatest }
        .toList()

    fun upcoming(from: Instant, limit: Int = 12, source: List<Eclipse> = all): List<Eclipse> =
        source.filter { !it.greatest.isBefore(from) }.take(limit)

    /** Whether the Moon is up from [place] at the moment of greatest lunar eclipse. */
    fun visibility(eclipse: Eclipse, place: Place): Visibility {
        if (eclipse.kind == EclipseKind.SOLAR) return Visibility.CHECK_LOCALLY
        val altitude = Astro.moonAltitudeDeg(eclipse.greatest, place.latitude, place.longitude)
        return when {
            altitude > 5.0 -> Visibility.VISIBLE
            altitude > -2.0 -> Visibility.NEAR_HORIZON
            else -> Visibility.BELOW_HORIZON
        }
    }
}
