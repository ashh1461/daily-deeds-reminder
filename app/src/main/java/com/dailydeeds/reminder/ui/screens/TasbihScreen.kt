package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.ui.components.KeepScreenOn
import com.dailydeeds.reminder.ui.components.rememberStrongBuzz
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.TasbihState
import com.dailydeeds.reminder.worship.ZahraStages
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R

/** Tasbih al-Zahra with its three stages, and a free counter with an optional target. */
@Composable
fun TasbihScreen(viewModel: WorshipViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.tasbih.collectAsState()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val hapticsOn = remember { PreferencesManager(context).isHapticsEnabled() }
    val strongBuzz = rememberStrongBuzz()
    var mode by rememberSaveable { mutableIntStateOf(0) }
    // A count is cleared only after the reader agrees: the reset button, or a new target while counting.
    var confirmReset by rememberSaveable { mutableStateOf(false) }
    var pendingTarget by rememberSaveable { mutableStateOf<Int?>(null) }
    KeepScreenOn()

    ToolScaffold(stringResource(R.string.tasbih_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val modeLabels = listOf(stringResource(R.string.tasbih_mode_zahra), stringResource(R.string.tasbih_mode_free))
            ChoiceRow(listOf(0, 1), mode, { modeLabels[it] }) { mode = it }

            if (mode == 0) {
                ZahraPanel(state)
            } else {
                FreePanel(state) { target ->
                    if (state.targetChangeLosesCount(target)) pendingTarget = target else viewModel.setFreeTarget(target)
                }
            }

            CounterButton(
                count = if (mode == 0) state.zahraCount else state.freeCount,
                finished = if (mode == 0) state.zahraDone else state.freeDone,
                onTap = {
                    // A strong buzz at 34, 67 and 100 (and at the free target), so one can count without looking.
                    val reachesMark = if (mode == 0) {
                        !state.zahraDone && ZahraStages.isStageEnd(state.zahraCount + 1)
                    } else {
                        !state.freeDone && state.tapFree().freeDone
                    }
                    if (hapticsOn) {
                        if (reachesMark) strongBuzz() else haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    if (mode == 0) viewModel.tapZahra() else viewModel.tapFree()
                }
            )

            OutlinedButton(onClick = {
                when {
                    state.resetLosesCount(zahra = mode == 0) -> confirmReset = true
                    mode == 0 -> viewModel.resetZahra()
                    else -> viewModel.resetFree()
                }
            }) {
                Text(stringResource(R.string.tasbih_reset))
            }
            WorshipNote(stringResource(R.string.tasbih_lifetime, state.lifetime.toString()))
        }
    }

    if (confirmReset) {
        val count = if (mode == 0) state.zahraCount else state.freeCount
        ClearCountDialog(
            text = stringResource(R.string.tasbih_reset_confirm, count.toString()),
            onConfirm = {
                confirmReset = false
                if (mode == 0) viewModel.resetZahra() else viewModel.resetFree()
            },
            onDismiss = { confirmReset = false }
        )
    }
    pendingTarget?.let { target ->
        ClearCountDialog(
            text = stringResource(R.string.tasbih_target_confirm, state.freeCount.toString()),
            onConfirm = {
                pendingTarget = null
                viewModel.setFreeTarget(target)
            },
            onDismiss = { pendingTarget = null }
        )
    }
}

@Composable
private fun ClearCountDialog(text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.tasbih_clear_title)) },
        text = { Text(text) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.tasbih_clear)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.tasbih_keep)) } }
    )
}

@Composable
private fun ZahraPanel(state: TasbihState) {
    val stage = ZahraStages.stageIndex(state.zahraCount)
    WorshipCard {
        Text(
            stringResource(R.string.tasbih_zahra_note),
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ZahraStages.STAGES.forEachIndexed { index, s ->
            val done = if (index < stage || state.zahraDone) s.target else if (index == stage) ZahraStages.doneInStage(state.zahraCount) else 0
            val current = index == stage && !state.zahraDone
            Row(
                Modifier.fillMaxWidth()
                    .background(
                        if (current) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(s.phrase, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = if (current) FontWeight.Bold else FontWeight.Normal)
                Text("$done / ${s.target}", style = MaterialTheme.typography.titleSmall)
            }
        }
        if (state.zahraDone) {
            Text(
                stringResource(R.string.tasbih_zahra_done, state.zahraCompletions.toString()),
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FreePanel(state: TasbihState, onPickTarget: (Int) -> Unit) {
    WorshipCard {
        WorshipHeading(stringResource(R.string.tasbih_target))
        val openLabel = stringResource(R.string.tasbih_target_open)
        ChoiceRow(TasbihState.TARGETS, state.freeTarget, { if (it == 0) openLabel else "$it" }, onPickTarget)
        if (state.freeDone) {
            Text(stringResource(R.string.tasbih_target_reached), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun CounterButton(count: Int, finished: Boolean, onTap: () -> Unit) {
    val container = if (finished) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    Box(
        Modifier.size(220.dp).clip(CircleShape).background(container).clickable(onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            Text(
                stringResource(if (finished) R.string.tasbih_done else R.string.tasbih_tap),
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.Center
            )
        }
    }
}
