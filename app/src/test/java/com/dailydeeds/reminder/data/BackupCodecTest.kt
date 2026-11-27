package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.adhan.AdhanPlanner
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.PlannedAlarm
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.model.PlacePresets
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.util.PrayerContext
import com.dailydeeds.reminder.util.PrayerSchedule
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupCodecTest {

    private fun ok(text: String) = BackupCodec.parse(text) as BackupCodec.Import.Ok
    private fun error(text: String) = BackupCodec.parse(text) as BackupCodec.Import.Error

    private val beirutBits = 33.8938.toRawBits()
    private val beirutLngBits = 35.5018.toRawBits()

    private fun sample(): Map<String, Any> = mapOf(
        "pref_favorites" to "quran:2,mafatih:kumayl",
        "pref_last_surah" to 18,
        "pref_last_page" to 302,
        "pref_quran_pages_mode" to false,
        "pref_font_quran" to 280,
        "pref_font_mafatih" to 240,
        "pref_font_mushaf" to 175,
        "pref_mushaf_fit" to false,
        "pref_theme_mode" to "dark",
        "pref_haptics_enabled" to false,
        "pref_morning_hour" to 6,
        "pref_alarm_cfg_1" to "ADHAN,default,10,0",
        "pref_place_name" to "بيروت",
        "pref_place_lat" to beirutBits,
        "pref_place_lng" to beirutLngBits,
        "pref_place_zone" to "Asia/Beirut",
        "deed_3_completed_2026-10-01" to true,
        "deed_3_count_2026-10-01" to 33,
        "worship_tasbih" to "40,2,5,33,999",
        "worship_imsak_enabled" to true,
        // Not part of a backup:
        "pref_last_active_date" to "2026-10-01",
        "pref_asked_notifications" to true
    )

    @Test
    fun exportThenImportKeepsEveryBackedUpValue() {
        val text = BackupCodec.export(sample(), "2026-10-09T00:00:00Z")
        val back = ok(text)
        assertEquals(0, back.skipped)
        assertEquals(sample().filterKeys { BackupCodec.isAllowedKey(it) }, back.entries)
        assertFalse("pref_last_active_date" in back.entries)
        assertFalse("pref_asked_notifications" in back.entries)
    }

    @Test
    fun onlyKnownKeysAreAllowed() {
        listOf("pref_favorites", "deed_12_stage_2026-01-01", "worship_qada", "pref_alarm_cfg_4", "pref_night_minute",
            "pref_last_page", "pref_quran_pages_mode")
            .forEach { assertTrue(it, BackupCodec.isAllowedKey(it)) }
        listOf("pref_last_active_date", "voice_store", "deed_1_completed_x", "worship_", "pref_alarm_cfg_9", "../pref_favorites", "", "x".repeat(200))
            .forEach { assertFalse(it, BackupCodec.isAllowedKey(it)) }
    }

    @Test
    fun rejectsFilesThatAreNotBackups() {
        assertEquals(BackupCodec.Reason.NOT_A_BACKUP, error("not json").reason)
        assertEquals(BackupCodec.Reason.OTHER_APP, error("{}").reason)
        error("""{"app":"other","version":1,"prefs":{"pref_favorites":{"t":"s","v":"x"}}}""")
        error("""{"app":"daily-deeds-backup","version":2,"prefs":{"pref_favorites":{"t":"s","v":"x"}}}""")
        error("""{"app":"daily-deeds-backup","version":1}""")
        error("""{"app":"daily-deeds-backup","version":1,"prefs":{}}""")
        assertEquals(BackupCodec.Reason.TOO_LARGE, error("x".repeat(BackupCodec.MAX_BYTES + 1)).reason)
    }

    @Test
    fun skipsUnknownKeysAndWrongTypes() {
        val text = JSONObject()
            .put("app", "daily-deeds-backup").put("version", 1)
            .put(
                "prefs", JSONObject()
                    .put("pref_theme_mode", JSONObject().put("t", "s").put("v", "light"))
                    .put("pref_last_active_date", JSONObject().put("t", "s").put("v", "2026-01-01"))
                    .put("pref_hijri_offset", JSONObject().put("t", "s").put("v", "1"))
                    .put("pref_haptics_enabled", JSONObject().put("t", "i").put("v", 1))
                    .put("deed_1_completed_2026-01-01", JSONObject().put("t", "i").put("v", 1))
                    .put("pref_morning_hour", JSONObject().put("t", "i").put("v", 7))
                    .put("pref_favorites", JSONObject().put("t", "s").put("v", "y".repeat(200_000)))
            ).toString()
        val r = ok(text)
        assertEquals(setOf("pref_theme_mode", "pref_morning_hour"), r.entries.keys)
        assertEquals(5, r.skipped)
    }

    /** Regression from 1.8.1.5: Int and Boolean reader keys fell through to "must be a String" and were dropped. */
    @Test
    fun readerKeysKeepTheirIntAndBooleanTypesAndRejectOthers() {
        fun entry(t: String, v: Any) = JSONObject().put("t", t).put("v", v)
        val text = JSONObject()
            .put("app", "daily-deeds-backup").put("version", 1)
            .put(
                "prefs", JSONObject()
                    .put("pref_last_page", entry("i", 41))
                    .put("pref_quran_pages_mode", entry("b", true))
                    .put("pref_font_mushaf", entry("i", 225))
                    .put("pref_mushaf_fit", entry("b", false))
                    .put("pref_font_quran", entry("s", "28"))
                    .put("pref_font_mafatih", entry("b", true))
            ).toString()
        val r = ok(text)
        assertEquals(41, r.entries["pref_last_page"])
        assertEquals(true, r.entries["pref_quran_pages_mode"])
        assertEquals(225, r.entries["pref_font_mushaf"])
        assertEquals(false, r.entries["pref_mushaf_fit"])
        assertFalse("pref_font_quran" in r.entries)
        assertFalse("pref_font_mafatih" in r.entries)
        assertEquals(2, r.skipped)
    }

    @Test
    fun aBadPlaceIsDroppedAsAWholeSoThatPrayerCalculationCannotBreak() {
        fun withPlace(lat: Long, lng: Long, zone: String, name: String = "x"): String {
            val prefs = JSONObject()
                .put("pref_theme_mode", JSONObject().put("t", "s").put("v", "dark"))
                .put("pref_place_name", JSONObject().put("t", "s").put("v", name))
                .put("pref_place_lat", JSONObject().put("t", "l").put("v", lat))
                .put("pref_place_lng", JSONObject().put("t", "l").put("v", lng))
                .put("pref_place_zone", JSONObject().put("t", "s").put("v", zone))
            return JSONObject().put("app", "daily-deeds-backup").put("version", 1).put("prefs", prefs).toString()
        }
        assertTrue("pref_place_name" in ok(withPlace(beirutBits, beirutLngBits, "Asia/Beirut")).entries)
        for (bad in listOf(
            withPlace(Double.NaN.toRawBits(), beirutLngBits, "Asia/Beirut"),
            withPlace(beirutBits, Double.POSITIVE_INFINITY.toRawBits(), "Asia/Beirut"),
            withPlace(95.0.toRawBits(), beirutLngBits, "Asia/Beirut"),
            withPlace(beirutBits, beirutLngBits, "Not/AZone"),
            withPlace(beirutBits, beirutLngBits, "Asia/Beirut", name = " "),
            withPlace(beirutBits, beirutLngBits, "Asia/Beirut", name = "n".repeat(101))
        )) {
            val r = ok(bad)
            assertEquals(setOf("pref_theme_mode"), r.entries.keys)
            assertEquals(4, r.skipped)
        }
    }

    @Test
    fun incompleteJsonValuesDoNotCrash() {
        error("""{"app":"daily-deeds-backup","version":1,"prefs":{"pref_theme_mode":null,"pref_favorites":{"t":"s"},"pref_last_surah":{"v":3}}}""")
    }
}

class ImsakPlannerTest {
    private val beirut = PlacePresets.default
    private val zone = ZoneId.of("Asia/Beirut")
    private val off = Prayer.values().associateWith { PrayerAlarmConfig(mode = AdhanMode.OFF) }

    @Test
    fun theImsakAlarmIsPlannedEvenWhenEveryPrayerIsOff() {
        val now = ZonedDateTime.of(2026, 10, 15, 1, 0, 0, 0, zone)
        val p = AdhanPlanner.next(now, beirut, PrayerContext(), off) { true }!!
        assertEquals(AlarmKind.IMSAK, p.kind)
        val times = PrayerSchedule.timesFor(LocalDate.of(2026, 10, 15), beirut)
        assertEquals(times.imsak, p.triggerAt.toLocalTime().withSecond(0).withNano(0))
        assertEquals(times.fajr!!.minusMinutes(10), times.imsak)
    }

    @Test
    fun imsakComesBeforeTheFajrAlarm() {
        val on = off + (Prayer.FAJR to PrayerAlarmConfig(mode = AdhanMode.ADHAN))
        val now = ZonedDateTime.of(2026, 10, 15, 1, 0, 0, 0, zone)
        assertEquals(AlarmKind.IMSAK, AdhanPlanner.next(now, beirut, PrayerContext(), on) { true }!!.kind)
        assertEquals(AlarmKind.MAIN, AdhanPlanner.next(now, beirut, PrayerContext(), on)!!.kind)
    }

    @Test
    fun aFajrPreAlarmAtTheSameInstantAsImsakIsHandledWithIt() {
        // A 10-minute pre-alarm and the default Imsak (10 minutes before Fajr) are due at the same instant.
        val on = off + (Prayer.FAJR to PrayerAlarmConfig(mode = AdhanMode.ADHAN, preMinutes = 10))
        val now = ZonedDateTime.of(2026, 10, 15, 1, 0, 0, 0, zone)
        val first = AdhanPlanner.next(now, beirut, PrayerContext(), on) { true }!!
        // The receiver handles the other one in the same run instead of setting an alarm for a past time.
        val tied = AdhanPlanner.dueWith(first, beirut, PrayerContext(), on) { true }
        assertEquals(1, tied.size)
        assertEquals(first.triggerAt, tied.single().triggerAt)
        assertEquals(listOf(AlarmKind.PRE, AlarmKind.IMSAK), listOf(first.kind, tied.single().kind))
        assertTrue(AdhanPlanner.dueWith(tied.single(), beirut, PrayerContext(), on) { true }.isEmpty())
        // As the receiver rebuilds the fired alarm from its intent: another zone, only the trigger time known.
        val utc = first.triggerAt.withZoneSameInstant(ZoneId.of("UTC"))
        assertEquals(tied, AdhanPlanner.dueWith(PlannedAlarm(first.prayer, first.kind, utc, utc), beirut, PrayerContext(), on) { true })
        // Planning after the last one handled gives the Fajr adhan, and is in the future whenever the receiver runs.
        val third = AdhanPlanner.next(first.triggerAt.plusSeconds(2), beirut, PrayerContext(), on, after = tied.last()) { true }!!
        assertEquals(AlarmKind.MAIN, third.kind)
        assertEquals(first.prayerAt, third.triggerAt)
        for (delay in listOf(0L, 1L, 60L, 9 * 60L + 59, 10 * 60L, 15 * 60L, 3 * 3600L)) {
            val clock = first.triggerAt.plusSeconds(delay)
            val planned = AdhanPlanner.next(clock, beirut, PrayerContext(), on, after = tied.last()) { true }!!
            assertTrue("after ${delay}s: $planned", planned.triggerAt.isAfter(clock))
        }
    }

    @Test
    fun noImsakAlarmOutsideTheChosenDays() {
        val now = ZonedDateTime.of(2026, 10, 15, 1, 0, 0, 0, zone)
        assertEquals(null, AdhanPlanner.next(now, beirut, PrayerContext(), off) { false })
    }

    @Test
    fun aFajrOffsetMovesImsakToo() {
        val date = LocalDate.of(2026, 10, 15)
        val base = PrayerSchedule.timesFor(date, beirut)
        val shifted = PrayerSchedule.timesFor(date, beirut, PrayerContext(offsets = mapOf(Prayer.FAJR to 4)))
        assertEquals(base.imsak!!.plusMinutes(4), shifted.imsak)
    }
}
