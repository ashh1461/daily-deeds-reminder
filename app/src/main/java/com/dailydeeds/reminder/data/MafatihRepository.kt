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

    fun getCategories(): List<MafatihCategoryType> = MafatihCategoryType.values().toList()

    fun getItemsByCategory(category: MafatihCategoryType): List<MafatihItem> = provider.getItemsByCategory(category)

    fun getItemById(id: String): MafatihItem? = provider.getItemById(id)

    fun getAllItems(): List<MafatihItem> = provider.items

    /** Builds the search index; call from a background thread before the first query. */
    fun warmUp() {
        searchKeys
    }

    fun searchMafatih(query: String, category: MafatihCategoryType? = null): List<MafatihItem> {
        val pool = if (category == null) provider.items else getItemsByCategory(category)
        val key = ArabicNormalizer.searchKey(query.take(MAX_QUERY_LENGTH))
        if (key.isEmpty()) return pool
        return pool.filter { searchKeys[it.id]?.contains(key) == true }
    }

    private companion object {
        const val MAX_QUERY_LENGTH = 200
    }
}
