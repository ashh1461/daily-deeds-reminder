package com.dailydeeds.reminder.adhan

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** The rights gate for downloadable voices, the URL allow-list, and the app-wide permission/network policy. */
class VoiceAndPolicyTest {

    private val hash = "a".repeat(64)

    private fun voice(
        id: String = "test_voice",
        licence: String = "CC0",
        permission: String = "Written permission from the reciter, 2026-10-01",
        url: String = "https://github.com/ashh1461/daily-deeds-reminder/releases/download/voices/test_voice.ogg",
        sha: String = hash,
        extra: String = ""
    ) = """{"id":"$id","nameAr":"أذان تجريبي","reciter":"Test","sizeBytes":1000000,"sha256":"$sha","url":"$url",
        "durationSec":120,"licence":"$licence","permission":"$permission"$extra}"""

    private fun manifest(vararg voices: String) = """{"version":1,"voices":[${voices.joinToString(",")}]}"""

    @Test
    fun aCompleteVoiceIsAccepted() {
        val r = VoiceManifest.parse(manifest(voice()))
        assertEquals(1, r.voices.size)
        assertTrue(r.rejected.isEmpty())
    }

    @Test
    fun voicesWithoutLicenceOrPermissionAreRejected() {
        val r = VoiceManifest.parse(manifest(voice(id = "a1", licence = ""), voice(id = "a2", permission = " "), voice(id = "a3")))
        assertEquals(listOf("a3"), r.voices.map { it.id })
        assertEquals(2, r.rejected.size)
    }

    @Test
    fun badUrlsHashesAndIdsAreRejected() {
        val r = VoiceManifest.parse(
            manifest(
                voice(id = "b1", url = "http://github.com/x.ogg"),
                voice(id = "b2", url = "https://evil.example.com/x.ogg"),
                voice(id = "b3", sha = "xyz"),
                voice(id = "Bad Id"),
                voice(id = "default")
            )
        )
        assertTrue(r.voices.isEmpty())
        assertEquals(5, r.rejected.size)
    }

    @Test
    fun placeholdersAwaitingRightsAreNeitherListedNorRejected() {
        val r = VoiceManifest.parse(manifest("""{"id":"x","nameAr":"أ","status":"awaiting-rights"}"""))
        assertTrue(r.voices.isEmpty())
        assertTrue(r.rejected.isEmpty())
    }

    @Test
    fun duplicateIdsAndGarbageAreHandled() {
        assertEquals(1, VoiceManifest.parse(manifest(voice(), voice())).voices.size)
        assertEquals(1, VoiceManifest.parse(manifest(voice(), voice())).rejected.size)
        assertEquals(1, VoiceManifest.parse("not json").rejected.size)
        assertEquals(1, VoiceManifest.parse("""{"version":2,"voices":[]}""").rejected.size)
    }

    @Test
    fun urlPolicyAllowsOnlyHttpsGithubHosts() {
        assertTrue(VoiceUrlPolicy.isAllowed(VoiceUrlPolicy.MANIFEST_URL))
        assertTrue(VoiceUrlPolicy.isAllowed("https://objects.githubusercontent.com/abc"))
        assertFalse(VoiceUrlPolicy.isAllowed("http://raw.githubusercontent.com/a"))
        assertFalse(VoiceUrlPolicy.isAllowed("https://raw.githubusercontent.com.evil.com/a"))
        assertFalse(VoiceUrlPolicy.isAllowed("https://user@github.com/a"))
        assertFalse(VoiceUrlPolicy.isAllowed("https://github.com:8443/a"))
        assertFalse(VoiceUrlPolicy.isAllowed("file:///etc/passwd"))
        assertFalse(VoiceUrlPolicy.isAllowed("javascript:alert(1)"))
        assertFalse(VoiceUrlPolicy.isAllowed(""))
    }

    @Test
    fun theManifestInTheRepositoryPassesTheRightsGate() {
        val file = File("../voices/manifest.json")
        assertTrue("voices/manifest.json must exist at the repository root", file.isFile)
        val result = VoiceManifest.parse(file.readText(Charsets.UTF_8))
        assertTrue("rejected voices: ${result.rejected}", result.rejected.isEmpty())
    }

    @Test
    fun manifestPermissionsAreExactlyTheReviewedSet() {
        val xml = File("src/main/AndroidManifest.xml").readText()
        val declared = Regex("<uses-permission android:name=\"([^\"]+)\"").findAll(xml).map { it.groupValues[1] }.toSet()
        val expected = setOf(
            "POST_NOTIFICATIONS", "SCHEDULE_EXACT_ALARM", "RECEIVE_BOOT_COMPLETED", "VIBRATE", "ACCESS_COARSE_LOCATION",
            "FOREGROUND_SERVICE", "FOREGROUND_SERVICE_MEDIA_PLAYBACK", "WAKE_LOCK", "INTERNET"
        ).map { "android.permission.$it" }.toSet()
        assertEquals(expected, declared)
        assertTrue(xml.contains("android:networkSecurityConfig"))
        assertTrue(xml.contains("android:allowBackup=\"false\""))
        // Only the boot receiver is exported (plus the launcher activity).
        assertEquals(2, Regex("android:exported=\"true\"").findAll(xml).count())
    }

    @Test
    fun onlyTheDownloaderAndUrlPolicyTouchTheNetwork() {
        val allowed = setOf("VoicePackDownloader.kt", "VoiceModels.kt")
        val offenders = File("src/main/java").walkTopDown()
            .filter { it.isFile && it.extension == "kt" && it.name !in allowed }
            .filter { f ->
                val text = f.readText()
                Regex("import java\\.net\\.|HttpURLConnection|okhttp|retrofit|URL\\(").containsMatchIn(text)
            }
            .map { it.name }.toList()
        assertTrue("network code outside the downloader: $offenders", offenders.isEmpty())
    }

    @Test
    fun cleartextTrafficIsDisabled() {
        val xml = File("src/main/res/xml/network_security_config.xml").readText()
        assertTrue(xml.contains("cleartextTrafficPermitted=\"false\""))
    }
}
