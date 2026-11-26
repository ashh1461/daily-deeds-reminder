package com.dailydeeds.reminder.ui.screens

import android.Manifest
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.model.PlacePresets
import kotlinx.coroutines.launch

/**
 * The place used for prayer times, with every way to change it: suggested cities, offline search, the phone's
 * location and typed coordinates. Shown on both the prayer-times screen and the adhan settings screen.
 * [extra] adds lines under the place name (the next prayer, on the prayer-times screen).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlacePickerCard(
    place: Place,
    onPick: (Place) -> Unit,
    modifier: Modifier = Modifier,
    extra: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var menuOpen by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showCoordinates by remember { mutableStateOf(false) }
    var showServicesOff by remember { mutableStateOf(false) }
    var locating by remember { mutableStateOf(false) }

    fun locate() {
        if (locating) return
        locating = true
        scope.launch {
            val outcome = locateDevice(context)
            locating = false
            when (outcome) {
                is LocationOutcome.Found -> onPick(outcome.place)
                LocationOutcome.ServicesOff -> showServicesOff = true
                LocationOutcome.Unavailable -> Toast.makeText(
                    context, "تعذّر تحديد الموقع؛ ابحث عن مدينتك أو اخترها من القائمة.", Toast.LENGTH_LONG
                ).show()
                LocationOutcome.NoPermission -> Unit
            }
        }
    }

    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) locate()
        else Toast.makeText(context, "لم يُمنح إذن الموقع؛ اختر مدينتك بالبحث أو من القائمة.", Toast.LENGTH_LONG).show()
    }

    Card(modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("المكان: ${place.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            extra()
            // FlowRow wraps the buttons onto a second line at large font sizes instead of clipping them.
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box {
                    OutlinedButton(onClick = { menuOpen = true }) { Text("مدن مقترحة") }
                    DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                        PlacePresets.all.forEach { p ->
                            DropdownMenuItem(text = { Text(p.name) }, onClick = {
                                onPick(p)
                                menuOpen = false
                            })
                        }
                    }
                }
                OutlinedButton(onClick = { showSearch = true }) { Text("بحث") }
                OutlinedButton(enabled = !locating, onClick = {
                    if (hasLocationPermission(context)) locate()
                    else permission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }) { Text(if (locating) "جارٍ تحديد الموقع…" else "موقعي") }
                OutlinedButton(onClick = { showCoordinates = true }) { Text("إدخال الإحداثيات") }
            }
        }
    }

    if (showSearch) {
        CitySearchDialog(onDismiss = { showSearch = false }, onPick = { onPick(it); showSearch = false })
    }
    if (showCoordinates) {
        CoordinatesDialog(onDismiss = { showCoordinates = false }, onPick = { onPick(it); showCoordinates = false })
    }
    if (showServicesOff) {
        AlertDialog(
            onDismissRequest = { showServicesOff = false },
            title = { Text("خدمة الموقع متوقفة") },
            text = { Text("فعّل خدمة الموقع في إعدادات الهاتف ثم اضغط «موقعي» مرة أخرى، أو اختر مدينتك بالبحث.") },
            confirmButton = {
                TextButton(onClick = {
                    showServicesOff = false
                    runCatching {
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }
                }) { Text("فتح الإعدادات") }
            },
            dismissButton = { TextButton(onClick = { showServicesOff = false }) { Text("إلغاء") } }
        )
    }
}
