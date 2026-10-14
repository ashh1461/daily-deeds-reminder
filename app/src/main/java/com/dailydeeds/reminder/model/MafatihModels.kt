package com.dailydeeds.reminder.model

/**
 * Sections of the Mafatih tab. The first five are thematic views over the complete book; [FULLBOOK]
 * lists every section of the book in order; [SAHIFA] is al-Sahifa al-Sajjadiyya al-Kamila.
 */
enum class MafatihCategoryType(val id: String, val titleArabic: String, val iconDescription: String) {
    ADIYAH("adiyah", "الأدعية", "كميل، السمات، الصباح، التوسل، الافتتاح، أبو حمزة..."),
    ZIYARAT("ziyarat", "الزيارات", "عاشوراء، الأربعين، الجامعة، أمين الله، زيارات الأئمة..."),
    MUNAJAT("munajat", "المناجاة", "المناجاة الخمس عشرة للإمام زين العابدين (ع)"),
    TAQIBAT("taqibat", "التعقيبات", "تعقيبات الصلوات العامة والخاصة"),
    AMAL("amal", "أعمال الشهور", "أعمال الجمعة ورجب وشعبان ورمضان وسائر الشهور"),
    SAHIFA("sahifa", "الصحيفة السجادية", "الصحيفة السجادية الكاملة: الأدعية الأربعة والخمسون وملحقاتها"),
    FULLBOOK("fullbook", "الكتاب كاملاً", "كل أبواب مفاتيح الجنان وفصوله بالترتيب")
}

data class MafatihItem(
    val id: String,
    val category: MafatihCategoryType,
    val title: String,
    val arabicText: String,
    /** True for sections of the Mafatih book (shown in [MafatihCategoryType.FULLBOOK]); false for the Sahifa. */
    val fromMafatihBook: Boolean = true
)
