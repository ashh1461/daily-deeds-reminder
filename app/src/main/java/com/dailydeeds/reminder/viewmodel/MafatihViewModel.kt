package com.dailydeeds.reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.DataReloads
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.data.ReaderFonts
import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.MafatihItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MafatihViewModel @JvmOverloads constructor(
    app: Application,
    private val repository: MafatihRepository = MafatihRepository()
) : AndroidViewModel(app) {

    private val prefs = PreferencesManager(app)

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

    private val _fontSizeSp = MutableStateFlow(prefs.getReaderFontSp(ReaderFonts.KEY_MAFATIH, ReaderFonts.MAFATIH))
    val fontSizeSp: StateFlow<Float> = _fontSizeSp.asStateFlow()

    private var searchJob: Job? = null

    init {
        // Loading the bundled book and building the search index happen off the main thread.
        viewModelScope.launch {
            withContext(Dispatchers.Default) { repository.warmUp() }
            refresh()
        }
        // A backup import replaces the text size; read it again so the next zoom does not undo it.
        val seen = DataReloads.imports.value
        viewModelScope.launch {
            DataReloads.imports.filter { it != seen }.collect {
                _fontSizeSp.value = prefs.getReaderFontSp(ReaderFonts.KEY_MAFATIH, ReaderFonts.MAFATIH)
            }
        }
    }

    fun selectCategory(category: MafatihCategoryType) {
        _selectedCategory.value = category
        _searchQuery.value = ""
        searchJob?.cancel()
        searchJob = viewModelScope.launch { refresh() }
    }

    /**
     * Opens an item with a fresh counter. Selecting the item that is already open is a no-op, so its counter survives
     * a rotation (and a back-and-reopen of the same item); the reader's reset button clears it.
     */
    fun selectItem(itemId: String) {
        if (_selectedItem.value?.id == itemId) return
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

    fun increaseFontSize() = setFont(ReaderFonts.MAFATIH.larger(_fontSizeSp.value))

    fun decreaseFontSize() = setFont(ReaderFonts.MAFATIH.smaller(_fontSizeSp.value))

    /** The size is kept between launches. */
    private fun setFont(sp: Float) {
        if (sp == _fontSizeSp.value) return
        _fontSizeSp.value = sp
        prefs.setReaderFontSp(ReaderFonts.KEY_MAFATIH, sp)
    }

    private companion object {
        const val DEBOUNCE_MS = 250L
    }
}
