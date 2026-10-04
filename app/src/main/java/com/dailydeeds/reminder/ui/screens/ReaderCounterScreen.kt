package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.model.DeedType
import com.dailydeeds.reminder.viewmodel.MainViewModel

@Composable
fun ReaderCounterScreen(
    deedId: Int,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val deed = DeedsRepository.getDeedById(deedId) ?: run {
        onNavigateBack()
        return
    }

    val completedMap by viewModel.completedMap.collectAsState()
    val countsMap by viewModel.countsMap.collectAsState()
    val stagesMap by viewModel.stagesMap.collectAsState()

    val isCompleted = completedMap[deedId] ?: false
    val currentCount = countsMap[deedId] ?: 0
    val currentStage = stagesMap[deedId] ?: 0

    var readerFontSize by remember { mutableFloatStateOf(21f) }
    var useAlternativeVariant by remember { mutableStateOf(false) }

    val activeContent = if (useAlternativeVariant && deed.alternativeContent != null) {
        deed.alternativeContent
    } else {
        deed.content
    }

    Scaffold(
        topBar = {
            ReaderTopBar(
                deed = deed,
                isCompleted = isCompleted,
                onNavigateBack = onNavigateBack,
                onToggleCompleted = { viewModel.toggleDeedCompleted(deedId) },
                onResetCount = { viewModel.resetDeedCount(deedId) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (deed.type == DeedType.READING) {
            ReaderReadingContent(
                deed = deed,
                isCompleted = isCompleted,
                innerPadding = innerPadding,
                readerFontSize = readerFontSize,
                onFontSizeChange = { readerFontSize = it },
                useAlternativeVariant = useAlternativeVariant,
                onAlternativeVariantChange = { useAlternativeVariant = it },
                activeContent = activeContent,
                onToggleCompleted = { viewModel.toggleDeedCompleted(deedId) }
            )
        } else {
            ReaderCounterContent(
                deed = deed,
                isCompleted = isCompleted,
                currentCount = currentCount,
                currentStage = currentStage,
                innerPadding = innerPadding,
                onIncrement = { viewModel.incrementDeedCount(deedId) },
                onToggleCompleted = { viewModel.toggleDeedCompleted(deedId) },
                onResetCount = { viewModel.resetDeedCount(deedId) }
            )
        }
    }
}
