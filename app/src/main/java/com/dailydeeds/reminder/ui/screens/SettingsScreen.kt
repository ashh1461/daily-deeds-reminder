package com.dailydeeds.reminder.ui.screens

import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.model.ReminderSettings
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MainViewModel, onNavigateBack: () -> Unit) {
    val reminders by viewModel.reminders.collectAsState()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("التنبيهات والإعدادات") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("مواعيد التنبيهات", style = MaterialTheme.typography.titleLarge)
            Text(
                "أوقات الليل والنوم وصباح الخميس أوقات تذكير مقترحة قابلة للتعديل، وليست ساعات محددة في النصوص. جميع الأوقات بحسب توقيت الجهاز.",
                style = MaterialTheme.typography.bodyMedium
            )
            ReminderType.values().forEach { type ->
                val settings = reminders[type] ?: ReminderSettings(true, type.defaultHour, 0)
                ReminderSettingCard(type, settings) { viewModel.setReminder(type, it) }
            }
            Text(
                "الزيارة المختصرة ودعاء استيداع المستقبل متاحان في قسم «في أي وقت»، وليس لهما موعد محدد.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("التفاعل", style = MaterialTheme.typography.titleLarge)
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("الاهتزاز أثناء العد", Modifier.weight(1f))
                        Switch(checked = hapticsEnabled, onCheckedChange = viewModel::toggleHaptics)
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("الأصوات التفاعلية", Modifier.weight(1f))
                        Switch(checked = soundEnabled, onCheckedChange = viewModel::toggleSound)
                    }
                }
            }
            Card {
                Text(
                    "يضم التطبيق الأعمال الإحدى عشرة الأصلية، وخمس إضافات. تظهر قراءات الخميس ضمن أعمال اليوم يوم الخميس فقط، ويمكن فتحها دائماً من قسم «صباح الخميس».",
                    Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium
                )
            }
            Card {
                Text(
                    "نص القرآن الكريم: مشروع تنزيل (tanzil.net) برواية حفص بالرسم العثماني. نص مفاتيح الجنان والصحيفة السجادية: مدونة OpenITI (رخصة MIT) المأخوذة عن المكتبة الشاملة. الخطوط: أميري وتجوال (رخصة SIL OFL).",
                    Modifier.padding(16.dp), style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedButton(onClick = { showResetDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text("إعادة تصفير إنجاز اليوم")
            }
        }
    }
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("تأكيد إعادة التصفير") },
            text = { Text("هل تود تصفير تقدم الأعمال والعدادات لليوم؟") },
            confirmButton = {
                TextButton(onClick = { viewModel.resetToday(); showResetDialog = false }) { Text("نعم، صفّر الإنجاز") }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("إلغاء") }
            }
        )
    }
}

@Composable
private fun ReminderSettingCard(
    type: ReminderType,
    settings: ReminderSettings,
    onChange: (ReminderSettings) -> Unit
) {
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
}
