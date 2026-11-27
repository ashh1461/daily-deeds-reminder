package com.dailydeeds.reminder.ui.screens

import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.util.Qibla
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import kotlin.math.abs
import kotlin.math.roundToInt
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R

/**
 * Heading of the screen's top edge, degrees clockwise from true north, or null without a compass. The sensor reports
 * the device's natural (portrait) axes, so in landscape the matrix is remapped to the screen's current rotation.
 */
@Composable
private fun rememberTrueHeading(latitude: Double, longitude: Double): Float? {
    val context = LocalContext.current
    val view = LocalView.current
    var heading by remember { mutableFloatStateOf(Float.NaN) }
    val declination = remember(latitude, longitude) {
        GeomagneticField(latitude.toFloat(), longitude.toFloat(), 0f, System.currentTimeMillis()).declination
    }
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager }
    val rotation = remember { sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }

    DisposableEffect(sensorManager, rotation, declination) {
        val listener = object : SensorEventListener {
            private val matrix = FloatArray(9)
            private val remapped = FloatArray(9)
            private val orientation = FloatArray(3)
            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(matrix, event.values)
                val screen = when (view.display?.rotation ?: Surface.ROTATION_0) {
                    Surface.ROTATION_90 -> remapped.also {
                        SensorManager.remapCoordinateSystem(matrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, it)
                    }
                    Surface.ROTATION_180 -> remapped.also {
                        SensorManager.remapCoordinateSystem(matrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, it)
                    }
                    Surface.ROTATION_270 -> remapped.also {
                        SensorManager.remapCoordinateSystem(matrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, it)
                    }
                    else -> matrix
                }
                SensorManager.getOrientation(screen, orientation)
                val raw = (Math.toDegrees(orientation[0].toDouble()).toFloat() + declination + 360f) % 360f
                heading = if (heading.isNaN()) raw else {
                    // Low-pass filter, taking the shortest way around the circle.
                    var d = raw - heading
                    if (d > 180f) d -= 360f
                    if (d < -180f) d += 360f
                    (heading + 0.15f * d + 360f) % 360f
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        if (sensorManager != null && rotation != null) {
            sensorManager.registerListener(listener, rotation, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sensorManager?.unregisterListener(listener) }
    }
    return if (rotation == null || heading.isNaN()) null else heading
}

@Composable
fun QiblaScreen(viewModel: ToolsViewModel, onNavigateBack: () -> Unit) {
    val place by viewModel.place.collectAsState()
    val bearing = remember(place) { Qibla.bearing(place.latitude, place.longitude) }
    val heading = rememberTrueHeading(place.latitude, place.longitude)
    val arrow = if (heading != null) (bearing.toFloat() - heading + 360f) % 360f else bearing.toFloat()
    val aligned = heading != null && abs(((arrow + 180f) % 360f) - 180f) < 5f

    ToolScaffold(stringResource(R.string.qibla_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(stringResource(R.string.qibla_from, place.name), style = MaterialTheme.typography.titleMedium)
            Icon(
                Icons.Default.Navigation,
                contentDescription = stringResource(R.string.qibla_arrow),
                tint = if (aligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(180.dp).rotate(arrow)
            )
            Text(stringResource(R.string.qibla_bearing, bearing.roundToInt().toString()), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
            Text(
                when {
                    heading == null -> stringResource(R.string.qibla_no_compass)
                    aligned -> stringResource(R.string.qibla_aligned)
                    else -> stringResource(R.string.qibla_turn)
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(R.string.qibla_tip),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
