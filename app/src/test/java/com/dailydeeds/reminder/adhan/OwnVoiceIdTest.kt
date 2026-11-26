package com.dailydeeds.reminder.adhan

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Ids of the user's own imported voices must never collide with a downloadable voice or with each other. */
class OwnVoiceIdTest {

    private val hash = "a".repeat(64)

    private fun manifestWith(id: String) = """{"version":1,"voices":[{"id":"$id","nameAr":"أذان","reciter":"Test",
        "sizeBytes":1000000,"sha256":"$hash","url":"https://github.com/ashh1461/daily-deeds-reminder/releases/download/voices/x.ogg",
        "durationSec":120,"licence":"CC0","permission":"Written permission, 2026-10-01"}]}"""

    @Test
    fun aDownloadableVoiceCannotTakeTheOwnPrefix() {
        val r = VoiceManifest.parse(manifestWith("own_abc123"))
        assertTrue(r.voices.isEmpty())
        assertEquals(listOf("own_abc123: reserved id"), r.rejected)
    }

    @Test
    fun anOrdinaryIdThatOnlyContainsOwnIsStillAccepted() {
        assertEquals(listOf("town_adhan"), VoiceManifest.parse(manifestWith("town_adhan")).voices.map { it.id })
    }

    @Test
    fun aFreeOwnIdIsThePrefixAndTheTimeInBase36() {
        assertEquals("own_" + java.lang.Long.toString(1_764_000_000L, 36), VoiceIds.newOwnId(1_764_000_000L) { false })
    }

    @Test
    fun twoImportsInTheSameSecondGetDifferentIds() {
        val taken = HashSet<String>()
        val first = VoiceIds.newOwnId(1_764_000_000L) { it in taken }.also { taken += it }
        val second = VoiceIds.newOwnId(1_764_000_000L) { it in taken }.also { taken += it }
        val third = VoiceIds.newOwnId(1_764_000_000L) { it in taken }
        assertNotEquals(first, second)
        assertEquals("${first}_2", second)
        assertEquals("${first}_3", third)
        assertTrue(listOf(first, second, third).all { it.startsWith(VoiceIds.OWN_PREFIX) && it.matches(Regex("[a-z0-9_]+")) })
    }
}
