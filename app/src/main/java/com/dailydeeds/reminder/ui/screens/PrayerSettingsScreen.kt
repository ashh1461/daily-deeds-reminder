package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.adhan.AdhanGlobalSettings
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.CalcMethod
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.adhan.PrayerSettings
import com.dailydeeds.reminder.adhan.VoiceIds
import com.dailydeeds.reminder.service.AdhanService
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.VoiceViewModel
import java.util.Locale

/** Calculation method, per-prayer alarm mode, pre-adhan, voice and manual offsets. */
@Composable
fun PrayerSettingsScreen(
    tools: ToolsViewModel,
    voices: VoiceViewModel,
    onNavigateBack: () -> Unit,
    onOpenVoices: () -> Unit
) {
    val settings by tools.prayerSettings.collectAsState()
    val configs by tools.alarmConfigs.collectAsState()
    val global by tools.adhanGlobal.collectAsState()
    val installed by voices.installed.collectAsState()
    val context = LocalContext.current

    ToolScaffold("إعدادات الأذان وطريقة الحساب", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionHealthCard()

            Heading("طريقة الحساب")
            SettingsBox {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    CalcMethod.values().forEach { m ->
                        FilterChip(
                            selected = settings.method == m,
                            onClick = { tools.setPrayerSettings(settings.copy(method = m)) },
                            label = { Text(m.labelArabic) }
                        )
                    }
                }
                val p = settings.params()
                Text(
                    String.format(Locale.US, "الفجر %.1f° • المغرب %.1f° • العشاء %.1f°", p.fajrAngle, p.maghribAngle, p.ishaAngle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (settings.method == CalcMethod.CUSTOM) {
                    Stepper("زاوية الفجر", String.format(Locale.US, "%.1f°", settings.customFajr),
                        { tools.setPrayerSettings(settings.copy(customFajr = (settings.customFajr - 0.5).coerceAtLeast(10.0))) },
                        { tools.setPrayerSettings(settings.copy(customFajr = (settings.customFajr + 0.5).coerceAtMost(22.0))) })
                    Stepper("زاوية العشاء", String.format(Locale.US, "%.1f°", settings.customIsha),
                        { tools.setPrayerSettings(settings.copy(customIsha = (settings.customIsha - 0.5).coerceAtLeast(10.0))) },
                        { tools.setPrayerSettings(settings.copy(customIsha = (settings.customIsha + 0.5).coerceAtMost(20.0))) })
                    Stepper("زاوية المغرب", String.format(Locale.US, "%.1f°", settings.customMaghrib),
                        { tools.setPrayerSettings(settings.copy(customMaghrib = (settings.customMaghrib - 0.5).coerceAtLeast(0.0))) },
                        { tools.setPrayerSettings(settings.copy(customMaghrib = (settings.customMaghrib + 0.5).coerceAtMost(8.0))) })
                }
                Stepper("تأخير المغرب بعد الغروب", if (settings.maghribDelayMinutes == 0) "بالزاوية" else "${settings.maghribDelayMinutes} د",
                    { tools.setPrayerSettings(settings.copy(maghribDelayMinutes = (settings.maghribDelayMinutes - 1).coerceAtLeast(0))) },
                    { tools.setPrayerSettings(settings.copy(maghribDelayMinutes = (settings.maghribDelayMinutes + 1).coerceAtMost(30))) })
                Stepper("الإمساك قبل الفجر", "${settings.imsakMinutes} د",
                    { tools.setPrayerSettings(settings.copy(imsakMinutes = (settings.imsakMinutes - 1).coerceAtLeast(0))) },
                    { tools.setPrayerSettings(settings.copy(imsakMinutes = (settings.imsakMinutes + 1).coerceAtMost(30))) })
            }

            Heading("تنبيه كل صلاة")
            Prayer.values().forEach { prayer ->
                val config = configs[prayer] ?: PrayerAlarmConfig()
                SettingsBox {
                    Text(prayer.nameArabic, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        AdhanMode.values().forEach { mode ->
                            FilterChip(
                                selected = config.mode == mode,
                                onClick = { tools.setAlarmConfig(prayer, config.copy(mode = mode)) },
                                label = { Text(mode.labelArabic) }
                            )
                        }
                    }
                    if (config.mode != AdhanMode.OFF && config.mode != AdhanMode.SILENT) {
                        Text("تنبيه قبل الصلاة", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                            PrayerAlarmConfig.PRE_CHOICES.forEach { minutes ->
                                FilterChip(
                                    selected = config.preMinutes == minutes,
                                    onClick = { tools.setAlarmConfig(prayer, config.copy(preMinutes = minutes)) },
                                    label = { Text(if (minutes == 0) "بلا" else "$minutes د") }
                                )
                            }
                        }
                    }
                    if (config.mode == AdhanMode.ADHAN) {
                        var menu by remember { mutableStateOf(false) }
                        val currentName = if (config.voiceId == VoiceIds.DEFAULT) "نغمة الهاتف الافتراضية"
                        else installed.firstOrNull { it.id == config.voiceId }?.nameAr ?: "صوت غير مثبّت (سيُستخدم الافتراضي)"
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) {
                                OutlinedButton(onClick = { menu = true }, modifier = Modifier.fillMaxWidth()) { Text("الصوت: $currentName", maxLines = 1) }
                                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                                    DropdownMenuItem(text = { Text("نغمة الهاتف الافتراضية") }, onClick = {
                                        tools.setAlarmConfig(prayer, config.copy(voiceId = VoiceIds.DEFAULT)); menu = false
                                    })
                                    installed.forEach { v ->
                                        DropdownMenuItem(text = { Text(v.nameAr) }, onClick = {
                                            tools.setAlarmConfig(prayer, config.copy(voiceId = v.id)); menu = false
                                        })
                                    }
                                }
                            }
                            OutlinedButton(onClick = { AdhanService.play(context, prayer, config.voiceId, test = true) }) { Text("تجربة") }
                        }
                    }
                    Stepper(
                        "تعديل الوقت يدوياً",
                        if (config.offsetMinutes > 0) "+${config.offsetMinutes} د" else "${config.offsetMinutes} د",
                        { tools.setAlarmConfig(prayer, config.copy(offsetMinutes = (config.offsetMinutes - 1).coerceAtLeast(-PrayerAlarmConfig.MAX_OFFSET))) },
                        { tools.setAlarmConfig(prayer, config.copy(offsetMinutes = (config.offsetMinutes + 1).coerceAtMost(PrayerAlarmConfig.MAX_OFFSET))) }
                    )
                }
            }

            Heading("عام")
            SettingsBox {
                SwitchRow("الاهتزاز مع الأذان", global.vibrate) { tools.setAdhanGlobal(global.copy(vibrate = it)) }
                SwitchRow("عدم تشغيل الصوت في الوضع الصامت", global.followRinger) { tools.setAdhanGlobal(global.copy(followRinger = it)) }
                Stepper("مدة التأجيل", "${global.snoozeMinutes} د",
                    { tools.setAdhanGlobal(global.copy(snoozeMinutes = (global.snoozeMinutes - 1).coerceAtLeast(1))) },
                    { tools.setAdhanGlobal(global.copy(snoozeMinutes = (global.snoozeMinutes + 1).coerceAtMost(30))) })
            }

            Button(onClick = onOpenVoices, modifier = Modifier.fillMaxWidth()) { Text("أصوات الأذان وتنزيلها") }
            Text(
                "عند وضع «أذان» يُشغَّل الصوت المختار عبر قناة المنبّه. إن لم يُثبَّت صوت يُستخدم صوت الإشعار الافتراضي في هاتفك.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Heading(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun SettingsBox(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun Stepper(label: String, value: String, onMinus: () -> Unit, onPlus: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(onClick = onMinus) { Text("−") }
        Text(value, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
        OutlinedButton(onClick = onPlus) { Text("+") }
    }
}
