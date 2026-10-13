package com.dailydeeds.reminder.ui

import com.dailydeeds.reminder.ui.screens.roundCoordinate
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** The device location is stored (and backed up) with about 1 km precision only. */
class CoordinateRoundingTest {

    @Test
    fun coordinatesKeepTwoDecimals() {
        assertEquals(33.89, roundCoordinate(33.888_888), 1e-9)
        assertEquals(35.5, roundCoordinate(35.501_234), 1e-9)
        assertEquals(-118.24, roundCoordinate(-118.243_68), 1e-9)
        assertEquals(0.0, roundCoordinate(0.004_9), 1e-9)
        assertEquals(180.0, roundCoordinate(179.999), 1e-9)
    }

    @Test
    fun roundingMovesThePlaceByAtMostHalfAHundredthOfADegree() {
        var value = -90.0
        while (value <= 90.0) {
            val rounded = roundCoordinate(value)
            assertTrue("$value -> $rounded", abs(rounded - value) <= 0.005 + 1e-9)
            assertEquals(rounded, Math.round(rounded * 100) / 100.0, 0.0)
            value += 0.001_37
        }
    }
}
