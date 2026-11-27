package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.util.AppLanguage

/**
 * The words of the morning notes ([DailyNotes]) and the blessed-eve reminder ([com.dailydeeds.reminder.calendar.BlessedEves])
 * in one UI language (v1.9.0). Pure, so the notes stay unit-tested; Arabic, the wording of every earlier version, is
 * the default. Occasion names and the Hijri date inside the notes are calendar data.
 */
interface NoteWords {
    fun occasionTitle(hijri: String): String
    val khumsTodayTitle: String
    val khumsTodayBody: String
    val khumsWeekTitle: String
    val khumsWeekBody: String
    fun eclipseTitle(type: EclipseType): String
    fun eclipseBody(clock: String, place: String): String
    val fridayNight: String
    val eveBody: String

    companion object {
        fun of(language: AppLanguage): NoteWords = when (language) {
            AppLanguage.ARABIC -> ArabicNoteWords
            AppLanguage.ENGLISH -> EnglishNoteWords
            AppLanguage.PERSIAN -> PersianNoteWords
        }
    }
}

object ArabicNoteWords : NoteWords {
    override fun occasionTitle(hijri: String) = "مناسبة اليوم: $hijri"
    override val khumsTodayTitle = "رأس السنة الخمسية"
    override val khumsTodayBody = "اليوم موعد حساب الخمس. افتح حاسبة الخمس لتقدير ما عليك."
    override val khumsWeekTitle = "اقتراب رأس السنة الخمسية"
    override val khumsWeekBody = "بقي أسبوع على موعد حساب الخمس."
    override fun eclipseTitle(type: EclipseType) = "خسوف القمر ${type.labelArabic}"
    override fun eclipseBody(clock: String, place: String) =
        "ذروة الخسوف عند $clock بتوقيت $place. تجب صلاة الآيات على من يرى الخسوف في بلده."
    override val fridayNight = "ليلة الجمعة"
    override val eveBody = "تبدأ الليلة المباركة بأذان المغرب هذا المساء."
}

object EnglishNoteWords : NoteWords {
    override fun occasionTitle(hijri: String) = "Occasion of the day: $hijri"
    override val khumsTodayTitle = "Your khums year starts today"
    override val khumsTodayBody = "Today is the date to calculate your khums. Open the khums calculator to estimate what is due."
    override val khumsWeekTitle = "Your khums year start is near"
    override val khumsWeekBody = "One week is left until the date to calculate your khums."
    override fun eclipseTitle(type: EclipseType) = when (type) {
        EclipseType.TOTAL -> "Total lunar eclipse"
        EclipseType.PARTIAL -> "Partial lunar eclipse"
        EclipseType.PENUMBRAL -> "Penumbral lunar eclipse"
        EclipseType.ANNULAR -> "Annular lunar eclipse"
        EclipseType.HYBRID -> "Hybrid lunar eclipse"
    }
    override fun eclipseBody(clock: String, place: String) =
        "The eclipse peaks at $clock, $place time. Salat al-Ayat is obligatory for whoever sees the eclipse in their land."
    override val fridayNight = "Eve of Friday"
    override val eveBody = "The blessed night begins with the Maghrib adhan this evening."
}

/** Drafted for v1.9.0; to be checked by a native speaker. */
object PersianNoteWords : NoteWords {
    override fun occasionTitle(hijri: String) = "مناسبت امروز: $hijri"
    override val khumsTodayTitle = "سر سال خمسی"
    override val khumsTodayBody = "امروز موعد محاسبه خمس است. ماشین‌حساب خمس را باز کنید تا مقدار آن را تخمین بزنید."
    override val khumsWeekTitle = "نزدیک شدن سر سال خمسی"
    override val khumsWeekBody = "یک هفته تا موعد محاسبه خمس مانده است."
    override fun eclipseTitle(type: EclipseType) = "خسوف " + when (type) {
        EclipseType.TOTAL -> "کلی"
        EclipseType.PARTIAL -> "جزئی"
        EclipseType.PENUMBRAL -> "نیم‌سایه‌ای"
        EclipseType.ANNULAR -> "حلقوی"
        EclipseType.HYBRID -> "ترکیبی"
    } + " ماه"
    override fun eclipseBody(clock: String, place: String) =
        "اوج خسوف ساعت $clock به وقت $place است. نماز آیات بر کسی که خسوف را در شهر خود ببیند واجب است."
    override val fridayNight = "شب جمعه"
    override val eveBody = "شب مبارک با اذان مغرب امروز آغاز می‌شود."
}
