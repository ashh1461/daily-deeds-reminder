package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.calendar.ShiaCalendar
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.chrono.HijrahDate

data class KhumsInput(
    /** Income and other gains during the khums year. */
    val gains: BigDecimal,
    /** Living expenses of the year (the "mu'na"). */
    val expenses: BigDecimal,
    /** Debts and other deductible items for the year. */
    val deductions: BigDecimal,
    /** Khums already paid on this year's surplus. */
    val alreadyPaid: BigDecimal
)

data class KhumsResult(
    val surplus: BigDecimal,
    val khums: BigDecimal,
    val remaining: BigDecimal,
    /** Half of the remaining khums goes to the share of the Imam (peace be upon him)... */
    val shareOfImam: BigDecimal,
    /** ...and half to the share of the sayyids. */
    val shareOfSadat: BigDecimal
)

/** The annual date (Hijri month and day) on which the khums year begins. */
data class KhumsYear(val month: Int, val day: Int) {
    init {
        require(month in 1..12 && day in 1..30)
    }

    fun encode() = "$month,$day"

    companion object {
        fun decode(text: String?): KhumsYear? {
            val p = text?.split(',') ?: return null
            if (p.size != 2) return null
            val m = p[0].trim().toIntOrNull() ?: return null
            val d = p[1].trim().toIntOrNull() ?: return null
            return if (m in 1..12 && d in 1..30) KhumsYear(m, d) else null
        }
    }
}

/**
 * Khums on the annual surplus: one fifth of what remains of the year's gains after expenses and debts.
 * This is the general rule only; details differ between maraji, so the screen says it is not a fatwa.
 */
object Khums {
    private val RATE = BigDecimal("0.20")
    private val MAX_AMOUNT = BigDecimal("1000000000000000")

    fun calculate(input: KhumsInput): KhumsResult {
        val surplus = (input.gains - input.expenses - input.deductions).max(BigDecimal.ZERO)
        val khums = (surplus * RATE).setScale(2, RoundingMode.HALF_UP)
        val remaining = (khums - input.alreadyPaid).max(BigDecimal.ZERO)
        val imam = remaining.divide(BigDecimal(2), 2, RoundingMode.HALF_UP)
        return KhumsResult(
            surplus.setScale(2, RoundingMode.HALF_UP), khums, remaining.setScale(2, RoundingMode.HALF_UP),
            imam, remaining.setScale(2, RoundingMode.HALF_UP) - imam
        )
    }

    /** Accepts Western or Arabic-Indic digits and ignores thousands separators; null for anything else. */
    fun parseAmount(text: String): BigDecimal? {
        val sb = StringBuilder()
        for (ch in text.trim()) {
            when {
                ch in '0'..'9' -> sb.append(ch)
                ch in '٠'..'٩' -> sb.append('0' + (ch - '٠'))
                ch in '۰'..'۹' -> sb.append('0' + (ch - '۰'))
                ch == '.' || ch == '٫' -> sb.append('.')
                ch == ',' || ch == '٬' || ch == ' ' || ch == ' ' -> Unit
                else -> return null
            }
        }
        if (sb.isEmpty() || sb.count { it == '.' } > 1 || sb.length > 24) return null
        val value = sb.toString().toBigDecimalOrNull() ?: return null
        return if (value.signum() < 0 || value > MAX_AMOUNT) null else value
    }

    /** The next date (today included) that falls on [year] in the Hijri calendar, within about 13 months. */
    fun nextAnniversary(today: LocalDate, year: KhumsYear, dayOffset: Int = 0): LocalDate? {
        for (i in 0..400) {
            val date = today.plusDays(i.toLong())
            val h = ShiaCalendar.toHijri(date, dayOffset)
            if (h.month != year.month) continue
            if (h.day == year.day) return date
            // Day 30 on a 29-day month means the last day of the month.
            if (year.day == 30 && h.day == 29 &&
                HijrahDate.from(date.plusDays(dayOffset.toLong())).lengthOfMonth() == 29
            ) return date
        }
        return null
    }
}
