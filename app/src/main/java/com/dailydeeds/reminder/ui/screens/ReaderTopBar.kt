package com.dailydeeds.reminder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.model.DeedType
import com.dailydeeds.reminder.model.Deed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReaderTopBar(
    deed: Deed,
    isCompleted: Boolean,
    onNavigateBack: () -> Unit,
    onToggleCompleted: () -> Unit,
    onResetCount: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = deed.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = deed.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    ),
                    maxLines = 1
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "رجوع",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = { onToggleCompleted() }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = if (isCompleted) "مكتمل (اضغط للإلغاء)" else "تحديد كـ مكتمل",
                    tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            if (deed.type != DeedType.READING) {
                IconButton(onClick = { onResetCount() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة ضبط العداد",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}
