package com.dailydeeds.reminder.adhan

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URI

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
    const val MANIFEST_URL = "https://raw.githubusercontent.com/ashh1461/daily-deeds-reminder/master/voices/manifest.json"

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
        if (!ID.matches(id) || id == VoiceIds.DEFAULT) return "bad id"
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
