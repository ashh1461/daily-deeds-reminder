package com.dailydeeds.reminder.ui.screens

import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.model.MafatihCategoryType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** The Mafatih reader draws a long section line by line in a lazy list; nothing may be lost or reordered. */
class ReaderLinesTest {

    @Test
    fun linesJoinBackToTheExactText() {
        val text = "سطر أول\n\nسطر ثالث\n"
        assertEquals(text, readerLines(text).joinToString("\n"))
    }

    @Test
    fun theLongestSectionIsSplitIntoManyLinesAndKeptWhole() {
        val longest = MafatihRepository().getItemsByCategory(MafatihCategoryType.INDEX).maxBy { it.arabicText.length }
        val lines = readerLines(longest.arabicText)
        assertTrue("lines: ${lines.size}", lines.size > 20)
        assertEquals(longest.arabicText, lines.joinToString("\n"))
    }

    @Test
    fun blocksGroupLinesAndKeepTheExactText() {
        val longest = MafatihRepository().getItemsByCategory(MafatihCategoryType.INDEX).maxBy { it.arabicText.length }
        val blocks = readerBlocks(longest.arabicText)
        assertEquals(longest.arabicText, blocks.joinToString("\n"))
        assertTrue("blocks: ${blocks.size}", blocks.size in 5 until readerLines(longest.arabicText).size)
        // Every block but the last reaches the size; none is cut inside a line.
        assertTrue(blocks.dropLast(1).all { it.length >= READER_BLOCK_CHARS })
    }

    @Test
    fun shortTextAndEmptyLinesStayAsTheyAre() {
        assertEquals(listOf("a\n\nb\n"), readerBlocks("a\n\nb\n"))
        assertEquals(listOf(""), readerBlocks(""))
    }
}
