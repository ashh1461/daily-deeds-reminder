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

        /** [latin]: the city's English name, for the English UI; otherwise its Arabic name when it has one. */
        fun toPlace(latin: Boolean = false): Place =
            Place("${if (latin) name else arabic.ifEmpty { name }} • $country", lat, lon, zone)
    }

    private val cities: List<City> by lazy { load() }

    /** Loads the list; call from a background thread before the first search. */
    fun warmUp() {
        cities
    }

    val size: Int get() = cities.size

    fun search(query: String, limit: Int = 30, latinNames: Boolean = false): List<Place> {
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
        return (starts + contains).take(limit).map { it.toPlace(latinNames) }
    }

    /** A bundled city near some coordinates: its display name, time zone and distance. */
    data class Nearest(val name: String, val zoneId: String, val km: Double)

    /** The bundled city closest to the coordinates, or null when none lies within [maxKm] (open sea, remote areas). */
    fun nearest(latitude: Double, longitude: Double, maxKm: Double = NEAREST_MAX_KM, latinNames: Boolean = false): Nearest? {
        var best: City? = null
        var bestKm = Double.MAX_VALUE
        for (c in cities) {
            val km = distanceKm(latitude, longitude, c.lat, c.lon)
            if (km < bestKm) {
                bestKm = km
                best = c
            }
        }
        val city = best ?: return null
        if (bestKm > maxKm) return null
        return Nearest(if (latinNames) city.name else city.arabic.ifEmpty { city.name }, city.zone, bestKm)
    }

    /**
     * A place for raw coordinates (typed, or from the phone's location). Its time zone comes from the nearest city,
     * so coordinates in another country get that country's clock, and it is labelled «[label] (قرب …)». With no
     * city nearby it keeps [fallbackZone].
     */
    fun placeAt(
        latitude: Double,
        longitude: Double,
        label: String,
        fallbackZone: String,
        nearLabel: (String) -> String = { "قرب $it" },
        latinNames: Boolean = false
    ): Place {
        val near = nearest(latitude, longitude, latinNames = latinNames) ?: return Place(label, latitude, longitude, fallbackZone)
        return Place("$label (${nearLabel(near.name)})", latitude, longitude, near.zoneId)
    }

    /** Great-circle distance in kilometres (haversine). */
    internal fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).let { it * it } +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2).let { it * it }
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a.coerceIn(0.0, 1.0)))
    }

    private const val EARTH_RADIUS_KM = 6371.0
    /** Beyond this, a city's time zone is no longer a safe guess for the coordinates. */
    private const val NEAREST_MAX_KM = 500.0

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
