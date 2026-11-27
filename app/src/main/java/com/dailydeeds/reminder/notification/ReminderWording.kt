package com.dailydeeds.reminder.notification

import android.content.Context
import androidx.annotation.StringRes
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.model.ReminderType

/**
 * Title and text of the deed reminders in the app language (v1.9.0). The dua and the ayah they quote are Arabic
 * content in every language (non-translatable strings).
 */
object ReminderWording {
    @StringRes
    fun titleRes(type: ReminderType): Int = when (type) {
        ReminderType.MORNING -> R.string.reminder_morning_title
        ReminderType.EVENING -> R.string.reminder_evening_title
        ReminderType.NIGHT -> R.string.reminder_night_title
        ReminderType.BEDTIME -> R.string.reminder_bedtime_title
        ReminderType.THURSDAY -> R.string.reminder_thursday_title
    }

    /** [context] must carry the app language ([com.dailydeeds.reminder.util.LocaleSupport.localized]). */
    fun message(context: Context, type: ReminderType): String = when (type) {
        ReminderType.MORNING -> context.getString(R.string.reminder_morning_message)
        ReminderType.EVENING -> context.getString(R.string.reminder_evening_message)
        ReminderType.NIGHT -> context.getString(R.string.reminder_night_message, context.getString(R.string.dhikr_night))
        ReminderType.BEDTIME -> context.getString(R.string.reminder_bedtime_message, context.getString(R.string.dhikr_bedtime))
        ReminderType.THURSDAY -> context.getString(R.string.reminder_thursday_message)
    }
}
