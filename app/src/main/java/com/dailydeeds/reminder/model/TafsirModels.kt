package com.dailydeeds.reminder.model

data class TafsirAlMizan(
    val id: String,
    val surahNumber: Int,
    val ayahStart: Int,
    val ayahEnd: Int,
    val title: String,
    val commentaryArabic: String,
    val intellectualTheme: String? = null
)
