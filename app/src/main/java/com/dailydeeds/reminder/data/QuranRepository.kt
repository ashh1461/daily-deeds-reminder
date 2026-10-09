package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.Ayah
import com.dailydeeds.reminder.model.Surah
import com.dailydeeds.reminder.util.ArabicNormalizer

class QuranRepository(
    private val dataProvider: QuranDataProvider = QuranDataProvider
) {

    fun getAllSurahs(): List<Surah> {
        return dataProvider.surahs
    }

    fun getSurahByNumber(number: Int): Surah? {
        return dataProvider.surahs.find { it.number == number }
    }

    fun getAyahsForSurah(surahNumber: Int): List<Ayah> {
        return dataProvider.getAyahs(surahNumber)
    }

    fun getAyah(surahNumber: Int, ayahNumber: Int): Ayah? {
        return dataProvider.getAyah(surahNumber, ayahNumber)
    }

    fun searchSurahs(query: String): List<Surah> {
        if (query.isBlank()) return getAllSurahs()
        val cleanQuery = query.trim().lowercase().replace("-", "").replace("'", "").replace(" ", "")
        return dataProvider.surahs.filter { surah ->
            val cleanEnglish = surah.nameEnglish.lowercase().replace("-", "").replace("'", "").replace(" ", "")
            ArabicNormalizer.contains(surah.nameArabic, query) ||
                    cleanEnglish.contains(cleanQuery) ||
                    surah.number.toString() == query.trim()
        }
    }
}
