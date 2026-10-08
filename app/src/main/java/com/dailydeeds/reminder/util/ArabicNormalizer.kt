package com.dailydeeds.reminder.util

object ArabicNormalizer {

    private val WHITESPACE_REGEX = Regex("\\s+")
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

        return cleaned.trim().replace(WHITESPACE_REGEX, " ")
    }

    /**
     * Matching key: [normalize] plus removal of every plain alef. Uthmani script writes many long
     * alefs as a dagger mark (صِرَٰطَ) while everyday spelling keeps them (صراط), so dropping alefs
     * from both sides lets either spelling find the other.
     */
    fun searchKey(text: String): String = normalize(text).replace("ا", "")

    fun contains(source: String, query: String): Boolean {
        if (query.isBlank()) return true
        return searchKey(source).contains(searchKey(query), ignoreCase = true)
    }
}
