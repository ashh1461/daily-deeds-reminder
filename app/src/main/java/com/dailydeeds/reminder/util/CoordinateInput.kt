package com.dailydeeds.reminder.util

/**
 * Parses a typed latitude or longitude. Accepts Western, Arabic-Indic (٠-٩) and Persian (۰-۹) digits, the
 * Arabic decimal separator «٫», a comma as decimal separator, and a leading minus (ASCII, Unicode minus or the
 * Arabic-script hyphen variants). Returns null for anything else.
 */
object CoordinateInput {

    sealed interface Result {
        data class Valid(val degrees: Double) : Result
        data class Invalid(val messageArabic: String) : Result
        /** Nothing typed yet: no error to show. */
        data object Empty : Result
    }

    fun parse(text: String): Double? {
        val sb = StringBuilder()
        // Arabic keyboards and copy-paste often insert invisible direction marks (U+200E, U+200F, U+061C).
        val trimmed = text.filterNot { Character.getType(it) == Character.FORMAT.toInt() }.trim()
        trimmed.forEachIndexed { i, ch ->
            when {
                ch in '0'..'9' -> sb.append(ch)
                ch in '٠'..'٩' -> sb.append('0' + (ch - '٠'))
                ch in '۰'..'۹' -> sb.append('0' + (ch - '۰'))
                ch == '.' || ch == '٫' || ch == ',' || ch == '،' -> sb.append('.')
                (ch == '-' || ch == '−' || ch == '‐' || ch == '–') && i == 0 -> sb.append('-')
                ch == '+' && i == 0 -> Unit
                ch == '°' && i == trimmed.lastIndex -> Unit
                else -> return null
            }
        }
        val digits = sb.count { it.isDigit() }
        if (digits == 0 || sb.count { it == '.' } > 1 || sb.length > 16) return null
        return sb.toString().toDoubleOrNull()?.takeIf { it.isFinite() }
    }

    fun latitude(text: String): Result = check(text, 90.0, "خط العرض")

    fun longitude(text: String): Result = check(text, 180.0, "خط الطول")

    private fun check(text: String, limit: Double, label: String): Result {
        if (text.isBlank()) return Result.Empty
        val value = parse(text) ?: return Result.Invalid("$label: اكتب رقماً، مثل ٣٣٫٢٧ أو 33.27")
        if (value < -limit || value > limit) {
            return Result.Invalid("$label يجب أن يكون بين -${limit.toInt()} و ${limit.toInt()}")
        }
        return Result.Valid(value)
    }
}
