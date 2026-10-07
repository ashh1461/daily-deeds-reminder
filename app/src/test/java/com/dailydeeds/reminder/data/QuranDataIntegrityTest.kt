package com.dailydeeds.reminder.data

import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Guards against shipping placeholder or partial Quran text. */
class QuranDataIntegrityTest {

    private val repository = QuranRepository()

    @Test
    fun everySurahHasExactlyItsDeclaredAyahCount() {
        for (surah in repository.getAllSurahs()) {
            val ayahs = repository.getAyahsForSurah(surah.number)
            assertEquals("Surah ${surah.number}", surah.ayahCount, ayahs.size)
            assertEquals((1..surah.ayahCount).toList(), ayahs.map { it.ayahNumber })
        }
    }

    @Test
    fun totalAyahCountIs6236() {
        assertEquals(6236, repository.getAllSurahs().sumOf { repository.getAyahsForSurah(it.number).size })
    }

    @Test
    fun noPlaceholderOrBlankText() {
        for (surah in repository.getAllSurahs()) {
            for (ayah in repository.getAyahsForSurah(surah.number)) {
                assertTrue("${surah.number}:${ayah.ayahNumber} blank", ayah.textArabic.isNotBlank())
                assertFalse("${surah.number}:${ayah.ayahNumber} placeholder", ayah.textArabic.contains("آية رقم"))
            }
        }
    }

    @Test
    fun bundledResourceMatchesPinnedChecksum() {
        val bytes = QuranDataProvider::class.java.classLoader!!
            .getResourceAsStream(QuranDataProvider.RESOURCE_PATH)!!.use { it.readBytes() }
        val sha = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        assertEquals("0f36e155aa79d2f12fb28b6f573d4588545398eb95c37f39532b26db89236adf", sha)
    }

    @Test
    fun tafsirIsNeverInventedForAyatWithoutAnEntry() {
        assertEquals(null, repository.getTafsirForAyah(18, 5))
        assertEquals(null, repository.getTafsirForAyah(2, 1))
    }
}
