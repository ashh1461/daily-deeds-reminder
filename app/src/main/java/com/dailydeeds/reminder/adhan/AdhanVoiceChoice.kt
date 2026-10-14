package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.Prayer

/**
 * One adhan voice for every prayer at once, kept pure so that it is unit-tested. The settings screen offers it
 * above the prayer cards; each card's own voice picker stays as an override.
 */
object AdhanVoiceChoice {
    /** The prayers that have an alarm card, in the order the settings screen shows them. */
    val ALARM_PRAYERS: List<Prayer> = Prayer.values().toList()

    /** The voice [voiceId] plays: the stored [VoiceIds.DEFAULT] and the explicit default recording are one choice. */
    fun effectiveVoiceId(voiceId: String): String =
        if (voiceId == VoiceIds.DEFAULT) BundledVoices.DEFAULT_ID else voiceId

    /** The effective voice all [prayers] share, or null when they differ; a prayer missing from [configs] has the default. */
    fun commonVoiceId(configs: Map<Prayer, PrayerAlarmConfig>, prayers: List<Prayer>): String? =
        prayers.map { effectiveVoiceId((configs[it] ?: PrayerAlarmConfig()).voiceId) }.toSet().singleOrNull()

    /** [configs] with [voiceId] for each of [prayers], keeping their mode, pre-alarm and offset; other entries stay. */
    fun withVoiceForAll(
        configs: Map<Prayer, PrayerAlarmConfig>,
        prayers: List<Prayer>,
        voiceId: String
    ): Map<Prayer, PrayerAlarmConfig> =
        configs + prayers.associateWith { (configs[it] ?: PrayerAlarmConfig()).copy(voiceId = voiceId) }
}
