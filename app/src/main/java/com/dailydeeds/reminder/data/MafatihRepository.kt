package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import com.dailydeeds.reminder.util.ArabicNormalizer

class MafatihRepository(
    private val provider: MafatihDataProvider = MafatihDataProvider
) {

    /** Normalized title + text of every item, computed once; searching only normalizes the query. */
    private val searchKeys: Map<String, String> by lazy {
        provider.items.associate { it.id to ArabicNormalizer.searchKey(it.title + " " + it.arabicText) }
    }

    /** The chips of the Mafatih tab. The Sahifa is a separate tab and is not part of this list. */
    fun getCategories(): List<MafatihCategoryType> = MafatihCategoryType.mafatihChips

    fun getItemsByCategory(category: MafatihCategoryType): List<MafatihItem> = provider.getItemsByCategory(category)

    fun getItemById(id: String): MafatihItem? = provider.getItemById(id)

    fun getBookItems(): List<MafatihItem> = provider.bookItems

    fun getSahifaItems(): List<MafatihItem> = provider.sahifaItems

    /** Every searchable item: the book followed by the Sahifa. */
    fun getAllItems(): List<MafatihItem> = provider.items

    /** Builds the search index; call from a background thread before the first query. */
    fun warmUp() {
        searchKeys
    }

    fun searchMafatih(query: String, category: MafatihCategoryType = MafatihCategoryType.INDEX): List<MafatihItem> =
        filter(getItemsByCategory(category), query)

    fun searchSahifa(query: String): List<MafatihItem> = filter(provider.sahifaItems, query)

    private fun filter(pool: List<MafatihItem>, query: String): List<MafatihItem> {
        val key = ArabicNormalizer.searchKey(query.take(MAX_QUERY_LENGTH))
        if (key.isEmpty()) return pool
        return pool.filter { searchKeys[it.id]?.contains(key) == true }
    }

    private companion object {
        const val MAX_QUERY_LENGTH = 200
    }
}
