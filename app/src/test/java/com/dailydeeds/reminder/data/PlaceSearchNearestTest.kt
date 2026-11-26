package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaceSearchNearestTest {

    @Test
    fun distanceMatchesKnownValue() {
        // Beirut to Baghdad is about 830 km as the crow flies.
        val km = PlaceSearch.distanceKm(33.89, 35.50, 33.31, 44.37)
        assertTrue("got $km", km in 800.0..860.0)
        assertEquals(0.0, PlaceSearch.distanceKm(10.0, 20.0, 10.0, 20.0), 1e-9)
    }

    @Test
    fun nearestCityGivesTheLocalTimeZone() {
        assertEquals("Asia/Beirut", PlaceSearch.nearest(33.27, 35.20)?.zoneId)
        assertEquals("Asia/Baghdad", PlaceSearch.nearest(32.0, 44.33)?.zoneId)
        assertEquals("Europe/London", PlaceSearch.nearest(51.5, -0.12)?.zoneId)
        assertEquals("Asia/Tehran", PlaceSearch.nearest(34.64, 50.88)?.zoneId)
    }

    @Test
    fun nothingNearbyInTheOpenOcean() {
        assertNull(PlaceSearch.nearest(0.0, -30.0))
    }

    @Test
    fun placeAtLabelsAndZonesCoordinates() {
        val najaf = PlaceSearch.placeAt(32.0, 44.33, "إحداثيات مخصصة", "Asia/Beirut")
        assertEquals("Asia/Baghdad", najaf.zoneId)
        assertTrue(najaf.name, najaf.name.startsWith("إحداثيات مخصصة (قرب "))
        val sea = PlaceSearch.placeAt(0.0, -30.0, "إحداثيات مخصصة", "Asia/Beirut")
        assertEquals("Asia/Beirut", sea.zoneId)
        assertEquals("إحداثيات مخصصة", sea.name)
        assertNotNull(PlaceSearch.nearest(33.27, 35.20)?.name)
    }
}
