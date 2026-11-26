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

    /** Settings are readable once the user has unlocked, or before that if they were already moved to device storage. */
    fun canReadSettings(unlocked: Boolean, deviceCopyExists: Boolean): Boolean = unlocked || deviceCopyExists

    /** Move the settings to device storage once: after unlock, when they still live only in credential storage. */
    fun shouldMove(unlocked: Boolean, deviceCopyExists: Boolean, credentialCopyExists: Boolean): Boolean =
        unlocked && !deviceCopyExists && credentialCopyExists

    fun bootWork(unlocked: Boolean, deviceCopyExists: Boolean): BootWork = when {
        !canReadSettings(unlocked, deviceCopyExists) -> BootWork.NONE
        !unlocked -> BootWork.PRAYER_ONLY
        else -> BootWork.ALL
    }
}

/**
 * Where the app's settings live. Since 1.8.2 they are in device-protected storage, so prayer alarms can be restored
 * right after a restart, before the first unlock (they are still encrypted, with the device key). The file is
 * moved there once, the first time the app runs unlocked after the update.
 *
 * Before unlock, callers must check [isReadable] first: opening the device copy before the move would cache an
 * empty file in this process.
 */
object PrefsStorage {
    const val NAME = "daily_deeds_prefs"
    private val lock = Any()
    @Volatile private var moved = false

    fun open(context: Context): SharedPreferences {
        val app = context.applicationContext ?: context
        val device = app.createDeviceProtectedStorageContext()
        if (!moved) {
            synchronized(lock) {
                if (!moved && UserManagerCompat.isUserUnlocked(app)) {
                    if (DirectBoot.shouldMove(true, prefsFile(device).exists(), prefsFile(app).exists())) {
                        device.moveSharedPreferencesFrom(app, NAME)
                    }
                    moved = true
                }
            }
        }
        return device.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    /** Lets an instrumented test run the one-time move again in the same process. */
    @VisibleForTesting
    internal fun resetMoveCheckForTest() {
        moved = false
    }

    fun isUnlocked(context: Context): Boolean = UserManagerCompat.isUserUnlocked(context)

    /** Whether the settings can be read now (always once unlocked; before unlock only if already moved). */
    fun isReadable(context: Context): Boolean {
        val app = context.applicationContext ?: context
        return DirectBoot.canReadSettings(isUnlocked(app), prefsFile(app.createDeviceProtectedStorageContext()).exists())
    }

    fun bootWork(context: Context): DirectBoot.BootWork {
        val app = context.applicationContext ?: context
        return DirectBoot.bootWork(isUnlocked(app), prefsFile(app.createDeviceProtectedStorageContext()).exists())
    }

    private fun prefsFile(context: Context) = File(context.dataDir, "shared_prefs/$NAME.xml")
}
