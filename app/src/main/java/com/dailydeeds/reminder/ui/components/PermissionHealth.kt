package com.dailydeeds.reminder.ui.components

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

internal fun canScheduleExactAlarms(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return true
    return alarmManager.canScheduleExactAlarms()
}

/**
 * Shows what is blocking reliable reminders (notifications turned off, exact alarms not allowed) with a
 * button that opens the right system settings page. Renders nothing when everything is fine.
 */
@Composable
fun PermissionHealthCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var notificationsOk by remember { mutableStateOf(true) }
    var exactOk by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        fun refresh() {
            notificationsOk = NotificationManagerCompat.from(context).areNotificationsEnabled()
            exactOk = canScheduleExactAlarms(context)
        }
        val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_RESUME) refresh() }
        lifecycleOwner.lifecycle.addObserver(observer)
        refresh()
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (notificationsOk && exactOk) return

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("تنبيهات الأذان والأعمال تحتاج إلى أذونات", fontWeight = FontWeight.Bold)
            if (!notificationsOk) {
                Text("الإشعارات متوقفة لهذا التطبيق، فلن تصلك أي تنبيهات.", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }) { Text("فتح إعدادات الإشعارات") }
            }
            if (!exactOk && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Text(
                    "لم يُسمح بالمنبّهات الدقيقة، فقد تتأخر التنبيهات عن موعدها بدقائق. اسمح بـ «المنبّهات والتذكيرات» لهذا التطبيق.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:${context.packageName}"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }) { Text("السماح بالمنبّهات الدقيقة") }
            }
        }
    }
}
