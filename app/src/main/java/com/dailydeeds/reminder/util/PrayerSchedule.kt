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

object PrayerSchedule {

    fun timesFor(date: LocalDate, place: Place): PrayerTimesResult =
        PrayerTimes.compute(date, place.latitude, place.longitude, place.zone)

    /** The first alarmable prayer strictly after [now], looking at today and the next two days. */
    fun next(now: ZonedDateTime, place: Place): PrayerInstant? {
        val zoned = now.withZoneSameInstant(place.zone)
        for (offset in 0L..2L) {
            val date = zoned.toLocalDate().plusDays(offset)
            val t = timesFor(date, place)
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
