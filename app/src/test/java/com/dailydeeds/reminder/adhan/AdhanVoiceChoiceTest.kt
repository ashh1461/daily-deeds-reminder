package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.Prayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** One adhan voice for all prayers: the stored default and the Karbala id are one choice, and only the voice changes. */
class AdhanVoiceChoiceTest {

    private val all = AdhanVoiceChoice.ALARM_PRAYERS
    private val mashhad = "mashhad_imam_reza"

    @Test
    fun alarmPrayersAreTheFourPrayerCardsInOrder() {
        assertEquals(listOf(Prayer.FAJR, Prayer.DHUHR, Prayer.MAGHRIB, Prayer.ISHA), all)
    }

    @Test
    fun effectiveVoiceIdMapsTheDefaultToKarbalaAndLeavesOthers() {
        assertEquals("karbala_imam_hussain", AdhanVoiceChoice.effectiveVoiceId(VoiceIds.DEFAULT))
        assertEquals(BundledVoices.DEFAULT_ID, AdhanVoiceChoice.effectiveVoiceId(VoiceIds.DEFAULT))
        for (id in BundledVoices.all.map { it.id } + VoiceIds.SYSTEM_TONE + "own_k3j2") {
            assertEquals(id, AdhanVoiceChoice.effectiveVoiceId(id))
        }
    }

    @Test
    fun commonVoiceIdIsSharedWhenAllFourMatchIncludingDefaultAndExplicitKarbala() {
        val mixed = mapOf(
            Prayer.FAJR to PrayerAlarmConfig(voiceId = VoiceIds.DEFAULT),
            Prayer.DHUHR to PrayerAlarmConfig(voiceId = BundledVoices.DEFAULT_ID),
            Prayer.MAGHRIB to PrayerAlarmConfig(mode = AdhanMode.ADHAN, voiceId = VoiceIds.DEFAULT),
            Prayer.ISHA to PrayerAlarmConfig(mode = AdhanMode.NOTIFICATION, voiceId = BundledVoices.DEFAULT_ID)
        )
        assertEquals(BundledVoices.DEFAULT_ID, AdhanVoiceChoice.commonVoiceId(mixed, all))
        assertEquals(mashhad, AdhanVoiceChoice.commonVoiceId(all.associateWith { PrayerAlarmConfig(voiceId = mashhad) }, all))
        assertEquals(
            VoiceIds.SYSTEM_TONE,
            AdhanVoiceChoice.commonVoiceId(all.associateWith { PrayerAlarmConfig(voiceId = VoiceIds.SYSTEM_TONE) }, all)
        )
    }

    @Test
    fun commonVoiceIdIsNullWhenOnePrayerDiffers() {
        val oneMashhad = all.associateWith { PrayerAlarmConfig(voiceId = VoiceIds.DEFAULT) } +
            (Prayer.ISHA to PrayerAlarmConfig(voiceId = mashhad))
        assertNull(AdhanVoiceChoice.commonVoiceId(oneMashhad, all))
        val oneTone = all.associateWith { PrayerAlarmConfig(voiceId = BundledVoices.DEFAULT_ID) } +
            (Prayer.DHUHR to PrayerAlarmConfig(voiceId = VoiceIds.SYSTEM_TONE))
        assertNull(AdhanVoiceChoice.commonVoiceId(oneTone, all))
    }

    @Test
    fun aPrayerMissingFromTheMapCountsAsTheDefault() {
        assertEquals(BundledVoices.DEFAULT_ID, AdhanVoiceChoice.commonVoiceId(emptyMap(), all))
        val onlyFajrKarbala = mapOf(Prayer.FAJR to PrayerAlarmConfig(voiceId = BundledVoices.DEFAULT_ID))
        assertEquals(BundledVoices.DEFAULT_ID, AdhanVoiceChoice.commonVoiceId(onlyFajrKarbala, all))
        val onlyFajrMashhad = mapOf(Prayer.FAJR to PrayerAlarmConfig(voiceId = mashhad))
        assertNull(AdhanVoiceChoice.commonVoiceId(onlyFajrMashhad, all))
    }

    @Test
    fun withVoiceForAllChangesOnlyTheVoiceOfTheListedPrayers() {
        val before = mapOf(
            Prayer.FAJR to PrayerAlarmConfig(AdhanMode.ADHAN, VoiceIds.DEFAULT, 10, -3),
            Prayer.DHUHR to PrayerAlarmConfig(AdhanMode.NOTIFICATION, VoiceIds.SYSTEM_TONE, 5, 2),
            Prayer.MAGHRIB to PrayerAlarmConfig(AdhanMode.OFF, "own_k3j2", 0, 15),
            Prayer.ISHA to PrayerAlarmConfig(AdhanMode.SILENT, VoiceIds.DEFAULT, 30, -15)
        )
        val listed = listOf(Prayer.FAJR, Prayer.MAGHRIB)
        val after = AdhanVoiceChoice.withVoiceForAll(before, listed, mashhad)
        assertEquals(before.keys, after.keys)
        for (p in Prayer.values()) {
            val old = before.getValue(p)
            val updated = after.getValue(p)
            if (p in listed) {
                assertEquals(p.name, mashhad, updated.voiceId)
                assertEquals(p.name, old.mode, updated.mode)
                assertEquals(p.name, old.preMinutes, updated.preMinutes)
                assertEquals(p.name, old.offsetMinutes, updated.offsetMinutes)
            } else {
                assertEquals(p.name, old, updated)
            }
        }

        // For all four prayers, one missing from the map gets a default config with the new voice.
        val allFour = AdhanVoiceChoice.withVoiceForAll(before - Prayer.ISHA, all, mashhad)
        assertEquals(PrayerAlarmConfig(voiceId = mashhad), allFour[Prayer.ISHA])
        assertEquals(before.getValue(Prayer.DHUHR).copy(voiceId = mashhad), allFour[Prayer.DHUHR])
        assertEquals(mashhad, AdhanVoiceChoice.commonVoiceId(allFour, all))
    }

    @Test
    fun theNewVoiceSurvivesEncodeAndDecodeForBundledVoicesAndThePhoneTone() {
        val before = all.associateWith { PrayerAlarmConfig(AdhanMode.ADHAN, VoiceIds.DEFAULT, 15, 7) }
        for (id in BundledVoices.all.map { it.id } + VoiceIds.SYSTEM_TONE) {
            val after = AdhanVoiceChoice.withVoiceForAll(before, all, id)
            for (p in all) {
                val config = after.getValue(p)
                val decoded = PrayerAlarmConfig.decode(config.encode())
                assertEquals("$id ${p.name}", config, decoded)
                assertEquals("$id ${p.name}", id, decoded?.voiceId)
            }
        }
    }
}
