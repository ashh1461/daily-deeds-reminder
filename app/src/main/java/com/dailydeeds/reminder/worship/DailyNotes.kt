package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.model.Place
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

data class DailyNote(val id: Int, val title: String, val body: String)

/** Which of the morning notes the user switched on. */
data class DailyNotesConfig(
    val occasions: Boolean = false,
    val khumsYear: KhumsYear? = null,
    val lunarEclipse: Boolean = false
) {
    val any: Boolean get() = occasions || khumsYear != null || lunarEclipse
}

/**
 * The notes shown by the single morning alarm: the occasion of the day, the khums-year reminder and a
 * lunar-eclipse heads-up. Pure so that it can be tested without Android.
 */
object DailyNotes {
    const val ID_OCCASION = 6000
    const val ID_KHUMS = 6001
    const val ID_ECLIPSE = 6002
    private val CLOCK = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

    fun build(
        now: ZonedDateTime,
        place: Place,
        dayOffset: Int,
        config: DailyNotesConfig,
        eclipses: List<Eclipse> = Eclipses.all
    ): List<DailyNote> {
        val today = now.toLocalDate()
        val notes = ArrayList<DailyNote>()

        if (config.occasions) {
            val occasions = ShiaCalendar.occasionsOn(today, dayOffset)
            if (occasions.isNotEmpty()) {
                notes += DailyNote(
                    ID_OCCASION, "مناسبة اليوم: ${ShiaCalendar.toHijri(today, dayOffset)}",
                    occasions.joinToString("\n") { it.title }
                )
            }
        }

        config.khumsYear?.let { year ->
            val next = Khums.nextAnniversary(today, year, dayOffset)
            val daysAway = next?.let { ChronoUnit.DAYS.between(today, it).toInt() }
            if (daysAway == 0) {
                notes += DailyNote(ID_KHUMS, "رأس السنة الخمسية", "اليوم موعد حساب الخمس. افتح حاسبة الخمس لتقدير ما عليك.")
            } else if (daysAway == 7) {
                notes += DailyNote(ID_KHUMS, "اقتراب رأس السنة الخمسية", "بقي أسبوع على موعد حساب الخمس.")
            }
        }

        if (config.lunarEclipse) {
            val horizon = now.toInstant().plus(Duration.ofHours(24))
            eclipses.firstOrNull { e ->
                e.kind == EclipseKind.LUNAR && e.needsPrayer &&
                    e.greatest.isAfter(now.toInstant()) && !e.greatest.isAfter(horizon) &&
                    Eclipses.visibility(e, place) != Visibility.BELOW_HORIZON
            }?.let { e ->
                val local = e.greatest.atZone(place.zone)
                notes += DailyNote(
                    ID_ECLIPSE, "خسوف القمر ${e.type.labelArabic}",
                    "ذروة الخسوف عند ${local.format(CLOCK)} بتوقيت ${place.name}. " +
                        "تجب صلاة الآيات على من يرى الخسوف في بلده."
                )
            }
        }
        return notes
    }
}
