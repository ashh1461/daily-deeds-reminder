package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.PrayerParams

/** What happens when a prayer's time arrives. */
enum class AdhanMode(val id: String, val labelArabic: String) {
    OFF("off", "إيقاف"),
    SILENT("silent", "صامت"),
    NOTIFICATION("notification", "إشعار"),
    ADHAN("adhan", "أذان");

    companion object {
        fun fromId(id: String?): AdhanMode? = values().find { it.id == id }
    }
}

object VoiceIds {
    /** The phone's default notification/alarm sound; always available. */
    const val DEFAULT = "default"
}

/** Per-prayer alarm configuration. */
data class PrayerAlarmConfig(
    val mode: AdhanMode = AdhanMode.OFF,
    val voiceId: String = VoiceIds.DEFAULT,
    /** Minutes before the prayer for a heads-up reminder; 0 = none. */
    val preMinutes: Int = 0,
    /** Manual adjustment of this prayer's time, in minutes (-15..+15). */
    val offsetMinutes: Int = 0
) {
    init {
        require(preMinutes in PRE_CHOICES.first()..PRE_CHOICES.last()) { "preMinutes out of range" }
        require(offsetMinutes in -MAX_OFFSET..MAX_OFFSET) { "offsetMinutes out of range" }
    }

    fun encode(): String = "${mode.id},$voiceId,$preMinutes,$offsetMinutes"

    companion object {
        val PRE_CHOICES = listOf(0, 5, 10, 15, 30)
        const val MAX_OFFSET = 15

        /** Returns null for anything malformed so that corrupted preferences fall back to the default. */
        fun decode(raw: String?): PrayerAlarmConfig? {
            val p = raw?.split(',') ?: return null
            if (p.size != 4) return null
            return try {
                PrayerAlarmConfig(
                    mode = AdhanMode.fromId(p[0]) ?: return null,
                    voiceId = p[1].takeIf { it.matches(Regex("[A-Za-z0-9_]{1,48}")) } ?: return null,
                    preMinutes = p[2].toInt(),
                    offsetMinutes = p[3].toInt()
                )
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}

/** Settings shared by all prayers. */
data class AdhanGlobalSettings(
    /** Do not play audio while the phone is on silent or vibrate. */
    val followRinger: Boolean = true,
    val vibrate: Boolean = true,
    val snoozeMinutes: Int = 5,
    /** Hard stop for a playing adhan. */
    val maxSeconds: Int = 300
) {
    init {
        require(snoozeMinutes in 1..30 && maxSeconds in 30..900)
    }

    fun encode(): String = "$followRinger,$vibrate,$snoozeMinutes,$maxSeconds"

    companion object {
        fun decode(raw: String?): AdhanGlobalSettings? {
            val p = raw?.split(',') ?: return null
            if (p.size != 4) return null
            return try {
                AdhanGlobalSettings(p[0].toBooleanStrict(), p[1].toBooleanStrict(), p[2].toInt(), p[3].toInt())
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}

enum class CalcMethod(val id: String, val labelArabic: String) {
    LEVA("leva", "معهد ليفا (قم)"),
    TEHRAN("tehran", "جامعة طهران"),
    CUSTOM("custom", "مخصص");

    companion object {
        fun fromId(id: String?): CalcMethod = values().find { it.id == id } ?: LEVA
    }
}

/** How prayer times are calculated. */
data class PrayerSettings(
    val method: CalcMethod = CalcMethod.LEVA,
    val customFajr: Double = 16.0,
    val customIsha: Double = 14.0,
    val customMaghrib: Double = 4.0,
    val maghribDelayMinutes: Int = 0,
    val imsakMinutes: Int = 10
) {
    fun params(): PrayerParams {
        val base = when (method) {
            CalcMethod.LEVA -> PrayerParams.LEVA
            CalcMethod.TEHRAN -> PrayerParams.TEHRAN
            CalcMethod.CUSTOM -> PrayerParams(customFajr, customIsha, customMaghrib)
        }
        return base.copy(maghribDelayMinutes = maghribDelayMinutes, imsakMinutes = imsakMinutes)
    }

    fun encode(): String = "${method.id},$customFajr,$customIsha,$customMaghrib,$maghribDelayMinutes,$imsakMinutes"

    companion object {
        fun decode(raw: String?): PrayerSettings? {
            val p = raw?.split(',') ?: return null
            if (p.size != 6) return null
            return try {
                PrayerSettings(
                    CalcMethod.fromId(p[0]), p[1].toDouble(), p[2].toDouble(), p[3].toDouble(), p[4].toInt(), p[5].toInt()
                ).also { it.params() }
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}
