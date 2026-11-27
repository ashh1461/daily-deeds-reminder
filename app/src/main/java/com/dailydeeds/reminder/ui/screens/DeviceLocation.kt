package com.dailydeeds.reminder.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import com.dailydeeds.reminder.data.PlaceSearch
import com.dailydeeds.reminder.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.time.ZoneId
import kotlin.coroutines.resume
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.util.AppLanguage
import com.dailydeeds.reminder.util.LocaleSupport

/** What «موقعي» found. */
internal sealed interface LocationOutcome {
    data class Found(val place: Place) : LocationOutcome
    /** Location services are switched off in the phone's settings. */
    data object ServicesOff : LocationOutcome
    /** No fix within the timeout and no recent fix either. */
    data object Unavailable : LocationOutcome
    data object NoPermission : LocationOutcome
}

/** How long «موقعي» waits for a fresh fix before falling back to the phone's last known location. */
private const val FIX_TIMEOUT_MS = 10_000L

internal fun hasLocationPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

/**
 * Asks the phone for a fresh location (up to 10 s), then falls back to the last known one. The place keeps the
 * phone's time zone (the phone is there, so its clock is the local one) and is labelled after the nearest city.
 */
internal suspend fun locateDevice(context: Context): LocationOutcome {
    if (!hasLocationPermission(context)) return LocationOutcome.NoPermission
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return LocationOutcome.Unavailable
    if (!LocationManagerCompat.isLocationEnabled(lm)) return LocationOutcome.ServicesOff
    val loc = withTimeoutOrNull(FIX_TIMEOUT_MS) { freshLocation(context, lm) }
        ?: lastKnownLocation(lm)
        ?: return LocationOutcome.Unavailable
    // The place is stored and exported in backups, so keep only ~1 km precision; prayer times move by seconds.
    val lat = roundCoordinate(loc.latitude)
    val lon = roundCoordinate(loc.longitude)
    val near = withContext(Dispatchers.Default) { PlaceSearch.nearest(lat, lon, latinNames = LocaleSupport.current(context) == AppLanguage.ENGLISH) }
    val name = near?.let { context.getString(R.string.place_my_location_near, it.name) }
        ?: context.getString(R.string.place_my_location_name)
    return LocationOutcome.Found(Place(name, lat, lon, ZoneId.systemDefault().id))
}

@SuppressLint("MissingPermission")
private suspend fun freshLocation(context: Context, lm: LocationManager): Location? {
    val provider = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
        .firstOrNull { runCatching { lm.isProviderEnabled(it) }.getOrDefault(false) }
        ?: return null
    return suspendCancellableCoroutine { cont ->
        val signal = CancellationSignal()
        cont.invokeOnCancellation { signal.cancel() }
        try {
            LocationManagerCompat.getCurrentLocation(lm, provider, signal, ContextCompat.getMainExecutor(context)) { loc ->
                if (cont.isActive) cont.resume(loc)
            }
        } catch (e: RuntimeException) {
            // SecurityException: before Android 12 the GPS provider needs fine location, which the app does not ask for.
            if (cont.isActive) cont.resume(null)
        }
    }
}

@SuppressLint("MissingPermission")
private fun lastKnownLocation(lm: LocationManager): Location? =
    listOf(LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER, LocationManager.GPS_PROVIDER)
        .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
        .maxByOrNull { it.time }

/** Rounds a latitude or longitude to 2 decimals (about 1.1 km). */
internal fun roundCoordinate(degrees: Double): Double = Math.round(degrees * 100) / 100.0
