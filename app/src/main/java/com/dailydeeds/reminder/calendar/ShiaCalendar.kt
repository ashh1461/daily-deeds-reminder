package com.dailydeeds.reminder.calendar

import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

enum class OccasionKind(val labelArabic: String) {
    EID("عيد"),
    BIRTH("مولد"),
    MARTYRDOM("وفاة / استشهاد"),
    NIGHT("ليلة مباركة"),
    EVENT("مناسبة")
}

/**
 * A Hijri date (month 1..12). Day 30 on a 29-day month means "the last day of the month".
 * A [OccasionKind.NIGHT] occasion is the night *before* its day (the Islamic day starts at sunset), so it is
 * listed on the civil date of its eve.
 */
data class Occasion(val month: Int, val day: Int, val title: String, val kind: OccasionKind) {
    /** The title as shown in lists and notifications; a night says it begins this evening. */
    val displayTitle: String get() = if (kind == OccasionKind.NIGHT) "$title (تبدأ بمغرب هذا اليوم)" else title
}

data class HijriDay(val year: Int, val month: Int, val day: Int) {
    val monthName: String get() = ShiaCalendar.MONTH_NAMES[month - 1]
    override fun toString() = "$day $monthName $year هـ"
}

/**
 * Hijri calendar helper with the occasions of the Ahl al-Bayt (peace be upon them).
 *
 * Dates come from the Umm al-Qura tables in java.time and can differ by a day from local moon
 * sighting, so every lookup takes a user-adjustable [dayOffset] (-2..+2). Several occasions are
 * reported on more than one date in the sources; the commonly observed one is listed.
 */
object ShiaCalendar {
    val MONTH_NAMES = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر", "جمادى الأولى", "جمادى الآخرة",
        "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    const val MAX_OFFSET = 2

    fun toHijri(date: LocalDate, dayOffset: Int = 0): HijriDay {
        val shifted = date.plusDays(dayOffset.toLong())
        val h = HijrahDate.from(shifted)
        return HijriDay(h.get(ChronoField.YEAR), h.get(ChronoField.MONTH_OF_YEAR), h.get(ChronoField.DAY_OF_MONTH))
    }

    private fun monthLength(date: LocalDate, dayOffset: Int): Int =
        HijrahDate.from(date.plusDays(dayOffset.toLong())).lengthOfMonth()

    /**
     * The occasions of the civil [date]: those of its Hijri day, plus the blessed nights that begin at its sunset
     * (a night belongs to the following Hijri day, so the 19th-of-Ramadan night falls on the evening of the 18th).
     */
    fun occasionsOn(date: LocalDate, dayOffset: Int = 0): List<Occasion> =
        matching(date, dayOffset).filter { it.kind != OccasionKind.NIGHT } +
            matching(date.plusDays(1), dayOffset).filter { it.kind == OccasionKind.NIGHT }

    private fun matching(date: LocalDate, dayOffset: Int): List<Occasion> {
        val h = toHijri(date, dayOffset)
        val last = monthLength(date, dayOffset)
        return OCCASIONS.filter { o ->
            o.month == h.month && (o.day == h.day || (o.day == 30 && last == 29 && h.day == 29))
        }
    }

    data class Upcoming(val date: LocalDate, val hijri: HijriDay, val occasion: Occasion, val daysAway: Int)

    /** Occasions from [from] (inclusive) over the next [days] days, in date order. */
    fun upcoming(from: LocalDate, days: Int, dayOffset: Int = 0): List<Upcoming> {
        val out = ArrayList<Upcoming>()
        for (i in 0..days) {
            val d = from.plusDays(i.toLong())
            val h = toHijri(d, dayOffset)
            occasionsOn(d, dayOffset).forEach { out += Upcoming(d, h, it, i) }
        }
        return out
    }

    val OCCASIONS: List<Occasion> = listOf(
        Occasion(1, 1, "رأس السنة الهجرية", OccasionKind.EVENT),
        Occasion(1, 10, "يوم عاشوراء: استشهاد الإمام الحسين (ع)", OccasionKind.MARTYRDOM),
        Occasion(1, 25, "استشهاد الإمام زين العابدين (ع)", OccasionKind.MARTYRDOM),
        // Mafatih al-Jinan (al-Qummi), chapter of Safar: al-Baqir's birth on the 3rd is the author's own choice
        // (1 Rajab is listed there as another opinion), al-Kazim's birth at Abwa on the 7th.
        Occasion(2, 3, "مولد الإمام محمد الباقر (ع) على مختار صاحب المفاتيح", OccasionKind.BIRTH),
        Occasion(2, 7, "مولد الإمام موسى الكاظم (ع)", OccasionKind.BIRTH),
        Occasion(2, 20, "أربعينية الإمام الحسين (ع)", OccasionKind.EVENT),
        Occasion(2, 28, "وفاة النبي (ص) واستشهاد الإمام الحسن المجتبى (ع)", OccasionKind.MARTYRDOM),
        Occasion(2, 30, "استشهاد الإمام الرضا (ع)", OccasionKind.MARTYRDOM),
        Occasion(3, 8, "استشهاد الإمام الحسن العسكري (ع)", OccasionKind.MARTYRDOM),
        Occasion(3, 9, "بدء إمامة الإمام المهدي (عج)", OccasionKind.EVENT),
        Occasion(3, 12, "مولد النبي الأكرم (ص) عند إخواننا أهل السنة", OccasionKind.BIRTH),
        Occasion(3, 17, "مولد النبي الأكرم (ص) والإمام جعفر الصادق (ع)", OccasionKind.BIRTH),
        Occasion(4, 8, "مولد الإمام الحسن العسكري (ع)", OccasionKind.BIRTH),
        Occasion(4, 10, "وفاة السيدة فاطمة المعصومة (ع)", OccasionKind.MARTYRDOM),
        Occasion(5, 5, "مولد السيدة زينب (ع)", OccasionKind.BIRTH),
        Occasion(6, 3, "استشهاد السيدة فاطمة الزهراء (ع) على إحدى الروايات", OccasionKind.MARTYRDOM),
        Occasion(6, 20, "مولد السيدة فاطمة الزهراء (ع)", OccasionKind.BIRTH),
        Occasion(7, 1, "مولد الإمام محمد الباقر (ع) على قول", OccasionKind.BIRTH),
        Occasion(7, 3, "استشهاد الإمام علي الهادي (ع)", OccasionKind.MARTYRDOM),
        Occasion(7, 10, "مولد الإمام محمد الجواد (ع)", OccasionKind.BIRTH),
        Occasion(7, 13, "مولد أمير المؤمنين الإمام علي (ع)", OccasionKind.BIRTH),
        Occasion(7, 15, "وفاة السيدة زينب (ع)", OccasionKind.MARTYRDOM),
        Occasion(7, 25, "استشهاد الإمام موسى الكاظم (ع)", OccasionKind.MARTYRDOM),
        Occasion(7, 27, "المبعث النبوي الشريف", OccasionKind.EID),
        Occasion(8, 3, "مولد الإمام الحسين (ع)", OccasionKind.BIRTH),
        Occasion(8, 4, "مولد أبي الفضل العباس (ع)", OccasionKind.BIRTH),
        Occasion(8, 5, "مولد الإمام زين العابدين (ع)", OccasionKind.BIRTH),
        Occasion(8, 11, "مولد علي الأكبر (ع)", OccasionKind.BIRTH),
        Occasion(8, 15, "مولد الإمام المهدي (عج)", OccasionKind.BIRTH),
        Occasion(8, 15, "ليلة النصف من شعبان", OccasionKind.NIGHT),
        Occasion(9, 1, "غرة شهر رمضان المبارك", OccasionKind.EVENT),
        Occasion(9, 10, "وفاة السيدة خديجة الكبرى (ع)", OccasionKind.MARTYRDOM),
        Occasion(9, 15, "مولد الإمام الحسن المجتبى (ع)", OccasionKind.BIRTH),
        // The strike (at the dawn prayer) and the martyrdom fall on the day; the Laylat al-Qadr nights precede it.
        Occasion(9, 19, "ضربة أمير المؤمنين (ع)", OccasionKind.EVENT),
        Occasion(9, 19, "ليلة القدر الأولى", OccasionKind.NIGHT),
        Occasion(9, 21, "استشهاد أمير المؤمنين (ع)", OccasionKind.MARTYRDOM),
        Occasion(9, 21, "ليلة القدر الثانية", OccasionKind.NIGHT),
        Occasion(9, 23, "ليلة القدر الثالثة", OccasionKind.NIGHT),
        Occasion(10, 1, "عيد الفطر المبارك", OccasionKind.EID),
        Occasion(10, 25, "استشهاد الإمام جعفر الصادق (ع)", OccasionKind.MARTYRDOM),
        Occasion(11, 1, "مولد السيدة فاطمة المعصومة (ع)", OccasionKind.BIRTH),
        Occasion(11, 11, "مولد الإمام علي الرضا (ع)", OccasionKind.BIRTH),
        Occasion(11, 29, "استشهاد الإمام محمد الجواد (ع)", OccasionKind.MARTYRDOM),
        // Mafatih al-Jinan, chapter of Dhu al-Hijja: al-Baqir's martyrdom on the 7th, al-Hadi's birth on the 15th.
        Occasion(12, 7, "استشهاد الإمام محمد الباقر (ع)", OccasionKind.MARTYRDOM),
        Occasion(12, 9, "يوم عرفة", OccasionKind.EVENT),
        Occasion(12, 10, "عيد الأضحى المبارك", OccasionKind.EID),
        Occasion(12, 15, "مولد الإمام علي الهادي (ع)", OccasionKind.BIRTH),
        Occasion(12, 18, "عيد الغدير الأغر", OccasionKind.EID),
        Occasion(12, 24, "يوم المباهلة", OccasionKind.EVENT)
    )
}
