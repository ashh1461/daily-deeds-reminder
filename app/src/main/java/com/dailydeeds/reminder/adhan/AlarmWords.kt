package com.dailydeeds.reminder.adhan

import com.dailydeeds.reminder.util.AppLanguage
import com.dailydeeds.reminder.util.Prayer

/**
 * The words of the prayer notifications in one UI language (v1.9.0). [PrayerAlarmText] decides which notification
 * fits (on time, late, snoozed, counted down); this only words it. Kept in plain Kotlin rather than string
 * resources because the notifications are built on the locked alarm path and each language counts minutes its own
 * way (Arabic has dual and plural forms), and so every case stays unit-tested.
 */
interface AlarmWords {
    fun prayerName(p: Prayer): String
    fun approaching(p: Prayer): String
    /** Body of an on-time heads-up: the configured [preMinutes] as set. */
    fun preOnTime(p: Prayer, preMinutes: Int): String
    /** Body of a heads-up delivered late: the minutes really [remaining]. */
    fun preRemaining(p: Prayer, remaining: Long): String
    fun timeCame(p: Prayer): String
    fun silentBody(p: Prayer): String
    fun reminderBody(p: Prayer): String
    fun begun(p: Prayer): String
    fun snoozedBody(p: Prayer): String
    fun passedBody(p: Prayer, ago: Long): String

    companion object {
        fun of(language: AppLanguage): AlarmWords = when (language) {
            AppLanguage.ARABIC -> ArabicAlarmWords
            AppLanguage.ENGLISH -> EnglishAlarmWords
            AppLanguage.PERSIAN -> PersianAlarmWords
        }
    }
}

/** The wording every version before 1.9.0 used, unchanged. */
object ArabicAlarmWords : AlarmWords {
    private fun name(p: Prayer) = p.nameArabic
    override fun prayerName(p: Prayer) = name(p)
    override fun approaching(p: Prayer) = "اقترب وقت صلاة ${name(p)}"
    override fun preOnTime(p: Prayer, preMinutes: Int) = "بعد $preMinutes دقيقة يحين موعد أذان ${name(p)}."
    override fun preRemaining(p: Prayer, remaining: Long) =
        "بعد ${PrayerAlarmText.minutes(remaining, genitive = true)} يحين موعد أذان ${name(p)}."
    override fun timeCame(p: Prayer) = "حان وقت صلاة ${name(p)}"
    override fun silentBody(p: Prayer) = "حان الآن موعد أذان ${name(p)}."
    override fun reminderBody(p: Prayer) = "حان الآن موعد أذان ${name(p)} بحسب الفقه الجعفري."
    override fun begun(p: Prayer) = "دخل وقت صلاة ${name(p)}"
    override fun snoozedBody(p: Prayer) = "مضى موعد أذان ${name(p)}."
    override fun passedBody(p: Prayer, ago: Long) = "مضى على موعد أذان ${name(p)} ${PrayerAlarmText.minutes(ago)}."
}

object EnglishAlarmWords : AlarmWords {
    private fun name(p: Prayer) = when (p) {
        Prayer.FAJR -> "Fajr"
        Prayer.DHUHR -> "Dhuhr"
        Prayer.MAGHRIB -> "Maghrib"
        Prayer.ISHA -> "Isha"
    }
    override fun prayerName(p: Prayer) = name(p)
    private fun minutes(n: Long) = if (n == 1L) "1 minute" else "$n minutes"
    override fun approaching(p: Prayer) = "${name(p)} prayer is near"
    override fun preOnTime(p: Prayer, preMinutes: Int) = "The ${name(p)} adhan is in ${minutes(preMinutes.toLong())}."
    override fun preRemaining(p: Prayer, remaining: Long) = "The ${name(p)} adhan is in ${minutes(remaining)}."
    override fun timeCame(p: Prayer) = "It is time for ${name(p)} prayer"
    override fun silentBody(p: Prayer) = "The ${name(p)} adhan time has come."
    override fun reminderBody(p: Prayer) = "The ${name(p)} adhan time has come, according to Jafari jurisprudence."
    override fun begun(p: Prayer) = "${name(p)} prayer time has begun"
    override fun snoozedBody(p: Prayer) = "The ${name(p)} adhan time has passed."
    override fun passedBody(p: Prayer, ago: Long) = "The ${name(p)} adhan time was ${minutes(ago)} ago."
}

/** Persian: a count takes the singular («دقیقه»), and numbers use Persian digits. Drafted; to be checked natively. */
object PersianAlarmWords : AlarmWords {
    private fun name(p: Prayer) = when (p) {
        Prayer.FAJR -> "صبح"
        Prayer.DHUHR -> "ظهر"
        Prayer.MAGHRIB -> "مغرب"
        Prayer.ISHA -> "عشا"
    }
    override fun prayerName(p: Prayer) = name(p)
    private fun digits(n: Long) = n.toString().map { if (it in '0'..'9') '۰' + (it - '0') else it }.joinToString("")
    override fun approaching(p: Prayer) = "وقت نماز ${name(p)} نزدیک است"
    override fun preOnTime(p: Prayer, preMinutes: Int) = "${digits(preMinutes.toLong())} دقیقه دیگر وقت اذان ${name(p)} است."
    override fun preRemaining(p: Prayer, remaining: Long) = "${digits(remaining)} دقیقه دیگر وقت اذان ${name(p)} است."
    override fun timeCame(p: Prayer) = "وقت نماز ${name(p)} رسید"
    override fun silentBody(p: Prayer) = "اکنون وقت اذان ${name(p)} است."
    override fun reminderBody(p: Prayer) = "اکنون وقت اذان ${name(p)} بر اساس فقه جعفری است."
    override fun begun(p: Prayer) = "وقت نماز ${name(p)} داخل شد"
    override fun snoozedBody(p: Prayer) = "وقت اذان ${name(p)} گذشته است."
    override fun passedBody(p: Prayer, ago: Long) = "${digits(ago)} دقیقه از وقت اذان ${name(p)} گذشته است."
}
