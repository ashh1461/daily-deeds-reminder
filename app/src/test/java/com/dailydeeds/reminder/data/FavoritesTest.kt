package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DayContentKind
import java.time.DayOfWeek
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesTest {

    @Test
    fun roundTripsEveryKeyType() {
        val keys = listOf(
            FavoriteKey.Ayah(2, 255),
            FavoriteKey.Mafatih("mj_16"),
            FavoriteKey.Weekday(DayContentKind.ZIYARAT, DayOfWeek.FRIDAY)
        )
        assertEquals(keys, FavoritesList.parse(FavoritesList.serialize(keys)))
    }

    @Test
    fun malformedEntriesAreIgnored() {
        assertNull(FavoriteKey.decode("ayah:x:y"))
        assertNull(FavoriteKey.decode("ayah:1"))
        assertNull(FavoriteKey.decode("weekday:NOPE:MONDAY"))
        assertNull(FavoriteKey.decode("mafatih:"))
        assertNull(FavoriteKey.decode("evil:../../etc"))
        val parsed = FavoritesList.parse("ayah:1:1\n???\nweekday:DUA:NOTADAY\nmafatih:dua_kumayl")
        assertEquals(listOf<FavoriteKey>(FavoriteKey.Ayah(1, 1), FavoriteKey.Mafatih("dua_kumayl")), parsed)
    }

    @Test
    fun toggleAddsNewestFirstAndRemoves() {
        val a = FavoriteKey.Ayah(1, 1)
        val b = FavoriteKey.Mafatih("x")
        var list = FavoritesList.toggle(emptyList(), a)
        list = FavoritesList.toggle(list, b)
        assertEquals(listOf(b, a), list)
        list = FavoritesList.toggle(list, a)
        assertEquals(listOf<FavoriteKey>(b), list)
        assertFalse(a in list)
        assertTrue(FavoritesList.parse(null).isEmpty())
    }
}
