package com.dailydeeds.reminder.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.dailydeeds.reminder.adhan.BundledVoices
import com.dailydeeds.reminder.adhan.PrayerAlarmText
import com.dailydeeds.reminder.adhan.VoiceIds
import com.dailydeeds.reminder.adhan.VoiceStore
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.receiver.AdhanActionReceiver
import com.dailydeeds.reminder.util.Prayer

/**
 * Plays the adhan as a foreground media service so Android keeps the process alive for the whole call.
 * The audio uses the alarm stream; the notification offers Stop and Snooze. The service stops itself when
 * the audio ends, fails, another app takes audio focus, or the time limit is reached (never shorter than a
 * bundled adhan; a test plays only the first seconds).
 */
class AdhanService : Service() {

    private var player: MediaPlayer? = null
    private var focusRequest: AudioFocusRequest? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private val handler = Handler(Looper.getMainLooper())
    private val timeout = Runnable { shutdown() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prayer = Prayer.fromId(intent?.getIntExtra(EXTRA_PRAYER_ID, -1) ?: -1)
        val voiceId = intent?.getStringExtra(EXTRA_VOICE_ID) ?: VoiceIds.DEFAULT
        val test = intent?.getBooleanExtra(EXTRA_TEST, false) ?: false

        goForeground(prayer, test)
        if (prayer == null) {
            shutdown()
            return START_NOT_STICKY
        }
        releasePlayback()
        startPlayback(voiceId, test)
        return START_NOT_STICKY
    }

    private fun goForeground(prayer: Prayer?, test: Boolean) {
        prayerForSnooze = prayer
        val title = if (test) "تجربة الأذان" else "أذان ${prayer?.nameArabic ?: ""}".trim()
        val stop = actionIntent(AdhanActionReceiver.ACTION_STOP, 1)
        val snooze = actionIntent(AdhanActionReceiver.ACTION_SNOOZE, 2)
        val builder = NotificationHelper.baseBuilder(
            this, NotificationHelper.CHANNEL_PRAYER_ADHAN, NOTIFICATION_ID, title, "يُشغَّل الأذان الآن"
        )
            .setOngoing(true)
            .setAutoCancel(false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(0, "إيقاف", stop)
        if (!test && prayer != null) builder.addAction(0, "تأجيل", snooze)
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
        ServiceCompat.startForeground(this, NOTIFICATION_ID, builder.build(), type)
    }

    private var prayerForSnooze: Prayer? = null

    private fun actionIntent(action: String, requestCode: Int): android.app.PendingIntent {
        val intent = Intent(this, AdhanActionReceiver::class.java).setAction(action)
            .putExtra(EXTRA_PRAYER_ID, prayerForSnooze?.id ?: -1)
        return android.app.PendingIntent.getBroadcast(
            this, requestCode, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun startPlayback(voiceId: String, test: Boolean, toneRetryAllowed: Boolean = true) {
        val settings = PreferencesManager(this).getAdhanGlobal()
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!test && settings.followRinger && audio.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            // The phone is on silent or vibrate: respect it, only vibrate if that is what the user chose.
            if (settings.vibrate && audio.ringerMode == AudioManager.RINGER_MODE_VIBRATE) vibrate()
            postReminder()
            handler.postDelayed(timeout, 3000)
            return
        }

        val mp = MediaPlayer()
        player = mp
        var bundledAttempt = false   // the source is a bundled adhan (also when it replaces an unknown voice)
        var started = false          // the player got as far as playing
        try {
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            val bundled = BundledVoices.resolve(voiceId)
            val file = if (bundled == null) VoiceStore(this).fileFor(voiceId) else null
            // A removed or unknown voice plays the bundled default adhan: a real adhan beats a tone.
            val voice = bundled
                ?: if (file == null && voiceId != VoiceIds.SYSTEM_TONE) BundledVoices.resolve(VoiceIds.DEFAULT) else null
            when {
                // Assets are stored uncompressed (build.gradle.kts), so they can be played straight from the APK.
                voice != null -> assets.openFd(voice.assetPath).use { afd ->
                    mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
                file != null -> mp.setDataSource(file.absolutePath)
                else -> {
                    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ?: Settings.System.DEFAULT_NOTIFICATION_URI
                    mp.setDataSource(this, uri)
                }
            }
            // The time limit (300 s by default) must not cut a bundled adhan before its last phrase; a test only
            // needs a few seconds, and has no other way to be stopped from inside the app.
            val fullSeconds = if (voice != null) maxOf(settings.maxSeconds, voice.seconds + 10) else settings.maxSeconds
            val timeoutSeconds = if (test) minOf(fullSeconds, TEST_SECONDS) else fullSeconds
            bundledAttempt = voice != null
            requestFocus(audio, test)
            val lock = (getSystemService(Context.POWER_SERVICE) as PowerManager)
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "dailydeeds:adhan")
            lock.acquire((timeoutSeconds + 15) * 1000L)
            wakeLock = lock
            mp.setOnCompletionListener { shutdown() }
            mp.setOnErrorListener { _, what, extra ->
                Log.w(TAG, "MediaPlayer error $what/$extra")
                failed(bundledAttempt && !started, test, toneRetryAllowed)
                true
            }
            mp.setOnPreparedListener {
                started = true
                it.start()
            }
            mp.prepareAsync()
            handler.postDelayed(timeout, timeoutSeconds * 1000L)
            if (settings.vibrate) vibrate()
        } catch (e: Exception) {
            Log.w(TAG, "Could not start adhan playback", e)
            failed(bundledAttempt && !started, test, toneRetryAllowed)
        }
    }

    /**
     * Playback failed. A bundled adhan that never started (a codec problem on this device) falls back once to the
     * phone's sound, so a prayer time is never silent; anything else leaves the usual notification behind and stops.
     */
    private fun failed(bundledNeverStarted: Boolean, test: Boolean, toneRetryAllowed: Boolean) {
        if (bundledNeverStarted && toneRetryAllowed) {
            releasePlayback()
            startPlayback(VoiceIds.SYSTEM_TONE, test, toneRetryAllowed = false)
            return
        }
        if (!test) postReminder()
        shutdown()
    }

    /** Shutdown removes the foreground notification, so leave the usual prayer-time notification behind. */
    private fun postReminder() {
        val prayer = prayerForSnooze ?: return
        val text = PrayerAlarmText.reminder(prayer)
        NotificationHelper.showNotification(
            this, PrayerAlarmText.notificationId(prayer), text.title, text.text,
            channelId = NotificationHelper.CHANNEL_PRAYER_REMINDER
        )
    }

    private fun requestFocus(audio: AudioManager, test: Boolean) {
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
            .setOnAudioFocusChangeListener { change ->
                // A call or another app took over: stop rather than play on top of it (the adhan runs about 5 minutes).
                if (change == AudioManager.AUDIOFOCUS_LOSS || change == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    if (!test) postReminder()
                    shutdown()
                }
            }
            .build()
        focusRequest = request
        audio.requestAudioFocus(request)
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 400, 200, 400), -1))
    }

    private fun releasePlayback() {
        handler.removeCallbacks(timeout)
        player?.let {
            runCatching { if (it.isPlaying) it.stop() }
            it.release()
        }
        player = null
        focusRequest?.let { (getSystemService(Context.AUDIO_SERVICE) as AudioManager).abandonAudioFocusRequest(it) }
        focusRequest = null
        wakeLock?.let { if (it.isHeld) it.release() }
        wakeLock = null
    }

    private fun shutdown() {
        releasePlayback()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        releasePlayback()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AdhanService"
        /** How long the «تجربة» button plays: enough to hear the voice, short enough not to need a Stop. */
        private const val TEST_SECONDS = 20
        const val NOTIFICATION_ID = 7001
        const val EXTRA_PRAYER_ID = "extra_prayer_id"
        const val EXTRA_VOICE_ID = "extra_voice_id"
        const val EXTRA_TEST = "extra_test"

        fun play(context: Context, prayer: Prayer, voiceId: String, test: Boolean = false) {
            val intent = Intent(context, AdhanService::class.java)
                .putExtra(EXTRA_PRAYER_ID, prayer.id)
                .putExtra(EXTRA_VOICE_ID, voiceId)
                .putExtra(EXTRA_TEST, test)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AdhanService::class.java))
        }
    }
}
