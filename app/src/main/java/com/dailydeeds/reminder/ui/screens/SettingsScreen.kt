package com.dailydeeds.reminder.ui.screens

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.model.ReminderSettings
import com.dailydeeds.reminder.model.ReminderType
import com.dailydeeds.reminder.ui.components.PermissionHealthCard
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.util.AppLanguage
import com.dailydeeds.reminder.viewmodel.MainViewModel
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import java.time.LocalDate
import java.util.Locale
import com.dailydeeds.reminder.notification.ReminderWording
import com.dailydeeds.reminder.calendar.CalendarNames
import com.dailydeeds.reminder.ui.appLanguage

/** All settings in one grouped screen: language, prayer, calendar, deed reminders, appearance and credits. */
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
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) worship.exportBackup(uri)
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) worship.importBackup(uri)
    }

    ToolScaffold(stringResource(R.string.settings_title), onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionHealthCard()

            SectionTitle(R.string.settings_section_language)
            SettingsCard { LanguagePicker(tools) }

            SectionTitle(R.string.settings_section_prayer)
            SettingsCard {
                Text(stringResource(R.string.settings_current_city, CalendarNames.place(place, appLanguage())), style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = { onOpenRoute("prayer") }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.settings_prayer_times))
                }
                OutlinedButton(onClick = { onOpenRoute("prayer/settings") }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.settings_adhan_and_method))
                }
            }

            SectionTitle(R.string.settings_section_calendar)
            SettingsCard {
                Text(stringResource(R.string.settings_calendar_desc), style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = { onOpenRoute("calendar") }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.settings_calendar_open))
                }
            }

            SectionTitle(R.string.settings_section_reminders)
            NoteText(R.string.settings_reminders_note)
            ReminderType.values().forEach { type ->
                val settings = reminders[type] ?: ReminderSettings(true, type.defaultHour, 0)
                ReminderSettingCard(type, settings) { viewModel.setReminder(type, it) }
            }
            NoteText(R.string.settings_reminders_anytime_note)

            SectionTitle(R.string.settings_section_appearance)
            SettingsCard {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        "system" to R.string.settings_theme_system,
                        "light" to R.string.settings_theme_light,
                        "dark" to R.string.settings_theme_dark
                    ).forEach { (mode, label) ->
                        FilterChip(
                            selected = themeMode == mode,
                            onClick = { tools.setThemeMode(mode) },
                            label = { Text(stringResource(label)) }
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.settings_haptics), Modifier.weight(1f))
                    Switch(checked = hapticsEnabled, onCheckedChange = viewModel::toggleHaptics)
                }
            }

            SectionTitle(R.string.settings_section_widget)
            SettingsCard {
                Text(stringResource(R.string.settings_widget_desc), style = MaterialTheme.typography.bodyMedium)
            }

            SectionTitle(R.string.settings_section_backup)
            SettingsCard {
                Text(stringResource(R.string.settings_backup_desc), style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(
                    onClick = { exportLauncher.launch("daily-deeds-backup-${LocalDate.now()}.json") },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.settings_backup_export)) }
                OutlinedButton(onClick = { importLauncher.launch(arrayOf("*/*")) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.settings_backup_import))
                }
                backup?.let {
                    Text(
                        it.message, style = MaterialTheme.typography.bodySmall,
                        color = if (it.ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            SectionTitle(R.string.settings_section_about)
            SettingsCard {
                Text(stringResource(R.string.settings_about_desc), style = MaterialTheme.typography.bodyMedium)
                // Attribution required by the data licences, plus the provenance of the bundled adhan; keep in step with
                // docs/licenses/DATA_LICENSE.md, THIRD_PARTY_NOTICES.md and ADHAN_AUDIO.md. The licence URLs have their
                // own lines so they do not break the RTL sentence order.
                listOf(
                    R.string.settings_credit_quran,
                    R.string.settings_credit_books,
                    R.string.settings_credit_changes,
                    R.string.settings_credit_license,
                    R.string.settings_credit_license_url,
                    R.string.settings_credit_geonames,
                    R.string.settings_credit_geonames_url,
                    R.string.settings_credit_eclipses,
                    R.string.settings_credit_fonts,
                    R.string.settings_credit_adhan
                ).forEach { line ->
                    Text(stringResource(line), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * Arabic, English, Persian, or the device language. Each language is named in its own script so it can be found
 * whatever the current UI language is. Choosing one recreates the activity, which reloads in the new language.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LanguagePicker(tools: ToolsViewModel) {
    val current by tools.appLanguage.collectAsState()
    val context = LocalContext.current
    val choices = AppLanguage.entries.map { it.tag to it.nativeName } +
        (AppLanguage.SYSTEM to stringResource(R.string.settings_language_system))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        choices.forEach { (pref, label) ->
            FilterChip(
                selected = current == pref,
                onClick = {
                    if (current != pref) {
                        tools.setAppLanguage(pref)
                        context.findActivity()?.recreate()
                    }
                },
                label = { Text(label) }
            )
        }
    }
    Text(
        stringResource(R.string.settings_language_note),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
private fun SectionTitle(@StringRes text: Int) {
    Text(
        stringResource(text), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun NoteText(@StringRes text: Int) {
    Text(stringResource(text), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        Text(stringResource(ReminderWording.titleRes(type)), style = MaterialTheme.typography.titleMedium)
        Text(
            stringResource(if (type.dayOfWeek != null) R.string.settings_reminder_thursday else R.string.settings_reminder_daily),
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
            }) { Text(stringResource(R.string.settings_change_time)) }
            Spacer(Modifier.width(12.dp))
            Switch(checked = settings.enabled, onCheckedChange = { onChange(settings.copy(enabled = it)) })
        }
    }
}
