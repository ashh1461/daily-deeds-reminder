package com.dailydeeds.reminder.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.os.UserManagerCompat
import java.io.File

/**
 * Decisions for running before the first unlock after a restart ("direct boot"). Pure logic, unit-tested; the
 * Android side is [PrefsStorage] and the boot receiver.
 */
object DirectBoot {
    enum class BootWork {
        /** The settings cannot be read yet: do nothing and wait for the unlock broadcast. */
        NONE,
        /** Before unlock: only the prayer alarms, whose receivers and service can run while the phone is locked. */
        PRAYER_ONLY,
        /** Unlocked: every alarm and reminder. */
        ALL
    }

    /** Settings are readable once the user has unlocked, or before that if the device-protected copy already exists. */
    fun canReadSettings(unlocked: Boolean, deviceCopyExists: Boolean): Boolean = unlocked || deviceCopyExists

    fun bootWork(unlocked: Boolean, deviceCopyExists: Boolean): BootWork = when {
        !canReadSettings(unlocked, deviceCopyExists) -> BootWork.NONE
        !unlocked -> BootWork.PRAYER_ONLY
        else -> BootWork.ALL
    }

    /**
     * The one-time split of the single settings file into a device-protected boot store and a credential-protected
     * user store runs after the first unlock, and only while it has not run before. It needs the credential store,
     * which is unreadable before unlock, so it is never attempted while the phone is locked.
     */
    fun shouldSplit(unlocked: Boolean, splitDone: Boolean): Boolean = unlocked && !splitDone
}

/**
 * Where the app's settings live. Two SharedPreferences files (finding F-1 of the APK audit):
 *
 * - [DEVICE_NAME] in **device-protected** storage holds only the keys the boot/alarm path reads before the first
 *   unlock ([PrefsClassifier.isBootKey]): prayer settings, alarm configs, reminder-independent alarm state, and the
 *   location used to compute prayer times. Device-protected storage is encrypted with the device key, so prayer
 *   alarms can be restored right after a restart, before the first unlock.
 * - [USER_NAME] in **credential-protected** storage holds everything else — favorites, reading positions, deed
 *   history, the Khums financial draft, tasbih and make-up counters, theme and reminder times. It only decrypts
 *   after the first unlock, so this file is never opened before then.
 *
 * Until 1.8.4 the whole file lived in device-protected storage. On the first unlocked run of this version the file
 * is split once: user content is moved to the credential store and removed from the device store. A user coming
 * straight from pre-1.8.2 (whose file never left credential storage) is split the same way. The one-time split is
 * marked by [KEY_SPLIT_DONE] in the device store.
 */
object PrefsStorage {
    /** Device-protected (direct-boot) store; keeps the original file name so 1.8.2–1.8.4 data is found in place. */
    const val DEVICE_NAME = "daily_deeds_prefs"

    /** Credential-protected store for ordinary user content; readable only after the first unlock. */
    const val USER_NAME = "daily_deeds_user_prefs"

    /** Back-compat alias for the device store name. */
    const val NAME = DEVICE_NAME

    /** Set in the device store once the one-time split into two stores has run. */
    const val KEY_SPLIT_DONE = "pref_store_split_done"

    private val lock = Any()
    @Volatile private var splitDone = false

    /**
     * The device-protected store. Always safe to open, before or after the first unlock. When the phone is unlocked
     * it also performs the one-time split (which needs the credential store, hence only while unlocked).
     */
    fun openDevice(context: Context): SharedPreferences {
        val app = context.applicationContext ?: context
        val deviceStore = app.createDeviceProtectedStorageContext().getSharedPreferences(DEVICE_NAME, Context.MODE_PRIVATE)
        if (DirectBoot.shouldSplit(UserManagerCompat.isUserUnlocked(app), splitDone)) {
            synchronized(lock) {
                if (DirectBoot.shouldSplit(UserManagerCompat.isUserUnlocked(app), splitDone)) {
                    migrateSplit(app, deviceStore)
                    splitDone = true
                }
            }
        }
        return deviceStore
    }

    /**
     * The credential-protected store. MUST only be opened after the first unlock (callers read user content, which
     * never happens on the locked boot path). Opening it first ensures the split has run, so the user keys are there.
     */
    fun openUser(context: Context): SharedPreferences {
        val app = context.applicationContext ?: context
        openDevice(app)
        return app.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Moves user content out of the single settings file into the credential-protected store, leaving only the
     * boot keys in the device store. Runs once, after the first unlock. Handles both the 1.8.2–1.8.4 layout (the
     * full file already in the device store) and a direct upgrade from pre-1.8.2 (the full file still in the
     * credential store under the same name).
     */
    private fun migrateSplit(app: Context, deviceStore: SharedPreferences) {
        if (deviceStore.getBoolean(KEY_SPLIT_DONE, false)) {
            return
        }
        val userStore = app.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)

        // The whole pre-split copy lives in exactly one place:
        //  - 1.8.2–1.8.4: the device store (the 1.8.2 upgrade moved the file there with moveSharedPreferencesFrom,
        //    which deletes the credential-context source), so the device store is authoritative once it has data.
        //  - pre-1.8.2: the file never moved and is still the credential-context file of the same name.
        // Reading from only one source means a key can never be resurrected from a stale leftover file.
        val deviceEntries = deviceStore.all.toMutableMap().apply { remove(KEY_SPLIT_DONE) }
        val legacyCredentialFile = File(app.dataDir, "shared_prefs/$DEVICE_NAME.xml")
        val legacyPresent = legacyCredentialFile.exists()
        val source: Map<String, Any?> = when {
            deviceEntries.isNotEmpty() -> deviceEntries                                   // 1.8.2–1.8.4
            legacyPresent -> app.getSharedPreferences(DEVICE_NAME, Context.MODE_PRIVATE).all // pre-1.8.2
            else -> emptyMap()                                                            // fresh install
        }

        val userEdit = userStore.edit()
        val deviceEdit = deviceStore.edit()
        for ((key, value) in source) {
            if (key == KEY_SPLIT_DONE) continue
            if (PrefsClassifier.isBootKey(key)) {
                put(deviceEdit, key, value) // keep it (or, from pre-1.8.2, bring it) in the device store
            } else {
                put(userEdit, key, value)   // move user content to the credential store
                deviceEdit.remove(key)      // and out of the device store
            }
        }
        deviceEdit.putBoolean(KEY_SPLIT_DONE, true)
        // One-time migration: commit so the user content is durably on disk before it is removed from the device
        // store and before the legacy file is deleted; a process kill mid-split must not lose data.
        userEdit.commit()
        deviceEdit.commit()

        // In the pre-1.8.2 case the credential file still holds the whole old copy (its keys were just copied into
        // the two stores); remove it. A harmless no-op otherwise.
        if (legacyPresent) {
            app.getSharedPreferences(DEVICE_NAME, Context.MODE_PRIVATE).edit().clear().commit()
            app.deleteSharedPreferences(DEVICE_NAME)
        }
    }

    private fun put(editor: SharedPreferences.Editor, key: String, value: Any?) {
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is String -> editor.putString(key, value)
            is Set<*> -> {
                @Suppress("UNCHECKED_CAST")
                editor.putStringSet(key, value as Set<String>)
            }
        }
    }

    /** Lets an instrumented test run the one-time split again in the same process. */
    @VisibleForTesting
    internal fun resetSplitCheckForTest() {
        splitDone = false
    }

    fun isUnlocked(context: Context): Boolean = UserManagerCompat.isUserUnlocked(context)

    /** Whether the settings can be read now (always once unlocked; before unlock only if the device copy exists). */
    fun isReadable(context: Context): Boolean {
        val app = context.applicationContext ?: context
        return DirectBoot.canReadSettings(isUnlocked(app), deviceFile(app).exists())
    }

    fun bootWork(context: Context): DirectBoot.BootWork {
        val app = context.applicationContext ?: context
        return DirectBoot.bootWork(isUnlocked(app), deviceFile(app).exists())
    }

    private fun deviceFile(context: Context) =
        File(context.createDeviceProtectedStorageContext().dataDir, "shared_prefs/$DEVICE_NAME.xml")
}
