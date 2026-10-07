package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.DayContent
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.model.SearchResultItem
import com.dailydeeds.reminder.model.SearchResultType
import com.dailydeeds.reminder.util.ArabicNormalizer

/**
 * Diacritic-neutral search over the Quran, Tafsir summaries, Mafatih and weekday duas/ziyarat.
 *
 * Every searchable string is normalized exactly once, when the index is first built, so a query only
 * pays for normalizing the query itself. Build the index off the main thread (see [warmUp]).
 */
class SearchRepository(
    private val quranRepo: QuranRepository = QuranRepository(),
    private val mafatihRepo: MafatihRepository = MafatihRepository(),
    private val tafsirProvider: TafsirAlMizanProvider = TafsirAlMizanProvider
) {

    private class Entry(
        val searchable: List<String>,
        val build: (normalizedQuery: String) -> SearchResultItem
    ) {
        fun matches(query: String) = searchable.any { it.contains(query) }
    }

    private val index: Map<SearchResultType, List<Entry>> by lazy { buildIndex() }

    /** Forces the lazy index to be built; call from a background dispatcher. */
    fun warmUp() {
        index
    }

    fun search(
        query: String,
        typeFilter: SearchResultType = SearchResultType.ALL,
        limit: Int = MAX_RESULTS // per result type
    ): List<SearchResultItem> {
        // Bound attacker/pasted input: a multi-megabyte query could otherwise stall the search thread.
        val normalizedQuery = ArabicNormalizer.searchKey(query.take(MAX_QUERY_LENGTH)).lowercase()
        if (normalizedQuery.isEmpty()) return emptyList()

        val types = if (typeFilter == SearchResultType.ALL) SEARCH_ORDER else listOf(typeFilter)
        val results = ArrayList<SearchResultItem>()
        for (type in types) {
            var found = 0
            for (entry in index[type].orEmpty()) {
                if (entry.matches(normalizedQuery)) {
                    results.add(entry.build(normalizedQuery))
                    if (++found >= limit) break
                }
            }
        }
        return results
    }

    private fun buildIndex(): Map<SearchResultType, List<Entry>> {
        val quran = ArrayList<Entry>()
        for (surah in quranRepo.getAllSurahs()) {
            quran += Entry(
                listOf(ArabicNormalizer.searchKey(surah.nameArabic), surah.nameEnglish.lowercase())
            ) {
                SearchResultItem(
                    id = "surah_${surah.number}",
                    type = SearchResultType.QURAN,
                    title = "سورة ${surah.nameArabic} (${surah.nameEnglish})",
                    snippet = "سورة ${surah.revelationType.arabicName}، عدد آياتها ${surah.ayahCount}، في الجزء ${surah.juzStart}",
                    surahNumber = surah.number,
                    ayahNumber = 1
                )
            }
            for (ayah in quranRepo.getAyahsForSurah(surah.number)) {
                quran += Entry(listOf(ArabicNormalizer.searchKey(ayah.textArabic))) { q ->
                    SearchResultItem(
                        id = "ayah_${surah.number}_${ayah.ayahNumber}",
                        type = SearchResultType.QURAN,
                        title = "سورة ${surah.nameArabic} - الآية ${ayah.ayahNumber}",
                        snippet = createSnippet(ayah.textArabic, q),
                        surahNumber = surah.number,
                        ayahNumber = ayah.ayahNumber
                    )
                }
            }
        }

        val tafsir = tafsirProvider.getAllEntries().map { t ->
            Entry(
                listOfNotNull(t.title, t.commentaryArabic, t.intellectualTheme).map(ArabicNormalizer::searchKey)
            ) { q ->
                SearchResultItem(
                    id = "tafsir_${t.id}",
                    type = SearchResultType.TAFSIR,
                    title = t.title,
                    snippet = createSnippet(t.commentaryArabic, q),
                    surahNumber = t.surahNumber,
                    ayahNumber = t.ayahStart
                )
            }
        }

        val mafatih = mafatihRepo.getAllItems().map { item ->
            Entry(listOf(item.title, item.arabicText, item.virtueOrSource).map(ArabicNormalizer::searchKey)) { q ->
                SearchResultItem(
                    id = "mafatih_${item.id}",
                    type = SearchResultType.MAFATIH,
                    title = "${item.title} (${item.category.titleArabic})",
                    snippet = createSnippet(item.arabicText, q),
                    mafatihItemId = item.id
                )
            }
        }

        val weekday = DayContentKind.values().flatMap { kind ->
            WeekdayRepository.all(kind).map { content -> weekdayEntry(content) }
        }

        return mapOf(
            SearchResultType.QURAN to quran,
            SearchResultType.TAFSIR to tafsir,
            SearchResultType.MAFATIH to mafatih,
            SearchResultType.WEEKDAY to weekday
        )
    }

    private fun weekdayEntry(content: DayContent) = Entry(
        listOf(content.title, content.text, content.honoree).map(ArabicNormalizer::searchKey)
    ) { q ->
        SearchResultItem(
            id = "weekday_${content.kind.name}_${content.day.name}",
            type = SearchResultType.WEEKDAY,
            title = content.title,
            snippet = createSnippet(content.text, q),
            weekdayKind = content.kind
        )
    }

    private fun createSnippet(text: String, normalizedQuery: String, maxLength: Int = 120): String {
        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val line = lines.firstOrNull { ArabicNormalizer.searchKey(it).contains(normalizedQuery) }
            ?: lines.firstOrNull()
            ?: text
        return if (line.length <= maxLength) line else line.take(maxLength) + "..."
    }

    companion object {
        const val MAX_RESULTS = 50
        const val MAX_QUERY_LENGTH = 200
        private val SEARCH_ORDER = listOf(
            SearchResultType.QURAN, SearchResultType.TAFSIR,
            SearchResultType.MAFATIH, SearchResultType.WEEKDAY
        )
    }
}
