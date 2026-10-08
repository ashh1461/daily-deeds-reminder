package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.model.SearchResultType
import com.dailydeeds.reminder.util.ArabicNormalizer
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Guards the bundled Mafatih al-Jinan and Sahifa Sajjadiyya texts against truncation and conversion damage. */
class MafatihFullBookTest {

    private val repository = MafatihRepository()
    private val book = repository.getItemsByCategory(MafatihCategoryType.INDEX)
    private val sahifa = repository.getItemsByCategory(MafatihCategoryType.SAHIFA)

    private fun sha256(path: String): String {
        val bytes = MafatihDataProvider::class.java.classLoader!!.getResourceAsStream(path)!!.use { it.readBytes() }
        return MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
    }

    @Test
    fun bookIsSubstantialAndEveryIdIsUnique() {
        assertTrue("sections: ${book.size}", book.size >= 120)
        assertTrue(book.sumOf { it.arabicText.length } > 800_000)
        assertEquals(book.size, book.map { it.id }.toSet().size)
    }

    @Test
    fun coversAllThreeBabsAndTheClosingColophon() {
        val all = book.joinToString("\n") { it.title + "\n" + it.arabicText }
        assertTrue(ArabicNormalizer.contains(all, "الباب الاول : في تعقيب الصلوات"))
        assertTrue(ArabicNormalizer.contains(all, "الباب الثاني في أعمال أشهر السنة"))
        assertTrue(ArabicNormalizer.contains(all, "الباب الثالث في الزيارات"))
        assertTrue(ArabicNormalizer.contains(book.last().arabicText, "وصلى الله على محمد وآله الطاهرين"))
        assertTrue(book.any { it.group.startsWith("الباب الأول") })
        assertTrue(book.any { it.group.startsWith("الباب الثاني") })
        assertTrue(book.any { it.group.startsWith("الباب الثالث") })
    }

    @Test
    fun noConversionArtifactsRemain() {
        for (item in book + sahifa) {
            assertFalse(item.id, item.arabicText.contains("PageV"))
            assertFalse(item.id, Regex("ms[0-9]+").containsMatchIn(item.arabicText))
            assertFalse(item.id, item.arabicText.contains("~~"))
            assertFalse(item.id, item.title.contains("ms9"))
            assertTrue(item.id, item.arabicText.isNotBlank())
            assertTrue(item.id, item.group.isNotBlank())
        }
    }

    @Test
    fun noWordsAreGluedTogetherAtPageBreaks() {
        val all = book.joinToString(" ") { it.arabicText }
        assertFalse(all.contains("الاحدبسم"))
        assertFalse(all.contains("الاثنينالحمد"))
    }

    @Test
    fun sahifaHasAllFiftyFourSupplicationsInOrderAndFourGroups() {
        val numbered = sahifa.mapNotNull { Regex("^الدعاء (\\d+):").find(it.title)?.groupValues?.get(1)?.toInt() }
        assertEquals((1..54).toList(), numbered)
        assertTrue(sahifa.any { it.title.contains("دعاؤه في يوم عرفة") && it.arabicText.length > 10_000 })
        assertTrue(sahifa.any { it.title.contains("دعاؤه لوداع شهر رمضان") })
        assertTrue(sahifa.size >= 80)
        assertTrue(ArabicNormalizer.contains(sahifa.first().arabicText, "الحمد لله الاول بلا اول كان قبله"))
        val groups = sahifa.map { it.group }.distinct()
        assertTrue(groups.containsAll(listOf("ملحقات الصحيفة", "أدعية الأيام السبعة", "المناجيات الخمس عشرة")))
        assertEquals(6 + 3, groups.size) // six groups of ten supplications (1-54) plus three appendix groups
    }

    @Test
    fun bundledResourcesMatchPinnedChecksums() {
        assertEquals("8ac641074647e7d4d4b3edeee24a7c57210f37e5ca18f5911153decd5af8a8ee", sha256(MafatihDataProvider.BOOK_RESOURCE))
        assertEquals("b8589958e79ee9d994018510b1aa548c74ed87ca92d5764af256f4b2dae8bea1", sha256(MafatihDataProvider.SAHIFA_RESOURCE))
    }

    @Test
    fun bookAndSahifaAreSearchableUnderTheirOwnTypes() {
        val search = SearchRepository()
        val inBook = search.search("اللهم اني اسالك برحمتك التي وسعت كل شيء", SearchResultType.MAFATIH)
        assertTrue(inBook.any { it.title.contains("دعاء كميل") })
        assertTrue(inBook.none { it.mafatihItemId?.startsWith("sj_") == true })
        val inSahifa = search.search("الحمد لله الاول بلا اول كان قبله", SearchResultType.SAHIFA)
        assertTrue(inSahifa.any { it.mafatihItemId == "sj_1" })
        assertTrue(inSahifa.all { it.type == SearchResultType.SAHIFA })
    }
}
