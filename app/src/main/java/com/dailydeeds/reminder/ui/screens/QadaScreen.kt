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
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.qadaTitle
import com.dailydeeds.reminder.ui.labelRes

private sealed interface QadaDialog {
    class AddTo(val kind: QadaKind) : QadaDialog
    object Bulk : QadaDialog
}

/** Counters for missed prayers and fasts that still have to be made up. */
@Composable
fun QadaScreen(viewModel: WorshipViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.qada.collectAsState()
    var dialog by remember { mutableStateOf<QadaDialog?>(null) }

    ToolScaffold(stringResource(R.string.qada_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WorshipCard {
                WorshipHeading(stringResource(R.string.qada_total))
                Text(stringResource(R.string.qada_totals, state.totalPrayers.toString(), state.totalFasts.toString()), style = MaterialTheme.typography.titleMedium)
                WorshipNote(stringResource(R.string.qada_note))
                OutlinedButton(onClick = { dialog = QadaDialog.Bulk }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.qada_add_days))
                }
            }

            WorshipCard {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.qada_full_day), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            stringResource(R.string.qada_full_day_note),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    FilledTonalButton(onClick = { viewModel.markQadaFullDay() }, enabled = state.canMarkFullDay) {
                        Text(stringResource(R.string.qada_did_full_day))
                    }
                }
            }

            QadaKind.values().forEach { kind ->
                WorshipCard {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                qadaTitle(kind),
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                            )
                            Text(stringResource(R.string.qada_remaining, state.owed(kind).toString()), style = MaterialTheme.typography.bodyMedium)
                        }
                        FilledTonalButton(onClick = { viewModel.markQada(kind) }, enabled = state.owed(kind) > 0) { Text(stringResource(R.string.qada_did_one)) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { viewModel.addQada(kind, 1) }) { Text("+1") }
                        OutlinedButton(onClick = { dialog = QadaDialog.AddTo(kind) }) { Text(stringResource(R.string.qada_add_count)) }
                    }
                }
            }

            if (state.history.isNotEmpty()) {
                WorshipCard {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        WorshipHeading(stringResource(R.string.qada_recent))
                        Column(Modifier.weight(1f)) {}
                        TextButton(onClick = { viewModel.undoQada() }) { Text(stringResource(R.string.qada_undo)) }
                    }
                    state.history.takeLast(8).reversed().forEach { e ->
                        val what = if (e.kind.isFast) stringResource(e.kind.labelRes)
                        else stringResource(R.string.qada_history_prayer, stringResource(e.kind.labelRes))
                        val verb = if (e.delta > 0) stringResource(R.string.qada_history_added, e.delta.toString())
                        else stringResource(R.string.qada_history_done, (-e.delta).toString())
                        Text("${LocalDate.ofEpochDay(e.epochDay)} — $verb: $what", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    when (val d = dialog) {
        is QadaDialog.AddTo -> NumberDialog(
            title = stringResource(R.string.qada_add_to, qadaTitle(d.kind)),
            label = stringResource(R.string.qada_count), confirm = stringResource(R.string.common_add),
            onDismiss = { dialog = null }
        ) { viewModel.addQada(d.kind, it) }
        QadaDialog.Bulk -> NumberDialog(
            title = stringResource(R.string.qada_add_days_title),
            label = stringResource(R.string.qada_days_count), confirm = stringResource(R.string.common_add),
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
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_cancel)) } }
    )
}
