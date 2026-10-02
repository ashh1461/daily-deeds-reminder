package com.dailydeeds.reminder.model

enum class DeedType {
    READING,
    COUNTER,
    MULTI_STAGE_COUNTER
}

enum class DeedCategory(val titleArabic: String) {
    ALL("الكل"),
    MORNING_EVENING("صباحاً ومساءً"),
    AFTER_PRAYER("عقيب الصلوات"),
    TASBEEH("أذكار وتسابيح"),
    QURAN("آيات وسور")
}

data class TasbeehStage(
    val stageIndex: Int,
    val phrase: String,
    val targetCount: Int,
    val description: String
)

data class Deed(
    val id: Int,
    val title: String,
    val subtitle: String,
    val type: DeedType,
    val targetCount: Int,
    val category: DeedCategory,
    val content: String,
    val instructions: String? = null,
    val stages: List<TasbeehStage>? = null
)
