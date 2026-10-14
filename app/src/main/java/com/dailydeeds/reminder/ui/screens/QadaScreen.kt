package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.QadaKind
import java.time.LocalDate

private sealed interface QadaDialog {
    class AddTo(val kind: QadaKind) : QadaDialog
    object Bulk : QadaDialog
}

/** Counters for missed prayers and fasts that still have to be made up. */
@Composable
fun QadaScreen(viewModel: WorshipViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.qada.collectAsState()
    var dialog by remember { mutableStateOf<QadaDialog?>(null) }

    ToolScaffold("القضاء", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WorshipCard {
                WorshipHeading("المجموع")
                Text("الصلوات: ${state.totalPrayers}    ·    الصيام: ${state.totalFasts} يوماً", style = MaterialTheme.typography.titleMedium)
                WorshipNote("سجّل ما عليك من صلوات وصيام فائت، وأنقص العدد كلما قضيتَ شيئاً. تُحفظ الأرقام على جهازك فقط.")
                OutlinedButton(onClick = { dialog = QadaDialog.Bulk }, modifier = Modifier.fillMaxWidth()) {
                    Text("إضافة صلوات عن عدد من الأيام")
                }
            }

            QadaKind.values().forEach { kind ->
                WorshipCard {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                if (kind.isFast) "قضاء الصيام" else "قضاء صلاة ${kind.labelArabic}",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                            )
                            Text("المتبقي: ${state.owed(kind)}", style = MaterialTheme.typography.bodyMedium)
                        }
                        FilledTonalButton(onClick = { viewModel.markQada(kind) }, enabled = state.owed(kind) > 0) { Text("قضيتُ 1") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.addQada(kind, 1) }) { Text("+1") }
                        OutlinedButton(onClick = { dialog = QadaDialog.AddTo(kind) }) { Text("إضافة عدد") }
                    }
                }
            }

            if (state.history.isNotEmpty()) {
                WorshipCard {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        WorshipHeading("آخر التغييرات")
                        Column(Modifier.weight(1f)) {}
                        TextButton(onClick = { viewModel.undoQada() }) { Text("تراجع عن الأخير") }
                    }
                    state.history.takeLast(8).reversed().forEach { e ->
                        val what = if (e.kind.isFast) "الصيام" else "صلاة ${e.kind.labelArabic}"
                        val verb = if (e.delta > 0) "أضفتَ ${e.delta}" else "قضيتَ ${-e.delta}"
                        Text("${LocalDate.ofEpochDay(e.epochDay)} — $verb: $what", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    when (val d = dialog) {
        is QadaDialog.AddTo -> NumberDialog(
            title = "إضافة إلى ${if (d.kind.isFast) "قضاء الصيام" else "قضاء صلاة ${d.kind.labelArabic}"}",
            label = "العدد", confirm = "إضافة",
            onDismiss = { dialog = null }
        ) { viewModel.addQada(d.kind, it) }
        QadaDialog.Bulk -> NumberDialog(
            title = "إضافة صلوات الأيام",
            label = "عدد الأيام (تُضاف إلى الصلوات الخمس)", confirm = "إضافة",
            onDismiss = { dialog = null }
        ) { viewModel.addQadaDays(it) }
        null -> Unit
    }
}

@Composable
private fun NumberDialog(title: String, label: String, confirm: String, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var text by remember { mutableStateOf("") }
    val value = text.trim().toIntOrNull()?.takeIf { it in 1..100_000 }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text, onValueChange = { text = it.filter { c -> c.isDigit() }.take(6) },
                label = { Text(label) }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            Button(onClick = { value?.let(onConfirm); onDismiss() }, enabled = value != null) { Text(confirm) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }
    )
}
