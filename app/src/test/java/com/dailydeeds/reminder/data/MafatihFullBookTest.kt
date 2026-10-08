package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.util.ArabicNormalizer
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Guards the bundled Mafatih al-Jinan and Sahifa Sajjadiyya texts against truncation and conversion damage. */
class MafatihFullBookTest {

    private val repository = MafatihRepository()
    private val book = repository.getItemsByCategory(MafatihCategoryType.FULLBOOK)
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
    }

    @Test
    fun noConversionArtifactsRemain() {
        for (item in book + sahifa) {
            assertFalse(item.id, item.arabicText.contains("PageV"))
            assertFalse(item.id, Regex("ms[0-9]+").containsMatchIn(item.arabicText))
            assertFalse(item.id, item.arabicText.contains("~~"))
            assertFalse(item.id, item.title.contains("ms9"))
            assertTrue(item.id, item.arabicText.isNotBlank())
        }
    }

    @Test
    fun noWordsAreGluedTogetherAtPageBreaks() {
        val all = book.joinToString(" ") { it.arabicText }
        // These glued forms appeared when page markers were removed without a separating space.
        assertFalse(all.contains("الاحدبسم"))
        assertFalse(all.contains("الاثنينالحمد"))
    }

    @Test
    fun sahifaHasAllFiftyFourSupplicationsInOrder() {
        val numbered = sahifa.mapNotNull { Regex("^الدعاء (\\d+):").find(it.title)?.groupValues?.get(1)?.toInt() }
        assertEquals((1..54).toList(), numbered)
        assertTrue(sahifa.any { it.title.contains("دعاؤه في يوم عرفة") && it.arabicText.length > 10_000 })
        assertTrue(sahifa.any { it.title.contains("دعاؤه لوداع شهر رمضان") })
        assertTrue(sahifa.size >= 80)
        assertTrue(sahifa.first().arabicText.contains("الحمد لله الاول بلا أول") || ArabicNormalizer.contains(sahifa.first().arabicText, "الحمد لله الاول بلا اول كان قبله"))
    }

    @Test
    fun bundledResourcesMatchPinnedChecksums() {
        assertEquals("89e9e207b325ab197eac5d5d8b7410d026e41d6a60edc98f883642202bec41a9", sha256(MafatihDataProvider.BOOK_RESOURCE))
        assertEquals("62d5a98af5e2e8cc787e9da8e592c5263048b5c1ce748cbff35d2ac5807812c0", sha256(MafatihDataProvider.SAHIFA_RESOURCE))
    }

    @Test
    fun bookAndSahifaAreSearchable() {
        val book = SearchRepository().search("اللهم اني اسالك برحمتك التي وسعت كل شيء", com.dailydeeds.reminder.model.SearchResultType.MAFATIH)
        assertTrue(book.any { it.title.contains("دعاء كميل") })
        val sj = SearchRepository().search("الحمد لله الاول بلا اول كان قبله", com.dailydeeds.reminder.model.SearchResultType.MAFATIH)
        assertTrue(sj.any { it.mafatihItemId == "sj_1" })
    }
}
