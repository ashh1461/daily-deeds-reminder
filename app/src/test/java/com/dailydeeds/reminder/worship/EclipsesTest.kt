package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.model.Place
import java.io.File
import java.security.MessageDigest
import java.time.Instant
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EclipsesTest {
    private val beirut = Place("بيروت", 33.8938, 35.5018, "Asia/Beirut")
    private val detroit = Place("ديترويت", 42.3223, -83.1763, "America/Detroit")
    private val tokyo = Place("طوكيو", 35.6762, 139.6503, "Asia/Tokyo")

    private fun eclipseOn(day: String, kind: EclipseKind) =
        Eclipses.all.first { it.kind == kind && it.greatest.toString().startsWith(day) }

    @Test
    fun theBundledTableIsPinnedAndComplete() {
        val bytes = File("src/main/resources/${Eclipses.RESOURCE_PATH}").readBytes()
        val sha = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        assertEquals("ad6324fa6563aa92f3d13353bfac136179d576eba58c4546f409582c1f0127b0", sha)

        val all = Eclipses.all
        assertEquals(89, all.size)
        assertEquals(45, all.count { it.kind == EclipseKind.LUNAR })
        assertEquals(all.sortedBy { it.greatest }, all)
        assertTrue(all.first().greatest.toString().startsWith("2021"))
        assertTrue(all.last().greatest.toString().startsWith("2040"))
        assertTrue(all.all { it.regionArabic.isNotBlank() })
        // Every umbral lunar eclipse has an umbral duration; penumbral and solar ones do not.
        assertTrue(all.filter { it.kind == EclipseKind.LUNAR && it.type != EclipseType.PENUMBRAL }.all { it.umbralMinutes > 0 })
    }

    @Test
    fun knownEclipsesAreInTheTable() {
        val total = eclipseOn("2026-03-03", EclipseKind.LUNAR)
        assertEquals(EclipseType.TOTAL, total.type)
        assertEquals(1.151, total.magnitude, 0.0005)
        assertEquals(EclipseType.TOTAL, eclipseOn("2026-08-12", EclipseKind.SOLAR).type)
        assertEquals(EclipseType.PARTIAL, eclipseOn("2026-08-28", EclipseKind.LUNAR).type)
        assertFalse(eclipseOn("2027-02-20", EclipseKind.LUNAR).needsPrayer)
        assertTrue(total.needsPrayer)
    }

    @Test
    fun upcomingStartsAfterTheGivenInstant() {
        val next = Eclipses.upcoming(Instant.parse("2026-10-08T00:00:00Z"), 3)
        assertEquals(3, next.size)
        assertTrue(next[0].greatest.toString().startsWith("2027-02-06") && next[0].kind == EclipseKind.SOLAR)
        assertTrue(next[1].greatest.toString().startsWith("2027-02-20"))
        assertTrue(next.all { it.greatest.isAfter(Instant.parse("2026-10-08T00:00:00Z")) })
    }

    @Test
    fun ephemerisPutsTheMoonOppositeTheSunAtEveryLunarEclipse() {
        // At greatest lunar eclipse the Moon is on the Sun-Earth line: umbral eclipses within ~1 degree of
        // it, penumbral ones within ~1.6. This checks both the catalogue times and the Moon formula.
        for (e in Eclipses.all.filter { it.kind == EclipseKind.LUNAR }) {
            val off = abs(180.0 - Astro.elongationDeg(Astro.julianDay(e.greatest)))
            val limit = if (e.type == EclipseType.PENUMBRAL) 2.3 else 1.6
            assertTrue("${e.greatest} ${e.type}: ${"%.2f".format(off)} degrees from the shadow axis", off < limit)
        }
    }

    @Test
    fun ephemerisPutsTheMoonNextToTheSunAtEverySolarEclipse() {
        for (e in Eclipses.all.filter { it.kind == EclipseKind.SOLAR }) {
            val sep = Astro.elongationDeg(Astro.julianDay(e.greatest))
            assertTrue("${e.greatest}: ${"%.2f".format(sep)} degrees from the Sun", sep < 2.2)
        }
    }

    @Test
    fun lunarVisibilityDependsOnThePlace() {
        val sept2025 = eclipseOn("2025-09-07", EclipseKind.LUNAR) // 18:12 UT
        assertEquals(Visibility.VISIBLE, Eclipses.visibility(sept2025, beirut)) // 21:12 local, Moon high
        assertEquals(Visibility.BELOW_HORIZON, Eclipses.visibility(sept2025, detroit)) // 14:12 local

        val march2026 = eclipseOn("2026-03-03", EclipseKind.LUNAR) // 11:33 UT
        assertEquals(Visibility.VISIBLE, Eclipses.visibility(march2026, tokyo)) // 20:33 local
        assertEquals(Visibility.BELOW_HORIZON, Eclipses.visibility(march2026, beirut)) // 13:33 local
    }

    @Test
    fun solarVisibilityMustBeCheckedLocally() {
        assertEquals(Visibility.CHECK_LOCALLY, Eclipses.visibility(eclipseOn("2026-08-12", EclipseKind.SOLAR), beirut))
    }

    @Test
    fun moonIsAboveTheHorizonAboutHalfTheTime() {
        var up = 0
        val start = Instant.parse("2026-10-01T00:00:00Z")
        for (h in 0 until 24 * 28) {
            if (Astro.moonAltitudeDeg(start.plusSeconds(h * 3600L), beirut.latitude, beirut.longitude) > 0) up++
        }
        assertTrue("moon up $up of ${24 * 28} hours", up in 24 * 28 * 4 / 10..24 * 28 * 6 / 10)
    }
}
