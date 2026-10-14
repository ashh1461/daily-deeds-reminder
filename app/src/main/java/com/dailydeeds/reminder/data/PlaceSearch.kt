package com.dailydeeds.reminder.data

import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.ArabicNormalizer
import java.text.Normalizer
import java.time.ZoneId

/**
 * Offline city search over the bundled GeoNames list (`places/cities.txt`, cities of 15,000+ people,
 * CC BY 4.0). Matches Latin names and, where GeoNames has one, the Arabic name.
 */
object PlaceSearch {
    const val RESOURCE = "places/cities.txt"

    private class City(
        val name: String,
        val arabic: String,
        val country: String,
        val lat: Double,
        val lon: Double,
        val zone: String
    ) {
        val latinKey: String = latin(name)
        val arabicKey: String = if (arabic.isEmpty()) "" else arabicKey(arabic)

        fun toPlace(): Place = Place("${arabic.ifEmpty { name }} • $country", lat, lon, zone)
    }

    private val cities: List<City> by lazy { load() }

    /** Loads the list; call from a background thread before the first search. */
    fun warmUp() {
        cities
    }

    val size: Int get() = cities.size

    fun search(query: String, limit: Int = 30): List<Place> {
        val q = query.trim().take(60)
        if (q.length < 2) return emptyList()
        val isArabic = q.any { it in '؀'..'ۿ' }
        val key = if (isArabic) arabicKey(q) else latin(q)
        if (key.isEmpty()) return emptyList()
        val keys: (City) -> String = if (isArabic) { c -> c.arabicKey } else { c -> c.latinKey }
        val starts = ArrayList<City>()
        val contains = ArrayList<City>()
        for (c in cities) {
            val k = keys(c)
            if (k.isEmpty()) continue
            when {
                k.startsWith(key) -> starts.add(c)
                k.contains(key) -> contains.add(c)
            }
            if (starts.size >= limit) break
        }
        return (starts + contains).take(limit).map { it.toPlace() }
    }

    private fun latin(s: String): String =
        Normalizer.normalize(s.lowercase(), Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "").replace(Regex("[^a-z0-9 ]"), " ").trim()

    /** Arabic matching key; the leading definite article is ignored so "النجف" finds "نجف". */
    private fun arabicKey(s: String): String =
        // searchKey drops plain alefs, so "ال..." becomes "ل...": strip that remainder of the article.
        ArabicNormalizer.searchKey(s).removePrefix("ل")

    private fun load(): List<City> {
        val stream = PlaceSearch::class.java.classLoader?.getResourceAsStream(RESOURCE) ?: return emptyList()
        return stream.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.mapNotNull { line ->
                val f = line.split('|')
                if (f.size != 6) return@mapNotNull null
                val lat = f[3].toDoubleOrNull() ?: return@mapNotNull null
                val lon = f[4].toDoubleOrNull() ?: return@mapNotNull null
                if (lat !in -90.0..90.0 || lon !in -180.0..180.0) return@mapNotNull null
                if (runCatching { ZoneId.of(f[5]) }.isFailure) return@mapNotNull null
                City(f[0], f[1], f[2], lat, lon, f[5])
            }.toList()
        }
    }
}
