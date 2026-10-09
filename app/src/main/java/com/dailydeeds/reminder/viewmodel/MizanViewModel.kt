package com.dailydeeds.reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.data.MizanData
import com.dailydeeds.reminder.data.MizanStore
import com.dailydeeds.reminder.model.MizanHit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface MizanState {
    object Loading : MizanState
    class Ready(val data: MizanData) : MizanState
    class Failed(val message: String) : MizanState
}

data class MizanSearchState(
    val query: String = "",
    val running: Boolean = false,
    val progress: Float = 0f,
    val hits: List<MizanHit> = emptyList(),
    val finished: Boolean = false
)

/** Opens the bundled Tafsir al-Mizan off the main thread and runs its full-text search. */
class MizanViewModel(app: Application) : AndroidViewModel(app) {
    private val _state = MutableStateFlow<MizanState>(MizanState.Loading)
    val state: StateFlow<MizanState> = _state.asStateFlow()

    private val _search = MutableStateFlow(MizanSearchState())
    val search: StateFlow<MizanSearchState> = _search.asStateFlow()

    private val _fontSp = MutableStateFlow(22f)
    val fontSp: StateFlow<Float> = _fontSp.asStateFlow()

    private var searchJob: Job? = null
    private var pendingQuery: String? = null

    init {
        load()
    }

    fun load() {
        _state.value = MizanState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = runCatching { MizanStore.open(getApplication()) }
                .fold({ MizanState.Ready(it) }, { MizanState.Failed(it.message ?: "تعذّر تجهيز الكتاب") })
            pendingQuery?.let { pending ->
                pendingQuery = null
                search(pending)
            }
        }
    }

    fun search(query: String) {
        val trimmed = query.trim()
        val data = (_state.value as? MizanState.Ready)?.data
        if (data == null) {
            pendingQuery = trimmed
            _search.value = MizanSearchState(query = trimmed, running = trimmed.length >= 2)
            return
        }
        searchJob?.cancel()
        if (trimmed.length < 2) {
            _search.value = MizanSearchState()
            return
        }
        _search.value = MizanSearchState(query = trimmed, running = true)
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            val job = coroutineContext[Job]
            val hits = data.search(
                trimmed,
                isCancelled = { job?.isActive == false },
                onProgress = { done, total -> _search.value = _search.value.copy(progress = done.toFloat() / total) }
            )
            if (job?.isActive != false) {
                _search.value = MizanSearchState(query = trimmed, hits = hits, finished = true)
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _search.value = MizanSearchState()
    }

    fun increaseFont() {
        if (_fontSp.value < 38f) _fontSp.value += 2f
    }

    fun decreaseFont() {
        if (_fontSp.value > 16f) _fontSp.value -= 2f
    }

    suspend fun readEntry(data: MizanData, id: Int) = withContext(Dispatchers.IO) { data.readEntry(id) }
}
