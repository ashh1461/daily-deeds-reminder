package com.dailydeeds.reminder.util

import com.dailydeeds.reminder.model.Place
import java.time.LocalDate
import java.time.ZonedDateTime

/** Prayers a reminder can be set for. Sunrise and midnight are shown but never alarmed. */
enum class Prayer(val id: Int, val nameArabic: String) {
    FAJR(1, "الفجر"),
    DHUHR(2, "الظهر"),
    MAGHRIB(3, "المغرب"),
    ISHA(4, "العشاء");

    companion object {
        fun fromId(id: Int): Prayer? = values().find { it.id == id }
    }
}

data class PrayerInstant(val prayer: Prayer, val time: ZonedDateTime)

/** Calculation parameters plus the user's manual per-prayer adjustments (minutes). */
data class PrayerContext(
    val params: PrayerParams = PrayerParams.LEVA,
    val offsets: Map<Prayer, Int> = emptyMap()
)

object PrayerSchedule {

    fun timesFor(date: LocalDate, place: Place, context: PrayerContext = PrayerContext()): PrayerTimesResult {
        val t = PrayerTimes.compute(date, place.latitude, place.longitude, place.zone, context.params)
        val o = context.offsets
        if (o.values.all { it == 0 }) return t
        fun shift(time: java.time.LocalTime?, prayer: Prayer) = time?.plusMinutes((o[prayer] ?: 0).toLong())
        return t.copy(
            fajr = shift(t.fajr, Prayer.FAJR),
            dhuhr = shift(t.dhuhr, Prayer.DHUHR),
            maghrib = shift(t.maghrib, Prayer.MAGHRIB),
            isha = shift(t.isha, Prayer.ISHA)
        )
    }

    /** The first alarmable prayer strictly after [now], looking at today and the next two days. */
    fun next(now: ZonedDateTime, place: Place, context: PrayerContext = PrayerContext()): PrayerInstant? {
        val zoned = now.withZoneSameInstant(place.zone)
        for (offset in 0L..2L) {
            val date = zoned.toLocalDate().plusDays(offset)
            val t = timesFor(date, place, context)
            val candidates = listOf(
                Prayer.FAJR to t.fajr, Prayer.DHUHR to t.dhuhr,
                Prayer.MAGHRIB to t.maghrib, Prayer.ISHA to t.isha
            )
            for ((prayer, local) in candidates) {
                if (local == null) continue
                val instant = date.atTime(local).atZone(place.zone)
                if (instant.isAfter(zoned)) return PrayerInstant(prayer, instant)
            }
        }
        return null
    }
}
