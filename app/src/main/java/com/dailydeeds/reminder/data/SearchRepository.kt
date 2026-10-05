package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.SearchResultItem
import com.dailydeeds.reminder.model.SearchResultType
import com.dailydeeds.reminder.util.ArabicNormalizer

class SearchRepository(
    private val quranRepo: QuranRepository = QuranRepository(),
    private val mafatihRepo: MafatihRepository = MafatihRepository(),
    private val tafsirProvider: TafsirAlMizanProvider = TafsirAlMizanProvider
) {

    fun search(query: String, typeFilter: SearchResultType = SearchResultType.ALL): List<SearchResultItem> {
        if (query.isBlank()) return emptyList()

        val results = mutableListOf<SearchResultItem>()
        val normalizedQuery = ArabicNormalizer.normalize(query)

        // 1. Search in Quran
        if (typeFilter == SearchResultType.ALL || typeFilter == SearchResultType.QURAN) {
            val surahs = quranRepo.getAllSurahs()
            for (surah in surahs) {
                if (ArabicNormalizer.contains(surah.nameArabic, query) ||
                    surah.nameEnglish.contains(query, ignoreCase = true)
                ) {
                    results.add(
                        SearchResultItem(
                            id = "surah_${surah.number}",
                            type = SearchResultType.QURAN,
                            title = "سورة ${surah.nameArabic} (${surah.nameEnglish})",
                            snippet = "سورة ${surah.revelationType.arabicName}، عدد آياتها ${surah.ayahCount}، في الجزء ${surah.juzStart}",
                            surahNumber = surah.number,
                            ayahNumber = 1
                        )
                    )
                }

                val ayahs = quranRepo.getAyahsForSurah(surah.number)
                for (ayah in ayahs) {
                    if (ArabicNormalizer.contains(ayah.textArabic, query)) {
                        results.add(
                            SearchResultItem(
                                id = "ayah_${surah.number}_${ayah.ayahNumber}",
                                type = SearchResultType.QURAN,
                                title = "سورة ${surah.nameArabic} - الآية ${ayah.ayahNumber}",
                                snippet = createSnippet(ayah.textArabic, normalizedQuery),
                                surahNumber = surah.number,
                                ayahNumber = ayah.ayahNumber
                            )
                        )
                    }
                }
            }
        }

        // 2. Search in Tafsir Al-Mizan
        if (typeFilter == SearchResultType.ALL || typeFilter == SearchResultType.TAFSIR) {
            val tafsirList = tafsirProvider.getAllEntries()
            for (tafsir in tafsirList) {
                if (ArabicNormalizer.contains(tafsir.title, query) ||
                    ArabicNormalizer.contains(tafsir.commentaryArabic, query) ||
                    (tafsir.intellectualTheme != null && ArabicNormalizer.contains(tafsir.intellectualTheme, query))
                ) {
                    results.add(
                        SearchResultItem(
                            id = "tafsir_${tafsir.id}",
                            type = SearchResultType.TAFSIR,
                            title = tafsir.title,
                            snippet = createSnippet(tafsir.commentaryArabic, normalizedQuery),
                            surahNumber = tafsir.surahNumber,
                            ayahNumber = tafsir.ayahStart
                        )
                    )
                }
            }
        }

        // 3. Search in Mafatih Al-Jinan
        if (typeFilter == SearchResultType.ALL || typeFilter == SearchResultType.MAFATIH) {
            val mafatihItems = mafatihRepo.getAllItems()
            for (item in mafatihItems) {
                if (ArabicNormalizer.contains(item.title, query) ||
                    ArabicNormalizer.contains(item.arabicText, query) ||
                    ArabicNormalizer.contains(item.virtueOrSource, query)
                ) {
                    results.add(
                        SearchResultItem(
                            id = "mafatih_${item.id}",
                            type = SearchResultType.MAFATIH,
                            title = "${item.title} (${item.category.titleArabic})",
                            snippet = createSnippet(item.arabicText, normalizedQuery),
                            mafatihItemId = item.id
                        )
                    )
                }
            }
        }

        return results
    }

    private fun createSnippet(text: String, normalizedQuery: String, maxLength: Int = 120): String {
        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }
        for (line in lines) {
            if (ArabicNormalizer.contains(line, normalizedQuery)) {
                return if (line.length <= maxLength) line else line.take(maxLength) + "..."
            }
        }
        val firstLine = lines.firstOrNull() ?: text
        return if (firstLine.length <= maxLength) firstLine else firstLine.take(maxLength) + "..."
    }
}
