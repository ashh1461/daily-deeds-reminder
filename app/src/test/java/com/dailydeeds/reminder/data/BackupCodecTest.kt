package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.adhan.AdhanPlanner
import com.dailydeeds.reminder.adhan.AlarmKind
import com.dailydeeds.reminder.adhan.AdhanMode
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
        listOf("pref_favorites", "deed_12_stage_2026-01-01", "worship_qada", "pref_alarm_cfg_4", "pref_night_minute")
            .forEach { assertTrue(it, BackupCodec.isAllowedKey(it)) }
        listOf("pref_last_active_date", "voice_store", "deed_1_completed_x", "worship_", "pref_alarm_cfg_9", "../pref_favorites", "", "x".repeat(200))
            .forEach { assertFalse(it, BackupCodec.isAllowedKey(it)) }
    }

    @Test
    fun rejectsFilesThatAreNotBackups() {
        error("not json")
        error("{}")
        error("""{"app":"other","version":1,"prefs":{"pref_favorites":{"t":"s","v":"x"}}}""")
        error("""{"app":"daily-deeds-backup","version":2,"prefs":{"pref_favorites":{"t":"s","v":"x"}}}""")
        error("""{"app":"daily-deeds-backup","version":1}""")
        error("""{"app":"daily-deeds-backup","version":1,"prefs":{}}""")
        error("x".repeat(BackupCodec.MAX_BYTES + 1))
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
