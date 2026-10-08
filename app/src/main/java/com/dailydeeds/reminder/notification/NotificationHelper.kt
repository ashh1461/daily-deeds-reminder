package com.dailydeeds.reminder.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dailydeeds.reminder.MainActivity
import com.dailydeeds.reminder.model.DeedCategory

object NotificationHelper {
    private const val TAG = "NotificationHelper"
    const val EXTRA_CATEGORY = "reminder_category"

    /** Deed reminders and occasion alerts. */
    const val CHANNEL_ID = "daily_deeds_channel"
    const val CHANNEL_NAME = "تنبيهات الأعمال اليومية"
    const val CHANNEL_DESC = "تذكير بالأعمال والأوراد اليومية المباركة"

    /** Prayer-time and pre-adhan reminders that use the phone's notification sound. */
    const val CHANNEL_PRAYER_REMINDER = "prayer_reminder"

    /** Foreground notification of a playing adhan; the audio itself is played by AdhanService. */
    const val CHANNEL_PRAYER_ADHAN = "prayer_adhan"

    /** Prayer-time notification without sound. */
    const val CHANNEL_PRAYER_SILENT = "prayer_silent"

    /** Creates every channel once; safe to call repeatedly (Android ignores existing channels). */
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                description = CHANNEL_DESC
                enableVibration(true)
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_PRAYER_REMINDER, "تذكير أوقات الصلاة", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "إشعار عند دخول وقت الصلاة وقبله بدقائق"
                enableVibration(true)
                setSound(
                    android.provider.Settings.System.DEFAULT_NOTIFICATION_URI,
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build()
                )
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_PRAYER_ADHAN, "الأذان", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "إشعار أثناء تشغيل الأذان مع زر الإيقاف"
                setSound(null, null)
                enableVibration(false)
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_PRAYER_SILENT, "أوقات الصلاة (صامت)", NotificationManager.IMPORTANCE_LOW).apply {
                description = "إشعار بدخول الوقت دون صوت"
                setSound(null, null)
            }
        )
    }

    /** Kept for existing callers. */
    fun createNotificationChannel(context: Context) = createChannels(context)

    fun contentIntent(context: Context, notificationId: Int, category: DeedCategory? = null): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            category?.let { putExtra(EXTRA_CATEGORY, it.name) }
        }
        return PendingIntent.getActivity(
            context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun baseBuilder(
        context: Context,
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        category: DeedCategory? = null
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.dailydeeds.reminder.R.drawable.ic_stat_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent(context, notificationId, category))
            .setAutoCancel(true)

    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        category: DeedCategory? = null,
        channelId: String = CHANNEL_ID
    ) {
        createChannels(context)
        val builder = baseBuilder(context, channelId, notificationId, title, message, category).apply {
            if (channelId == CHANNEL_PRAYER_SILENT) setPriority(NotificationCompat.PRIORITY_LOW)
            if (channelId != CHANNEL_ID) setCategory(NotificationCompat.CATEGORY_REMINDER)
        }
        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            Log.w(TAG, "Notification permission revoked; skipping notification", e)
        }
    }
}
