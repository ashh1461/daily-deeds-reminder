package com.dailydeeds.reminder.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import java.time.ZonedDateTime

/** The current time, refreshed every [periodMs] so countdowns and "next prayer" never go stale. */
@Composable
fun rememberNow(periodMs: Long = 30_000L): State<ZonedDateTime> {
    val now = remember { mutableStateOf(ZonedDateTime.now()) }
    LaunchedEffect(periodMs) {
        while (true) {
            delay(periodMs)
            now.value = ZonedDateTime.now()
        }
    }
    return now
}
