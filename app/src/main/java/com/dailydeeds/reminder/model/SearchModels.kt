package com.dailydeeds.reminder.model

enum class SearchResultType(val labelArabic: String) {
    ALL("الكل"),
    QURAN("القرآن الكريم"),
    MAFATIH("مفاتيح الجنان"),
    SAHIFA("الصحيفة السجادية"),
    WEEKDAY("أدعية وزيارات الأيام"),
    DEEDS("الأعمال اليومية")
}

data class SearchResultItem(
    val id: String,
    val type: SearchResultType,
    val title: String,
    val snippet: String,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val mafatihItemId: String? = null,
    val weekdayKind: DayContentKind? = null,
    /** The day of a weekday du'a or ziyarah, so the result opens on that day's text. */
    val weekdayDay: java.time.DayOfWeek? = null,
    val deedId: Int? = null
)
