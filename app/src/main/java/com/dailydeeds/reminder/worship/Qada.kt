package com.dailydeeds.reminder.worship

enum class QadaKind(val id: String, val labelArabic: String, val isFast: Boolean = false) {
    FAJR("fajr", "الصبح"),
    DHUHR("dhuhr", "الظهر"),
    ASR("asr", "العصر"),
    MAGHRIB("maghrib", "المغرب"),
    ISHA("isha", "العشاء"),
    FAST("fast", "الصيام", isFast = true);

    companion object {
        fun fromId(id: String): QadaKind? = values().find { it.id == id }
        val PRAYERS = values().filter { !it.isFast }
    }
}

/**
 * One change to the make-up counter: positive when added to the debt, negative when made up. Entries written by one
 * action (adding days of all five prayers) share a non-zero [batch], so they are undone together.
 */
data class QadaEntry(val epochDay: Long, val kind: QadaKind, val delta: Int, val batch: Long = 0)

/**
 * Make-up (qada) prayers and fasts still owed, with a short history so a mistaken tap can be undone.
 * Immutable: every operation returns a new state.
 */
data class QadaState(
    val owed: Map<QadaKind, Int> = emptyMap(),
    val history: List<QadaEntry> = emptyList()
) {
    fun owed(kind: QadaKind): Int = owed[kind] ?: 0
    val totalPrayers: Int get() = QadaKind.PRAYERS.sumOf { owed(it) }
    val totalFasts: Int get() = owed(QadaKind.FAST)

    fun add(kind: QadaKind, count: Int, epochDay: Long): QadaState = add(kind, count, epochDay, batch = 0)

    private fun add(kind: QadaKind, count: Int, epochDay: Long, batch: Long): QadaState {
        val room = MAX_OWED - owed(kind)
        val n = count.coerceIn(0, room)
        if (n == 0) return this
        return change(kind, n, epochDay, batch)
    }

    /**
     * Adds [days] to each of the five daily prayers (for example a year of missed prayers). The five entries share one
     * batch id, so «تراجع عن الأخير» reverts all of them.
     */
    fun addDaysOfPrayers(days: Int, epochDay: Long): QadaState {
        val batch = (history.maxOfOrNull { it.batch } ?: 0L) + 1
        return QadaKind.PRAYERS.fold(this) { state, kind -> state.add(kind, days, epochDay, batch) }
    }

    /** Marks up to [count] as made up; never goes below zero. */
    fun markMade(kind: QadaKind, count: Int, epochDay: Long): QadaState {
        val n = count.coerceIn(0, owed(kind))
        if (n == 0) return this
        return change(kind, -n, epochDay, batch = 0)
    }

    /** Reverts the latest change; when it was part of a batch, reverts the whole batch. */
    fun undoLast(): QadaState {
        val last = history.lastOrNull() ?: return this
        val count = if (last.batch == 0L) 1 else history.takeLastWhile { it.batch == last.batch }.size
        val undone = history.takeLast(count)
        val restored = undone.fold(owed) { acc, e ->
            acc + (e.kind to ((acc[e.kind] ?: 0) - e.delta).coerceIn(0, MAX_OWED))
        }
        return QadaState(restored, history.dropLast(count))
    }

    private fun change(kind: QadaKind, delta: Int, epochDay: Long, batch: Long): QadaState {
        val updated = owed + (kind to owed(kind) + delta)
        return QadaState(updated, (history + QadaEntry(epochDay, kind, delta, batch)).takeLast(MAX_HISTORY))
    }

    /** `fajr=3,dhuhr=2|19000:fajr:3;19001:fajr:-1;19002:asr:30:4` (a fourth field is the batch id, when there is one). */
    fun encode(): String {
        val o = QadaKind.values().filter { owed(it) > 0 }.joinToString(",") { "${it.id}=${owed(it)}" }
        val h = history.joinToString(";") { e ->
            "${e.epochDay}:${e.kind.id}:${e.delta}" + if (e.batch != 0L) ":${e.batch}" else ""
        }
        return "$o|$h"
    }

    companion object {
        const val MAX_OWED = 1_000_000
        const val MAX_HISTORY = 100

        fun decode(text: String?): QadaState {
            if (text.isNullOrBlank()) return QadaState()
            val parts = text.split('|', limit = 2)
            val owed = HashMap<QadaKind, Int>()
            parts[0].split(',').filter { it.isNotBlank() }.forEach { pair ->
                val kv = pair.split('=')
                val kind = kv.getOrNull(0)?.let(QadaKind::fromId) ?: return@forEach
                val n = kv.getOrNull(1)?.toIntOrNull() ?: return@forEach
                if (n > 0) owed[kind] = n.coerceAtMost(MAX_OWED)
            }
            val history = (parts.getOrNull(1) ?: "").split(';').mapNotNull { e ->
                val f = e.split(':')
                // Three fields before 1.8.1.6; a fourth (the batch id) since.
                if (f.size != 3 && f.size != 4) return@mapNotNull null
                val day = f[0].toLongOrNull() ?: return@mapNotNull null
                val kind = QadaKind.fromId(f[1]) ?: return@mapNotNull null
                val delta = f[2].toIntOrNull() ?: return@mapNotNull null
                val batch = if (f.size == 4) f[3].toLongOrNull()?.takeIf { it > 0 } ?: return@mapNotNull null else 0L
                if (delta == 0 || delta > MAX_OWED || delta < -MAX_OWED) null else QadaEntry(day, kind, delta, batch)
            }.takeLast(MAX_HISTORY)
            return QadaState(owed, history)
        }
    }
}
