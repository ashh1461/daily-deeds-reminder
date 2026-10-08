package com.dailydeeds.reminder.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

/** Shared top bar of the five tabs: the title plus Search, Favorites and Settings. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onSearch: () -> Unit,
    onFavorites: () -> Unit,
    onSettings: () -> Unit
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) },
        actions = {
            IconButton(onClick = onSearch) { Icon(Icons.Default.Search, contentDescription = "بحث") }
            IconButton(onClick = onFavorites) { Icon(Icons.Default.Star, contentDescription = "المفضلة") }
            IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, contentDescription = "الإعدادات") }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}
