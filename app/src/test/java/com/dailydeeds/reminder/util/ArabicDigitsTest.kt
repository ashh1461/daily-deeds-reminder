package com.dailydeeds.reminder.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArabicDigitsTest {

    @Test
    fun formatsWithArabicIndicDigits() {
        assertEquals("٦٠٤", ArabicDigits.format(604))
        assertEquals("١", ArabicDigits.format(1))
        assertEquals("٢٥٥", ArabicDigits.format(255))
    }

    @Test
    fun parsesLatinArabicAndPersianDigits() {
        assertEquals(604, ArabicDigits.parsePositive("604"))
        assertEquals(604, ArabicDigits.parsePositive("٦٠٤"))
        assertEquals(12, ArabicDigits.parsePositive("۱۲"))
        assertEquals(30, ArabicDigits.parsePositive(" ‏٣٠ "))
    }

    @Test
    fun rejectsAnythingThatIsNotAPositiveWholeNumber() {
        assertNull(ArabicDigits.parsePositive(""))
        assertNull(ArabicDigits.parsePositive("abc"))
        assertNull(ArabicDigits.parsePositive("0"))
        assertNull(ArabicDigits.parsePositive("-3"))
        assertNull(ArabicDigits.parsePositive("1.5"))
        assertNull(ArabicDigits.parsePositive("1234567"))
    }
}
