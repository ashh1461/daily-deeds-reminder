package com.dailydeeds.reminder.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CoordinateInputTest {

    @Test
    fun parsesWesternArabicIndicAndPersianDigits() {
        assertEquals(33.27, CoordinateInput.parse("33.27")!!, 1e-9)
        assertEquals(33.27, CoordinateInput.parse("٣٣٫٢٧")!!, 1e-9)
        assertEquals(33.27, CoordinateInput.parse("۳۳٫۲۷")!!, 1e-9)
        assertEquals(33.27, CoordinateInput.parse("33,27")!!, 1e-9)
        assertEquals(35.0, CoordinateInput.parse(" 35° ")!!, 1e-9)
        assertEquals(33.27, CoordinateInput.parse("٣٣،٢٧")!!, 1e-9)
    }

    @Test
    fun ignoresInvisibleDirectionMarks() {
        assertEquals(-33.27, CoordinateInput.parse("‏-33.27‎")!!, 1e-9)
        assertEquals(33.27, CoordinateInput.parse("؜٣٣٫٢٧")!!, 1e-9)
    }

    @Test
    fun parsesNegativeValuesWithAnyMinusSign() {
        assertEquals(-118.24, CoordinateInput.parse("-118.24")!!, 1e-9)
        assertEquals(-118.24, CoordinateInput.parse("−118.24")!!, 1e-9)
        assertEquals(-0.5, CoordinateInput.parse("-.5")!!, 1e-9)
        assertEquals(12.0, CoordinateInput.parse("+12")!!, 1e-9)
    }

    @Test
    fun rejectsMalformedInput() {
        assertNull(CoordinateInput.parse(""))
        assertNull(CoordinateInput.parse("-"))
        assertNull(CoordinateInput.parse("1.2.3"))
        assertNull(CoordinateInput.parse("33a"))
        assertNull(CoordinateInput.parse("3-3"))
        assertNull(CoordinateInput.parse("°33"))
    }

    @Test
    fun reportsWhyInputIsInvalid() {
        assertEquals(CoordinateInput.Result.Empty, CoordinateInput.latitude("  "))
        assertEquals(CoordinateInput.Result.Valid(33.27), CoordinateInput.latitude("٣٣٫٢٧"))
        val outOfRange = CoordinateInput.latitude("95")
        assertEquals(CoordinateInput.Result.Invalid(CoordinateInput.Axis.LATITUDE, CoordinateInput.Reason.OUT_OF_RANGE), outOfRange)
        assertEquals(90.0, CoordinateInput.Axis.LATITUDE.limit, 0.0)
        assertEquals(
            CoordinateInput.Result.Invalid(CoordinateInput.Axis.LONGITUDE, CoordinateInput.Reason.NOT_A_NUMBER),
            CoordinateInput.longitude("abc")
        )
        assertEquals(CoordinateInput.Result.Valid(-180.0), CoordinateInput.longitude("-180"))
        assertTrue(CoordinateInput.longitude("180.01") is CoordinateInput.Result.Invalid)
    }
}
