package com.dailydeeds.reminder.adhan

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.IOException

/** Adhan audio kept in the app's private storage: downloaded packs and files the user picked. */
class VoiceStore(private val context: Context) {

    data class Installed(val id: String, val nameAr: String, val own: Boolean, val file: File)

    private val dir: File get() = File(context.filesDir, "voices").also { it.mkdirs() }
    private val index = context.getSharedPreferences("voice_store", Context.MODE_PRIVATE)

    private fun audioFile(id: String) = File(dir, "$id.audio")

    fun partFile(id: String) = File(dir, "$id.part")

    fun installed(): List<Installed> =
        index.all.mapNotNull { (id, value) ->
            val parts = (value as? String)?.split('|', limit = 2) ?: return@mapNotNull null
            val file = audioFile(id)
            if (!file.isFile || parts.size != 2) null else Installed(id, parts[1], parts[0] == "own", file)
        }.sortedBy { it.nameAr }

    /** The audio file for [voiceId], or null for the system default or a missing file. */
    fun fileFor(voiceId: String): File? =
        if (voiceId == VoiceIds.DEFAULT) null else audioFile(voiceId).takeIf { it.isFile }

    fun isInstalled(voiceId: String) = fileFor(voiceId) != null

    /** Moves a fully downloaded and verified [partFile] into place. */
    fun commit(id: String, nameAr: String, own: Boolean) {
        val target = audioFile(id)
        if (target.exists()) target.delete()
        if (!partFile(id).renameTo(target)) throw IOException("could not move voice into place")
        index.edit().putString(id, (if (own) "own" else "pack") + "|" + nameAr.replace('|', ' ')).apply()
    }

    fun remove(id: String) {
        audioFile(id).delete()
        partFile(id).delete()
        index.edit().remove(id).apply()
    }

    /** Copies a user-chosen audio file into private storage after checking size, type and that it decodes. */
    fun importOwn(uri: Uri): Installed {
        val resolver = context.contentResolver
        val type = resolver.getType(uri).orEmpty()
        if (!type.startsWith("audio/") && type != "application/ogg") throw IOException("not an audio file")
        val name = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use {
            if (it.moveToFirst()) it.getString(0) else null
        }?.substringBeforeLast('.')?.take(60)?.ifBlank { null } ?: "ملف صوتي"
        val id = "own_" + java.lang.Long.toString(System.currentTimeMillis() / 1000, 36)
        val part = partFile(id)
        try {
            resolver.openInputStream(uri)?.use { input ->
                part.outputStream().use { output ->
                    val buffer = ByteArray(16 * 1024)
                    var total = 0L
                    while (true) {
                        val n = input.read(buffer)
                        if (n < 0) break
                        total += n
                        if (total > VoiceManifest.MAX_VOICE_BYTES) throw IOException("file is larger than 25 MB")
                        output.write(buffer, 0, n)
                    }
                }
            } ?: throw IOException("cannot open file")
            val durationMs = MediaMetadataRetriever().run {
                try {
                    setDataSource(part.absolutePath)
                    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                } finally {
                    release()
                }
            }
            if (durationMs < 3000) throw IOException("audio is too short or cannot be decoded")
            commit(id, name, own = true)
        } catch (e: Exception) {
            part.delete()
            throw if (e is IOException) e else IOException("could not import audio", e)
        }
        return Installed(id, name, true, audioFile(id))
    }
}
