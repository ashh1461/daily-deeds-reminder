package com.dailydeeds.reminder.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R

private data class HubEntry(@StringRes val title: Int, @StringRes val subtitle: Int, val icon: ImageVector, val route: String)

private val hubEntries = listOf(
    HubEntry(R.string.prayer_times_title, R.string.hub_prayer_desc, Icons.Default.AccessTime, "prayer"),
    HubEntry(R.string.hub_adhan_settings, R.string.hub_adhan_settings_desc, Icons.Default.AccessTime, "prayer/settings"),
    HubEntry(R.string.qibla_title, R.string.hub_qibla_desc, Icons.Default.Explore, "qibla"),
    HubEntry(R.string.settings_calendar_open, R.string.hub_calendar_desc, Icons.Default.CalendarMonth, "calendar"),
    HubEntry(R.string.lunar_title, R.string.hub_lunar_desc, Icons.Default.DateRange, "lunar_months"),
    HubEntry(R.string.tasbih_title, R.string.hub_tasbih_desc, Icons.Default.TouchApp, "tasbih"),
    HubEntry(R.string.qada_title, R.string.hub_qada_desc, Icons.Default.Checklist, "qada"),
    HubEntry(R.string.ramadan_title, R.string.hub_ramadan_desc, Icons.Default.NightsStay, "ramadan"),
    HubEntry(R.string.ayat_title, R.string.hub_ayat_desc, Icons.Default.DarkMode, "ayat"),
    HubEntry(R.string.khums_title, R.string.hub_khums_desc, Icons.Default.Calculate, "khums"),
    HubEntry(R.string.hub_mizan, R.string.hub_mizan_desc, Icons.Default.LibraryBooks, "mizan"),
    HubEntry(R.string.hub_duas, R.string.hub_duas_desc, Icons.Default.AutoStories, "duas"),
    HubEntry(R.string.hub_ziyarat, R.string.hub_ziyarat_desc, Icons.Default.Mosque, "ziyarat")
)

/** The "العبادات" tab: every worship tool in one place. */
@Composable
fun WorshipHubScreen(onOpenRoute: (String) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "health") { PermissionHealthCard() }
        hubEntries.forEach { entry ->
            item(key = entry.route) {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onOpenRoute(entry.route) },
                    shape = MaterialTheme.shapes.large,
                    border = cardBorder(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(entry.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(entry.title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                stringResource(entry.subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
