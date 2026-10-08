package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DayContentKind
import java.time.DayOfWeek

/** A bookmarkable item. Encoded as a short string so it can live in SharedPreferences. */
sealed class FavoriteKey {
    abstract fun encode(): String

    data class Ayah(val surah: Int, val ayah: Int) : FavoriteKey() {
        override fun encode() = "ayah:$surah:$ayah"
    }

    data class Mafatih(val itemId: String) : FavoriteKey() {
        override fun encode() = "mafatih:$itemId"
    }

    data class Weekday(val kind: DayContentKind, val day: DayOfWeek) : FavoriteKey() {
        override fun encode() = "weekday:${kind.name}:${day.name}"
    }

    companion object {
        /** Returns null for anything malformed, so corrupted or old preference entries are ignored. */
        fun decode(raw: String): FavoriteKey? {
            val p = raw.split(':')
            return try {
                when (p.firstOrNull()) {
                    "ayah" -> if (p.size == 3) Ayah(p[1].toInt(), p[2].toInt()) else null
                    "mafatih" -> if (p.size == 2 && p[1].isNotBlank()) Mafatih(p[1]) else null
                    "weekday" -> if (p.size == 3) Weekday(DayContentKind.valueOf(p[1]), DayOfWeek.valueOf(p[2])) else null
                    else -> null
                }
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}

/** Ordered, de-duplicated favorites list (newest first). Pure logic so it can be unit-tested. */
object FavoritesList {
    private const val SEPARATOR = "\n"

    fun parse(stored: String?): List<FavoriteKey> =
        stored.orEmpty().split(SEPARATOR).filter { it.isNotBlank() }.mapNotNull(FavoriteKey::decode).distinct()

    fun serialize(items: List<FavoriteKey>): String = items.distinct().joinToString(SEPARATOR) { it.encode() }

    fun toggle(items: List<FavoriteKey>, key: FavoriteKey): List<FavoriteKey> =
        if (key in items) items - key else listOf(key) + items
}
