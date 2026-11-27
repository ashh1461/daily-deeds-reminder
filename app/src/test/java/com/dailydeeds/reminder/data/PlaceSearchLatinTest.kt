package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** City names for the English UI (v1.9.0); the Arabic results are covered by PlaceSearchTest and stay the default. */
class PlaceSearchLatinTest {
    private val arabicScript = Regex("[\u0600-\u06FF]")

    @Test
    fun theDefaultSearchIsUnchanged() {
        assertEquals(PlaceSearch.search("Beirut"), PlaceSearch.search("Beirut", latinNames = false))
    }

    @Test
    fun theEnglishUiGetsLatinCityNames() {
        val first = PlaceSearch.search("Beirut", latinNames = true).first()
        assertTrue(first.name, first.name.startsWith("Beirut"))
        assertFalse(first.name, arabicScript.containsMatchIn(first.name))
    }

    @Test
    fun locatedPlacesAreLabelledInTheAppLanguage() {
        val p = PlaceSearch.placeAt(33.89, 35.50, "My current location", "UTC", { "near $it" }, latinNames = true)
        assertTrue(p.name, p.name.startsWith("My current location (near "))
        assertFalse(p.name, arabicScript.containsMatchIn(p.name))
    }
}
