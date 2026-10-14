package com.dailydeeds.reminder.ui.screens

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
import androidx.compose.material.icons.filled.Explore
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

private data class HubEntry(val title: String, val subtitle: String, val icon: ImageVector, val route: String)

private val hubEntries = listOf(
    HubEntry("أوقات الصلاة والأذان", "المواعيد بالفقه الجعفري وتنبيهات الأذان", Icons.Default.AccessTime, "prayer"),
    HubEntry("إعدادات الأذان", "الصوت، التنبيه قبل الصلاة، طريقة الحساب", Icons.Default.AccessTime, "prayer/settings"),
    HubEntry("اتجاه القبلة", "بوصلة القبلة من مدينتك", Icons.Default.Explore, "qibla"),
    HubEntry("التقويم والمناسبات", "التاريخ الهجري ومناسبات أهل البيت (ع)", Icons.Default.CalendarMonth, "calendar"),
    HubEntry("المسبحة", "تسبيح الزهراء (ع) وعدّاد حر", Icons.Default.TouchApp, "tasbih"),
    HubEntry("القضاء", "تتبّع الصلوات والصيام الفائت", Icons.Default.Checklist, "qada"),
    HubEntry("رمضان والإمساك", "جدول الإمساك والإفطار وليالي القدر مع PDF", Icons.Default.NightsStay, "ramadan"),
    HubEntry("صلاة الآيات", "الكيفية والخسوف والكسوف القادم", Icons.Default.DarkMode, "ayat"),
    HubEntry("حاسبة الخمس", "حساب الخمس ورأس السنة الخمسية", Icons.Default.Calculate, "khums"),
    HubEntry("أدعية الأيام", "دعاء لكل يوم من أيام الأسبوع", Icons.Default.AutoStories, "duas"),
    HubEntry("زيارات الأيام", "زيارة لكل يوم من أيام الأسبوع", Icons.Default.Mosque, "ziyarat")
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
                            Text(entry.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                entry.subtitle,
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
