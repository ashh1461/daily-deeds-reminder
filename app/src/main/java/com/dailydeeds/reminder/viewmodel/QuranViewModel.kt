package com.dailydeeds.reminder.viewmodel

import androidx.lifecycle.ViewModel
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.model.Surah
import com.dailydeeds.reminder.model.TafsirAlMizan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranViewModel(
    private val repository: QuranRepository = QuranRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _surahs = MutableStateFlow<List<Surah>>(repository.getAllSurahs())
    val surahs: StateFlow<List<Surah>> = _surahs.asStateFlow()

    private val _selectedSurah = MutableStateFlow<Surah?>(null)
    val selectedSurah: StateFlow<Surah?> = _selectedSurah.asStateFlow()

    private val _ayahs = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahs: StateFlow<List<Ayah>> = _ayahs.asStateFlow()

    private val _selectedTafsir = MutableStateFlow<TafsirAlMizan?>(null)
    val selectedTafsir: StateFlow<TafsirAlMizan?> = _selectedTafsir.asStateFlow()

    private val _isTafsirVisible = MutableStateFlow(false)
    val isTafsirVisible: StateFlow<Boolean> = _isTafsirVisible.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(24f)
    val fontSizeSp: StateFlow<Float> = _fontSizeSp.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _surahs.value = repository.searchSurahs(query)
    }

    fun selectSurah(surahNumber: Int) {
        val surah = repository.getSurahByNumber(surahNumber)
        _selectedSurah.value = surah
        if (surah != null) {
            _ayahs.value = repository.getAyahsForSurah(surahNumber)
        }
    }

    fun openTafsir(surahNumber: Int, ayahNumber: Int) {
        val tafsir = repository.getTafsirForAyah(surahNumber, ayahNumber)
        _selectedTafsir.value = tafsir
        _isTafsirVisible.value = true
    }

    fun dismissTafsir() {
        _isTafsirVisible.value = false
        _selectedTafsir.value = null
    }

    fun increaseFontSize() {
        if (_fontSizeSp.value < 40f) {
            _fontSizeSp.value += 2f
        }
    }

    fun decreaseFontSize() {
        if (_fontSizeSp.value > 16f) {
            _fontSizeSp.value -= 2f
        }
    }
}
