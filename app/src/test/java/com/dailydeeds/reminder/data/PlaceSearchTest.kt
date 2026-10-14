package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceSearchTest {

    @Test
    fun theBundledListIsLarge() {
        assertTrue(PlaceSearch.size > 30_000)
    }

    @Test
    fun findsCitiesByLatinName() {
        val karbala = PlaceSearch.search("karbala")
        assertTrue(karbala.isNotEmpty())
        val p = karbala.first()
        assertEquals(32.6, p.latitude, 0.3)
        assertEquals(44.0, p.longitude, 0.3)
        assertEquals("Asia/Baghdad", p.zoneId)
    }

    @Test
    fun findsCitiesByArabicNameWithOrWithoutTheArticle() {
        assertTrue(PlaceSearch.search("النجف").any { it.zoneId == "Asia/Baghdad" && it.latitude in 31.9..32.2 })
        assertTrue(PlaceSearch.search("نجف").any { it.zoneId == "Asia/Baghdad" && it.latitude in 31.9..32.2 })
        assertTrue(PlaceSearch.search("بيروت").any { it.zoneId == "Asia/Beirut" })
        assertTrue(PlaceSearch.search("قم").any { it.zoneId == "Asia/Tehran" && it.latitude in 34.5..34.8 })
    }

    @Test
    fun shortOrHostileQueriesAreSafe() {
        assertTrue(PlaceSearch.search("").isEmpty())
        assertTrue(PlaceSearch.search("a").isEmpty())
        assertTrue(PlaceSearch.search("x".repeat(100_000)).isEmpty())
        assertTrue(PlaceSearch.search("%%%%").isEmpty())
    }

    @Test
    fun resultsAreBoundedAndValid() {
        val r = PlaceSearch.search("san", limit = 30)
        assertTrue(r.size <= 30)
        assertTrue(r.all { it.latitude in -90.0..90.0 && it.longitude in -180.0..180.0 })
    }
}
