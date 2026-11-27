package com.dailydeeds.reminder.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * The three UI languages (Arabic default, English, Persian) must stay in lockstep: every key in every file, the
 * same format placeholders, nothing empty. A key added to Arabic without its translations fails the build here
 * instead of silently showing Arabic in an English or Persian UI.
 */
class StringResourcesTest {

    private val resDir = File("src/main/res")

    private fun load(folder: String): Map<String, String> {
        val file = File(resDir, "$folder/strings.xml")
        assertTrue("missing ${file.path}", file.isFile)
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val nodes = doc.getElementsByTagName("string")
        return (0 until nodes.length).map { nodes.item(it) as Element }
            .filter { it.getAttribute("translatable") != "false" }
            .associate { it.getAttribute("name") to it.textContent }
    }

    private val arabic by lazy { load("values") }
    private val translations by lazy { mapOf("en" to load("values-en"), "fa" to load("values-fa")) }

    private val placeholder = Regex("""%(\d+\$)?[sd]""")
    private fun placeholders(text: String) = placeholder.findAll(text).map { it.value }.sorted().toList()

    @Test
    fun everyLanguageHasExactlyTheArabicKeys() {
        translations.forEach { (lang, map) ->
            assertEquals("keys missing from values-$lang", emptySet<String>(), arabic.keys - map.keys)
            assertEquals("keys in values-$lang that Arabic does not have", emptySet<String>(), map.keys - arabic.keys)
        }
    }

    @Test
    fun placeholdersMatchTheArabicOriginal() {
        translations.forEach { (lang, map) ->
            arabic.forEach { (key, text) ->
                assertEquals("placeholders of $key in values-$lang", placeholders(text), placeholders(map[key] ?: ""))
            }
        }
    }

    @Test
    fun noStringIsEmpty() {
        (translations + ("ar" to arabic)).forEach { (lang, map) ->
            map.forEach { (key, text) -> assertTrue("$key is empty in $lang", text.isNotBlank()) }
        }
    }

    @Test
    fun englishCarriesNoArabicScript() {
        // A forgotten copy-paste of the Arabic text into values-en would otherwise pass the key check.
        val arabicScript = Regex("[؀-ۿ]")
        translations.getValue("en").forEach { (key, text) ->
            assertFalse("$key in values-en contains Arabic script: $text", arabicScript.containsMatchIn(text))
        }
    }
}
