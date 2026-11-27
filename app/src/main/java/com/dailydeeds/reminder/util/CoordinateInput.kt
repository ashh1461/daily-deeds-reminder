package com.dailydeeds.reminder.util

/**
 * Parses a typed latitude or longitude. Accepts Western, Arabic-Indic (٠-٩) and Persian (۰-۹) digits, the
 * Arabic decimal separator «٫», a comma as decimal separator, and a leading minus (ASCII, Unicode minus or the
 * Arabic-script hyphen variants). Returns null for anything else.
 */
object CoordinateInput {

    sealed interface Result {
        data class Valid(val degrees: Double) : Result
        /** The UI words it (ui/Labels.kt coordinateErrorText). */
        data class Invalid(val axis: Axis, val reason: Reason) : Result
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

    enum class Axis(val limit: Double) { LATITUDE(90.0), LONGITUDE(180.0) }

    enum class Reason { NOT_A_NUMBER, OUT_OF_RANGE }

    fun latitude(text: String): Result = check(text, Axis.LATITUDE)

    fun longitude(text: String): Result = check(text, Axis.LONGITUDE)

    private fun check(text: String, axis: Axis): Result {
        if (text.isBlank()) return Result.Empty
        val value = parse(text) ?: return Result.Invalid(axis, Reason.NOT_A_NUMBER)
        if (value < -axis.limit || value > axis.limit) return Result.Invalid(axis, Reason.OUT_OF_RANGE)
        return Result.Valid(value)
    }
}
