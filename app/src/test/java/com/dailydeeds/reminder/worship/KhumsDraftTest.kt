package com.dailydeeds.reminder.worship

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** The khums figures are kept as a draft between visits; the stored text is untrusted (it can come from a backup). */
class KhumsDraftTest {

    @Test
    fun aDraftSurvivesEncodeAndDecode() {
        val draft = KhumsDraft("١٢٬٥٠٠", "8,000.50", "", "100")
        assertEquals(draft, KhumsDraft.decode(draft.encode()))
    }

    @Test
    fun separatorsAndQuotesInsideAFieldDoNotBreakTheDraft() {
        val draft = KhumsDraft("1|2", "\"3\"", "4,5", "[6]")
        assertEquals(draft, KhumsDraft.decode(draft.encode()))
    }

    @Test
    fun missingOrBrokenTextGivesAnEmptyDraft() {
        listOf(null, "", "not json", "[1,2]", "{\"a\":1}", "[\"1\",\"2\",\"3\",\"4\",\"5\"]").forEach {
            assertTrue("input $it", KhumsDraft.decode(it).isEmpty)
        }
    }

    @Test
    fun fieldsAreCappedToTheInputLength() {
        val long = "9".repeat(100)
        val decoded = KhumsDraft.decode("""["$long","1","2","3"]""")
        assertEquals(KhumsDraft.MAX_FIELD, decoded.gains.length)
        assertEquals("1", decoded.expenses)
    }

    @Test
    fun aDraftOfBlanksIsEmpty() {
        assertTrue(KhumsDraft().isEmpty)
        assertTrue(KhumsDraft(" ", "", "", "").isEmpty)
    }
}
