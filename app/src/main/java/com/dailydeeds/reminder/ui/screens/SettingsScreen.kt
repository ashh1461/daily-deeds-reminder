package com.dailydeeds.reminder.ui.screens

import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.ReminderSettings
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.MainViewModel
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import java.util.Locale

/** All settings in one grouped screen: prayer, calendar, deed reminders, appearance and credits. */
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    tools: ToolsViewModel,
    onNavigateBack: () -> Unit,
    onOpenRoute: (String) -> Unit
) {
    val reminders by viewModel.reminders.collectAsState()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsState()
    val themeMode by tools.themeMode.collectAsState()
    val place by tools.place.collectAsState()

    ToolScaffold("الإعدادات", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionHealthCard()

            SectionTitle("الأذان والصلاة")
            SettingsCard {
                Text("المدينة الحالية: ${place.name}", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = { onOpenRoute("prayer") }, modifier = Modifier.fillMaxWidth()) {
                    Text("أوقات الصلاة وتنبيهات الأذان")
                }
            }

            SectionTitle("التقويم")
            SettingsCard {
                Text("تعديل رؤية الهلال وتذكير مناسبات أهل البيت (ع).", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = { onOpenRoute("calendar") }, modifier = Modifier.fillMaxWidth()) {
                    Text("التقويم والمناسبات")
                }
            }

            SectionTitle("تذكيرات الأعمال")
            Text(
                "أوقات الليل والنوم وصباح الخميس أوقات تذكير مقترحة قابلة للتعديل. جميع الأوقات بحسب توقيت الجهاز.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ReminderType.values().forEach { type ->
                val settings = reminders[type] ?: ReminderSettings(true, type.defaultHour, 0)
                ReminderSettingCard(type, settings) { viewModel.setReminder(type, it) }
            }
            Text(
                "الزيارة المختصرة ودعاء استيداع المستقبل متاحان ضمن «في أي وقت» وليس لهما موعد محدد.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SectionTitle("المظهر والتفاعل")
            SettingsCard {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf("system" to "حسب النظام", "light" to "فاتح", "dark" to "داكن").forEach { (mode, label) ->
                        FilterChip(selected = themeMode == mode, onClick = { tools.setThemeMode(mode) }, label = { Text(label) })
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("الاهتزاز أثناء العد", Modifier.weight(1f))
                    Switch(checked = hapticsEnabled, onCheckedChange = viewModel::toggleHaptics)
                }
            }

            SectionTitle("حول التطبيق")
            SettingsCard {
                Text(
                    "يضم التطبيق الأعمال اليومية، والقرآن الكريم كاملاً، ومفاتيح الجنان كاملاً، والصحيفة السجادية الكاملة، وأوقات الصلاة والقبلة والتقويم.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "نص القرآن الكريم: مشروع تنزيل (tanzil.net) برواية حفص بالرسم العثماني. نص مفاتيح الجنان والصحيفة السجادية: مدونة OpenITI (رخصة MIT) المأخوذة عن المكتبة الشاملة. الخطوط: أميري وتجوال (رخصة SIL OFL).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { content() }
    }
}

@Composable
private fun ReminderSettingCard(
    type: ReminderType,
    settings: ReminderSettings,
    onChange: (ReminderSettings) -> Unit
) {
    val context = LocalContext.current
    SettingsCard {
        Text(type.title, style = MaterialTheme.typography.titleMedium)
        Text(
            if (type.dayOfWeek != null) "كل خميس صباحاً، عند الخروج لطلب الحاجة" else "كل يوم",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                String.format(Locale.getDefault(), "%02d:%02d", settings.hour, settings.minute),
                Modifier.weight(1f), style = MaterialTheme.typography.titleMedium
            )
            OutlinedButton(onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute -> onChange(settings.copy(hour = hour, minute = minute)) },
                    settings.hour, settings.minute, DateFormat.is24HourFormat(context)
                ).show()
            }) { Text("تعديل الوقت") }
            Spacer(Modifier.width(12.dp))
            Switch(checked = settings.enabled, onCheckedChange = { onChange(settings.copy(enabled = it)) })
        }
    }
}
