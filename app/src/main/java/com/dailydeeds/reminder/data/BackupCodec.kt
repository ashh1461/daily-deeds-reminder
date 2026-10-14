package com.dailydeeds.reminder.data

import java.time.ZoneId
import org.json.JSONException
import org.json.JSONObject

/**
 * Export and import of the user's data (favorites, reading positions, deed history, tasbih, make-up
 * counters, prayer and reminder settings) as one JSON file.
 *
 * A backup file is untrusted input: only known keys are accepted, every value must have the type that key
 * has in the app, and the place (which crashes prayer calculation when it is nonsense) is validated as a
 * whole. Voice files are not part of a backup.
 */
object BackupCodec {
    const val FORMAT = "daily-deeds-backup"
    const val VERSION = 1
    const val MAX_BYTES = 2_000_000
    private const val MAX_STRING = 100_000
    private const val MAX_ENTRIES = 20_000

    private val ALLOWED_KEYS = listOf(
        Regex("^pref_favorites$"),
        Regex("^pref_last_(surah|mafatih|sahifa)$"),
        Regex("^pref_(theme_mode|haptics_enabled|hijri_offset|occasion_reminder|adhan_global|prayer_settings|prayer_reminder_enabled)$"),
        Regex("^pref_place_(name|lat|lng|zone)$"),
        Regex("^pref_alarm_cfg_[1-4]$"),
        Regex("^pref_(morning|evening|night|bedtime|thursday)_(reminder_enabled|hour|minute)$"),
        Regex("^deed_\\d{1,3}_(completed|count|stage)_\\d{4}-\\d{2}-\\d{2}$"),
        Regex("^worship_[a-z_]{1,40}$")
    )

    fun isAllowedKey(key: String): Boolean = key.length <= 80 && ALLOWED_KEYS.any { it.matches(key) }

    sealed class Import {
        class Ok(val entries: Map<String, Any>, val skipped: Int) : Import()
        class Error(val message: String) : Import()
    }

    /** [all] is `SharedPreferences.getAll()`; keys that are not part of the backup are left out. */
    fun export(all: Map<String, *>, createdAt: String): String {
        val prefs = JSONObject()
        all.toSortedMap().forEach { (key, value) ->
            if (!isAllowedKey(key)) return@forEach
            val entry = JSONObject()
            when (value) {
                is Boolean -> entry.put("t", "b").put("v", value)
                is Int -> entry.put("t", "i").put("v", value)
                is Long -> entry.put("t", "l").put("v", value)
                is String -> entry.put("t", "s").put("v", value)
                else -> return@forEach
            }
            prefs.put(key, entry)
        }
        return JSONObject()
            .put("app", FORMAT).put("version", VERSION).put("createdAt", createdAt)
            .put("prefs", prefs).toString()
    }

    fun parse(text: String): Import {
        if (text.length > MAX_BYTES) return Import.Error("الملف أكبر من الحد المسموح")
        val root = try {
            JSONObject(text)
        } catch (e: JSONException) {
            return Import.Error("الملف ليس نسخة احتياطية صالحة")
        }
        if (root.optString("app") != FORMAT) return Import.Error("الملف ليس نسخة احتياطية من هذا التطبيق")
        if (root.optInt("version", -1) != VERSION) return Import.Error("إصدار النسخة الاحتياطية غير مدعوم")
        val prefs = root.optJSONObject("prefs") ?: return Import.Error("النسخة الاحتياطية فارغة")
        if (prefs.length() > MAX_ENTRIES) return Import.Error("النسخة الاحتياطية كبيرة جداً")

        val entries = LinkedHashMap<String, Any>()
        var skipped = 0
        val keys = prefs.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = decodeEntry(key, prefs.optJSONObject(key))
            if (value == null) skipped++ else entries[key] = value
        }
        if (!placeIsValid(entries)) {
            listOf("pref_place_name", "pref_place_lat", "pref_place_lng", "pref_place_zone").forEach {
                if (entries.remove(it) != null) skipped++
            }
        }
        if (entries.isEmpty()) return Import.Error("لا توجد بيانات صالحة في الملف")
        return Import.Ok(entries, skipped)
    }

    private fun decodeEntry(key: String, entry: JSONObject?): Any? {
        if (entry == null || !isAllowedKey(key)) return null
        val raw = entry.opt("v") ?: return null
        val value: Any = when (entry.optString("t")) {
            "b" -> raw as? Boolean ?: return null
            "i" -> (raw as? Int) ?: return null
            "l" -> when (raw) { is Long -> raw; is Int -> raw.toLong(); else -> return null }
            "s" -> (raw as? String)?.takeIf { it.length <= MAX_STRING } ?: return null
            else -> return null
        }
        return if (typeMatchesKey(key, value)) value else null
    }

    /** Each key keeps the type it has in [PreferencesManager]; a wrong type would crash on read. */
    private fun typeMatchesKey(key: String, value: Any): Boolean = when {
        key == "pref_place_lat" || key == "pref_place_lng" -> value is Long
        key.startsWith("pref_place_") -> value is String
        key == "pref_hijri_offset" -> value is Int
        key.endsWith("_hour") || key.endsWith("_minute") -> value is Int
        key.endsWith("_enabled") || key == "pref_occasion_reminder" -> value is Boolean
        key.startsWith("pref_last_surah") -> value is Int
        key.startsWith("deed_") -> if (key.contains("_completed_")) value is Boolean else value is Int
        else -> value is String
    }

    private fun placeIsValid(entries: Map<String, Any>): Boolean {
        val present = listOf("pref_place_name", "pref_place_lat", "pref_place_lng", "pref_place_zone").count { it in entries }
        if (present == 0) return true
        if (present != 4) return false
        val lat = Double.fromBits(entries["pref_place_lat"] as Long)
        val lng = Double.fromBits(entries["pref_place_lng"] as Long)
        val name = entries["pref_place_name"] as String
        val zone = entries["pref_place_zone"] as String
        return lat.isFinite() && lng.isFinite() && lat in -90.0..90.0 && lng in -180.0..180.0 &&
            name.isNotBlank() && name.length <= 100 &&
            runCatching { ZoneId.of(zone) }.isSuccess
    }
}
