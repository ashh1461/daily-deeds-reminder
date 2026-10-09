package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MizanKind
import com.dailydeeds.reminder.util.ArabicNormalizer
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/** The complete Tafsir al-Mizan: the bundled file is pinned, complete and linked to every ayah. */
class MizanDataTest {

    companion object {
        @ClassRule
        @JvmField
        val folder = TemporaryFolder()

        lateinit var data: MizanData

        @BeforeClass
        @JvmStatic
        fun openBook() {
            val dataFile = File(folder.root, "mizan.txt")
            val indexFile = File(folder.root, "mizan.idx")
            val stream = MizanDataTest::class.java.classLoader!!.getResourceAsStream(MizanFormat.RESOURCE_PATH)!!
            MizanData.prepare(stream, dataFile, indexFile)
            data = MizanData.open(dataFile, indexFile)
        }
    }

    @Test
    fun theBundledBookIsPinned() {
        val bytes = File("src/main/resources/${MizanFormat.RESOURCE_PATH}").readBytes()
        val sha = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
        assertEquals(24_042_381, bytes.size)
        assertEquals("1bcec540382cea40505149eca3bfb3445a344db361bff2721c498d6c66a12aa3", sha)
    }

    @Test
    fun theBookHasTheExpectedEntries() {
        assertEquals(615, data.headers.size)
        assertEquals(1, data.headers.count { it.kind == MizanKind.FRONT })
        assertEquals(594, data.headers.count { it.kind == MizanKind.SECTION })
        assertEquals(20, data.indexes.size)
        assertEquals(MizanKind.FRONT, data.headers.first().kind)
        assertEquals(data.headers.indices.toList(), data.headers.map { it.id })
        assertTrue(data.headers.zipWithNext().all { (a, b) -> a.offset + a.length + 1 == b.offset })
    }

    @Test
    fun everyAyahHasASectionAndTheSectionsFollowTheQuran() {
        for (surah in QuranDataProvider.surahs) {
            for (ayah in 1..surah.ayahCount) {
                val id = data.sectionIdFor(surah.number, ayah)
                assertNotNull("no section for ${surah.number}:$ayah", id)
                val header = data.header(id!!)!!
                assertTrue(ayah in header.from..header.to)
            }
        }
        val sections = data.headers.filter { it.kind == MizanKind.SECTION }
        assertTrue(sections.zipWithNext().all { (a, b) -> a.surah < b.surah || (a.surah == b.surah && a.from < b.from) })
        assertNull(data.sectionIdFor(1, 8))
        assertNull(data.sectionIdFor(115, 1))
        assertTrue(data.sectionsOf(114).isNotEmpty())
    }

    @Test
    fun theFirstSectionIsTheCommentaryOnTheBasmala() {
        val id = data.sectionIdFor(1, 1)!!
        val entry = data.readEntry(id)!!
        assertEquals(1, entry.header.surah)
        assertEquals(1, entry.header.from)
        val text = entry.paragraphs.joinToString(" ") { it.text }
        assertTrue(ArabicNormalizer.contains(text, "الناس ربما يعملون عملا أو يبتدئون في عمل"))
        assertTrue(entry.paragraphs.any { it.headingLevel > 0 })
    }

    @Test
    fun ayatAlKursiLandsInASectionThatCoversIt() {
        val h = data.header(data.sectionIdFor(2, 255)!!)!!
        assertEquals(2, h.surah)
        assertTrue(255 in h.from..h.to)
    }

    @Test
    fun theWholeTextIsCleanAndWellFormed() {
        var paragraphs = 0
        var pages = 0
        var maxLength = 0
        for (header in data.headers) {
            val entry = data.readEntry(header.id)!!
            assertTrue("entry ${header.id} has no text", entry.paragraphs.any { it.tag == "P" })
            for (p in entry.paragraphs) {
                paragraphs++
                assertTrue("unknown tag ${p.tag}", p.tag == "P" || p.tag == "G" || p.headingLevel in 1..4)
                if (p.isPage) {
                    pages++
                    assertTrue(Regex("\\d+:\\d+").matches(p.text))
                    continue
                }
                assertFalse("marker left in ${header.id}", p.text.contains("@QUR@") || p.text.contains("PageV") || p.text.contains("%~%"))
                assertFalse(Regex("\\bms\\d+\\b").containsMatchIn(p.text))
                assertEquals("unbalanced quotation in entry ${header.id}", p.text.count { it == '﴿' }, p.text.count { it == '﴾' })
                maxLength = maxOf(maxLength, p.text.length)
            }
        }
        assertEquals(74_894, paragraphs)
        assertEquals(8_468, pages)
        assertTrue("a paragraph is suspiciously long: $maxLength", maxLength < 5_000)
    }

    @Test
    fun quotedAyatKeepTheirOrnateBrackets() {
        val entry = data.readEntry(data.sectionIdFor(2, 255)!!)!!
        assertTrue(entry.paragraphs.any { it.text.contains("﴿") && it.text.contains("﴾") })
    }

    @Test
    fun paragraphsAreSplitOnBackslashN() {
        val p = MizanFormat.parseBody("H3 ( بيان )\\nP first line\\nG 2:446\\nP back\\\\slash")
        assertEquals(listOf("H3", "P", "G", "P"), p.map { it.tag })
        assertEquals("( بيان )", p[0].text)
        assertEquals(3, p[0].headingLevel)
        assertTrue(p[2].isPage)
        assertEquals("back\\slash", p[3].text)
        assertTrue(MizanFormat.parseBody("").isEmpty())
    }

    @Test
    fun headersAreParsedStrictly() {
        assertNotNull(MizanFormat.parseHeader("S|2|8|20||", 0, 0, 10))
        assertNotNull(MizanFormat.parseHeader("I|0|0|0|الفهرس|", 3, 5, 10))
        assertNull(MizanFormat.parseHeader("X|2|8|20||", 0, 0, 10))
        assertNull(MizanFormat.parseHeader("S|a|8|20||", 0, 0, 10))
        assertNull(MizanFormat.parseHeader("S|2|8", 0, 0, 10))
    }

    @Test
    fun theSearchKeyMatchesTheSharedNormalizer() {
        val sample = data.readEntry(data.sectionIdFor(2, 255)!!)!!.paragraphs.filter { !it.isPage }.take(300)
        for (p in sample) {
            val shared = ArabicNormalizer.searchKey(p.text).lowercase().replace("ـ", "").replace(Regex("\\s+"), " ").trim()
            assertEquals(shared, MizanFormat.searchKey(p.text))
        }
        assertEquals("بسم لله لرحمن لرحيم", MizanFormat.searchKey("  بِسْمِ   اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ "))
        assertEquals(MizanFormat.searchKey("الصلاة"), MizanFormat.searchKey("الصلاه"))
    }

    @Test
    fun searchFindsTextIgnoringDiacriticsAndGivesASnippet() {
        val hits = data.search("الناس ربما يعملون عملا أو يبتدئون", limit = 5)
        assertTrue(hits.isNotEmpty())
        val header = data.header(hits.first().entryId)!!
        assertEquals(MizanKind.SECTION, header.kind)
        assertEquals(1, header.surah)
        assertTrue(ArabicNormalizer.contains(hits.first().snippet, "ربما يعملون"))
        // the same words with vowel marks and another alef spelling
        assertTrue(data.search("النَّاسُ رُبَّمَا يَعْمَلُونَ عَمَلًا", limit = 5).isNotEmpty())
    }

    @Test
    fun searchIsBoundedAndSafeWithHostileInput() {
        assertTrue(data.search("").isEmpty())
        assertTrue(data.search("ا").isEmpty())
        assertTrue(data.search("   ").isEmpty())
        assertTrue(data.search("(((.*+?[]\\", limit = 3).size <= 3)
        val long = data.search("و".repeat(100_000), limit = 3)
        assertTrue(long.size <= 3)
        assertTrue(data.search("قوله تعالى", limit = 7).size <= 7)
    }

    @Test
    fun searchStopsWhenCancelled() {
        var calls = 0
        val hits = data.search("zzzzqqqq", isCancelled = { ++calls > 2 })
        assertTrue(hits.isEmpty())
        assertTrue(calls <= 4)
    }

    @Test
    fun aMismatchedIndexIsRejected() {
        val dataFile = File(folder.root, "mizan.txt")
        val bad = File(folder.root, "bad.idx")
        bad.writeText("v1|${dataFile.length() + 1}|615\n")
        try {
            MizanData.open(dataFile, bad)
            throw AssertionError("a wrong index must be rejected")
        } catch (e: IOException) {
            // expected
        }
    }
}
