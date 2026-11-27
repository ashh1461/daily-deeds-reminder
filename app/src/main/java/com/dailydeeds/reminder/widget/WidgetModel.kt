package com.dailydeeds.reminder.widget

import com.dailydeeds.reminder.calendar.ShiaCalendar
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.util.PrayerSchedule
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.calendar.HijriDay
import com.dailydeeds.reminder.calendar.Occasion

/** Text shown on the home-screen widget. */
data class WidgetModel(
    val placeName: String,
    val hijriDate: String,
    val nextPrayer: String,
    val nextTime: String,
    /** When the widget content stops being right: the next prayer time (or null if none is known). */
    val validUntil: ZonedDateTime?,
    val occasion: String?,
    /** The next prayer itself, so the widget can name it in the app language. */
    val prayer: Prayer? = null,
    /** The data behind the texts above, so the widget can name them in the app language. */
    val hijriDay: HijriDay? = null,
    val occasionItem: Occasion? = null,
    val placeItem: Place? = null
) {
    companion object {
        private val CLOCK = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

        fun build(now: ZonedDateTime, place: Place, context: PrayerContext, dayOffset: Int): WidgetModel {
            val local = now.withZoneSameInstant(place.zone)
            val next = PrayerSchedule.next(local, place, context)
            val today = local.toLocalDate()
            return WidgetModel(
                placeName = place.name,
                hijriDate = ShiaCalendar.toHijri(today, dayOffset).toString(),
                nextPrayer = next?.prayer?.nameArabic ?: "—",
                nextTime = next?.time?.format(CLOCK) ?: "--:--",
                validUntil = next?.time,
                occasion = ShiaCalendar.occasionsOn(today, dayOffset).firstOrNull()?.displayTitle,
                prayer = next?.prayer,
                hijriDay = ShiaCalendar.toHijri(today, dayOffset),
                occasionItem = ShiaCalendar.occasionsOn(today, dayOffset).firstOrNull(),
                placeItem = place
            )
        }
    }
}
