package com.dailydeeds.reminder.model

enum class SearchResultType(val labelArabic: String) {
    ALL("الكل"),
    QURAN("القرآن الكريم"),
    TAFSIR("ملخصات الميزان"),
    MAFATIH("مفاتيح الجنان"),
    WEEKDAY("أدعية وزيارات الأيام")
}

data class SearchResultItem(
    val id: String,
    val type: SearchResultType,
    val title: String,
    val snippet: String,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val mafatihItemId: String? = null,
    val weekdayKind: DayContentKind? = null
)
