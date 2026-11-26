package com.dailydeeds.reminder.data

import org.junit.Assert.assertEquals
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
    fun theKhumsDraftIsPartOfABackup() {
        assertTrue(BackupCodec.isAllowedKey("worship_khums_draft"))
    }
}
