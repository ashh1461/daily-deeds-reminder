package com.dailydeeds.reminder.adhan

import java.io.File
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** The adhan recordings shipped in the APK: pinned files, honest durations, and ids nothing else can take. */
class BundledVoicesTest {

    // Pinned here as well as in BundledVoices, so a swapped file or an edited constant fails on its own.
    private val pinned = mapOf(
        "karbala_imam_hussain" to Pair(2_857_049L, "3689a380cb2880806b58ba251d544ae916a874233000a95de5298c2597d7245b"),
        "mashhad_imam_reza" to Pair(2_802_699L, "e59472508dc5d08e2e97982b83ace228f7ff5db3c5a6cebce78a5a77224da843")
    )

    private fun assetOf(v: BundledVoice) = File("src/main/assets/${v.assetPath}")

    private fun indexOf(bytes: ByteArray, text: String): Int {
        val needle = text.toByteArray(Charsets.US_ASCII)
        outer@ for (i in 0..bytes.size - needle.size) {
            for (j in needle.indices) if (bytes[i + j] != needle[j]) continue@outer
            return i
        }
        return -1
    }

    /**
     * Walks the Ogg pages to the last one; its granule position (little-endian 64-bit at page offset 6) counts
     * 48 kHz samples, and the Opus pre-skip from the OpusHead packet is not played.
     */
    private fun opusSeconds(bytes: ByteArray): Double {
        fun u8(i: Int): Int = bytes[i].toInt() and 0xFF
        val head = indexOf(bytes, "OpusHead")
        assertTrue("no OpusHead packet", head >= 0)
        val preSkip = u8(head + 10) or (u8(head + 11) shl 8)
        var pos = 0
        var granule = 0L
        while (pos < bytes.size) {
            assertEquals("page at $pos", "OggS", String(bytes, pos, 4, Charsets.US_ASCII))
            granule = 0L
            for (i in 0 until 8) granule = granule or (u8(pos + 6 + i).toLong() shl (8 * i))
            val segments = u8(pos + 26)
            var body = 0
            for (s in 0 until segments) body += u8(pos + 27 + s)
            pos += 27 + segments + body
        }
        assertEquals("the last page must end at the end of the file", bytes.size, pos)
        return (granule - preSkip) / 48_000.0
    }

    @Test
    fun everyAssetExistsWithItsPinnedSizeAndChecksum() {
        assertEquals(pinned.keys, BundledVoices.all.map { it.id }.toSet())
        for (v in BundledVoices.all) {
            val (size, sha) = pinned.getValue(v.id)
            assertEquals(v.id, size, v.bytes)
            assertEquals(v.id, sha, v.sha256)
            val file = assetOf(v)
            assertTrue("${file.path} must exist", file.isFile)
            val bytes = file.readBytes()
            assertEquals(v.id, size, bytes.size.toLong())
            assertEquals(v.id, sha, MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })
        }
    }

    @Test
    fun assetsAreOggOpus() {
        for (v in BundledVoices.all) {
            val bytes = assetOf(v).readBytes()
            assertEquals(v.id, "OggS", String(bytes, 0, 4, Charsets.US_ASCII))
            assertTrue(v.id, indexOf(bytes, "OpusHead") >= 0)
            assertTrue(v.id, v.assetPath.startsWith("adhan/") && v.assetPath.endsWith(".ogg"))
        }
    }

    @Test
    fun declaredSecondsCoverTheRealDurationWithinThreeSeconds() {
        for (v in BundledVoices.all) {
            val real = opusSeconds(assetOf(v).readBytes())
            // The playback time limit is derived from seconds, so it must never be shorter than the audio.
            assertTrue("${v.id}: ${v.seconds} s is shorter than $real s", v.seconds >= real)
            assertTrue("${v.id}: ${v.seconds} s is more than 3 s over $real s", v.seconds - real <= 3.0)
        }
    }

    @Test
    fun idsAreValidUniqueAndDistinctFromTheSpecialIds() {
        val ids = BundledVoices.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        for (v in BundledVoices.all) {
            assertTrue(v.id, v.id.matches(Regex("[A-Za-z0-9_]{1,48}")))
            assertFalse(v.id, v.id == VoiceIds.DEFAULT || v.id == VoiceIds.SYSTEM_TONE)
            assertTrue(v.id, v.nameAr.isNotBlank() && v.nameEn.isNotBlank())
        }
        assertNotNull(BundledVoices.byId(BundledVoices.DEFAULT_ID))
    }

    @Test
    fun resolveMapsTheDefaultAndBundledIdsOnly() {
        assertEquals(BundledVoices.DEFAULT_ID, BundledVoices.resolve(VoiceIds.DEFAULT)?.id)
        for (v in BundledVoices.all) assertEquals(v, BundledVoices.resolve(v.id))
        assertNull(BundledVoices.resolve(VoiceIds.SYSTEM_TONE))
        assertNull(BundledVoices.resolve("own_k3j2"))
        assertNull(BundledVoices.resolve(""))
    }

    @Test
    fun alarmConfigsKeepABundledVoiceAndThePhoneTone() {
        for (id in BundledVoices.all.map { it.id } + VoiceIds.SYSTEM_TONE) {
            val c = PrayerAlarmConfig(AdhanMode.ADHAN, id, 5, 0)
            assertEquals(c, PrayerAlarmConfig.decode(c.encode()))
        }
        // A fresh config, like one saved before audio was bundled, means the app default: the bundled adhan.
        assertEquals(VoiceIds.DEFAULT, PrayerAlarmConfig().voiceId)
    }

    @Test
    fun aDownloadableVoiceCannotTakeAReservedOrBundledId() {
        fun entry(id: String) = """{"id":"$id","nameAr":"أذان تجريبي","reciter":"Test","sizeBytes":1000000,
            "sha256":"${"a".repeat(64)}","url":"https://github.com/ashh1461/daily-deeds-reminder/releases/download/voices/$id.ogg",
            "durationSec":120,"licence":"CC0","permission":"Written permission from the reciter, 2026-10-01"}"""
        val taken = BundledVoices.all.map { it.id } + VoiceIds.DEFAULT + VoiceIds.SYSTEM_TONE
        val r = VoiceManifest.parse("""{"version":1,"voices":[${(taken + "free_voice").joinToString(",") { entry(it) }}]}""")
        assertEquals(listOf("free_voice"), r.voices.map { it.id })
        assertEquals(taken.size, r.rejected.size)
        assertTrue(r.rejected.toString(), r.rejected.all { it.endsWith("reserved id") })
    }
}
