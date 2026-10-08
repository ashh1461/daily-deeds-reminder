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
import com.dailydeeds.reminder.adhan.VoiceIds
import com.dailydeeds.reminder.adhan.VoiceStore
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.receiver.AdhanActionReceiver
import com.dailydeeds.reminder.util.Prayer

/**
 * Plays the adhan as a foreground media service so Android keeps the process alive for the whole call.
 * The audio uses the alarm stream; the notification offers Stop and Snooze. The service stops itself when
 * the audio ends, fails, or the configured maximum duration is reached.
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

    private fun startPlayback(voiceId: String, test: Boolean) {
        val settings = PreferencesManager(this).getAdhanGlobal()
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (!test && settings.followRinger && audio.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            // The phone is on silent or vibrate: respect it, only vibrate if that is what the user chose.
            if (settings.vibrate && audio.ringerMode == AudioManager.RINGER_MODE_VIBRATE) vibrate()
            handler.postDelayed(timeout, 3000)
            return
        }

        val mp = MediaPlayer()
        player = mp
        try {
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            val file = VoiceStore(this).fileFor(voiceId)
            if (file != null) {
                mp.setDataSource(file.absolutePath)
            } else {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ?: Settings.System.DEFAULT_NOTIFICATION_URI
                mp.setDataSource(this, uri)
            }
            requestFocus(audio)
            val lock = (getSystemService(Context.POWER_SERVICE) as PowerManager)
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "dailydeeds:adhan")
            lock.acquire((settings.maxSeconds + 15) * 1000L)
            wakeLock = lock
            mp.setOnCompletionListener { shutdown() }
            mp.setOnErrorListener { _, what, extra ->
                Log.w(TAG, "MediaPlayer error $what/$extra")
                shutdown()
                true
            }
            mp.setOnPreparedListener { it.start() }
            mp.prepareAsync()
            handler.postDelayed(timeout, settings.maxSeconds * 1000L)
            if (settings.vibrate) vibrate()
        } catch (e: Exception) {
            Log.w(TAG, "Could not start adhan playback", e)
            shutdown()
        }
    }

    private fun requestFocus(audio: AudioManager) {
        val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE)
            .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
            .setOnAudioFocusChangeListener { }
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
