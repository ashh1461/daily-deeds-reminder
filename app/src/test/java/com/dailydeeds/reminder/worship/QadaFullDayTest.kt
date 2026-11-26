package com.dailydeeds.reminder.worship

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/** «قضيتُ يوماً كاملاً»: one of each of the five prayers made up in one tap, undone as one batch. */
class QadaFullDayTest {
    private val day = 20_000L

    private fun owing(vararg pairs: Pair<QadaKind, Int>): QadaState =
        pairs.fold(QadaState()) { s, (k, n) -> s.add(k, n, day) }

    @Test
    fun marksOneOfEachPrayerAndLeavesTheFast() {
        val before = owing(QadaKind.FAJR to 3, QadaKind.DHUHR to 3, QadaKind.ASR to 3, QadaKind.MAGHRIB to 3, QadaKind.ISHA to 3, QadaKind.FAST to 2)
        val after = before.markFullDay(day + 1)
        QadaKind.PRAYERS.forEach { assertEquals(2, after.owed(it)) }
        assertEquals(2, after.owed(QadaKind.FAST))
        assertEquals(5, after.history.size - before.history.size)
        assertTrue(after.history.takeLast(5).all { it.delta == -1 && it.batch != 0L && it.epochDay == day + 1 })
        assertEquals(1, after.history.takeLast(5).map { it.batch }.distinct().size)
    }

    @Test
    fun oneUndoRestoresAllFive() {
        val before = owing(QadaKind.FAJR to 1, QadaKind.DHUHR to 1, QadaKind.ASR to 1, QadaKind.MAGHRIB to 1, QadaKind.ISHA to 1)
        val undone = before.markFullDay(day).undoLast()
        assertEquals(before.owed, undone.owed)
        assertEquals(before.history, undone.history)
    }

    @Test
    fun aFullDayAfterAddedDaysIsItsOwnBatch() {
        // The bulk add is one batch; the full day must not join it, or one undo would revert both.
        val added = QadaState().addDaysOfPrayers(10, day)
        val undone = added.markFullDay(day).undoLast()
        QadaKind.PRAYERS.forEach { assertEquals(10, undone.owed(it)) }
    }

    @Test
    fun onlyAvailableWhenEveryPrayerIsOwed() {
        val all = owing(QadaKind.FAJR to 1, QadaKind.DHUHR to 1, QadaKind.ASR to 1, QadaKind.MAGHRIB to 1, QadaKind.ISHA to 1)
        assertTrue(all.canMarkFullDay)
        val missingOne = owing(QadaKind.FAJR to 4, QadaKind.DHUHR to 4, QadaKind.ASR to 4, QadaKind.MAGHRIB to 4)
        assertFalse(missingOne.canMarkFullDay)
        assertFalse(QadaState().canMarkFullDay)
        // Called anyway (a stale tap): nothing changes, nothing goes below zero.
        assertSame(missingOne, missingOne.markFullDay(day))
    }

    @Test
    fun theBatchSurvivesSavingAndLoading() {
        val state = owing(QadaKind.FAJR to 2, QadaKind.DHUHR to 2, QadaKind.ASR to 2, QadaKind.MAGHRIB to 2, QadaKind.ISHA to 2)
            .markFullDay(day)
        val loaded = QadaState.decode(state.encode())
        assertEquals(state, loaded)
        QadaKind.PRAYERS.forEach { assertEquals(2, loaded.undoLast().owed(it)) }
    }
}
