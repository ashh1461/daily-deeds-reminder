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

/** A Hijri date (month 1..12). Day 30 on a 29-day month means "the last day of the month". */
data class Occasion(val month: Int, val day: Int, val title: String, val kind: OccasionKind)

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

    fun occasionsOn(date: LocalDate, dayOffset: Int = 0): List<Occasion> {
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
        Occasion(7, 1, "مولد الإمام محمد الباقر (ع)", OccasionKind.BIRTH),
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
        Occasion(8, 15, "مولد الإمام المهدي (عج) وليلة النصف من شعبان", OccasionKind.BIRTH),
        Occasion(9, 1, "غرة شهر رمضان المبارك", OccasionKind.EVENT),
        Occasion(9, 10, "وفاة السيدة خديجة الكبرى (ع)", OccasionKind.MARTYRDOM),
        Occasion(9, 15, "مولد الإمام الحسن المجتبى (ع)", OccasionKind.BIRTH),
        Occasion(9, 19, "ليلة القدر الأولى وضربة أمير المؤمنين (ع)", OccasionKind.NIGHT),
        Occasion(9, 21, "ليلة القدر الثانية واستشهاد أمير المؤمنين (ع)", OccasionKind.NIGHT),
        Occasion(9, 23, "ليلة القدر الثالثة", OccasionKind.NIGHT),
        Occasion(10, 1, "عيد الفطر المبارك", OccasionKind.EID),
        Occasion(10, 25, "استشهاد الإمام جعفر الصادق (ع)", OccasionKind.MARTYRDOM),
        Occasion(11, 1, "مولد السيدة فاطمة المعصومة (ع)", OccasionKind.BIRTH),
        Occasion(11, 11, "مولد الإمام علي الرضا (ع)", OccasionKind.BIRTH),
        Occasion(11, 29, "استشهاد الإمام محمد الجواد (ع)", OccasionKind.MARTYRDOM),
        Occasion(12, 9, "يوم عرفة", OccasionKind.EVENT),
        Occasion(12, 10, "عيد الأضحى المبارك", OccasionKind.EID),
        Occasion(12, 18, "عيد الغدير الأغر", OccasionKind.EID),
        Occasion(12, 24, "يوم المباهلة", OccasionKind.EVENT)
    )
}
