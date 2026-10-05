package com.dailydeeds.reminder.viewmodel

import androidx.lifecycle.ViewModel
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MafatihViewModel(
    private val repository: MafatihRepository = MafatihRepository()
) : ViewModel() {

    val categories: List<MafatihCategoryType> = repository.getCategories()

    private val _selectedCategory = MutableStateFlow(MafatihCategoryType.ADIYAH)
    val selectedCategory: StateFlow<MafatihCategoryType> = _selectedCategory.asStateFlow()

    private val _items = MutableStateFlow<List<MafatihItem>>(
        repository.getItemsByCategory(MafatihCategoryType.ADIYAH)
    )
    val items: StateFlow<List<MafatihItem>> = _items.asStateFlow()

    private val _selectedItem = MutableStateFlow<MafatihItem?>(null)
    val selectedItem: StateFlow<MafatihItem?> = _selectedItem.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(22f)
    val fontSizeSp: StateFlow<Float> = _fontSizeSp.asStateFlow()

    fun selectCategory(category: MafatihCategoryType) {
        _selectedCategory.value = category
        _searchQuery.value = ""
        _items.value = repository.getItemsByCategory(category)
    }

    fun selectItem(itemId: String) {
        val item = repository.getItemById(itemId)
        _selectedItem.value = item
        _counter.value = 0
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _items.value = repository.getItemsByCategory(_selectedCategory.value)
        } else {
            _items.value = repository.searchMafatih(query).filter { item: MafatihItem ->
                item.category == _selectedCategory.value
            }
        }
    }

    fun incrementCounter() {
        _counter.value += 1
    }

    fun resetCounter() {
        _counter.value = 0
    }

    fun increaseFontSize() {
        if (_fontSizeSp.value < 36f) {
            _fontSizeSp.value += 2f
        }
    }

    fun decreaseFontSize() {
        if (_fontSizeSp.value > 16f) {
            _fontSizeSp.value -= 2f
        }
    }
}
