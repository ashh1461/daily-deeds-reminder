package com.dailydeeds.reminder.data

import kotlin.math.roundToInt

/**
 * Text sizes of the three readers: their defaults, limits and steps, and how they are stored.
 *
 * Sizes are saved as whole tenths of an sp (`Int`), because the mushaf zoom starts from a fitted size that can be a
 * half step such as 17.5 sp. The backup type check in [BackupCodec] expects these keys to hold an `Int`.
 */
object ReaderFonts {
    const val KEY_QURAN = "pref_font_quran"
    const val KEY_MAFATIH = "pref_font_mafatih"
    const val KEY_MUSHAF = "pref_font_mushaf"
    const val KEY_MUSHAF_FIT = "pref_mushaf_fit"

    private const val STEP_SP = 2f
    private const val TENTHS = 10f

    data class Range(val defaultSp: Float, val minSp: Float, val maxSp: Float) {
        fun clamp(sp: Float): Float = sp.coerceIn(minSp, maxSp)
        fun larger(fromSp: Float): Float = clamp(fromSp + STEP_SP)
        fun smaller(fromSp: Float): Float = clamp(fromSp - STEP_SP)
    }

    /** The smallest mushaf size, shared by fitted pages and the zoom buttons so "smaller" can never enlarge the text. */
    const val MUSHAF_MIN_SP = 12f

    val QURAN = Range(defaultSp = 24f, minSp = 16f, maxSp = 40f)
    val MAFATIH = Range(defaultSp = 22f, minSp = 16f, maxSp = 36f)
    val MUSHAF = Range(defaultSp = 20f, minSp = MUSHAF_MIN_SP, maxSp = 40f)

    fun toStored(sp: Float): Int = (sp * TENTHS).roundToInt()

    /** A stored value back to sp, clamped so a hand-edited backup cannot set an unusable size. */
    fun fromStored(stored: Int, range: Range): Float = range.clamp(stored / TENTHS)

    /** The basmala in the mushaf: its line height follows its size, so a wrapped basmala never overlaps itself. */
    const val BASMALA_LINE_HEIGHT = 1.4f
    fun basmalaLineHeightSp(sp: Float): Float = sp * BASMALA_LINE_HEIGHT
}
