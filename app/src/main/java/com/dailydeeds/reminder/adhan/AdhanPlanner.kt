package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.util.PrayerSchedule
import java.time.ZonedDateTime

enum class AlarmKind { PRE, MAIN }

/** One alarm to set: when it fires ([triggerAt]) and the prayer time it is about ([prayerAt]). */
data class PlannedAlarm(
    val prayer: Prayer,
    val kind: AlarmKind,
    val triggerAt: ZonedDateTime,
    val prayerAt: ZonedDateTime
)

/**
 * Pure planning of the single next alarm across all prayers. The Android glue (AlarmManager, receivers)
 * stays thin so that the interesting rules are unit-tested.
 */
object AdhanPlanner {

    fun next(
        now: ZonedDateTime,
        place: Place,
        context: PrayerContext,
        configs: Map<Prayer, PrayerAlarmConfig>
    ): PlannedAlarm? {
        val zoned = now.withZoneSameInstant(place.zone)
        var best: PlannedAlarm? = null
        for (dayOffset in 0L..2L) {
            val date = zoned.toLocalDate().plusDays(dayOffset)
            val times = PrayerSchedule.timesFor(date, place, context)
            val perPrayer = listOf(
                Prayer.FAJR to times.fajr, Prayer.DHUHR to times.dhuhr,
                Prayer.MAGHRIB to times.maghrib, Prayer.ISHA to times.isha
            )
            for ((prayer, local) in perPrayer) {
                val config = configs[prayer] ?: continue
                if (config.mode == AdhanMode.OFF || local == null) continue
                val prayerAt = date.atTime(local).atZone(place.zone)
                val candidates = buildList {
                    add(PlannedAlarm(prayer, AlarmKind.MAIN, prayerAt, prayerAt))
                    if (config.preMinutes > 0 && config.mode != AdhanMode.SILENT) {
                        add(PlannedAlarm(prayer, AlarmKind.PRE, prayerAt.minusMinutes(config.preMinutes.toLong()), prayerAt))
                    }
                }
                for (c in candidates) {
                    if (c.triggerAt.isAfter(zoned) && (best == null || c.triggerAt.isBefore(best!!.triggerAt))) best = c
                }
            }
            // Later days can only be later than anything found today.
            if (best != null) break
        }
        return best
    }
}
