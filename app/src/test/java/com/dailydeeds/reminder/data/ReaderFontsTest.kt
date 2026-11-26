package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.ui.screens.MushafLayout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReaderFontsTest {

    /** Fix 3: fitted pages and the zoom buttons share one minimum. */
    @Test
    fun theMushafZoomAndFittedPagesShareOneMinimum() {
        assertEquals(MushafLayout.FIT_MIN_SP, ReaderFonts.MUSHAF.minSp, 0f)
    }

    /** Fix 3: «تصغير الخط» from the smallest fitted size, or any fitted size, never makes the text bigger. */
    @Test
    fun steppingDownNeverEnlargesTheMushafText() {
        var sp = MushafLayout.FIT_MIN_SP
        while (sp <= MushafLayout.FIT_MAX_SP) {
            assertTrue("from $sp", ReaderFonts.MUSHAF.smaller(sp) <= sp)
            sp += 0.5f
        }
        assertEquals(12f, ReaderFonts.MUSHAF.smaller(12f), 0f)
        assertEquals(15.5f, ReaderFonts.MUSHAF.smaller(17.5f), 0f)
    }

    @Test
    fun eachReaderStaysWithinItsRange() {
        assertEquals(40f, ReaderFonts.QURAN.larger(40f), 0f)
        assertEquals(16f, ReaderFonts.QURAN.smaller(16f), 0f)
        assertEquals(26f, ReaderFonts.QURAN.larger(24f), 0f)
        assertEquals(36f, ReaderFonts.MAFATIH.larger(36f), 0f)
        assertEquals(20f, ReaderFonts.MAFATIH.smaller(22f), 0f)
        assertEquals(40f, ReaderFonts.MUSHAF.larger(39.5f), 0f)
    }

    /** Fix 6: sizes are stored as tenths of an sp, so half steps survive a round trip. */
    @Test
    fun storedSizesRoundTripIncludingHalfSteps() {
        for (sp in listOf(12f, 17.5f, 20f, 24f, 39.5f)) {
            assertEquals(sp, ReaderFonts.fromStored(ReaderFonts.toStored(sp), ReaderFonts.MUSHAF), 0f)
        }
        assertEquals(175, ReaderFonts.toStored(17.5f))
    }

    /** Fix 6: a stored value from a hand-edited backup cannot set an unusable size. */
    @Test
    fun storedSizesAreClampedToTheReadersRange() {
        assertEquals(16f, ReaderFonts.fromStored(10, ReaderFonts.QURAN), 0f)
        assertEquals(40f, ReaderFonts.fromStored(9_999, ReaderFonts.QURAN), 0f)
        assertEquals(12f, ReaderFonts.fromStored(-50, ReaderFonts.MUSHAF), 0f)
        assertEquals(36f, ReaderFonts.fromStored(400, ReaderFonts.MAFATIH), 0f)
    }

    /** Fix 6: the stored keys are part of a backup. */
    @Test
    fun theReaderKeysAreBackedUp() {
        listOf(ReaderFonts.KEY_QURAN, ReaderFonts.KEY_MAFATIH, ReaderFonts.KEY_MUSHAF, ReaderFonts.KEY_MUSHAF_FIT)
            .forEach { assertTrue(it, BackupCodec.isAllowedKey(it)) }
    }

    /** Fix 7: the basmala's line height grows with its size instead of staying at 28 sp. */
    @Test
    fun theBasmalaLineHeightScalesWithItsSize() {
        for (sp in listOf(12f, 20f, 30f, 40f)) {
            assertTrue("at $sp", ReaderFonts.basmalaLineHeightSp(sp) > sp)
        }
        assertEquals(56f, ReaderFonts.basmalaLineHeightSp(40f), 0.001f)
        assertTrue(ReaderFonts.basmalaLineHeightSp(30f) > 28f)
    }
}
