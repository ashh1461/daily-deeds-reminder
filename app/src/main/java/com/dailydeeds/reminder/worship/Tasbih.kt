package com.dailydeeds.reminder.worship

/** Tasbih al-Zahra (peace be upon her): 34 takbir, 33 tahmid, 33 tasbih, in that order. */
object ZahraStages {
    class Stage(val phrase: String, val target: Int)

    val STAGES = listOf(
        Stage("اللَّهُ أَكْبَرُ", 34),
        Stage("الْحَمْدُ لِلَّهِ", 33),
        Stage("سُبْحَانَ اللَّهِ", 33)
    )
    const val TOTAL = 100

    /** The stage (0..2) the [count]-th tap belongs to; 0 before the first tap. */
    fun stageIndex(count: Int): Int = when {
        count <= 34 -> 0
        count <= 67 -> 1
        else -> 2
    }

    /** Taps done inside the current stage. */
    fun doneInStage(count: Int): Int {
        val c = count.coerceIn(0, TOTAL)
        return c - STAGES.take(stageIndex(c)).sumOf { it.target }
    }
}

/** State of the two counters on the tasbih screen plus the lifetime total, stored as one short string. */
data class TasbihState(
    val zahraCount: Int = 0,
    val zahraCompletions: Int = 0,
    val freeCount: Int = 0,
    /** 0 means no target. */
    val freeTarget: Int = 0,
    val lifetime: Long = 0L
) {
    val zahraDone: Boolean get() = zahraCount >= ZahraStages.TOTAL
    val freeDone: Boolean get() = freeTarget > 0 && freeCount >= freeTarget

    fun tapZahra(): TasbihState {
        if (zahraDone) return this
        val next = zahraCount + 1
        return copy(
            zahraCount = next,
            zahraCompletions = if (next >= ZahraStages.TOTAL) zahraCompletions + 1 else zahraCompletions,
            lifetime = lifetime + 1
        )
    }

    fun tapFree(): TasbihState {
        if (freeDone || freeCount >= MAX_FREE) return this
        return copy(freeCount = freeCount + 1, lifetime = lifetime + 1)
    }

    fun resetZahra() = copy(zahraCount = 0)
    fun resetFree() = copy(freeCount = 0)
    fun withTarget(target: Int) = copy(freeTarget = target.coerceIn(0, MAX_FREE), freeCount = 0)

    fun encode(): String = "$zahraCount,$zahraCompletions,$freeCount,$freeTarget,$lifetime"

    companion object {
        const val MAX_FREE = 1_000_000
        val TARGETS = listOf(0, 33, 34, 99, 100, 313, 1000)

        fun decode(text: String?): TasbihState {
            val p = text?.split(',') ?: return TasbihState()
            if (p.size != 5) return TasbihState()
            val n = p.map { it.trim().toLongOrNull() ?: return TasbihState() }
            val target = n[3].coerceIn(0, MAX_FREE.toLong()).toInt()
            return TasbihState(
                zahraCount = n[0].coerceIn(0, ZahraStages.TOTAL.toLong()).toInt(),
                zahraCompletions = n[1].coerceIn(0, 1_000_000).toInt(),
                freeCount = n[2].coerceIn(0, if (target > 0) target.toLong() else MAX_FREE.toLong()).toInt(),
                freeTarget = target,
                lifetime = n[4].coerceIn(0, Long.MAX_VALUE / 2)
            )
        }
    }
}
