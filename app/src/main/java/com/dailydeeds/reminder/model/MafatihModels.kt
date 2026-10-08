package com.dailydeeds.reminder.model

enum class MafatihCategoryType(val id: String, val titleArabic: String, val iconDescription: String) {
    ADIYAH("adiyah", "الأدعية المشهورة", "دعاء كميل، التوسل، الصباح، السمات..."),
    ZIYARAT("ziyarat", "الزيارات المباركة", "عاشوراء، وارث، الجامعة الكبيرة، أمين الله..."),
    TAQIBAT("taqibat", "تعقيبات الصلوات", "التعقيبات العامة والخاصة للصلوات اليومية"),
    MUNAJAT("munajat", "المناجاة الخمس عشرة", "مناجاة التائبين، الشاكين، الخائفين..."),
    AMAL("amal", "أعمال الأيام والشهور", "أعمال رجب، شعبان، رمضان، والجمعة..."),
    BAQIYAT("baqiyat", "باقيات الصالحات", "حروز وأدعية الحفظ والرزق وقضاء الحوائج"),
    FULLBOOK("fullbook", "الكتاب كاملاً", "النص الكامل لمفاتيح الجنان بأبوابه وفصوله (بلا تشكيل)")
}

data class MafatihItem(
    val id: String,
    val category: MafatihCategoryType,
    val title: String,
    val arabicText: String,
    val virtueOrSource: String = "",
    val hasCounter: Boolean = false,
    val targetCount: Int = 1
) {
    /** True when the bundled text is abridged (marked with an ellipsis) rather than the full printed text. */
    val isExcerpt: Boolean get() = category != MafatihCategoryType.FULLBOOK && arabicText.contains("...")
}
