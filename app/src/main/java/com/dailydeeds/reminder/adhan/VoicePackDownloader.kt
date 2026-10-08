package com.dailydeeds.reminder.adhan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

/**
 * The only place in the app that talks to the network. Strict policy: HTTPS only, allow-listed hosts
 * (re-checked after every redirect), at most 3 redirects, hard size caps, and the SHA-256 listed in the
 * manifest must match before a voice is installed. Nothing is sent to the server except a plain GET.
 */
class VoicePackDownloader(private val store: VoiceStore) {

    suspend fun fetchManifest(): Result<VoiceManifest.ParseResult> = withContext(Dispatchers.IO) {
        runCatching {
            val text = open(VoiceUrlPolicy.MANIFEST_URL).use { conn ->
                conn.inputStream.use { it.readLimited(MAX_MANIFEST_BYTES) }
            }.toString(Charsets.UTF_8)
            VoiceManifest.parse(text)
        }
    }

    suspend fun download(voice: RemoteVoice, onProgress: (Float) -> Unit): Result<Unit> = withContext(Dispatchers.IO) {
        val part = store.partFile(voice.id)
        runCatching {
            val digest = MessageDigest.getInstance("SHA-256")
            open(voice.url).use { conn ->
                val declared = conn.contentLengthLong
                if (declared > VoiceManifest.MAX_VOICE_BYTES) throw IOException("server reports a file that is too large")
                conn.inputStream.use { input ->
                    part.outputStream().use { output ->
                        val buffer = ByteArray(16 * 1024)
                        var total = 0L
                        while (true) {
                            val n = input.read(buffer)
                            if (n < 0) break
                            total += n
                            if (total > VoiceManifest.MAX_VOICE_BYTES) throw IOException("download exceeded the size limit")
                            digest.update(buffer, 0, n)
                            output.write(buffer, 0, n)
                            onProgress((total.toFloat() / voice.sizeBytes).coerceIn(0f, 1f))
                        }
                        if (total != voice.sizeBytes) throw IOException("size does not match the manifest")
                    }
                }
            }
            val actual = digest.digest().joinToString("") { "%02x".format(it) }
            if (actual != voice.sha256) throw IOException("checksum mismatch; the file was discarded")
            store.commit(voice.id, voice.nameAr, own = false)
        }.onFailure { part.delete() }
    }

    private fun open(startUrl: String): HttpURLConnection {
        var url = startUrl
        repeat(MAX_REDIRECTS + 1) {
            if (!VoiceUrlPolicy.isAllowed(url)) throw IOException("blocked URL")
            val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                connectTimeout = 15_000
                readTimeout = 30_000
                instanceFollowRedirects = false
                requestMethod = "GET"
                useCaches = false
            }
            when (conn.responseCode) {
                HttpURLConnection.HTTP_OK -> return conn
                301, 302, 303, 307, 308 -> {
                    val next = conn.getHeaderField("Location") ?: throw IOException("redirect without location")
                    conn.disconnect()
                    url = next
                }
                else -> {
                    val code = conn.responseCode
                    conn.disconnect()
                    throw IOException("HTTP $code")
                }
            }
        }
        throw IOException("too many redirects")
    }

    private fun java.io.InputStream.readLimited(limit: Int): ByteArray {
        val out = java.io.ByteArrayOutputStream()
        val buffer = ByteArray(8 * 1024)
        while (true) {
            val n = read(buffer)
            if (n < 0) break
            if (out.size() + n > limit) throw IOException("response too large")
            out.write(buffer, 0, n)
        }
        return out.toByteArray()
    }

    private inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T =
        try { block(this) } finally { disconnect() }

    private companion object {
        const val MAX_REDIRECTS = 3
        const val MAX_MANIFEST_BYTES = 256 * 1024
    }
}
