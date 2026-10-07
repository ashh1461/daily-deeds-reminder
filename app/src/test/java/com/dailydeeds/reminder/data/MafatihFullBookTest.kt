package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MafatihCategoryType
import com.dailydeeds.reminder.util.ArabicNormalizer
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MafatihFullBookTest {

    private val items = MafatihRepository().getItemsByCategory(MafatihCategoryType.FULLBOOK)

    @Test
    fun fullBookIsBundledAndSubstantial() {
        assertTrue("sections: ${items.size}", items.size >= 100)
        assertTrue(items.sumOf { it.arabicText.length } > 800_000)
        assertEquals(items.size, items.map { it.id }.toSet().size)
    }

    @Test
    fun coversThreeBabsAndClosingColophon() {
        val all = items.joinToString("\n") { it.title + "\n" + it.arabicText }
        assertTrue(ArabicNormalizer.contains(all, "الباب الاول : في تعقيب الصلوات"))
        assertTrue(ArabicNormalizer.contains(all, "الباب الثاني في أعمال أشهر السنة"))
        assertTrue(ArabicNormalizer.contains(all, "الباب الثالث في الزيارات"))
        assertTrue(ArabicNormalizer.contains(all, "دعاء كميل"))
        assertTrue(ArabicNormalizer.contains(all, "زيارة عاشوراء"))
        assertTrue(ArabicNormalizer.contains(items.last().arabicText, "وصلى الله على محمد وآله الطاهرين"))
    }

    @Test
    fun noConversionArtifactsRemain() {
        for (item in items) {
            assertFalse(item.id, item.arabicText.contains("PageV"))
            assertFalse(item.id, Regex("ms[0-9]+").containsMatchIn(item.arabicText))
            assertFalse(item.id, item.arabicText.contains("~~"))
            assertTrue(item.id, item.arabicText.isNotBlank())
            assertFalse(item.id, item.isExcerpt)
        }
    }

    @Test
    fun bundledResourceMatchesPinnedChecksum() {
        val bytes = MafatihDataProvider::class.java.classLoader!!
            .getResourceAsStream(MafatihDataProvider.FULLBOOK_RESOURCE)!!.use { it.readBytes() }
        val sha = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        assertEquals("678d3b945b610220a28d1791b69083b42f7b09177ea7a042b29f1e0353104c6a", sha)
    }

    @Test
    fun fullBookIsSearchable() {
        val results = SearchRepository().search("اللهم إني أسألك برحمتك التي وسعت كل شيء", com.dailydeeds.reminder.model.SearchResultType.MAFATIH)
        assertTrue(results.any { it.mafatihItemId?.startsWith("mj_") == true })
    }
}
