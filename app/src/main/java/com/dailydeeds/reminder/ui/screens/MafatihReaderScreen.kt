package com.dailydeeds.reminder.ui.screens

import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.LaunchedEffect
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.remember
import com.dailydeeds.reminder.ui.components.KeepScreenOn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.viewmodel.MafatihViewModel
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.theme.RtlContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MafatihReaderScreen(
    itemId: String,
    viewModel: MafatihViewModel,
    tools: ToolsViewModel,
    onNavigateBack: () -> Unit
) {
    val item by viewModel.selectedItem.collectAsState()
    val counter by viewModel.counter.collectAsState()
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()

    val favorites by tools.favorites.collectAsState()
    val currentItem = item ?: return
    LaunchedEffect(currentItem.id) {
        if (currentItem.fromMafatihBook) tools.recordMafatih(currentItem.id) else tools.recordSahifa(currentItem.id)
    }
    KeepScreenOn()
    // Sections run to 21.8k characters, so they are drawn in blocks of lines and only the blocks on screen are laid out.
    val lines = remember(currentItem.id) { readerBlocks(currentItem.arabicText) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = currentItem.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = currentItem.category.titleArabic,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                    }
                },
                actions = {
                    IconButton(onClick = { tools.toggleFavorite((if (currentItem.fromMafatihBook) FavoriteKey.Mafatih(currentItem.id) else FavoriteKey.Sahifa(currentItem.id))) }) {
                        Icon(
                            if ((if (currentItem.fromMafatihBook) FavoriteKey.Mafatih(currentItem.id) else FavoriteKey.Sahifa(currentItem.id)) in favorites) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = stringResource(R.string.action_favorites),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { viewModel.increaseFontSize() }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = stringResource(R.string.font_larger))
                    }
                    IconButton(onClick = { viewModel.decreaseFontSize() }) {
                        Icon(Icons.Default.ZoomOut, contentDescription = stringResource(R.string.font_smaller))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedIconButton(onClick = { viewModel.resetCounter() }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.counter_reset_short))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$counter",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.reader_repeat),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    FilledTonalButton(
                        onClick = { viewModel.incrementCounter() },
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.reader_count_action))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.reader_count), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues -> RtlContent {
        val panel = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
        val textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = fontSizeSp.sp, lineHeight = (fontSizeSp * 1.7f).sp)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }
            // One rounded panel as before: the first line rounds the top corners, the last the bottom ones.
            itemsIndexed(lines) { index, line ->
                val shape = readerLineShape(index, lines.lastIndex)
                Text(
                    text = line,
                    style = textStyle,
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = panel, shape = shape)
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = if (index == 0) 18.dp else 0.dp,
                            bottom = if (index == lines.lastIndex) 18.dp else 0.dp
                        )
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
}

/** The section's lines, split at its line breaks; joined with "\n" they give back the exact text. */
internal fun readerLines(text: String): List<String> = text.split('\n')

/** About this many characters per block: small enough to lay out quickly, large enough for TalkBack to read on. */
internal const val READER_BLOCK_CHARS = 1_200

/**
 * Consecutive lines grouped into blocks of about [READER_BLOCK_CHARS] characters, never splitting a line. Joined with
 * "\n" they give back the exact text.
 */
internal fun readerBlocks(text: String): List<String> {
    val blocks = ArrayList<String>()
    val current = StringBuilder()
    var started = false
    for (line in readerLines(text)) {
        if (started && current.length >= READER_BLOCK_CHARS) {
            blocks += current.toString()
            current.setLength(0)
            started = false
        }
        if (started) current.append('\n')
        current.append(line)
        started = true
    }
    blocks += current.toString()
    return blocks
}

private val PanelCorner = 16.dp

private fun readerLineShape(index: Int, lastIndex: Int): RoundedCornerShape = RoundedCornerShape(
    topStart = if (index == 0) PanelCorner else 0.dp,
    topEnd = if (index == 0) PanelCorner else 0.dp,
    bottomStart = if (index == lastIndex) PanelCorner else 0.dp,
    bottomEnd = if (index == lastIndex) PanelCorner else 0.dp
)
