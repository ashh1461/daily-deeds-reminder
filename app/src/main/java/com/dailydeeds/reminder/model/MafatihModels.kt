package com.dailydeeds.reminder.model

/**
 * Sections of the Mafatih tab, plus [SAHIFA] which has its own tab and is never shown as a Mafatih chip.
 * [INDEX] is the table of contents of the whole book, grouped by chapter.
 */
enum class MafatihCategoryType(val id: String, val titleArabic: String, val iconDescription: String) {
    ADIYAH("adiyah", "الأدعية", "كميل، السمات، الصباح، التوسل، الافتتاح، أبو حمزة..."),
    ZIYARAT("ziyarat", "الزيارات", "عاشوراء، الأربعين، الجامعة، أمين الله، زيارات الأئمة والمراقد..."),
    MUNAJAT("munajat", "المناجاة", "المناجاة الخمس عشرة للإمام زين العابدين (ع)"),
    TAQIBAT("taqibat", "التعقيبات", "تعقيبات الصلوات العامة والخاصة"),
    JUMUAH("jumuah", "أعمال الجمعة", "فضل ليلة الجمعة ونهارها وأعمالها وصلواتها"),
    AMAL("amal", "أعمال الشهور", "أعمال رجب وشعبان ورمضان وسائر الشهور"),
    INDEX("index", "الفهرس", "كل أبواب الكتاب وفصوله بالترتيب"),
    SAHIFA("sahifa", "الصحيفة السجادية", "الصحيفة السجادية الكاملة");

    companion object {
        /** The chips shown on the Mafatih tab, in display order. */
        val mafatihChips: List<MafatihCategoryType> = listOf(ADIYAH, ZIYARAT, MUNAJAT, TAQIBAT, JUMUAH, AMAL, INDEX)
    }
}

data class MafatihItem(
    val id: String,
    val category: MafatihCategoryType,
    /** Chapter label, e.g. "الباب الأول › الفصل الثالث: الأدعية اليومية". */
    val group: String,
    val title: String,
    val arabicText: String,
    /** True for sections of the Mafatih book; false for the Sahifa. */
    val fromMafatihBook: Boolean = true
)
