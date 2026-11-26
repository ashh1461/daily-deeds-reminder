package com.dailydeeds.reminder.worship

import org.json.JSONArray
import org.json.JSONException

/**
 * The four figures typed into the khums calculator, kept so they are still there on the next visit. Stored as a JSON
 * array of four strings in `worship_khums_draft`, which is part of a backup, so [decode] treats the text as untrusted.
 */
data class KhumsDraft(
    val gains: String = "",
    val expenses: String = "",
    val deductions: String = "",
    val paid: String = ""
) {
    val isEmpty: Boolean get() = listOf(gains, expenses, deductions, paid).all { it.isBlank() }

    fun encode(): String = JSONArray(listOf(gains, expenses, deductions, paid)).toString()

    companion object {
        /** The calculator's fields accept at most this many characters. */
        const val MAX_FIELD = 26

        fun decode(text: String?): KhumsDraft {
            if (text.isNullOrBlank()) return KhumsDraft()
            val array = try {
                JSONArray(text)
            } catch (e: JSONException) {
                return KhumsDraft()
            }
            if (array.length() != 4) return KhumsDraft()
            val fields = (0 until 4).map { i -> (array.opt(i) as? String)?.take(MAX_FIELD) ?: return KhumsDraft() }
            return KhumsDraft(fields[0], fields[1], fields[2], fields[3])
        }
    }
}
