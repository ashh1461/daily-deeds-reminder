package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.PlaceSearch
import com.dailydeeds.reminder.model.Place
import com.dailydeeds.reminder.util.CoordinateInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneId

/** Offline city search (Arabic or Latin names) over the bundled GeoNames list. */
@Composable
fun CitySearchDialog(onDismiss: () -> Unit, onPick: (Place) -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Place>>(emptyList()) }
    LaunchedEffect(query) {
        delay(200)
        results = withContext(Dispatchers.Default) { PlaceSearch.search(query) }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("بحث عن مدينة") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    placeholder = { Text("اكتب اسم المدينة بالعربية أو الإنجليزية") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (query.trim().length >= 2 && results.isEmpty()) {
                    Text("لا نتائج. جرّب كتابة الاسم بالإنجليزية أو أدخل الإحداثيات يدوياً.", style = MaterialTheme.typography.bodySmall)
                }
                LazyColumn(Modifier.heightIn(max = 320.dp)) {
                    items(results) { place ->
                        Text(
                            place.name,
                            Modifier.fillMaxWidth().clickable { onPick(place) }.padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("إغلاق") } }
    )
}

/**
 * Manual latitude / longitude entry for places that are not in the city list. Arabic-Indic digits and «٫» are
 * accepted and an invalid entry says why. The time zone comes from the nearest city, so coordinates in another
 * country get that country's clock.
 */
@Composable
fun CoordinatesDialog(onDismiss: () -> Unit, onPick: (Place) -> Unit) {
    var lat by remember { mutableStateOf("") }
    var lon by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val latResult = CoordinateInput.latitude(lat)
    val lonResult = CoordinateInput.longitude(lon)
    val latValue = (latResult as? CoordinateInput.Result.Valid)?.degrees
    val lonValue = (lonResult as? CoordinateInput.Result.Valid)?.degrees
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إحداثيات يدوية") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CoordinateField(lat, { lat = it }, "خط العرض (من -90 إلى 90)", latResult)
                CoordinateField(lon, { lon = it }, "خط الطول (من -180 إلى 180)", lonResult)
                Text("يُستخدم توقيت أقرب مدينة إلى الإحداثيات لحساب الأوقات.", style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(
                enabled = !saving && latValue != null && lonValue != null,
                onClick = {
                    if (latValue == null || lonValue == null) return@TextButton
                    saving = true
                    scope.launch {
                        val place = withContext(Dispatchers.Default) {
                            PlaceSearch.placeAt(latValue, lonValue, "إحداثيات مخصصة", ZoneId.systemDefault().id)
                        }
                        onPick(place)
                    }
                }
            ) { Text("اعتماد") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }
    )
}

@Composable
private fun CoordinateField(value: String, onChange: (String) -> Unit, label: String, result: CoordinateInput.Result) {
    val error = (result as? CoordinateInput.Result.Invalid)?.messageArabic
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        singleLine = true,
        label = { Text(label) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        // Text, not Decimal: some Arabic keyboards hide the minus sign on the numeric pad.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth()
    )
}
