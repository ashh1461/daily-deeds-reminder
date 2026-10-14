package com.dailydeeds.reminder.adhan

/**
 * An adhan recording shipped in the APK under `assets/`. [seconds] is rounded up so the playback time limit
 * never cuts the last phrase; [bytes] and [sha256] are pinned by `BundledVoicesTest`.
 */
data class BundledVoice(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val assetPath: String,
    val seconds: Int,
    val bytes: Long,
    val sha256: String
)

/** The built-in adhan voices. Where the audio comes from and how it was processed: docs/licenses/ADHAN_AUDIO.md. */
object BundledVoices {
    const val DEFAULT_ID = "karbala_imam_hussain"

    val all: List<BundledVoice> = listOf(
        BundledVoice(
            id = DEFAULT_ID,
            nameAr = "أذان الحرم الحسيني – كربلاء المقدسة",
            nameEn = "Adhan, Shrine of Imam Hussain (a.s.), Karbala",
            assetPath = "adhan/adhan_karbala_imam_hussain.ogg",
            seconds = 300,
            bytes = 2_857_049L,
            sha256 = "3689a380cb2880806b58ba251d544ae916a874233000a95de5298c2597d7245b"
        ),
        BundledVoice(
            id = "mashhad_imam_reza",
            nameAr = "أذان الحرم الرضوي – مشهد المقدسة",
            nameEn = "Adhan, Shrine of Imam al-Ridha (a.s.), Mashhad",
            assetPath = "adhan/adhan_mashhad_imam_reza.ogg",
            seconds = 311,
            bytes = 2_802_699L,
            sha256 = "e59472508dc5d08e2e97982b83ace228f7ff5db3c5a6cebce78a5a77224da843"
        )
    )

    fun byId(id: String): BundledVoice? = all.firstOrNull { it.id == id }

    /**
     * The bundled recording that [voiceId] plays: [VoiceIds.DEFAULT] is the app's default adhan. Null for the
     * phone's tone and for downloaded, imported or unknown ids; the player decides what those play.
     */
    fun resolve(voiceId: String): BundledVoice? =
        if (voiceId == VoiceIds.DEFAULT) byId(DEFAULT_ID) else byId(voiceId)
}
