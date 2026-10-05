package com.dailydeeds.reminder.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ArabicNormalizerTest {

    @Test
    fun testStripTashkeelRemovesBasicDiacritics() {
        val vowelled = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
        val expected = "بسم الله الرحمن الرحيم"
        val normalized = ArabicNormalizer.normalize(vowelled)
        assertEquals(expected, normalized)
    }

    @Test
    fun testNormalizeAlifForms() {
        val input = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ"
        val expected = "انا اعطيناك الكوثر"
        assertEquals(expected, ArabicNormalizer.normalize(input))
    }

    @Test
    fun testNormalizeTaMarbutaAndAlifMaqsura() {
        val input = "فَاطِمَةُ الزَّهْرَاءُ عَلَيْهَا السَّلَامُ وَالْهُدَىٰ"
        val expected = "فاطمه الزهراء عليها السلام والهدي"
        assertEquals(expected, ArabicNormalizer.normalize(input))
    }

    @Test
    fun testRemoveQuranicPauseMarks() {
        val input = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۚ"
        val normalized = ArabicNormalizer.normalize(input)
        assertTrue(normalized.contains("قل هو الله احد"))
        assertTrue(normalized.contains("الله الصمد"))
    }

    @Test
    fun testMatchesFunctionPerformsDiacriticNeutralSearch() {
        val quranAyah = "اللَّهُ نُورُ السَّمَاوَاتِ وَالْأَرْضِ ۚ مَثَلُ نُورِهِ كَمِشْكَاةٍ"
        assertTrue(ArabicNormalizer.contains(quranAyah, "نور السماوات"))
        assertTrue(ArabicNormalizer.contains(quranAyah, "كمشكاة"))
        assertTrue(ArabicNormalizer.contains(quranAyah, "الله نور"))
    }
}
