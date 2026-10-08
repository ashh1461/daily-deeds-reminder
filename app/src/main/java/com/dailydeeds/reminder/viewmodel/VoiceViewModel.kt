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

    fun refreshCatalogue() {
        _remote.value = RemoteVoicesState.Loading
        viewModelScope.launch {
            _remote.value = downloader.fetchManifest().fold(
                onSuccess = { RemoteVoicesState.Loaded(it.voices, it.rejected.size) },
                onFailure = { RemoteVoicesState.Failed("تعذّر تحميل قائمة الأصوات. تحقق من الاتصال بالإنترنت.") }
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
                onSuccess = { "تم تنزيل «${voice.nameAr}»" },
                onFailure = { "تعذّر تنزيل «${voice.nameAr}»: ${it.message ?: "خطأ غير معروف"}" }
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
                onSuccess = { "تمت إضافة «${it.nameAr}»" },
                onFailure = { "تعذّر استيراد الملف: ${it.message ?: "خطأ غير معروف"}" }
            )
        }
    }
}
