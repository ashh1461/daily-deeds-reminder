package com.dailydeeds.reminder

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Source and resource files must never be zero bytes. Something on the build machine (an editor or a sync tool
 * saving a stale buffer) has emptied files before, and an empty Kotlin or XML file only shows up later as a
 * confusing compile error or, worse, a silently missing screen. This fails first and names the file.
 */
class NoEmptySourceFilesTest {

    private val checked = setOf("kt", "kts", "xml", "txt", "json", "md", "py", "pro", "yml")

    private fun emptyFilesUnder(root: File): List<String> =
        if (!root.isDirectory) emptyList()
        else root.walkTopDown()
            .filter { it.isFile && it.extension in checked && it.length() == 0L }
            .map { it.relativeTo(root.parentFile.parentFile).path }
            .toList()

    @Test
    fun noSourceOrResourceFileIsEmpty() {
        val empty = emptyFilesUnder(File("src/main")) + emptyFilesUnder(File("src/test"))
        assertTrue("empty files: $empty", empty.isEmpty())
    }

    @Test
    fun theReleaseNotesAndScriptsAreNotEmptyEither() {
        val repo = File("..")
        val files = listOf("docs/releases", "scripts", "branding").flatMap { dir ->
            File(repo, dir).walkTopDown().filter { it.isFile && it.extension in checked && it.length() == 0L }.toList()
        }
        assertTrue("empty files: ${files.map { it.path }}", files.isEmpty())
    }
}
