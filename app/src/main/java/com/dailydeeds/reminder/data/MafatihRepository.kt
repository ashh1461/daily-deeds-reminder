package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import com.dailydeeds.reminder.util.ArabicNormalizer

class MafatihRepository(
    private val provider: MafatihDataProvider = MafatihDataProvider
) {

    fun getCategories(): List<MafatihCategoryType> {
        return MafatihCategoryType.values().toList()
    }

    fun getItemsByCategory(category: MafatihCategoryType): List<MafatihItem> {
        return provider.getItemsByCategory(category)
    }

    fun getItemById(id: String): MafatihItem? {
        return provider.getItemById(id)
    }

    fun getAllItems(): List<MafatihItem> {
        return provider.items
    }

    fun searchMafatih(query: String): List<MafatihItem> {
        if (query.isBlank()) return getAllItems()
        return provider.items.filter { item ->
            ArabicNormalizer.contains(item.title, query) ||
                    ArabicNormalizer.contains(item.arabicText, query) ||
                    ArabicNormalizer.contains(item.virtueOrSource, query)
        }
    }
}
