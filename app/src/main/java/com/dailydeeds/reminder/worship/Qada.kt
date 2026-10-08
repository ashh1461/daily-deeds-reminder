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

/** One change to the make-up counter: positive when added to the debt, negative when made up. */
data class QadaEntry(val epochDay: Long, val kind: QadaKind, val delta: Int)

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

    fun add(kind: QadaKind, count: Int, epochDay: Long): QadaState {
        val room = MAX_OWED - owed(kind)
        val n = count.coerceIn(0, room)
        if (n == 0) return this
        return change(kind, n, epochDay)
    }

    /** Adds [days] to each of the five daily prayers (for example a year of missed prayers). */
    fun addDaysOfPrayers(days: Int, epochDay: Long): QadaState =
        QadaKind.PRAYERS.fold(this) { state, kind -> state.add(kind, days, epochDay) }

    /** Marks up to [count] as made up; never goes below zero. */
    fun markMade(kind: QadaKind, count: Int, epochDay: Long): QadaState {
        val n = count.coerceIn(0, owed(kind))
        if (n == 0) return this
        return change(kind, -n, epochDay)
    }

    /** Reverts the latest change. */
    fun undoLast(): QadaState {
        val last = history.lastOrNull() ?: return this
        val restored = (owed(last.kind) - last.delta).coerceIn(0, MAX_OWED)
        return QadaState(owed + (last.kind to restored), history.dropLast(1))
    }

    private fun change(kind: QadaKind, delta: Int, epochDay: Long): QadaState {
        val updated = owed + (kind to owed(kind) + delta)
        return QadaState(updated, (history + QadaEntry(epochDay, kind, delta)).takeLast(MAX_HISTORY))
    }

    /** `fajr=3,dhuhr=2|19000:fajr:3;19001:fajr:-1` */
    fun encode(): String {
        val o = QadaKind.values().filter { owed(it) > 0 }.joinToString(",") { "${it.id}=${owed(it)}" }
        val h = history.joinToString(";") { "${it.epochDay}:${it.kind.id}:${it.delta}" }
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
                if (f.size != 3) return@mapNotNull null
                val day = f[0].toLongOrNull() ?: return@mapNotNull null
                val kind = QadaKind.fromId(f[1]) ?: return@mapNotNull null
                val delta = f[2].toIntOrNull() ?: return@mapNotNull null
                if (delta == 0 || delta > MAX_OWED || delta < -MAX_OWED) null else QadaEntry(day, kind, delta)
            }.takeLast(MAX_HISTORY)
            return QadaState(owed, history)
        }
    }
}
