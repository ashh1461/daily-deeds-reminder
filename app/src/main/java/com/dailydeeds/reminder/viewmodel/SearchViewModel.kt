package com.dailydeeds.reminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.SearchRepository
import com.dailydeeds.reminder.model.SearchResultItem
import com.dailydeeds.reminder.model.SearchResultType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository = SearchRepository()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedFilter = MutableStateFlow(SearchResultType.ALL)
    val selectedFilter: StateFlow<SearchResultType> = _selectedFilter.asStateFlow()

    private val _results = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val results: StateFlow<List<SearchResultItem>> = _results.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        performSearch()
    }

    fun setFilter(filter: SearchResultType) {
        _selectedFilter.value = filter
        performSearch()
    }

    private var searchJob: Job? = null

    init {
        // Build the search index off the main thread so the first query is fast.
        viewModelScope.launch(Dispatchers.Default) { repository.warmUp() }
    }

    private fun performSearch() {
        val q = _query.value
        val filter = _selectedFilter.value
        searchJob?.cancel()
        if (q.isBlank()) {
            _results.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_MS)
            _results.value = withContext(Dispatchers.Default) { repository.search(q, filter) }
        }
    }

    private companion object {
        const val DEBOUNCE_MS = 250L
    }
}
