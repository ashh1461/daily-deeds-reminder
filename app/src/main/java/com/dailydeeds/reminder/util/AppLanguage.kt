package com.dailydeeds.reminder.util

/**
 * The UI languages the app ships (v1.9.0). Arabic is the default resource set (`res/values`); English and Persian
 * live in `values-en` and `values-fa`. Only the app's own interface is translated: religious text (Quran, Mafatih,
 * Sahifa, al-Mizan, supplications) is content and always stays in its original Arabic.
 *
 * Pure logic, unit-tested; the Android side is [LocaleSupport].
 */
enum class AppLanguage(val tag: String, val rtl: Boolean, val nativeName: String) {
    ARABIC("ar", rtl = true, nativeName = "العربية"),
    ENGLISH("en", rtl = false, nativeName = "English"),
    PERSIAN("fa", rtl = true, nativeName = "فارسی");

    companion object {
        /** Stored value meaning "use the device language when the app has it". */
        const val SYSTEM = "system"

        /** What a user who never opened the language setting gets: Arabic, as every version before 1.9.0. */
        val DEFAULT_PREF = ARABIC.tag

        fun fromTag(tag: String?): AppLanguage? = entries.firstOrNull { it.tag == tag }

        /**
         * The language to show for the stored [pref] (a tag, [SYSTEM], or nothing yet) on a device whose language
         * is [deviceLanguage] (an ISO 639 code such as "en"). A language the app does not ship resolves to Arabic.
         */
        fun resolve(pref: String?, deviceLanguage: String): AppLanguage =
            if (pref == SYSTEM) fromTag(deviceLanguage) ?: ARABIC else fromTag(pref) ?: ARABIC
    }
}
