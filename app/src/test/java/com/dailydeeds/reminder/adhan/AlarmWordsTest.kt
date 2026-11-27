package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.AppLanguage
import com.dailydeeds.reminder.util.Prayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * The prayer notifications in each UI language (v1.9.0). The Arabic wording, its plural rules and the late-alarm
 * cases are covered by [PrayerAlarmTextTest]; this checks that the same cases read correctly in English and Persian.
 */
class AlarmWordsTest {
    private val minute = 60_000L
    private val t0 = 1_700_000_000_000L
    private val en = AlarmWords.of(AppLanguage.ENGLISH)
    private val fa = AlarmWords.of(AppLanguage.PERSIAN)

    @Test
    fun arabicIsTheDefaultSoExistingWordingIsUnchanged() {
        assertSame(ArabicAlarmWords, AlarmWords.of(AppLanguage.ARABIC))
        assertEquals(PrayerAlarmText.reminder(Prayer.ISHA), PrayerAlarmText.reminder(Prayer.ISHA, words = ArabicAlarmWords))
    }

    @Test
    fun englishOnTimeAndLateWording() {
        assertEquals(
            AlarmText("Fajr prayer is near", "The Fajr adhan is in 10 minutes."),
            PrayerAlarmText.pre(Prayer.FAJR, 10, t0, t0 + 30_000, en)
        )
        assertEquals("The Dhuhr adhan is in 1 minute.", PrayerAlarmText.pre(Prayer.DHUHR, 30, t0, t0 + 29 * minute + 30_000, en).text)
        assertEquals(
            AlarmText("It is time for Isha prayer", "The Isha adhan time has come, according to Jafari jurisprudence."),
            PrayerAlarmText.reminder(Prayer.ISHA, words = en)
        )
        assertEquals("The Dhuhr adhan time has come.", PrayerAlarmText.silent(Prayer.DHUHR, words = en).text)
        assertEquals(
            AlarmText("Fajr prayer time has begun", "The Fajr adhan time was 35 minutes ago."),
            PrayerAlarmText.pre(Prayer.FAJR, 5, t0, t0 + 40 * minute, en)
        )
        assertEquals("The Maghrib adhan time has passed.", PrayerAlarmText.reminder(Prayer.MAGHRIB, t0, t0 + 15 * minute, true, en).text)
    }

    @Test
    fun persianOnTimeAndLateWording() {
        assertEquals(
            AlarmText("وقت نماز صبح نزدیک است", "۱۰ دقیقه دیگر وقت اذان صبح است."),
            PrayerAlarmText.pre(Prayer.FAJR, 10, t0, t0 + 30_000, fa)
        )
        assertEquals("اکنون وقت اذان عشا بر اساس فقه جعفری است.", PrayerAlarmText.reminder(Prayer.ISHA, words = fa).text)
        assertEquals("۳۵ دقیقه از وقت اذان صبح گذشته است.", PrayerAlarmText.pre(Prayer.FAJR, 5, t0, t0 + 40 * minute, fa).text)
        assertEquals("وقت اذان مغرب گذشته است.", PrayerAlarmText.reminder(Prayer.MAGHRIB, t0, t0 + 15 * minute, true, fa).text)
    }

    @Test
    fun prayerNamesMatchTheStringResources() {
        // The notifications word prayers in plain Kotlin; the screens and the adhan notification read prayer_* from
        // resources. Both must name a prayer the same way in every language.
        val keys = mapOf(Prayer.FAJR to "prayer_fajr", Prayer.DHUHR to "prayer_dhuhr", Prayer.MAGHRIB to "prayer_maghrib", Prayer.ISHA to "prayer_isha")
        mapOf("values" to ArabicAlarmWords, "values-en" to EnglishAlarmWords, "values-fa" to PersianAlarmWords).forEach { (dir, words) ->
            val xml = java.io.File("src/main/res/$dir/strings.xml").readText()
            keys.forEach { (prayer, key) ->
                val value = Regex("""<string name="$key">([^<]*)</string>""").find(xml)?.groupValues?.get(1)
                assertEquals("$key in $dir", value, words.prayerName(prayer))
            }
        }
    }

    @Test
    fun englishWordingCarriesNoArabicScript() {
        val arabicScript = Regex("[؀-ۿ]")
        Prayer.entries.forEach { p ->
            listOf(
                PrayerAlarmText.pre(p, 10, t0, t0, en), PrayerAlarmText.pre(p, 10, t0, t0 + 2 * minute, en),
                PrayerAlarmText.silent(p, words = en), PrayerAlarmText.reminder(p, words = en),
                PrayerAlarmText.silent(p, t0, t0 + 30 * minute, words = en), PrayerAlarmText.reminder(p, t0, t0 + 30 * minute, true, en)
            ).forEach { assertFalse(it.toString(), arabicScript.containsMatchIn(it.title + it.text)) }
        }
    }
}
