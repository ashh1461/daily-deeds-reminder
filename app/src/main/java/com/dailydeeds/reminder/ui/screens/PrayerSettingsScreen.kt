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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.adhan.AdhanGlobalSettings
import com.dailydeeds.reminder.adhan.AdhanMode
import com.dailydeeds.reminder.adhan.AdhanVoiceChoice
import com.dailydeeds.reminder.adhan.BundledVoices
import com.dailydeeds.reminder.adhan.CalcMethod
import com.dailydeeds.reminder.adhan.PrayerAlarmConfig
import com.dailydeeds.reminder.adhan.PrayerSettings
import com.dailydeeds.reminder.adhan.VoiceIds
import com.dailydeeds.reminder.adhan.VoiceStore
import com.dailydeeds.reminder.service.AdhanService
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.util.Prayer
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.VoiceViewModel
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.labelRes

/** Calculation method, per-prayer alarm mode, pre-adhan, voice and manual offsets. */
@Composable
fun PrayerSettingsScreen(
    tools: ToolsViewModel,
    voices: VoiceViewModel,
    onNavigateBack: () -> Unit,
    onOpenVoices: () -> Unit
) {
    val place by tools.place.collectAsState()
    val settings by tools.prayerSettings.collectAsState()
    val configs by tools.alarmConfigs.collectAsState()
    val global by tools.adhanGlobal.collectAsState()
    val installed by voices.installed.collectAsState()
    val context = LocalContext.current

    ToolScaffold(stringResource(R.string.prayer_settings_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionHealthCard()

            Heading(stringResource(R.string.prayer_heading_place))
            PlacePickerCard(place, onPick = tools::setPlace)

            Heading(stringResource(R.string.prayer_heading_method))
            SettingsBox {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    CalcMethod.values().forEach { m ->
                        FilterChip(
                            selected = settings.method == m,
                            onClick = { tools.setPrayerSettings(settings.copy(method = m)) },
                            label = { Text(stringResource(m.labelRes)) }
                        )
                    }
                }
                val p = settings.params()
                Text(
                    stringResource(
                        R.string.prayer_angles, "%.1f".format(Locale.US, p.fajrAngle),
                        "%.1f".format(Locale.US, p.maghribAngle), "%.1f".format(Locale.US, p.ishaAngle)
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (settings.method == CalcMethod.CUSTOM) {
                    Stepper(stringResource(R.string.prayer_angle_fajr), String.format(Locale.US, "%.1f°", settings.customFajr),
                        { tools.setPrayerSettings(settings.copy(customFajr = (settings.customFajr - 0.5).coerceAtLeast(10.0))) },
                        { tools.setPrayerSettings(settings.copy(customFajr = (settings.customFajr + 0.5).coerceAtMost(22.0))) })
                    Stepper(stringResource(R.string.prayer_angle_isha), String.format(Locale.US, "%.1f°", settings.customIsha),
                        { tools.setPrayerSettings(settings.copy(customIsha = (settings.customIsha - 0.5).coerceAtLeast(10.0))) },
                        { tools.setPrayerSettings(settings.copy(customIsha = (settings.customIsha + 0.5).coerceAtMost(20.0))) })
                    Stepper(stringResource(R.string.prayer_angle_maghrib), String.format(Locale.US, "%.1f°", settings.customMaghrib),
                        { tools.setPrayerSettings(settings.copy(customMaghrib = (settings.customMaghrib - 0.5).coerceAtLeast(0.0))) },
                        { tools.setPrayerSettings(settings.copy(customMaghrib = (settings.customMaghrib + 0.5).coerceAtMost(8.0))) })
                }
                Stepper(
                    stringResource(R.string.prayer_maghrib_delay),
                    if (settings.maghribDelayMinutes == 0) stringResource(R.string.prayer_by_angle)
                    else stringResource(R.string.unit_minutes_short, settings.maghribDelayMinutes.toString()),
                    { tools.setPrayerSettings(settings.copy(maghribDelayMinutes = (settings.maghribDelayMinutes - 1).coerceAtLeast(0))) },
                    { tools.setPrayerSettings(settings.copy(maghribDelayMinutes = (settings.maghribDelayMinutes + 1).coerceAtMost(30))) })
                Stepper(stringResource(R.string.prayer_imsak_before_fajr), stringResource(R.string.unit_minutes_short, settings.imsakMinutes.toString()),
                    { tools.setPrayerSettings(settings.copy(imsakMinutes = (settings.imsakMinutes - 1).coerceAtLeast(0))) },
                    { tools.setPrayerSettings(settings.copy(imsakMinutes = (settings.imsakMinutes + 1).coerceAtMost(30))) })
            }

            Heading(stringResource(R.string.prayer_heading_alerts))
            SettingsBox {
                Text(stringResource(R.string.voice_all_prayers), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                var allMenu by remember { mutableStateOf(false) }
                val commonVoice = AdhanVoiceChoice.commonVoiceId(configs, AdhanVoiceChoice.ALARM_PRAYERS)
                val allName = commonVoice?.let { voiceLabel(it, installed) } ?: stringResource(R.string.voice_varies)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) {
                        OutlinedButton(onClick = { allMenu = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.voice_current, allName), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        VoiceMenu(allMenu, commonVoice, installed, onDismiss = { allMenu = false }) { id ->
                            val updated = AdhanVoiceChoice.withVoiceForAll(configs, AdhanVoiceChoice.ALARM_PRAYERS, id)
                            AdhanVoiceChoice.ALARM_PRAYERS.forEach { p ->
                                val next = updated.getValue(p)
                                if (next != configs[p]) tools.setAlarmConfig(p, next)
                            }
                        }
                    }
                    OutlinedButton(onClick = {
                        val testVoice = commonVoice
                            ?: (configs[AdhanVoiceChoice.ALARM_PRAYERS.first()] ?: PrayerAlarmConfig()).voiceId
                        AdhanService.play(context, Prayer.FAJR, testVoice, test = true)
                    }) { Text(stringResource(R.string.voice_try)) }
                }
                Text(
                    stringResource(R.string.voice_mode_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AdhanVoiceChoice.ALARM_PRAYERS.forEach { prayer ->
                val config = configs[prayer] ?: PrayerAlarmConfig()
                SettingsBox {
                    Text(stringResource(prayer.labelRes), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                        AdhanMode.values().forEach { mode ->
                            FilterChip(
                                selected = config.mode == mode,
                                onClick = { tools.setAlarmConfig(prayer, config.copy(mode = mode)) },
                                label = { Text(stringResource(mode.labelRes)) }
                            )
                        }
                    }
                    if (config.mode != AdhanMode.OFF && config.mode != AdhanMode.SILENT) {
                        Text(stringResource(R.string.prayer_pre_alert), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                            PrayerAlarmConfig.PRE_CHOICES.forEach { minutes ->
                                FilterChip(
                                    selected = config.preMinutes == minutes,
                                    onClick = { tools.setAlarmConfig(prayer, config.copy(preMinutes = minutes)) },
                                    label = {
                                        Text(
                                            if (minutes == 0) stringResource(R.string.prayer_pre_none)
                                            else stringResource(R.string.unit_minutes_short, minutes.toString())
                                        )
                                    }
                                )
                            }
                        }
                    }
                    if (config.mode == AdhanMode.ADHAN) {
                        var menu by remember { mutableStateOf(false) }
                        val currentName = voiceLabel(config.voiceId, installed)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) {
                                OutlinedButton(onClick = { menu = true }, modifier = Modifier.fillMaxWidth()) {
                                    Text(stringResource(R.string.voice_current, currentName), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                VoiceMenu(menu, config.voiceId, installed, onDismiss = { menu = false }) { id ->
                                    tools.setAlarmConfig(prayer, config.copy(voiceId = id))
                                }
                            }
                            OutlinedButton(onClick = { AdhanService.play(context, prayer, config.voiceId, test = true) }) { Text(stringResource(R.string.voice_try)) }
                        }
                    }
                    Stepper(
                        stringResource(R.string.prayer_manual_offset),
                        stringResource(
                            R.string.unit_minutes_short,
                            if (config.offsetMinutes > 0) "+${config.offsetMinutes}" else config.offsetMinutes.toString()
                        ),
                        { tools.setAlarmConfig(prayer, config.copy(offsetMinutes = (config.offsetMinutes - 1).coerceAtLeast(-PrayerAlarmConfig.MAX_OFFSET))) },
                        { tools.setAlarmConfig(prayer, config.copy(offsetMinutes = (config.offsetMinutes + 1).coerceAtMost(PrayerAlarmConfig.MAX_OFFSET))) }
                    )
                }
            }

            Heading(stringResource(R.string.prayer_heading_general))
            SettingsBox {
                SwitchRow(stringResource(R.string.adhan_vibrate), global.vibrate) { tools.setAdhanGlobal(global.copy(vibrate = it)) }
                SwitchRow(stringResource(R.string.adhan_follow_ringer), global.followRinger) { tools.setAdhanGlobal(global.copy(followRinger = it)) }
                SwitchRow(stringResource(R.string.adhan_hardware_stop), global.hardwareStop) { tools.setAdhanGlobal(global.copy(hardwareStop = it)) }
                SwitchRow(stringResource(R.string.adhan_fade_in), global.fadeIn) { tools.setAdhanGlobal(global.copy(fadeIn = it)) }
                Text(
                    stringResource(R.string.adhan_stop_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Stepper(stringResource(R.string.adhan_snooze), stringResource(R.string.unit_minutes_short, global.snoozeMinutes.toString()),
                    { tools.setAdhanGlobal(global.copy(snoozeMinutes = (global.snoozeMinutes - 1).coerceAtLeast(1))) },
                    { tools.setAdhanGlobal(global.copy(snoozeMinutes = (global.snoozeMinutes + 1).coerceAtMost(30))) })
            }

            Button(onClick = onOpenVoices, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.voices_open)) }
            Text(
                stringResource(R.string.adhan_channel_note),
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

/** The name of [voiceId] as the voice buttons show it. Voice names themselves are Arabic. */
@Composable
private fun voiceLabel(voiceId: String, installed: List<VoiceStore.Installed>): String = when (voiceId) {
    VoiceIds.DEFAULT -> stringResource(R.string.voice_default_name, BundledVoices.byId(BundledVoices.DEFAULT_ID)?.nameAr.orEmpty())
    VoiceIds.SYSTEM_TONE -> stringResource(R.string.voice_phone_tone)
    else -> BundledVoices.byId(voiceId)?.nameAr
        ?: installed.firstOrNull { it.id == voiceId }?.nameAr
        ?: stringResource(R.string.voice_not_installed)
}

/**
 * The voice menu of both the all-prayers picker and each prayer's picker: the bundled voices, the phone's tone,
 * then the installed voices. The item matching [selectedId] (null = none) is marked with a check.
 */
@Composable
private fun VoiceMenu(
    expanded: Boolean,
    selectedId: String?,
    installed: List<VoiceStore.Installed>,
    onDismiss: () -> Unit,
    onPick: (String) -> Unit
) {
    val selected = selectedId?.let { AdhanVoiceChoice.effectiveVoiceId(it) }
    val entries = BundledVoices.all.map { it.id to it.nameAr } +
        listOf(VoiceIds.SYSTEM_TONE to stringResource(R.string.voice_phone_tone)) +
        installed.map { it.id to it.nameAr }
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        entries.forEach { (id, name) ->
            DropdownMenuItem(text = { Text(if (id == selected) "✓ $name" else name) }, onClick = {
                onPick(id); onDismiss()
            })
        }
    }
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
