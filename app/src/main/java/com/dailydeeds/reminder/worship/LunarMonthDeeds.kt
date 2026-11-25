package com.dailydeeds.reminder.worship

import com.dailydeeds.reminder.model.MafatihItem

/**
 * «أعمال الأشهر القمرية»: the second part of Mafatih al-Jinan («الباب الثاني: في أعمال أشهر السنة») arranged by
 * Hijri month. Every section is the book's own full text (the reader opens the original items); nothing is
 * summarised. Al-Qummi treats Rabi' al-Akhir and both Jumadas in one chapter, so that section covers three months.
 */
object LunarMonthDeeds {

    data class Section(val title: String, val months: List<Int>, val items: List<MafatihItem>)

    private const val PART = "الباب الثاني"
    private const val INTRO_GROUP = "$PART › مقدمة أعمال الشهور"
    private const val GENERAL_CHAPTER = "عامة الشهور والنيروز"

    /** Mafatih chapter name (the part after «: » in the group) → title shown and the Hijri months it covers. */
    private val CHAPTERS: List<Triple<String, String, List<Int>>> = listOf(
        Triple("شهر محرم", "شهر محرم الحرام", listOf(1)),
        Triple("شهر صفر", "شهر صفر", listOf(2)),
        Triple("شهر ربيع الأول", "شهر ربيع الأول", listOf(3)),
        Triple("ربيع الآخر وجمادى", "ربيع الآخر وجمادى الأولى وجمادى الآخرة", listOf(4, 5, 6)),
        Triple("شهر رجب", "شهر رجب الأصب", listOf(7)),
        Triple("شهر شعبان", "شهر شعبان المعظم", listOf(8)),
        Triple("شهر رمضان", "شهر رمضان المبارك", listOf(9)),
        Triple("شهر شوال", "شهر شوال", listOf(10)),
        Triple("شهر ذي القعدة", "شهر ذي القعدة الحرام", listOf(11)),
        Triple("شهر ذي الحجة", "شهر ذي الحجة الحرام", listOf(12))
    )

    private fun chapterOf(item: MafatihItem): String? =
        if (item.fromMafatihBook && item.group.startsWith(PART) && item.group.contains(": ")) item.group.substringAfter(": ")
        else null

    /** The twelve months in Hijri order (Muharram first), each with its Mafatih chapter in book order. */
    fun sections(bookItems: List<MafatihItem>): List<Section> = CHAPTERS.map { (chapter, title, months) ->
        Section(title, months, bookItems.filter { chapterOf(it) == chapter })
    }

    /** The introduction of the part and the deeds that apply to every month (the first day, the nights, Nowruz). */
    fun general(bookItems: List<MafatihItem>): Section = Section(
        "أعمال عامة لكل الشهور",
        (1..12).toList(),
        bookItems.filter { (it.fromMafatihBook && it.group == INTRO_GROUP) || chapterOf(it) == GENERAL_CHAPTER }
    )

    /** The section that covers Hijri [month] (1..12), or null for an invalid month. */
    fun sectionFor(month: Int, sections: List<Section>): Section? = sections.firstOrNull { month in it.months }
}
