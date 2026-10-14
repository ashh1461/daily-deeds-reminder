package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.viewmodel.SahifaViewModel

/** The "الصحيفة" tab: al-Sahifa al-Sajjadiyya al-Kamila only, grouped, with its own search. */
@Composable
fun SahifaScreen(viewModel: SahifaViewModel, onOpenItem: (String) -> Unit) {
    val query by viewModel.query.collectAsState()
    val items by viewModel.items.collectAsState()
    val rows = remember(items) { buildRows(items) }

    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)) {
        SearchField(query, "ابحث في الصحيفة السجادية...", viewModel::onQueryChanged)
        Spacer(Modifier.height(10.dp))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            mafatihRows(rows, onOpenItem)
            item(key = "end") { Spacer(Modifier.height(16.dp)) }
        }
    }
}
