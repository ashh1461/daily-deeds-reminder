package com.dailydeeds.reminder.adhan

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URI
import java.security.MessageDigest

/** A downloadable adhan voice. Only voices with a recorded licence and permission are ever listed. */
data class RemoteVoice(
    val id: String,
    val nameAr: String,
    val reciter: String,
    val includesWilayah: Boolean,
    val sizeBytes: Long,
    val sha256: String,
    val url: String,
    val durationSec: Int,
    val licence: String,
    val permission: String
)

/** Where voice packs may be fetched from. HTTPS and a small host allow-list only. */
object VoiceUrlPolicy {
    /**
     * The manifest is pinned to an **immutable commit**, not the mutable `master` branch (audit F-2). GitHub serves
     * a raw blob by commit SHA permanently and by content, so a force-push to a branch cannot change what a released
     * build fetches, and the bytes for this path can never differ from what was reviewed. Update this together with
     * [VoiceManifest.EXPECTED_SHA256] whenever the manifest changes (push the new manifest, then bump both constants
     * and release). The test `theRepoManifestMatchesThePinnedDigest` keeps the pin honest.
     */
    const val MANIFEST_COMMIT = "e05caa253c8e85444226857f18e3968b44daf885"
    const val MANIFEST_URL =
        "https://raw.githubusercontent.com/ashh1461/daily-deeds-reminder/$MANIFEST_COMMIT/voices/manifest.json"

    private val ALLOWED_HOSTS = setOf(
        "raw.githubusercontent.com",
        "github.com",
        "objects.githubusercontent.com",
        "release-assets.githubusercontent.com"
    )

    fun isAllowed(url: String): Boolean = try {
        val uri = URI(url)
        uri.scheme == "https" && uri.host in ALLOWED_HOSTS && uri.userInfo == null && (uri.port == -1 || uri.port == 443)
    } catch (_: Exception) {
        false
    }
}

/**
 * Parses and validates `voices/manifest.json`. A voice is accepted only if every field is valid and it
 * carries a non-empty licence and permission note: this is the rights gate, enforced here and in tests.
 */
object VoiceManifest {
    const val MAX_VOICE_BYTES = 25_000_000L
    private val ID = Regex("[a-z0-9_]{1,40}")
    private val SHA256 = Regex("[0-9a-f]{64}")

    /**
     * SHA-256 of the exact `voices/manifest.json` the shipped build trusts (audit F-2). The downloader verifies the
     * fetched bytes against this pin before parsing, so a manifest swapped at the origin is rejected even though the
     * file and its per-voice hashes share that origin. The APK is owner-signed, so this constant is as trustworthy
     * as a public key baked into the build — no separate signing key is needed. Update it together with
     * [VoiceUrlPolicy.MANIFEST_COMMIT] when the manifest changes; `theRepoManifestMatchesThePinnedDigest` enforces it.
     */
    const val EXPECTED_SHA256 = "bb67932dc44ea1ce967a8960499867802fc29471752fc10c56b4f182a9e9892c"

    /** Lower-case hex SHA-256 of [bytes]. */
    fun sha256Of(bytes: ByteArray): String =
        MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }

    /** Whether [bytes] are exactly the pinned manifest. Checked before [parse] on every fetch. */
    fun matchesPinnedDigest(bytes: ByteArray): Boolean = sha256Of(bytes) == EXPECTED_SHA256

    data class ParseResult(val voices: List<RemoteVoice>, val rejected: List<String>)

    fun parse(json: String): ParseResult {
        val root = try {
            JSONObject(json)
        } catch (e: JSONException) {
            return ParseResult(emptyList(), listOf("manifest is not valid JSON"))
        }
        if (root.optInt("version") != 1) return ParseResult(emptyList(), listOf("unsupported manifest version"))
        val array = root.optJSONArray("voices") ?: JSONArray()
        val voices = ArrayList<RemoteVoice>()
        val rejected = ArrayList<String>()
        val seen = HashSet<String>()
        for (i in 0 until array.length()) {
            val o = array.optJSONObject(i) ?: run { rejected += "entry $i is not an object"; null } ?: continue
            if (o.optString("status") == "awaiting-rights") continue // placeholder, not downloadable, not listed
            val error = validate(o)
            val id = o.optString("id")
            when {
                error != null -> rejected += "$id: $error"
                !seen.add(id) -> rejected += "$id: duplicate id"
                else -> voices += RemoteVoice(
                    id = id,
                    nameAr = o.getString("nameAr").trim(),
                    reciter = o.getString("reciter").trim(),
                    includesWilayah = o.optBoolean("includesWilayah", false),
                    sizeBytes = o.getLong("sizeBytes"),
                    sha256 = o.getString("sha256"),
                    url = o.getString("url"),
                    durationSec = o.getInt("durationSec"),
                    licence = o.getString("licence").trim(),
                    permission = o.getString("permission").trim()
                )
            }
        }
        return ParseResult(voices, rejected)
    }

    private fun validate(o: JSONObject): String? {
        val id = o.optString("id")
        if (!ID.matches(id)) return "bad id"
        // A pack must not take over the app default, the phone tone, a bundled adhan or a voice the user imported.
        if (id == VoiceIds.DEFAULT || id == VoiceIds.SYSTEM_TONE || BundledVoices.byId(id) != null) return "reserved id"
        if (id.startsWith(VoiceIds.OWN_PREFIX)) return "reserved id"
        if (o.optString("nameAr").isBlank() || o.optString("nameAr").length > 80) return "bad nameAr"
        if (o.optString("reciter").isBlank()) return "missing reciter"
        if (o.optLong("sizeBytes") !in 10_000L..MAX_VOICE_BYTES) return "bad sizeBytes"
        if (!SHA256.matches(o.optString("sha256"))) return "bad sha256"
        if (!VoiceUrlPolicy.isAllowed(o.optString("url"))) return "url not allowed"
        if (o.optInt("durationSec") !in 5..900) return "bad durationSec"
        if (o.optString("licence").isBlank() || o.optString("licence").length < 3) return "missing licence"
        if (o.optString("permission").isBlank() || o.optString("permission").length < 3) return "missing permission"
        return null
    }
}
