package com.dailydeeds.reminder.model

import java.time.DayOfWeek

/** نوع المحتوى الأسبوعي: دعاء اليوم أو زيارة اليوم. */
enum class DayContentKind(val titleArabic: String) {
    DUA("أدعية الأيام"),
    ZIYARAT("زيارات الأيام")
}

/**
 * محتوى مرتبط بيوم من أيام الأسبوع (دعاء أو زيارة) كما ورد في مفاتيح الجنان.
 *
 * @param honoree صاحب اليوم (المعصوم أو المعصومون المنسوب إليهم اليوم)
 * @param isExcerpt true إذا كان النص مقتطفاً وليس النص الكامل
 */
data class DayContent(
    val day: DayOfWeek,
    val kind: DayContentKind,
    val dayNameArabic: String,
    val honoree: String,
    val title: String,
    val text: String,
    val source: String = "مفاتيح الجنان • أعمال أيام الأسبوع",
    val isExcerpt: Boolean = false
)

