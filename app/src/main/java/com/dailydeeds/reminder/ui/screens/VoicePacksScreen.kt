package com.dailydeeds.reminder.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailydeeds.reminder.ui.components.cardBorder
import com.dailydeeds.reminder.viewmodel.RemoteVoicesState
import com.dailydeeds.reminder.viewmodel.VoiceViewModel
import java.util.Locale

/** Installed adhan voices, the downloadable catalogue, and importing a file from the phone. */
@Composable
fun VoicePacksScreen(voices: VoiceViewModel, onNavigateBack: () -> Unit) {
    val installed by voices.installed.collectAsState()
    val remote by voices.remote.collectAsState()
    val progress by voices.progress.collectAsState()
    val message by voices.message.collectAsState()

    LaunchedEffect(Unit) { voices.refreshCatalogue() }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) voices.importOwn(uri)
    }

    ToolScaffold("أصوات الأذان", onNavigateBack) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            message?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(it, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = voices::clearMessage) { Text("حسناً") }
                    }
                }
            }

            Title("ملف من جهازك")
            Box2 {
                Text(
                    "اختر ملف أذان تملك حق استخدامه (mp3 أو ogg أو m4a، حتى 25 ميغابايت). يُنسخ إلى مساحة التطبيق الخاصة.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = { picker.launch(arrayOf("audio/*")) }, modifier = Modifier.fillMaxWidth()) { Text("اختيار ملف صوتي") }
            }

            Title("المثبّتة")
            if (installed.isEmpty()) {
                Text("لا توجد أصوات مثبّتة بعد. يُستخدم صوت الإشعار الافتراضي في هاتفك.", style = MaterialTheme.typography.bodyMedium)
            }
            installed.forEach { v ->
                Box2 {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(v.nameAr, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(if (v.own) "ملف من جهازك" else "حزمة صوت منزّلة", style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        OutlinedButton(onClick = { voices.remove(v.id) }) { Text("حذف") }
                    }
                }
            }

            Title("أصوات للتنزيل")
            when (val state = remote) {
                RemoteVoicesState.Idle, RemoteVoicesState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                is RemoteVoicesState.Failed -> Box2 {
                    Text(state.reason, style = MaterialTheme.typography.bodyMedium)
                    OutlinedButton(onClick = voices::refreshCatalogue) { Text("إعادة المحاولة") }
                }
                is RemoteVoicesState.Loaded -> {
                    if (state.voices.isEmpty()) {
                        Text(
                            "لا توجد أصوات منشورة للتنزيل حالياً. تُضاف الأصوات هنا بعد توثيق حقوقها؛ ويمكنك في الأثناء استخدام ملف من جهازك.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    state.voices.forEach { v ->
                        val done = installed.any { it.id == v.id }
                        val p = progress[v.id]
                        Box2 {
                            Text(v.nameAr, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                "المؤذن: ${v.reciter} • ${String.format(Locale.US, "%.1f", v.sizeBytes / 1_000_000.0)} م.ب" +
                                    if (v.includesWilayah) " • يتضمن الشهادة الثالثة" else "",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text("الترخيص: ${v.licence}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            when {
                                p != null -> LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth())
                                done -> Text("مثبّت", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                else -> Button(onClick = { voices.download(v) }) { Text("تنزيل") }
                            }
                        }
                    }
                    if (state.rejected > 0) {
                        Text("${state.rejected} صوتاً في القائمة لم يُعرض لعدم اكتمال بيانات الترخيص.", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            Text(
                "التنزيل لا يتم إلا بضغطك على «تنزيل»، عبر اتصال آمن (HTTPS) من مستودع التطبيق على GitHub، ويُتحقق من بصمة الملف (SHA-256) قبل تثبيته.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Title(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun Box2(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = cardBorder(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { content() }
    }
}
