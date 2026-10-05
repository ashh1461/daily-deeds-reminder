package com.dailydeeds.reminder.util

object ArabicNormalizer {

    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u0655\\u0670\\u06D6-\\u06ED۝ۚۖۗۘۙۛۜ]")

    fun normalize(text: String): String {
        if (text.isBlank()) return ""

        var cleaned = TASHKEEL_REGEX.replace(text, "")

        cleaned = cleaned
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')

        cleaned = cleaned.replace('ة', 'ه')
        cleaned = cleaned.replace('ى', 'ي')

        return cleaned.trim().replace(Regex("\\s+"), " ")
    }

    fun contains(source: String, query: String): Boolean {
        if (query.isBlank()) return true
        val normalizedSource = normalize(source)
        val normalizedQuery = normalize(query)
        return normalizedSource.contains(normalizedQuery, ignoreCase = true)
    }
}
