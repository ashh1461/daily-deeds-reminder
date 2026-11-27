package com.dailydeeds.reminder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.RemoteVoicesState
import com.dailydeeds.reminder.viewmodel.VoiceViewModel
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R

/** Installed adhan voices, the downloadable catalogue, and importing a file from the phone. */
@Composable
fun VoicePacksScreen(voices: VoiceViewModel, onNavigateBack: () -> Unit) {
    val installed by voices.installed.collectAsState()
    val remote by voices.remote.collectAsState()
    val progress by voices.progress.collectAsState()
    val message by voices.message.collectAsState()

    LaunchedEffect(Unit) { voices.refreshCatalogue() }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) voices.importOwn(uri)
    }

    ToolScaffold(stringResource(R.string.voices_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            message?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(it, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = voices::clearMessage) { Text(stringResource(R.string.common_ok)) }
                    }
                }
            }

            Title(stringResource(R.string.voices_own_file))
            Box2 {
                Text(
                    stringResource(R.string.voices_pick_note),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = { picker.launch(arrayOf("audio/*")) }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.voices_pick_file)) }
            }

            Title(stringResource(R.string.voices_installed))
            if (installed.isEmpty()) {
                Text(stringResource(R.string.voices_none_installed), style = MaterialTheme.typography.bodyMedium)
            }
            installed.forEach { v ->
                Box2 {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(v.nameAr, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(stringResource(if (v.own) R.string.voices_own_file else R.string.voices_downloaded_pack), style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        OutlinedButton(onClick = { voices.remove(v.id) }) { Text(stringResource(R.string.common_delete)) }
                    }
                }
            }

            Title(stringResource(R.string.voices_available))
            when (val state = remote) {
                RemoteVoicesState.Idle, RemoteVoicesState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                is RemoteVoicesState.Failed -> Box2 {
                    Text(state.reason, style = MaterialTheme.typography.bodyMedium)
                    OutlinedButton(onClick = voices::refreshCatalogue) { Text(stringResource(R.string.common_retry)) }
                }
                is RemoteVoicesState.Loaded -> {
                    if (state.voices.isEmpty()) {
                        Text(
                            stringResource(R.string.voices_none_published),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    state.voices.forEach { v ->
                        val done = installed.any { it.id == v.id }
                        val p = progress[v.id]
                        Box2 {
                            Text(v.nameAr, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                stringResource(R.string.voices_reciter_size, v.reciter, String.format(Locale.US, "%.1f", v.sizeBytes / 1_000_000.0)) +
                                    if (v.includesWilayah) " " + stringResource(R.string.voices_includes_wilayah) else "",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(stringResource(R.string.voices_licence, v.licence), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            when {
                                p != null -> LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth())
                                done -> Text(stringResource(R.string.voices_installed_badge), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                else -> Button(onClick = { voices.download(v) }) { Text(stringResource(R.string.voices_download)) }
                            }
                        }
                    }
                    if (state.rejected > 0) {
                        Text(stringResource(R.string.voices_rejected, state.rejected.toString()), style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Text(
                stringResource(R.string.voices_download_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun Box2(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
    }
}
