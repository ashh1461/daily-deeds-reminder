package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem

/**
 * The complete text of Mafatih al-Jinan (Shaykh Abbas al-Qummi) split into named sections, and of
 * al-Sahifa al-Sajjadiyya al-Kamila, both read from resources bundled in the APK.
 *
 * `mafatih/items.txt`:  `n|category|group|title|firstPage|lastPage|text`
 * `mafatih/sahifa.txt`: `id|group|title|text`
 * Newlines inside `text` are stored as a backslash followed by `n`.
 */
object MafatihDataProvider {
    const val BOOK_RESOURCE = "mafatih/items.txt"
    const val SAHIFA_RESOURCE = "mafatih/sahifa.txt"

    private const val NEWLINE_ESCAPE = "\\n"

    /** Every section of the Mafatih book, in book order. */
    val bookItems: List<MafatihItem> by lazy { loadBook() }

    val sahifaItems: List<MafatihItem> by lazy { loadSahifa() }

    val items: List<MafatihItem> by lazy { bookItems + sahifaItems }

    private val byId: Map<String, MafatihItem> by lazy { items.associateBy { it.id } }

    fun getItemsByCategory(category: MafatihCategoryType): List<MafatihItem> = when (category) {
        MafatihCategoryType.INDEX -> bookItems
        MafatihCategoryType.SAHIFA -> sahifaItems
        else -> bookItems.filter { it.category == category }
    }

    fun getItemById(id: String): MafatihItem? = byId[id]

    private fun open(path: String) = MafatihDataProvider::class.java.classLoader?.getResourceAsStream(path)
        ?: error("Missing bundled resource: $path")

    private fun loadBook(): List<MafatihItem> =
        open(BOOK_RESOURCE).bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.filter { it.isNotBlank() }.map { line ->
                val f = line.split('|', limit = 7)
                MafatihItem(
                    id = "bk_${f[0]}",
                    category = MafatihCategoryType.values().firstOrNull { it.id == f[1] } ?: MafatihCategoryType.INDEX,
                    group = f[2],
                    title = f[3],
                    arabicText = f[6].replace(NEWLINE_ESCAPE, "\n")
                )
            }.toList()
        }

    private fun loadSahifa(): List<MafatihItem> =
        open(SAHIFA_RESOURCE).bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.filter { it.isNotBlank() }.map { line ->
                val f = line.split('|', limit = 4)
                MafatihItem(
                    id = f[0],
                    category = MafatihCategoryType.SAHIFA,
                    group = f[1],
                    title = f[2],
                    arabicText = f[3].replace(NEWLINE_ESCAPE, "\n"),
                    fromMafatihBook = false
                )
            }.toList()
        }
}
