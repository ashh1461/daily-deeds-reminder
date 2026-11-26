package com.dailydeeds.reminder.util

/** Arabic-Indic digits for display (page and ayah numbers) and a lenient parser for typed numbers. */
object ArabicDigits {
    private const val ARABIC_ZERO = '٠'
    private const val PERSIAN_ZERO = '۰'
    private const val MAX_INPUT = 6

    fun format(value: Int): String = value.toString().map { c ->
        if (c in '0'..'9') ARABIC_ZERO + (c - '0') else c
    }.joinToString("")

    /** Parses a positive whole number typed with Latin, Arabic-Indic or Persian digits; null for anything else. */
    fun parsePositive(text: String): Int? {
        val digits = text.trim().filterNot { it == '‏' || it == '‎' || it == '؜' }
        if (digits.isEmpty() || digits.length > MAX_INPUT) return null
        val latin = digits.map { c ->
            when (c) {
                in '0'..'9' -> c
                in ARABIC_ZERO..(ARABIC_ZERO + 9) -> '0' + (c - ARABIC_ZERO)
                in PERSIAN_ZERO..(PERSIAN_ZERO + 9) -> '0' + (c - PERSIAN_ZERO)
                else -> return null
            }
        }.joinToString("")
        return latin.toIntOrNull()?.takeIf { it > 0 }
    }
}
