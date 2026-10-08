package com.dailydeeds.reminder.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.R

/** Naskh typeface for scripture and supplications (Amiri, SIL OFL). */
val ReadingFamily = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_regular, FontWeight.Medium),
    Font(R.font.amiri_bold, FontWeight.SemiBold),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

/** Clean modern Arabic UI typeface (Tajawal, SIL OFL). */
val UiFamily = FontFamily(
    Font(R.font.tajawal_regular, FontWeight.Normal),
    Font(R.font.tajawal_medium, FontWeight.Medium),
    Font(R.font.tajawal_medium, FontWeight.SemiBold),
    Font(R.font.tajawal_bold, FontWeight.Bold)
)

val Typography = Typography(
    headlineLarge = TextStyle(fontFamily = ReadingFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = ReadingFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 34.sp),
    headlineSmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 30.sp),
    titleLarge = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 25.sp),
    titleSmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.sp),
    // bodyLarge is the reading style: Quran, supplications, ziyarat.
    bodyLarge = TextStyle(fontFamily = ReadingFamily, fontWeight = FontWeight.Normal, fontSize = 20.sp, lineHeight = 36.sp),
    bodyMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 24.sp),
    bodySmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontFamily = UiFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp)
)
