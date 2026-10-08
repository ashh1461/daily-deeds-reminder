package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.model.RevelationType
import com.dailydeeds.reminder.model.Surah

object QuranDataProvider {

    val surahs: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", RevelationType.MAKKI, 7, 1, 1),
        Surah(2, "البقرة", "Al-Baqarah", RevelationType.MADANI, 286, 1, 2),
        Surah(3, "آل عمران", "Ali 'Imran", RevelationType.MADANI, 200, 3, 50),
        Surah(4, "النساء", "An-Nisa'", RevelationType.MADANI, 176, 4, 77),
        Surah(5, "المائدة", "Al-Ma'idah", RevelationType.MADANI, 120, 6, 106),
        Surah(6, "الأنعام", "Al-An'am", RevelationType.MAKKI, 165, 7, 128),
        Surah(7, "الأعراف", "Al-A'raf", RevelationType.MAKKI, 206, 8, 151),
        Surah(8, "الأنفال", "Al-Anfal", RevelationType.MADANI, 75, 9, 177),
        Surah(9, "التوبة", "At-Tawbah", RevelationType.MADANI, 129, 10, 187),
        Surah(10, "يونس", "Yunus", RevelationType.MAKKI, 109, 11, 208),
        Surah(11, "هود", "Hud", RevelationType.MAKKI, 123, 11, 221),
        Surah(12, "يوسف", "Yusuf", RevelationType.MAKKI, 111, 12, 235),
        Surah(13, "الرعد", "Ar-Ra'd", RevelationType.MADANI, 43, 13, 249),
        Surah(14, "إبراهيم", "Ibrahim", RevelationType.MAKKI, 52, 13, 255),
        Surah(15, "الحجر", "Al-Hijr", RevelationType.MAKKI, 99, 14, 262),
        Surah(16, "النحل", "An-Nahl", RevelationType.MAKKI, 128, 14, 267),
        Surah(17, "الإسراء", "Al-Isra'", RevelationType.MAKKI, 111, 15, 282),
        Surah(18, "الكهف", "Al-Kahf", RevelationType.MAKKI, 110, 15, 293),
        Surah(19, "مريم", "Maryam", RevelationType.MAKKI, 98, 16, 305),
        Surah(20, "طه", "Ta-Ha", RevelationType.MAKKI, 135, 16, 312),
        Surah(21, "الأنبياء", "Al-Anbiya'", RevelationType.MAKKI, 112, 17, 322),
        Surah(22, "الحج", "Al-Hajj", RevelationType.MADANI, 78, 17, 332),
        Surah(23, "المؤمنون", "Al-Mu'minun", RevelationType.MAKKI, 118, 18, 342),
        Surah(24, "النور", "An-Nur", RevelationType.MADANI, 64, 18, 350),
        Surah(25, "الفرقان", "Al-Furqan", RevelationType.MAKKI, 77, 18, 359),
        Surah(26, "الشعراء", "Ash-Shu'ara'", RevelationType.MAKKI, 227, 19, 367),
        Surah(27, "النمل", "An-Naml", RevelationType.MAKKI, 93, 19, 377),
        Surah(28, "القصص", "Al-Qasas", RevelationType.MAKKI, 88, 20, 385),
        Surah(29, "العنكبوت", "Al-'Ankabut", RevelationType.MAKKI, 69, 20, 396),
        Surah(30, "الروم", "Ar-Rum", RevelationType.MAKKI, 60, 21, 404),
        Surah(31, "لقمان", "Luqman", RevelationType.MAKKI, 34, 21, 411),
        Surah(32, "السجدة", "As-Sajdah", RevelationType.MAKKI, 30, 21, 415),
        Surah(33, "الأحزاب", "Al-Ahzab", RevelationType.MADANI, 73, 21, 418),
        Surah(34, "سبأ", "Saba'", RevelationType.MAKKI, 54, 22, 428),
        Surah(35, "فاطر", "Fatir", RevelationType.MAKKI, 45, 22, 434),
        Surah(36, "يس", "Ya-Sin", RevelationType.MAKKI, 83, 22, 440),
        Surah(37, "الصافات", "As-Saffat", RevelationType.MAKKI, 182, 23, 446),
        Surah(38, "ص", "Sad", RevelationType.MAKKI, 88, 23, 453),
        Surah(39, "الزمر", "Az-Zumar", RevelationType.MAKKI, 75, 23, 458),
        Surah(40, "غافر", "Ghafir", RevelationType.MAKKI, 85, 24, 467),
        Surah(41, "فصلت", "Fussilat", RevelationType.MAKKI, 54, 24, 477),
        Surah(42, "الشورى", "Ash-Shura", RevelationType.MAKKI, 53, 25, 483),
        Surah(43, "الزخرف", "Az-Zukhruf", RevelationType.MAKKI, 89, 25, 489),
        Surah(44, "الدخان", "Ad-Dukhan", RevelationType.MAKKI, 59, 25, 496),
        Surah(45, "الجاثية", "Al-Jathiyah", RevelationType.MAKKI, 37, 25, 499),
        Surah(46, "الأحقاف", "Al-Ahqaf", RevelationType.MAKKI, 35, 26, 502),
        Surah(47, "محمد", "Muhammad", RevelationType.MADANI, 38, 26, 507),
        Surah(48, "الفتح", "Al-Fath", RevelationType.MADANI, 29, 26, 511),
        Surah(49, "الحجرات", "Al-Hujurat", RevelationType.MADANI, 18, 26, 515),
        Surah(50, "ق", "Qaf", RevelationType.MAKKI, 45, 26, 518),
        Surah(51, "الذاريات", "Adh-Dhariyat", RevelationType.MAKKI, 60, 26, 520),
        Surah(52, "الطور", "At-Tur", RevelationType.MAKKI, 49, 27, 523),
        Surah(53, "النجم", "An-Najm", RevelationType.MAKKI, 62, 27, 526),
        Surah(54, "القمر", "Al-Qamar", RevelationType.MAKKI, 55, 27, 528),
        Surah(55, "الرحمن", "Ar-Rahman", RevelationType.MADANI, 78, 27, 531),
        Surah(56, "الواقعة", "Al-Waqi'ah", RevelationType.MAKKI, 96, 27, 534),
        Surah(57, "الحديد", "Al-Hadid", RevelationType.MADANI, 29, 27, 537),
        Surah(58, "المجادلة", "Al-Mujadila", RevelationType.MADANI, 22, 28, 542),
        Surah(59, "الحشر", "Al-Hashr", RevelationType.MADANI, 24, 28, 545),
        Surah(60, "الممتحنة", "Al-Mumtahanah", RevelationType.MADANI, 13, 28, 549),
        Surah(61, "الصف", "As-Saff", RevelationType.MADANI, 14, 28, 551),
        Surah(62, "الجمعة", "Al-Jumu'ah", RevelationType.MADANI, 11, 28, 553),
        Surah(63, "المنافقون", "Al-Munafiqun", RevelationType.MADANI, 11, 28, 554),
        Surah(64, "التغابن", "At-Taghabun", RevelationType.MADANI, 18, 28, 556),
        Surah(65, "الطلاق", "At-Talaq", RevelationType.MADANI, 12, 28, 558),
        Surah(66, "التحريم", "At-Tahrim", RevelationType.MADANI, 12, 28, 560),
        Surah(67, "الملك", "Al-Mulk", RevelationType.MAKKI, 30, 29, 562),
        Surah(68, "القلم", "Al-Qalam", RevelationType.MAKKI, 52, 29, 564),
        Surah(69, "الحاقة", "Al-Haqqah", RevelationType.MAKKI, 52, 29, 566),
        Surah(70, "المعارج", "Al-Ma'arij", RevelationType.MAKKI, 44, 29, 568),
        Surah(71, "نوح", "Nuh", RevelationType.MAKKI, 28, 29, 570),
        Surah(72, "الجن", "Al-Jinn", RevelationType.MAKKI, 28, 29, 572),
        Surah(73, "المزمل", "Al-Muzzammil", RevelationType.MAKKI, 20, 29, 574),
        Surah(74, "المدثر", "Al-Muddaththir", RevelationType.MAKKI, 56, 29, 575),
        Surah(75, "القيامة", "Al-Qiyamah", RevelationType.MAKKI, 40, 29, 577),
        Surah(76, "الإنسان", "Al-Insan", RevelationType.MADANI, 31, 29, 578),
        Surah(77, "المرسلات", "Al-Mursalat", RevelationType.MAKKI, 50, 29, 580),
        Surah(78, "النبأ", "An-Naba'", RevelationType.MAKKI, 40, 30, 582),
        Surah(79, "النازعات", "An-Nazi'at", RevelationType.MAKKI, 46, 30, 583),
        Surah(80, "عبس", "'Abasa", RevelationType.MAKKI, 42, 30, 585),
        Surah(81, "التكوير", "At-Takwir", RevelationType.MAKKI, 29, 30, 586),
        Surah(82, "الانفطار", "Al-Infitar", RevelationType.MAKKI, 19, 30, 587),
        Surah(83, "المطففين", "Al-Mutaffifin", RevelationType.MAKKI, 36, 30, 587),
        Surah(84, "الانشقاق", "Al-Inshiqaq", RevelationType.MAKKI, 25, 30, 589),
        Surah(85, "البروج", "Al-Buruj", RevelationType.MAKKI, 22, 30, 590),
        Surah(86, "الطارق", "At-Tariq", RevelationType.MAKKI, 17, 30, 591),
        Surah(87, "الأعلى", "Al-A'la", RevelationType.MAKKI, 19, 30, 591),
        Surah(88, "الغاشية", "Al-Ghashiyah", RevelationType.MAKKI, 26, 30, 592),
        Surah(89, "الفجر", "Al-Fajr", RevelationType.MAKKI, 30, 30, 593),
        Surah(90, "البلد", "Al-Balad", RevelationType.MAKKI, 20, 30, 594),
        Surah(91, "الشمس", "Ash-Shams", RevelationType.MAKKI, 15, 30, 595),
        Surah(92, "الليل", "Al-Layl", RevelationType.MAKKI, 21, 30, 595),
        Surah(93, "الضحى", "Ad-Duha", RevelationType.MAKKI, 11, 30, 596),
        Surah(94, "الشرح", "Ash-Sharh", RevelationType.MAKKI, 8, 30, 596),
        Surah(95, "التين", "At-Tin", RevelationType.MAKKI, 8, 30, 597),
        Surah(96, "العلق", "Al-'Alaq", RevelationType.MAKKI, 19, 30, 597),
        Surah(97, "القدر", "Al-Qadr", RevelationType.MAKKI, 5, 30, 598),
        Surah(98, "البينة", "Al-Bayyinah", RevelationType.MADANI, 8, 30, 598),
        Surah(99, "الزلزلة", "Az-Zalzalah", RevelationType.MADANI, 8, 30, 599),
        Surah(100, "العاديات", "Al-'Adiyat", RevelationType.MAKKI, 11, 30, 599),
        Surah(101, "القارعة", "Al-Qari'ah", RevelationType.MAKKI, 11, 30, 600),
        Surah(102, "التكاثر", "At-Takathur", RevelationType.MAKKI, 8, 30, 600),
        Surah(103, "العصر", "Al-'Asr", RevelationType.MAKKI, 3, 30, 601),
        Surah(104, "الهمزة", "Al-Humazah", RevelationType.MAKKI, 9, 30, 601),
        Surah(105, "الفيل", "Al-Fil", RevelationType.MAKKI, 5, 30, 601),
        Surah(106, "قريش", "Quraysh", RevelationType.MAKKI, 4, 30, 602),
        Surah(107, "الماعون", "Al-Ma'un", RevelationType.MAKKI, 7, 30, 602),
        Surah(108, "الكوثر", "Al-Kawthar", RevelationType.MAKKI, 3, 30, 602),
        Surah(109, "الكافرون", "Al-Kafirun", RevelationType.MAKKI, 6, 30, 603),
        Surah(110, "النصر", "An-Nasr", RevelationType.MADANI, 3, 30, 603),
        Surah(111, "المسد", "Al-Masad", RevelationType.MAKKI, 5, 30, 603),
        Surah(112, "الإخلاص", "Al-Ikhlas", RevelationType.MAKKI, 4, 30, 604),
        Surah(113, "الفلق", "Al-Falaq", RevelationType.MAKKI, 5, 30, 604),
        Surah(114, "الناس", "An-Nas", RevelationType.MAKKI, 6, 30, 604)
    )

    /** Resource bundled in the APK: one ayah per line as `surah|ayah|juz|page|text`. */
    const val RESOURCE_PATH = "quran/uthmani.txt"

    private val ayahsBySurah: Map<Int, List<Ayah>> by lazy { loadAyahs() }

    fun getAyahs(surahNumber: Int): List<Ayah> = ayahsBySurah[surahNumber].orEmpty()

    fun getAyah(surahNumber: Int, ayahNumber: Int): Ayah? =
        ayahsBySurah[surahNumber]?.getOrNull(ayahNumber - 1)

    private fun loadAyahs(): Map<Int, List<Ayah>> {
        val stream = QuranDataProvider::class.java.classLoader?.getResourceAsStream(RESOURCE_PATH)
            ?: error("Missing bundled Quran resource: $RESOURCE_PATH")
        return stream.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.filter { it.isNotBlank() }
                .map { line ->
                    val f = line.split('|', limit = 5)
                    Ayah(f[0].toInt(), f[1].toInt(), f[4], f[2].toInt(), f[3].toInt())
                }
                .groupBy { it.surahNumber }
        }
    }
}
