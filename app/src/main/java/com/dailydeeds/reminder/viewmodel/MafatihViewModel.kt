package com.dailydeeds.reminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MafatihViewModel(
    private val repository: MafatihRepository = MafatihRepository()
) : ViewModel() {

    val categories: List<MafatihCategoryType> = repository.getCategories()

    private val _selectedCategory = MutableStateFlow(MafatihCategoryType.ADIYAH)
    val selectedCategory: StateFlow<MafatihCategoryType> = _selectedCategory.asStateFlow()

    private val _items = MutableStateFlow<List<MafatihItem>>(emptyList())
    val items: StateFlow<List<MafatihItem>> = _items.asStateFlow()

    private val _selectedItem = MutableStateFlow<MafatihItem?>(null)
    val selectedItem: StateFlow<MafatihItem?> = _selectedItem.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(22f)
    val fontSizeSp: StateFlow<Float> = _fontSizeSp.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Loading the bundled book and building the search index happen off the main thread.
        viewModelScope.launch {
            withContext(Dispatchers.Default) { repository.warmUp() }
            refresh()
        }
    }

    fun selectCategory(category: MafatihCategoryType) {
        _selectedCategory.value = category
        _searchQuery.value = ""
        searchJob?.cancel()
        searchJob = viewModelScope.launch { refresh() }
    }

    fun selectItem(itemId: String) {
        _selectedItem.value = repository.getItemById(itemId)
        _counter.value = 0
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) delay(DEBOUNCE_MS)
            refresh()
        }
    }

    private suspend fun refresh() {
        val category = _selectedCategory.value
        val query = _searchQuery.value
        _items.value = withContext(Dispatchers.Default) {
            if (query.isBlank()) repository.getItemsByCategory(category) else repository.searchMafatih(query, category)
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

    private companion object {
        const val DEBOUNCE_MS = 250L
    }
}
