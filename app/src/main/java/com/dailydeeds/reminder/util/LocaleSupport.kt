package com.dailydeeds.reminder.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.data.PrefsStorage
import java.util.Locale

/**
 * Applies the language chosen in Settings ([AppLanguage]) to a [Context], so `getString` and Compose
 * `stringResource` read `values`, `values-en` or `values-fa`, and the layout direction follows the language.
 *
 * The app manages its language itself rather than through the Android 13 per-app language API, so it works the
 * same from API 26 and inside the direct-boot alarm path: the choice is a boot key ([PrefsStorage.openDevice]),
 * readable before the first unlock, and the adhan notification is written in it.
 *
 * Wrap points: [com.dailydeeds.reminder.MainActivity.attachBaseContext] and the service; receivers, notification
 * and widget code call [localized] on the context they were given before reading any string.
 */
object LocaleSupport {

    /** The language of the device itself, ignoring the app's choice. */
    fun deviceLanguage(): String = Resources.getSystem().configuration.locales[0]?.language.orEmpty()

    fun current(context: Context): AppLanguage {
        val pref = PrefsStorage.openDevice(context).getString(PreferencesManager.KEY_APP_LANGUAGE, AppLanguage.DEFAULT_PREF)
        return AppLanguage.resolve(pref, deviceLanguage())
    }

    /** [base] with its locale and layout direction set to the app language. */
    fun localized(base: Context): Context {
        val locale = Locale.forLanguageTag(current(base).tag)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return base.createConfigurationContext(config)
    }
}
