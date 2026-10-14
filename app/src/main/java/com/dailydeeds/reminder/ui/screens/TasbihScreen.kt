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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.worship.TasbihState
import com.dailydeeds.reminder.worship.ZahraStages

/** Tasbih al-Zahra with its three stages, and a free counter with an optional target. */
@Composable
fun TasbihScreen(viewModel: WorshipViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.tasbih.collectAsState()
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val hapticsOn = remember { PreferencesManager(context).isHapticsEnabled() }
    var mode by rememberSaveable { mutableIntStateOf(0) }

    ToolScaffold("المسبحة", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChoiceRow(listOf(0, 1), mode, { if (it == 0) "تسبيح الزهراء (ع)" else "عدّاد حر" }) { mode = it }

            if (mode == 0) {
                ZahraPanel(state)
            } else {
                FreePanel(state, viewModel)
            }

            CounterButton(
                count = if (mode == 0) state.zahraCount else state.freeCount,
                finished = if (mode == 0) state.zahraDone else state.freeDone,
                onTap = {
                    if (hapticsOn) haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (mode == 0) viewModel.tapZahra() else viewModel.tapFree()
                }
            )

            OutlinedButton(onClick = { if (mode == 0) viewModel.resetZahra() else viewModel.resetFree() }) {
                Text("إعادة العدّ")
            }
            WorshipNote("إجمالي ما سبّحته عبر التطبيق: ${state.lifetime}")
        }
    }
}

@Composable
private fun ZahraPanel(state: TasbihState) {
    val stage = ZahraStages.stageIndex(state.zahraCount)
    WorshipCard {
        Text(
            "تسبيح فاطمة الزهراء (ع): 34 تكبيرة، ثم 33 تحميدة، ثم 33 تسبيحة، عقيب كل صلاة.",
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
                "أتممتَ التسبيح. تقبّل الله منك. (عدد مرات الإتمام: ${state.zahraCompletions})",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FreePanel(state: TasbihState, viewModel: WorshipViewModel) {
    WorshipCard {
        WorshipHeading("الهدف")
        ChoiceRow(TasbihState.TARGETS, state.freeTarget, { if (it == 0) "مفتوح" else "$it" }) { viewModel.setFreeTarget(it) }
        if (state.freeDone) {
            Text("بلغتَ الهدف.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
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
                if (finished) "اكتمل" else "اضغط للعدّ",
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.Center
            )
        }
    }
}
