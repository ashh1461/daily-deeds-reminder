package com.dailydeeds.reminder.ui

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import com.dailydeeds.reminder.ui.screens.ListRow
import com.dailydeeds.reminder.ui.screens.buildRows
import com.dailydeeds.reminder.util.TimeFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RowsAndTimeTest {
    private fun item(id: String, group: String) =
        MafatihItem(id, MafatihCategoryType.ADIYAH, group, "t$id", "text")

    @Test
    fun headersAppearWhenTheChapterChanges() {
        val rows = buildRows(listOf(item("1", "A"), item("2", "A"), item("3", "B")))
        assertEquals(5, rows.size)
        assertTrue(rows[0] is ListRow.Header && (rows[0] as ListRow.Header).text == "A")
        assertTrue(rows[3] is ListRow.Header && (rows[3] as ListRow.Header).text == "B")
        assertEquals(rows.size, rows.map { it.key }.toSet().size)
    }

    @Test
    fun singleChapterListsGetNoHeader() {
        val rows = buildRows(listOf(item("1", "A"), item("2", "A")))
        assertTrue(rows.all { it is ListRow.Entry })
        assertTrue(buildRows(emptyList()).isEmpty())
    }

    @Test
    fun countdownFormatsHoursAndMinutesAndNeverGoesNegative() {
        val zone = ZoneId.of("Asia/Beirut")
        val now = ZonedDateTime.of(2026, 10, 15, 10, 0, 0, 0, zone)
        assertEquals("بعد 2 ساعة و30 دقيقة", TimeFormat.countdown(now, now.plusMinutes(150)))
        assertEquals("بعد 3 ساعة", TimeFormat.countdown(now, now.plusHours(3)))
        assertEquals("بعد 45 دقيقة", TimeFormat.countdown(now, now.plusMinutes(45)))
        assertEquals("الآن", TimeFormat.countdown(now, now.plusSeconds(20)))
        assertEquals("الآن", TimeFormat.countdown(now, now.minusMinutes(5)))
    }
}
