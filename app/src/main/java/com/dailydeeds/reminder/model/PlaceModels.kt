package com.dailydeeds.reminder.model

import java.time.ZoneId

/** Where prayer times and qibla are computed. [zoneId] is an IANA id such as "Asia/Beirut". */
data class Place(val name: String, val latitude: Double, val longitude: Double, val zoneId: String) {
    val zone: ZoneId get() = runCatching { ZoneId.of(zoneId) }.getOrDefault(ZoneId.systemDefault())

    init {
        require(latitude in -90.0..90.0 && longitude in -180.0..180.0) { "coordinates out of range" }
    }
}

object PlacePresets {
    val all: List<Place> = listOf(
        Place("بيروت", 33.8938, 35.5018, "Asia/Beirut"),
        Place("صور", 33.2705, 35.2038, "Asia/Beirut"),
        Place("النبطية", 33.3772, 35.4836, "Asia/Beirut"),
        Place("بعلبك", 34.0047, 36.2110, "Asia/Beirut"),
        Place("دمشق", 33.5138, 36.2765, "Asia/Damascus"),
        Place("بغداد", 33.3152, 44.3661, "Asia/Baghdad"),
        Place("النجف الأشرف", 32.0000, 44.3360, "Asia/Baghdad"),
        Place("كربلاء المقدسة", 32.6160, 44.0249, "Asia/Baghdad"),
        Place("الكاظمية", 33.3801, 44.3394, "Asia/Baghdad"),
        Place("سامراء", 34.1983, 43.8742, "Asia/Baghdad"),
        Place("البصرة", 30.5081, 47.7835, "Asia/Baghdad"),
        Place("قم المقدسة", 34.6401, 50.8764, "Asia/Tehran"),
        Place("مشهد المقدسة", 36.2605, 59.6168, "Asia/Tehran"),
        Place("طهران", 35.6892, 51.3890, "Asia/Tehran"),
        Place("الكويت", 29.3759, 47.9774, "Asia/Kuwait"),
        Place("المنامة", 26.2285, 50.5860, "Asia/Bahrain"),
        Place("القطيف", 26.5652, 50.0089, "Asia/Riyadh"),
        Place("الأحساء", 25.3830, 49.5866, "Asia/Riyadh"),
        Place("المدينة المنورة", 24.4686, 39.6142, "Asia/Riyadh"),
        Place("مكة المكرمة", 21.4225, 39.8262, "Asia/Riyadh"),
        Place("دبي", 25.2048, 55.2708, "Asia/Dubai"),
        Place("القاهرة", 30.0444, 31.2357, "Africa/Cairo"),
        Place("كراتشي", 24.8607, 67.0011, "Asia/Karachi"),
        Place("لاهور", 31.5497, 74.3436, "Asia/Karachi"),
        Place("لندن", 51.5074, -0.1278, "Europe/London"),
        Place("ديربورن", 42.3223, -83.1763, "America/Detroit"),
        Place("تورونتو", 43.6532, -79.3832, "America/Toronto"),
        Place("سيدني", -33.8688, 151.2093, "Australia/Sydney")
    )

    val default: Place = all.first()
}
