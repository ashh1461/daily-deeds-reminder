package com.dailydeeds.reminder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.MafatihRepository
import com.dailydeeds.reminder.model.MafatihItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** State of the Sahifa Sajjadiyya tab: the grouped list and its scoped search. */
class SahifaViewModel(
    private val repository: MafatihRepository = MafatihRepository()
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _items = MutableStateFlow<List<MafatihItem>>(emptyList())
    val items: StateFlow<List<MafatihItem>> = _items.asStateFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) { repository.warmUp() }
            refresh()
        }
    }

    fun onQueryChanged(q: String) {
        _query.value = q
        job?.cancel()
        job = viewModelScope.launch {
            if (q.isNotBlank()) delay(DEBOUNCE_MS)
            refresh()
        }
    }

    private suspend fun refresh() {
        val q = _query.value
        _items.value = withContext(Dispatchers.Default) { repository.searchSahifa(q) }
    }

    private companion object {
        const val DEBOUNCE_MS = 250L
    }
}
