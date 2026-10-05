package com.dailydeeds.reminder.model

enum class RevelationType(val arabicName: String) {
    MAKKI("مكية"),
    MADANI("مدنية")
}

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val revelationType: RevelationType,
    val ayahCount: Int,
    val juzStart: Int,
    val pageNumber: Int
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val juz: Int = 1,
    val page: Int = 1
)
