package com.dailydeeds.reminder.data

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.dailydeeds.reminder.model.MizanEntry
import com.dailydeeds.reminder.model.MizanHeader
import com.dailydeeds.reminder.model.MizanHit
import com.dailydeeds.reminder.model.MizanKind
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile

/**
 * The complete Tafsir al-Mizan (about 24 MB of text). The text stays on disk and an entry is read only when
 * it is opened, so the app never holds the whole book in memory.
 */
class MizanData private constructor(private val file: File, val headers: List<MizanHeader>) {

    val front: MizanHeader? = headers.firstOrNull { it.kind == MizanKind.FRONT }
    val indexes: List<MizanHeader> = headers.filter { it.kind == MizanKind.INDEX }
    private val sectionsBySurah: Map<Int, List<MizanHeader>> = headers.filter { it.kind == MizanKind.SECTION }.groupBy { it.surah }

    fun header(id: Int): MizanHeader? = headers.getOrNull(id)

    fun sectionsOf(surah: Int): List<MizanHeader> = sectionsBySurah[surah].orEmpty()

    /** The section that comments on this ayah (a few ayat share one section). */
    fun sectionIdFor(surah: Int, ayah: Int): Int? =
        sectionsBySurah[surah]?.firstOrNull { ayah in it.from..it.to }?.id

    fun readEntry(id: Int): MizanEntry? {
        val header = header(id) ?: return null
        return RandomAccessFile(file, "r").use { raf -> MizanEntry(header, MizanFormat.parseBody(readBody(raf, header))) }
    }

    private fun readBody(raf: RandomAccessFile, header: MizanHeader): String {
        val bytes = ByteArray(header.length)
        raf.seek(header.offset)
        raf.readFully(bytes)
        val line = String(bytes, Charsets.UTF_8)
        var pipes = 0
        var i = 0
        while (i < line.length && pipes < 5) {
            if (line[i] == '|') pipes++
            i++
        }
        return line.substring(i)
    }

    /**
     * Searches every paragraph of the book, ignoring diacritics and alef spelling. Reads the file once;
     * [onProgress] gets (entries done, entries total) and [isCancelled] stops the scan early.
     */
    fun search(
        query: String,
        limit: Int = 60,
        isCancelled: () -> Boolean = { false },
        onProgress: (Int, Int) -> Unit = { _, _ -> }
    ): List<MizanHit> {
        val needle = MizanFormat.searchKey(query.take(MAX_QUERY))
        if (needle.length < 2) return emptyList()
        val hits = ArrayList<MizanHit>()
        RandomAccessFile(file, "r").use { raf ->
            for (header in headers) {
                if (isCancelled() || hits.size >= limit) break
                val paragraphs = MizanFormat.parseBody(readBody(raf, header))
                for ((index, paragraph) in paragraphs.withIndex()) {
                    if (paragraph.isPage) continue
                    if (!MizanFormat.searchKey(paragraph.text).contains(needle)) continue
                    hits += MizanHit(header.id, index, snippet(paragraph.text, needle))
                    if (hits.size >= limit) break
                }
                onProgress(header.id + 1, headers.size)
            }
        }
        return hits
    }

    private fun snippet(text: String, needle: String): String {
        val (key, map) = MizanFormat.keyWithMap(text)
        val at = key.indexOf(needle)
        if (at < 0) return text.take(SNIPPET_CHARS)
        val start = (map[at] - SNIPPET_BEFORE).coerceAtLeast(0)
        val end = (start + SNIPPET_CHARS).coerceAtMost(text.length)
        return (if (start > 0) "… " else "") + text.substring(start, end) + (if (end < text.length) " …" else "")
    }

    companion object {
        private const val MAX_QUERY = 200
        private const val SNIPPET_BEFORE = 60
        private const val SNIPPET_CHARS = 200
        private const val INDEX_VERSION = "v1"

        fun open(dataFile: File, indexFile: File): MizanData {
            val headers = readIndex(indexFile, dataFile.length())
                ?: throw IOException("the Mizan index does not match ${dataFile.name}")
            return MizanData(dataFile, headers)
        }

        /** Copies [input] to [dataFile] while building [indexFile]; both are written under a temporary name first. */
        fun prepare(input: InputStream, dataFile: File, indexFile: File) {
            val tmpData = File(dataFile.path + ".tmp")
            val tmpIndex = File(indexFile.path + ".tmp")
            val builder = MizanIndexBuilder()
            input.use { src ->
                tmpData.outputStream().use { out ->
                    val buffer = ByteArray(64 * 1024)
                    while (true) {
                        val n = src.read(buffer)
                        if (n < 0) break
                        out.write(buffer, 0, n)
                        builder.feed(buffer, n)
                    }
                }
            }
            val headers = builder.finish()
            tmpIndex.bufferedWriter(Charsets.UTF_8).use { w ->
                w.write("$INDEX_VERSION|${tmpData.length()}|${headers.size}\n")
                for (h in headers) {
                    w.write("${h.offset}|${h.length}|${kindCode(h.kind)}|${h.surah}|${h.from}|${h.to}|${h.title}\n")
                }
            }
            if (!tmpData.renameTo(dataFile) || !tmpIndex.renameTo(indexFile)) throw IOException("could not store the Mizan files")
        }

        private fun kindCode(kind: MizanKind) = when (kind) {
            MizanKind.FRONT -> "F"
            MizanKind.SECTION -> "S"
            MizanKind.INDEX -> "I"
        }

        private fun readIndex(indexFile: File, dataLength: Long): List<MizanHeader>? {
            if (!indexFile.isFile) return null
            val lines = indexFile.readLines(Charsets.UTF_8)
            val first = lines.firstOrNull()?.split('|') ?: return null
            if (first.size != 3 || first[0] != INDEX_VERSION || first[1].toLongOrNull() != dataLength) return null
            val headers = ArrayList<MizanHeader>()
            for (line in lines.drop(1)) {
                if (line.isEmpty()) continue
                val f = line.split('|', limit = 7)
                if (f.size < 7) return null
                val head = "${f[2]}|${f[3]}|${f[4]}|${f[5]}|${f[6]}|"
                headers += MizanFormat.parseHeader(head, headers.size, f[0].toLongOrNull() ?: return null, f[1].toIntOrNull() ?: return null) ?: return null
            }
            return if (headers.size == first[2].toIntOrNull()) headers else null
        }
    }
}

/** Opens the bundled book, copying it to the app's private storage the first time (and after an update). */
object MizanStore {
    fun open(context: Context): MizanData {
        val dir = File(context.filesDir, "mizan").apply { mkdirs() }
        val stamp = PackageInfoCompat.getLongVersionCode(context.packageManager.getPackageInfo(context.packageName, 0))
        val data = File(dir, "mizan-$stamp.txt")
        val index = File(dir, "mizan-$stamp.idx")
        if (!data.isFile || !index.isFile) {
            val stream = MizanStore::class.java.classLoader?.getResourceAsStream(MizanFormat.RESOURCE_PATH)
                ?: throw IOException("missing bundled resource ${MizanFormat.RESOURCE_PATH}")
            MizanData.prepare(stream, data, index)
        }
        dir.listFiles()?.filter { it.name.startsWith("mizan-") && !it.name.startsWith("mizan-$stamp.") }?.forEach { it.delete() }
        return try {
            MizanData.open(data, index)
        } catch (e: IOException) {
            // A damaged copy is rebuilt from the bundled file.
            data.delete()
            index.delete()
            val stream = MizanStore::class.java.classLoader?.getResourceAsStream(MizanFormat.RESOURCE_PATH)
                ?: throw IOException("missing bundled resource ${MizanFormat.RESOURCE_PATH}")
            MizanData.prepare(stream, data, index)
            MizanData.open(data, index)
        }
    }
}
