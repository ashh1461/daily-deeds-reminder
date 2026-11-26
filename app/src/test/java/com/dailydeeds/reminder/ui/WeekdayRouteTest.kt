package com.dailydeeds.reminder.ui

import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.ui.screens.WeekdayRoute
import java.time.DayOfWeek
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Fix 2: a weekday favourite or search result opens on its own day, not today's. */
class WeekdayRouteTest {

    @Test
    fun theRouteCarriesTheDay() {
        assertEquals("duas?day=SUNDAY", WeekdayRoute.of(DayContentKind.DUA, DayOfWeek.SUNDAY))
        assertEquals("ziyarat?day=FRIDAY", WeekdayRoute.of(DayContentKind.ZIYARAT, DayOfWeek.FRIDAY))
    }

    @Test
    fun withoutADayTheRouteIsThePlainOneThatOpensOnToday() {
        assertEquals("duas", WeekdayRoute.of(DayContentKind.DUA))
        assertEquals("ziyarat", WeekdayRoute.of(DayContentKind.ZIYARAT, null))
    }

    @Test
    fun theRegisteredPatternMatchesTheBuiltRoutes() {
        assertEquals("duas?day={day}", WeekdayRoute.pattern(DayContentKind.DUA))
        assertEquals("ziyarat?day={day}", WeekdayRoute.pattern(DayContentKind.ZIYARAT))
    }

    @Test
    fun everyDayRoundTripsAndBadValuesFallBackToToday() {
        DayOfWeek.values().forEach { day ->
            val route = WeekdayRoute.of(DayContentKind.DUA, day)
            assertEquals(day, WeekdayRoute.parseDay(route.substringAfter("day=")))
        }
        assertNull(WeekdayRoute.parseDay(null))
        assertNull(WeekdayRoute.parseDay(""))
        assertNull(WeekdayRoute.parseDay("sunday"))
        assertNull(WeekdayRoute.parseDay("NOTADAY"))
    }
}
