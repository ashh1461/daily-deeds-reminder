package com.dailydeeds.reminder.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailydeeds.reminder.adhan.RemoteVoice
import com.dailydeeds.reminder.adhan.VoicePackDownloader
import com.dailydeeds.reminder.adhan.VoiceStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.annotation.StringRes
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.util.LocaleSupport

sealed interface RemoteVoicesState {
    data object Idle : RemoteVoicesState
    data object Loading : RemoteVoicesState
    data class Loaded(val voices: List<RemoteVoice>, val rejected: Int) : RemoteVoicesState
    data class Failed(val reason: String) : RemoteVoicesState
}

/** Installed adhan voices, the downloadable catalogue and the progress of downloads. */
class VoiceViewModel(app: Application) : AndroidViewModel(app) {
    private val store = VoiceStore(app)
    private val downloader = VoicePackDownloader(store)

    private val _installed = MutableStateFlow(store.installed())
    val installed: StateFlow<List<VoiceStore.Installed>> = _installed.asStateFlow()

    private val _remote = MutableStateFlow<RemoteVoicesState>(RemoteVoicesState.Idle)
    val remote: StateFlow<RemoteVoicesState> = _remote.asStateFlow()

    private val _progress = MutableStateFlow<Map<String, Float>>(emptyMap())
    val progress: StateFlow<Map<String, Float>> = _progress.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun clearMessage() {
        _message.value = null
    }

    private fun text(@StringRes id: Int, vararg args: Any): String =
        LocaleSupport.localized(getApplication()).getString(id, *args)

    fun refreshCatalogue() {
        _remote.value = RemoteVoicesState.Loading
        viewModelScope.launch {
            _remote.value = downloader.fetchManifest().fold(
                onSuccess = { RemoteVoicesState.Loaded(it.voices, it.rejected.size) },
                onFailure = { RemoteVoicesState.Failed(text(R.string.voices_catalogue_failed)) }
            )
        }
    }

    fun download(voice: RemoteVoice) {
        if (_progress.value.containsKey(voice.id)) return
        _progress.value = _progress.value + (voice.id to 0f)
        viewModelScope.launch {
            val result = downloader.download(voice) { p -> _progress.value = _progress.value + (voice.id to p) }
            _progress.value = _progress.value - voice.id
            _installed.value = store.installed()
            _message.value = result.fold(
                onSuccess = { text(R.string.voices_downloaded, voice.nameAr) },
                onFailure = { text(R.string.voices_download_failed, voice.nameAr, it.message ?: text(R.string.common_unknown_error)) }
            )
        }
    }

    fun remove(id: String) {
        store.remove(id)
        _installed.value = store.installed()
    }

    fun importOwn(uri: Uri) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { runCatching { store.importOwn(uri) } }
            _installed.value = store.installed()
            _message.value = result.fold(
                onSuccess = { text(R.string.voices_added, it.nameAr) },
                onFailure = { text(R.string.voices_import_failed, it.message ?: text(R.string.common_unknown_error)) }
            )
        }
    }
}
