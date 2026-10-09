package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.MizanHeader
import com.dailydeeds.reminder.model.MizanKind
import com.dailydeeds.reminder.model.MizanParagraph
import java.io.ByteArrayOutputStream

/**
 * Format of `tafsir/mizan.txt`, one record per line: `kind|surah|from|to|title|body`.
 * The body holds the paragraphs joined by a backslash and `n`; every paragraph starts with a tag and a space.
 * A literal backslash is written twice. See scripts/build_mizan.py.
 */
object MizanFormat {
    const val RESOURCE_PATH = "tafsir/mizan.txt"

    fun parseHeader(head: String, id: Int, offset: Long, length: Int): MizanHeader? {
        val f = head.split('|')
        if (f.size < 5) return null
        val kind = when (f[0]) {
            "F" -> MizanKind.FRONT
            "S" -> MizanKind.SECTION
            "I" -> MizanKind.INDEX
            else -> return null
        }
        val surah = f[1].toIntOrNull() ?: return null
        val from = f[2].toIntOrNull() ?: return null
        val to = f[3].toIntOrNull() ?: return null
        return MizanHeader(id, kind, surah, from, to, f[4], offset, length)
    }

    fun parseBody(body: String): List<MizanParagraph> {
        val out = ArrayList<MizanParagraph>()
        val text = StringBuilder()

        fun flush() {
            if (text.isEmpty()) return
            val raw = text.toString()
            val space = raw.indexOf(' ')
            out += if (space <= 0) MizanParagraph("P", raw) else MizanParagraph(raw.substring(0, space), raw.substring(space + 1))
            text.setLength(0)
        }

        var i = 0
        while (i < body.length) {
            val c = body[i]
            if (c == '\\' && i + 1 < body.length) {
                val next = body[i + 1]
                if (next == 'n') {
                    flush()
                    i += 2
                    continue
                }
                if (next == '\\') {
                    text.append('\\')
                    i += 2
                    continue
                }
            }
            text.append(c)
            i++
        }
        flush()
        return out
    }

    private fun isTashkeel(c: Char): Boolean =
        c in 'ً'..'ٕ' || c == 'ٰ' || c in 'ۖ'..'ۭ' || c == '۝' || c == 'ـ'

    private fun isAlef(c: Char): Boolean = c == 'ا' || c == 'أ' || c == 'إ' || c == 'آ' || c == 'ٱ'

    /**
     * Matching key for the full-text search: no diacritics, no alefs (Uthmani and everyday spelling meet),
     * ta marbuta as ha, alef maqsura as ya, single spaces. A one-pass version of
     * [com.dailydeeds.reminder.util.ArabicNormalizer.searchKey] because it runs over ten million letters.
     */
    fun searchKey(text: String): String = keyWithMap(text, false).first

    /** [searchKey] plus, for every key character, the index it came from in [text]. */
    fun keyWithMap(text: String, withMap: Boolean = true): Pair<String, IntArray> {
        val key = StringBuilder(text.length)
        val map = if (withMap) IntArray(text.length + 1) else IntArray(0)
        var pendingSpace = -1
        for (i in text.indices) {
            val ch = text[i]
            if (isTashkeel(ch) || isAlef(ch)) continue
            if (ch.isWhitespace()) {
                if (key.isNotEmpty() && pendingSpace < 0) pendingSpace = i
                continue
            }
            if (pendingSpace >= 0) {
                if (withMap) map[key.length] = pendingSpace
                key.append(' ')
                pendingSpace = -1
            }
            if (withMap) map[key.length] = i
            key.append(
                when (ch) {
                    'ة' -> 'ه'
                    'ى' -> 'ي'
                    else -> ch.lowercaseChar()
                }
            )
        }
        return key.toString() to map
    }
}

/**
 * Finds the records of the bundled file while it streams past, without keeping the text in memory:
 * feed the bytes in order, then call [finish] for one [MizanHeader] per line with its byte offset and length.
 */
class MizanIndexBuilder {
    private val headers = ArrayList<MizanHeader>()
    private val head = ByteArrayOutputStream()
    private var pipes = 0
    private var position = 0L
    private var lineStart = 0L

    fun feed(buffer: ByteArray, count: Int) {
        for (i in 0 until count) {
            val b = buffer[i]
            if (b == NEWLINE) {
                endLine()
            } else if (pipes < HEADER_PIPES) {
                head.write(b.toInt())
                if (b == PIPE) pipes++
            }
            position++
        }
    }

    fun finish(): List<MizanHeader> {
        if (position > lineStart) endLine()
        return headers
    }

    private fun endLine() {
        if (position > lineStart) {
            val header = MizanFormat.parseHeader(head.toString(Charsets.UTF_8.name()), headers.size, lineStart, (position - lineStart).toInt())
                ?: throw IllegalStateException("malformed Mizan record at byte $lineStart")
            headers += header
        }
        head.reset()
        pipes = 0
        lineStart = position + 1
    }

    private companion object {
        const val NEWLINE = '\n'.code.toByte()
        const val PIPE = '|'.code.toByte()
        const val HEADER_PIPES = 5
    }
}
