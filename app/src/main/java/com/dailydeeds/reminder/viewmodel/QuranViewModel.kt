package com.dailydeeds.reminder.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.data.QuranRepository
import com.dailydeeds.reminder.data.ReaderFonts
import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.model.Surah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** The Quran list, the ayah reader and the mushaf pages. Text sizes and fit mode are kept between launches. */
class QuranViewModel @JvmOverloads constructor(
    app: Application,
    private val repository: QuranRepository = QuranRepository()
) : AndroidViewModel(app) {

    private val prefs = PreferencesManager(app)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _surahs = MutableStateFlow<List<Surah>>(repository.getAllSurahs())
    val surahs: StateFlow<List<Surah>> = _surahs.asStateFlow()

    private val _selectedSurah = MutableStateFlow<Surah?>(null)
    val selectedSurah: StateFlow<Surah?> = _selectedSurah.asStateFlow()

    private val _ayahs = MutableStateFlow<List<Ayah>>(emptyList())
    val ayahs: StateFlow<List<Ayah>> = _ayahs.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(prefs.getReaderFontSp(ReaderFonts.KEY_QURAN, ReaderFonts.QURAN))
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

    fun increaseFontSize() = setListFont(ReaderFonts.QURAN.larger(_fontSizeSp.value))

    fun decreaseFontSize() = setListFont(ReaderFonts.QURAN.smaller(_fontSizeSp.value))

    private fun setListFont(sp: Float) {
        if (sp == _fontSizeSp.value) return
        _fontSizeSp.value = sp
        prefs.setReaderFontSp(ReaderFonts.KEY_QURAN, sp)
    }

    /** Text size of the page view; smaller by default than the ayah list so most of a page fits the screen. */
    private val _mushafFontSp = MutableStateFlow(prefs.getReaderFontSp(ReaderFonts.KEY_MUSHAF, ReaderFonts.MUSHAF))
    val mushafFontSp: StateFlow<Float> = _mushafFontSp.asStateFlow()

    /** Each page picks the largest text size that fits the screen, so a page is read without scrolling. */
    private val _mushafFit = MutableStateFlow(prefs.getMushafFit())
    val mushafFit: StateFlow<Boolean> = _mushafFit.asStateFlow()

    fun setMushafFit(fit: Boolean) {
        if (fit == _mushafFit.value) return
        _mushafFit.value = fit
        prefs.setMushafFit(fit)
    }

    /** Zooming leaves fit mode; [fromSp] is the size on screen, so the first step starts from what the reader sees. */
    fun increaseMushafFont(fromSp: Float = _mushafFontSp.value) = setMushafFont(ReaderFonts.MUSHAF.larger(fromSp))

    fun decreaseMushafFont(fromSp: Float = _mushafFontSp.value) = setMushafFont(ReaderFonts.MUSHAF.smaller(fromSp))

    private fun setMushafFont(sp: Float) {
        setMushafFit(false)
        _mushafFontSp.value = sp
        prefs.setReaderFontSp(ReaderFonts.KEY_MUSHAF, sp)
    }
}
