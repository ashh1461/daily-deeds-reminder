package com.dailydeeds.reminder.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** After a backup import, the view models that hold settings in memory are told to read them again. */
class DataReloadsTest {

    @Test
    fun eachImportIsAnnouncedOnce() {
        val before = DataReloads.imports.value
        DataReloads.backupImported()
        assertEquals(before + 1, DataReloads.imports.value)
        DataReloads.backupImported()
        assertEquals(before + 2, DataReloads.imports.value)
    }

    @Test
    fun aViewModelHearsOnlyImportsAfterItWasCreated() {
        val seen = DataReloads.imports.value
        // Nothing imported since: no reload (it already read the current values).
        assertNull(runBlocking { withTimeoutOrNull(200) { DataReloads.importsAfter(seen).first() } })
        DataReloads.backupImported()
        assertEquals(seen + 1, runBlocking { withTimeout(2_000) { DataReloads.importsAfter(seen).first() } })
    }

    @Test
    fun theNewSettingsOf184AreBackedUpWithTheirTypes() {
        val values = mapOf<String, Any>("pref_last_ayah" to 45, "pref_blessed_eve_enabled" to true)
        values.keys.forEach { assertTrue(it, BackupCodec.isAllowedKey(it)) }
        val back = BackupCodec.parse(BackupCodec.export(values, "2026-11-26T00:00:00Z")) as BackupCodec.Import.Ok
        assertEquals(0, back.skipped)
        assertEquals(values, back.entries)
    }

    @Test
    fun theKhumsDraftIsPartOfABackup() {
        assertTrue(BackupCodec.isAllowedKey("worship_khums_draft"))
    }
}
