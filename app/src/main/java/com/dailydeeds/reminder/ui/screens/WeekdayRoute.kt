package com.dailydeeds.reminder.ui.screens

import com.dailydeeds.reminder.model.DayContentKind
import java.time.DayOfWeek

/**
 * Routes of the weekday du'a and ziyarah screens. A favourite or a search result carries its day (`duas?day=SUNDAY`),
 * so it opens on that day's text instead of today's.
 */
internal object WeekdayRoute {
    const val DAY_ARG = "day"
    const val DUAS = "duas"
    const val ZIYARAT = "ziyarat"

    fun base(kind: DayContentKind): String = if (kind == DayContentKind.DUA) DUAS else ZIYARAT

    /** The pattern registered with the navigation graph. */
    fun pattern(kind: DayContentKind): String = "${base(kind)}?$DAY_ARG={$DAY_ARG}"

    fun of(kind: DayContentKind, day: DayOfWeek? = null): String =
        if (day == null) base(kind) else "${base(kind)}?$DAY_ARG=${day.name}"

    /** The day in a route argument; `null` (open on today) when it is missing or not a day name. */
    fun parseDay(raw: String?): DayOfWeek? =
        raw?.let { name -> DayOfWeek.values().firstOrNull { it.name == name } }
}
