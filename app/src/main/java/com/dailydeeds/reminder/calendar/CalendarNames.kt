package com.dailydeeds.reminder.calendar

import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.AppLanguage
import java.time.DayOfWeek

/**
 * Names in the calendar and place data, in each UI language (v1.9.0): Hijri months and dates, the occasions of the
 * Ahl al-Bayt (peace be upon them), weekdays, and the suggested cities. Arabic returns the data exactly as stored,
 * so nothing changes for Arabic users. Stored values (a saved place's name, occasion titles) are never rewritten;
 * only their display is translated. Pure, so it is unit-tested; the Persian names are a draft to be checked natively.
 */
object CalendarNames {

    private val MONTHS_EN = listOf(
        "Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani", "Jumada al-Ula", "Jumada al-Akhirah",
        "Rajab", "Shaban", "Ramadan", "Shawwal", "Dhu al-Qadah", "Dhu al-Hijjah"
    )
    private val MONTHS_FA = listOf(
        "محرم", "صفر", "ربیع‌الاول", "ربیع‌الثانی", "جمادی‌الاول", "جمادی‌الثانی",
        "رجب", "شعبان", "رمضان", "شوال", "ذی‌القعده", "ذی‌الحجه"
    )

    fun month(month: Int, language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> ShiaCalendar.MONTH_NAMES[month - 1]
        AppLanguage.ENGLISH -> MONTHS_EN[month - 1]
        AppLanguage.PERSIAN -> MONTHS_FA[month - 1]
    }

    fun date(day: HijriDay, language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> day.toString()
        AppLanguage.ENGLISH -> "${day.day} ${month(day.month, language)} ${day.year} AH"
        AppLanguage.PERSIAN -> "${persianDigits(day.day)} ${month(day.month, language)} ${persianDigits(day.year)} ق"
    }

    fun occasion(o: Occasion, language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> o.title
        AppLanguage.ENGLISH -> OCCASIONS[o.title]?.first ?: o.title
        AppLanguage.PERSIAN -> OCCASIONS[o.title]?.second ?: o.title
    }

    /** As [Occasion.displayTitle]: a night says it begins this evening. */
    fun occasionDisplay(o: Occasion, language: AppLanguage): String {
        if (language == AppLanguage.ARABIC) return o.displayTitle
        if (o.kind != OccasionKind.NIGHT) return occasion(o, language)
        val suffix = if (language == AppLanguage.ENGLISH) "(begins at Maghrib today)" else "(از مغرب امروز آغاز می‌شود)"
        return "${occasion(o, language)} $suffix"
    }

    fun weekday(day: DayOfWeek, language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> when (day) {
            DayOfWeek.SATURDAY -> "السبت"
            DayOfWeek.SUNDAY -> "الأحد"
            DayOfWeek.MONDAY -> "الاثنين"
            DayOfWeek.TUESDAY -> "الثلاثاء"
            DayOfWeek.WEDNESDAY -> "الأربعاء"
            DayOfWeek.THURSDAY -> "الخميس"
            DayOfWeek.FRIDAY -> "الجمعة"
        }
        AppLanguage.ENGLISH -> day.name.lowercase().replaceFirstChar { it.uppercase() }
        AppLanguage.PERSIAN -> when (day) {
            DayOfWeek.SATURDAY -> "شنبه"
            DayOfWeek.SUNDAY -> "یکشنبه"
            DayOfWeek.MONDAY -> "دوشنبه"
            DayOfWeek.TUESDAY -> "سه‌شنبه"
            DayOfWeek.WEDNESDAY -> "چهارشنبه"
            DayOfWeek.THURSDAY -> "پنجشنبه"
            DayOfWeek.FRIDAY -> "جمعه"
        }
    }

    /** A suggested city in the app language; any other place (searched, coordinates, located) keeps its stored name. */
    fun place(p: Place, language: AppLanguage): String = when (language) {
        AppLanguage.ARABIC -> p.name
        AppLanguage.ENGLISH -> CITIES[p.name]?.first ?: p.name
        AppLanguage.PERSIAN -> CITIES[p.name]?.second ?: p.name
    }

    private fun persianDigits(n: Int) = n.toString().map { if (it in '0'..'9') '۰' + (it - '0') else it }.joinToString("")

    /** Occasion title (as in [ShiaCalendar.OCCASIONS]) to its English and Persian titles. */
    private val OCCASIONS: Map<String, Pair<String, String>> = mapOf(
        "رأس السنة الهجرية" to ("Islamic New Year" to "آغاز سال هجری قمری"),
        "يوم عاشوراء: استشهاد الإمام الحسين (ع)" to ("Day of Ashura: martyrdom of Imam Husayn (AS)" to "روز عاشورا: شهادت امام حسین (ع)"),
        "استشهاد الإمام زين العابدين (ع)" to ("Martyrdom of Imam Zayn al-Abidin (AS)" to "شهادت امام زین‌العابدین (ع)"),
        "مولد الإمام محمد الباقر (ع) على مختار صاحب المفاتيح" to
            ("Birth of Imam Muhammad al-Baqir (AS), as chosen by the author of Mafatih" to "ولادت امام محمد باقر (ع) بنا بر قول برگزیده صاحب مفاتیح"),
        "مولد الإمام موسى الكاظم (ع)" to ("Birth of Imam Musa al-Kazim (AS)" to "ولادت امام موسی کاظم (ع)"),
        "أربعينية الإمام الحسين (ع)" to ("Arbaeen of Imam Husayn (AS)" to "اربعین امام حسین (ع)"),
        "وفاة النبي (ص) واستشهاد الإمام الحسن المجتبى (ع)" to
            ("Passing of the Prophet (PBUH) and martyrdom of Imam Hasan al-Mujtaba (AS)" to "رحلت پیامبر اکرم (ص) و شهادت امام حسن مجتبی (ع)"),
        "استشهاد الإمام الرضا (ع)" to ("Martyrdom of Imam al-Rida (AS)" to "شهادت امام رضا (ع)"),
        "استشهاد الإمام الحسن العسكري (ع)" to ("Martyrdom of Imam Hasan al-Askari (AS)" to "شهادت امام حسن عسکری (ع)"),
        "بدء إمامة الإمام المهدي (عج)" to ("Start of the Imamate of Imam al-Mahdi (AJ)" to "آغاز امامت امام مهدی (عج)"),
        "مولد النبي الأكرم (ص) عند إخواننا أهل السنة" to
            ("Birth of the Prophet (PBUH), as observed by our Sunni brothers" to "ولادت پیامبر اکرم (ص) نزد برادران اهل سنت"),
        "مولد النبي الأكرم (ص) والإمام جعفر الصادق (ع)" to
            ("Birth of the Prophet (PBUH) and of Imam Jafar al-Sadiq (AS)" to "ولادت پیامبر اکرم (ص) و امام جعفر صادق (ع)"),
        "مولد الإمام الحسن العسكري (ع)" to ("Birth of Imam Hasan al-Askari (AS)" to "ولادت امام حسن عسکری (ع)"),
        "وفاة السيدة فاطمة المعصومة (ع)" to ("Passing of Lady Fatima al-Masuma (AS)" to "وفات حضرت فاطمه معصومه (س)"),
        "مولد السيدة زينب (ع)" to ("Birth of Lady Zaynab (AS)" to "ولادت حضرت زینب (س)"),
        "استشهاد السيدة فاطمة الزهراء (ع) على إحدى الروايات" to
            ("Martyrdom of Lady Fatima al-Zahra (AS), by one narration" to "شهادت حضرت فاطمه زهرا (س) بنا بر یک روایت"),
        "مولد السيدة فاطمة الزهراء (ع)" to ("Birth of Lady Fatima al-Zahra (AS)" to "ولادت حضرت فاطمه زهرا (س)"),
        "مولد الإمام محمد الباقر (ع) على قول" to ("Birth of Imam Muhammad al-Baqir (AS), by one report" to "ولادت امام محمد باقر (ع) بنا بر یک قول"),
        "استشهاد الإمام علي الهادي (ع)" to ("Martyrdom of Imam Ali al-Hadi (AS)" to "شهادت امام علی هادی (ع)"),
        "مولد الإمام محمد الجواد (ع)" to ("Birth of Imam Muhammad al-Jawad (AS)" to "ولادت امام محمد جواد (ع)"),
        "مولد أمير المؤمنين الإمام علي (ع)" to ("Birth of the Commander of the Faithful, Imam Ali (AS)" to "ولادت امیرالمؤمنین امام علی (ع)"),
        "وفاة السيدة زينب (ع)" to ("Passing of Lady Zaynab (AS)" to "وفات حضرت زینب (س)"),
        "استشهاد الإمام موسى الكاظم (ع)" to ("Martyrdom of Imam Musa al-Kazim (AS)" to "شهادت امام موسی کاظم (ع)"),
        "المبعث النبوي الشريف" to ("The Prophet’s Mission (al-Mabath)" to "مبعث پیامبر اکرم (ص)"),
        "مولد الإمام الحسين (ع)" to ("Birth of Imam Husayn (AS)" to "ولادت امام حسین (ع)"),
        "مولد أبي الفضل العباس (ع)" to ("Birth of Abu al-Fadl al-Abbas (AS)" to "ولادت حضرت ابوالفضل العباس (ع)"),
        "مولد الإمام زين العابدين (ع)" to ("Birth of Imam Zayn al-Abidin (AS)" to "ولادت امام زین‌العابدین (ع)"),
        "مولد علي الأكبر (ع)" to ("Birth of Ali al-Akbar (AS)" to "ولادت حضرت علی اکبر (ع)"),
        "مولد الإمام المهدي (عج)" to ("Birth of Imam al-Mahdi (AJ)" to "ولادت امام مهدی (عج)"),
        "ليلة النصف من شعبان" to ("The night of mid-Shaban" to "شب نیمه شعبان"),
        "غرة شهر رمضان المبارك" to ("First day of the blessed month of Ramadan" to "آغاز ماه مبارک رمضان"),
        "وفاة السيدة خديجة الكبرى (ع)" to ("Passing of Lady Khadija al-Kubra (AS)" to "وفات حضرت خدیجه کبری (س)"),
        "مولد الإمام الحسن المجتبى (ع)" to ("Birth of Imam Hasan al-Mujtaba (AS)" to "ولادت امام حسن مجتبی (ع)"),
        "ضربة أمير المؤمنين (ع)" to ("The striking of the Commander of the Faithful (AS)" to "ضربت خوردن امیرالمؤمنین (ع)"),
        "ليلة القدر الأولى" to ("The first Night of Qadr" to "شب قدر اول"),
        "استشهاد أمير المؤمنين (ع)" to ("Martyrdom of the Commander of the Faithful (AS)" to "شهادت امیرالمؤمنین (ع)"),
        "ليلة القدر الثانية" to ("The second Night of Qadr" to "شب قدر دوم"),
        "ليلة القدر الثالثة" to ("The third Night of Qadr" to "شب قدر سوم"),
        "عيد الفطر المبارك" to ("Eid al-Fitr" to "عید سعید فطر"),
        "استشهاد الإمام جعفر الصادق (ع)" to ("Martyrdom of Imam Jafar al-Sadiq (AS)" to "شهادت امام جعفر صادق (ع)"),
        "مولد السيدة فاطمة المعصومة (ع)" to ("Birth of Lady Fatima al-Masuma (AS)" to "ولادت حضرت فاطمه معصومه (س)"),
        "مولد الإمام علي الرضا (ع)" to ("Birth of Imam Ali al-Rida (AS)" to "ولادت امام علی بن موسی الرضا (ع)"),
        "استشهاد الإمام محمد الجواد (ع)" to ("Martyrdom of Imam Muhammad al-Jawad (AS)" to "شهادت امام محمد جواد (ع)"),
        "استشهاد الإمام محمد الباقر (ع)" to ("Martyrdom of Imam Muhammad al-Baqir (AS)" to "شهادت امام محمد باقر (ع)"),
        "يوم عرفة" to ("Day of Arafah" to "روز عرفه"),
        "عيد الأضحى المبارك" to ("Eid al-Adha" to "عید سعید قربان"),
        "مولد الإمام علي الهادي (ع)" to ("Birth of Imam Ali al-Hadi (AS)" to "ولادت امام علی هادی (ع)"),
        "عيد الغدير الأغر" to ("Eid al-Ghadir" to "عید سعید غدیر"),
        "يوم المباهلة" to ("Day of Mubahala" to "روز مباهله"),
    )

    /** Suggested city name (as in [com.dailydeeds.reminder.model.PlacePresets]) to its English and Persian names. */
    private val CITIES: Map<String, Pair<String, String>> = mapOf(
        "بيروت" to ("Beirut" to "بیروت"),
        "صور" to ("Tyre" to "صور"),
        "النبطية" to ("Nabatieh" to "نبطیه"),
        "بعلبك" to ("Baalbek" to "بعلبک"),
        "دمشق" to ("Damascus" to "دمشق"),
        "بغداد" to ("Baghdad" to "بغداد"),
        "النجف الأشرف" to ("Najaf" to "نجف اشرف"),
        "كربلاء المقدسة" to ("Karbala" to "کربلای معلی"),
        "الكاظمية" to ("Kadhimiya" to "کاظمین"),
        "سامراء" to ("Samarra" to "سامرا"),
        "البصرة" to ("Basra" to "بصره"),
        "قم المقدسة" to ("Qom" to "قم"),
        "مشهد المقدسة" to ("Mashhad" to "مشهد مقدس"),
        "طهران" to ("Tehran" to "تهران"),
        "الكويت" to ("Kuwait City" to "کویت"),
        "المنامة" to ("Manama" to "منامه"),
        "القطيف" to ("Qatif" to "قطیف"),
        "الأحساء" to ("Al-Ahsa" to "احساء"),
        "المدينة المنورة" to ("Medina" to "مدینه منوره"),
        "مكة المكرمة" to ("Mecca" to "مکه مکرمه"),
        "دبي" to ("Dubai" to "دبی"),
        "القاهرة" to ("Cairo" to "قاهره"),
        "كراتشي" to ("Karachi" to "کراچی"),
        "لاهور" to ("Lahore" to "لاهور"),
        "لندن" to ("London" to "لندن"),
        "ديربورن" to ("Dearborn" to "دیربورن"),
        "تورونتو" to ("Toronto" to "تورنتو"),
        "سيدني" to ("Sydney" to "سیدنی"),
    )
}
