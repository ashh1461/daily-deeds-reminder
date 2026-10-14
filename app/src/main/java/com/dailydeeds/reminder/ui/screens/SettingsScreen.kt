package com.dailydeeds.reminder.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import java.time.LocalDate
import java.util.Locale

/** All settings in one grouped screen: prayer, calendar, deed reminders, appearance and credits. */
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    tools: ToolsViewModel,
    worship: WorshipViewModel,
    onNavigateBack: () -> Unit,
    onOpenRoute: (String) -> Unit
) {
    val reminders by viewModel.reminders.collectAsState()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsState()
    val themeMode by tools.themeMode.collectAsState()
    val place by tools.place.collectAsState()
    val backup by worship.backup.collectAsState()
    val context = LocalContext.current
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) worship.exportBackup(uri)
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) worship.importBackup(uri)
    }

    if (backup?.restartNeeded == true) {
        AlertDialog(
            onDismissRequest = { worship.clearBackupStatus() },
            title = { Text("تم الاستيراد") },
            text = { Text("أعد تشغيل التطبيق لتظهر البيانات المستوردة.") },
            confirmButton = { Button(onClick = { restartApp(context) }) { Text("إعادة التشغيل الآن") } },
            dismissButton = { TextButton(onClick = { worship.clearBackupStatus() }) { Text("لاحقاً") } }
        )
    }

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
                    Text("أوقات الصلاة")
                }
                OutlinedButton(onClick = { onOpenRoute("prayer/settings") }, modifier = Modifier.fillMaxWidth()) {
                    Text("الأذان وطريقة الحساب")
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

            SectionTitle("الودجت")
            SettingsCard {
                Text(
                    "أضف ودجت «الصلاة القادمة» إلى شاشتك الرئيسية: اضغط مطولاً على مساحة فارغة، اختر الأدوات (Widgets)، ثم «الأعمال اليومية».",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            SectionTitle("البيانات والنسخ الاحتياطي")
            SettingsCard {
                Text(
                    "صدّر المفضلة وأماكن القراءة وسجل الأعمال والمسبحة والقضاء وإعدادات الصلاة والتنبيهات إلى ملف، ثم استوردها على جهاز آخر. " +
                        "لا تُرسَل البيانات إلى أي خادم، ولا تشمل النسخة ملفات الأصوات.",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedButton(
                    onClick = { exportLauncher.launch("daily-deeds-backup-${LocalDate.now()}.json") },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("تصدير نسخة احتياطية") }
                OutlinedButton(onClick = { importLauncher.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth()) {
                    Text("استيراد نسخة احتياطية")
                }
                backup?.let {
                    Text(
                        it.message, style = MaterialTheme.typography.bodySmall,
                        color = if (it.ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            SectionTitle("حول التطبيق")
            SettingsCard {
                Text(
                    "يضم التطبيق الأعمال اليومية، والقرآن الكريم كاملاً، وتفسير الميزان كاملاً، ومفاتيح الجنان كاملاً، والصحيفة السجادية الكاملة، وأوقات الصلاة والقبلة والتقويم.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "نص القرآن الكريم: مشروع تنزيل (tanzil.net) برواية حفص بالرسم العثماني. نصوص مفاتيح الجنان والصحيفة السجادية وتفسير الميزان: مدونة OpenITI (رخصة CC BY-NC-SA 4.0) المأخوذة عن المكتبة الشاملة ومكتبات رقمية، وقد قُسِّم الميزان بحسب الآيات دون حذف حرف منه؛ والتطبيق مجاني وغير تجاري. الخطوط: أميري وتجوال (رخصة SIL OFL).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun restartApp(context: Context) {
    val launch = context.packageManager.getLaunchIntentForPackage(context.packageName)?.component ?: return
    context.startActivity(Intent.makeRestartActivityTask(launch))
    Runtime.getRuntime().exit(0)
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
