package com.dailydeeds.reminder.service

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.VolumeProvider
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.dailydeeds.reminder.MainActivity
import com.dailydeeds.reminder.adhan.BundledVoices
import com.dailydeeds.reminder.adhan.PrayerAlarmText
import com.dailydeeds.reminder.adhan.VoiceIds
import com.dailydeeds.reminder.adhan.VoiceStore
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.receiver.AdhanActionReceiver
import com.dailydeeds.reminder.util.Prayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Plays the adhan as a foreground media service so Android keeps the process alive for the whole call.
 * The audio uses the alarm stream. It stops when the audio ends, fails, another app takes audio focus, or the
 * time limit is reached (never shorter than a bundled adhan; a test plays only the first seconds), and whenever
 * the user asks: the Stop button, swiping the notification away, tapping it (which also opens the app), the
 * banner inside the app, and, unless switched off, the power button or either volume key.
 */
class AdhanService : Service() {

    private var player: MediaPlayer? = null
    private var focusRequest: AudioFocusRequest? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var mediaSession: MediaSession? = null
    private var screenOffReceiver: BroadcastReceiver? = null
    private val handler = Handler(Looper.getMainLooper())
    private val timeout = Runnable { shutdown() }
    private var prayerForSnooze: Prayer? = null

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
            this, NotificationHelper.CHANNEL_PRAYER_ADHAN, NOTIFICATION_ID, title, "يُشغَّل الأذان الآن. المس الإشعار لإيقافه."
        )
            .setOngoing(true)
            .setAutoCancel(false)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            // Android 14 lets the user swipe a foreground notification away: that must silence the adhan too.
            .setDeleteIntent(stop)
            // A tap opens the app with a stop request (an activity intent: Android 12+ blocks notification trampolines).
            .setContentIntent(openAndStopIntent())
            .addAction(0, "إيقاف", stop)
        if (!test && prayer != null) builder.addAction(0, "تأجيل", snooze)
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK else 0
        ServiceCompat.startForeground(this, NOTIFICATION_ID, builder.build(), type)
    }

    private fun actionIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(this, AdhanActionReceiver::class.java).setAction(action)
            .putExtra(EXTRA_PRAYER_ID, prayerForSnooze?.id ?: -1)
        return PendingIntent.getBroadcast(
            this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun openAndStopIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .putExtra(EXTRA_STOP_ADHAN, true)
        return PendingIntent.getActivity(
            this, OPEN_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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
            // Set before the asset is opened, so a bundled file that cannot be opened still falls back to the tone.
            bundledAttempt = voice != null
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
            // needs a few seconds.
            val fullSeconds = if (voice != null) maxOf(settings.maxSeconds, voice.seconds + 10) else settings.maxSeconds
            val timeoutSeconds = if (test) minOf(fullSeconds, TEST_SECONDS) else fullSeconds
            if (!requestFocus(audio, test)) {
                // Refused focus means a phone call (or another alarm) owns the audio: never play over it.
                Log.i(TAG, "Audio focus refused; showing the prayer notification instead")
                if (!test) postReminder()
                shutdown()
                return
            }
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
            if (settings.hardwareStop) armHardwareStop()
            playingState.value = true
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

    /** Returns false when the system refuses focus (a call in progress): the caller must not play. */
    private fun requestFocus(audio: AudioManager, test: Boolean): Boolean {
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
        return audio.requestAudioFocus(request) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    /**
     * The power button and the volume keys stop the adhan, as in most adhan apps.
     *
     * Power: the screen turning off is the only signal Android gives a service, and a screen timeout looks the same.
     * So screen-off stops the adhan only once the user has shown they are at the phone: the screen was already on
     * when the adhan began, they unlocked it, or they turned it on themselves (later than [USER_WAKE_AFTER_MS], so a
     * phone that lights the screen for the notification does not count). A screen woken only by the notification
     * then timing out leaves the adhan playing.
     *
     * Volume: a media session with its own volume handler receives the keys while it is the active playback. Some
     * phones (Pixels since Android 12) do not give volume keys to such a session; there the keys change a stream's
     * volume, and the volume-changed broadcast stops the adhan instead.
     */
    private fun armHardwareStop() {
        if (screenOffReceiver == null) {
            val armedAt = SystemClock.elapsedRealtime()
            var screenOffStops = (getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val sinceStart = SystemClock.elapsedRealtime() - armedAt
                    when (intent.action) {
                        Intent.ACTION_USER_PRESENT -> screenOffStops = true
                        Intent.ACTION_SCREEN_ON -> if (sinceStart > USER_WAKE_AFTER_MS) screenOffStops = true
                        Intent.ACTION_SCREEN_OFF -> if (screenOffStops) shutdown()
                        VOLUME_CHANGED_ACTION -> if (sinceStart > VOLUME_GRACE_MS) shutdown()
                    }
                }
            }
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_USER_PRESENT)
                addAction(VOLUME_CHANGED_ACTION)
            }
            // All four are protected broadcasts that only the system can send.
            ContextCompat.registerReceiver(this, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
            screenOffReceiver = receiver
        }
        if (mediaSession == null) {
            val session = MediaSession(this, "WirdAdhan")
            session.setPlaybackToRemote(object : VolumeProvider(VOLUME_CONTROL_RELATIVE, VOLUME_STEPS, VOLUME_STEPS / 2) {
                // Called on the main thread: stop at once, so a stale stop cannot hit an adhan started right after.
                override fun onAdjustVolume(direction: Int) {
                    if (direction != 0) shutdown()
                }
            })
            session.setCallback(object : MediaSession.Callback() {
                override fun onStop() = shutdown()
                override fun onPause() = shutdown()
            })
            session.setPlaybackState(
                PlaybackState.Builder()
                    .setActions(PlaybackState.ACTION_STOP or PlaybackState.ACTION_PAUSE or PlaybackState.ACTION_PLAY_PAUSE)
                    .setState(PlaybackState.STATE_PLAYING, 0L, 1f)
                    .build()
            )
            session.isActive = true
            mediaSession = session
        }
    }

    private fun disarmHardwareStop() {
        screenOffReceiver?.let { runCatching { unregisterReceiver(it) } }
        screenOffReceiver = null
        mediaSession?.let {
            it.isActive = false
            it.release()
        }
        mediaSession = null
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
        disarmHardwareStop()
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
        playingState.value = false
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        releasePlayback()
        playingState.value = false
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AdhanService"
        /** How long the «تجربة» button plays: enough to hear the voice. */
        private const val TEST_SECONDS = 20
        private const val OPEN_REQUEST_CODE = 3
        private const val VOLUME_STEPS = 100
        /** A screen turned on later than this after the adhan began was turned on by the user, not the notification. */
        private const val USER_WAKE_AFTER_MS = 3_000L
        /** Volume changes right at the start (some phones adjust streams as playback begins) are ignored. */
        private const val VOLUME_GRACE_MS = 1_500L
        /** System broadcast sent when a stream's volume changes (not in the public SDK constants, stable since Android 4). */
        private const val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
        const val NOTIFICATION_ID = 7001
        const val EXTRA_PRAYER_ID = "extra_prayer_id"
        const val EXTRA_VOICE_ID = "extra_voice_id"
        const val EXTRA_TEST = "extra_test"
        /** Set on the activity intent of the adhan notification: opening the app from it stops the adhan. */
        const val EXTRA_STOP_ADHAN = "extra_stop_adhan"

        private val playingState = MutableStateFlow(false)

        /** True while an adhan (or a test) is playing; the app shows a stop banner meanwhile. */
        val playing: StateFlow<Boolean> = playingState.asStateFlow()

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
