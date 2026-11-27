package com.dailydeeds.reminder.ui.screens

import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.collectAsState
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.viewmodel.ToolsViewModel
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.data.WeekdayRepository
import com.dailydeeds.reminder.model.DayContentKind
import java.time.DayOfWeek
import java.time.LocalDate
import androidx.compose.ui.res.stringResource
import com.dailydeeds.reminder.R
import com.dailydeeds.reminder.ui.theme.RtlContent
import com.dailydeeds.reminder.calendar.CalendarNames
import com.dailydeeds.reminder.ui.appLanguage

/**
 * شاشة الأدعية أو الزيارات الأسبوعية: شريط أيام الأسبوع (مع تمييز اليوم الحالي)
 * وبطاقة قراءة كبيرة مع نسخ النص وتغيير حجم الخط.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekdayContentScreen(
    kind: DayContentKind,
    tools: ToolsViewModel,
    onNavigateBack: (() -> Unit)? = null,
    initialDay: DayOfWeek? = null
) {
    val today = remember(kind) { LocalDate.now().dayOfWeek }
    // Opened from a favourite or a search result, the screen starts on that item's day rather than today.
    var selectedName by rememberSaveable(kind.name, initialDay?.name) { mutableStateOf((initialDay ?: today).name) }
    var fontSize by rememberSaveable(kind.name + "_font") { mutableIntStateOf(21) }
    val selectedDay = DayOfWeek.valueOf(selectedName)
    val content = WeekdayRepository.get(kind, selectedDay)
    val favorites by tools.favorites.collectAsState()
    val favKey = FavoriteKey.Weekday(kind, selectedDay)

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.common_back))
                        }
                    }
                },
                title = {
                    Column {
                        Text(
                            text = kind.titleArabic,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.weekday_source),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { tools.toggleFavorite(favKey) }) {
                        Icon(
                            if (favKey in favorites) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = stringResource(R.string.action_favorites),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { if (fontSize > 16) fontSize -= 2 }) {
                        Icon(
                            Icons.Default.TextDecrease,
                            contentDescription = stringResource(R.string.font_smaller),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { if (fontSize < 34) fontSize += 2 }) {
                        Icon(
                            Icons.Default.TextIncrease,
                            contentDescription = stringResource(R.string.font_larger),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        clipboard.setText(AnnotatedString("${content.title}\n\n${content.text}"))
                        Toast.makeText(context, context.getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = stringResource(R.string.copy_text),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding -> RtlContent {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DaySelectorRow(
                selected = selectedDay,
                today = today,
                onSelect = { selectedName = it.name }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                            RoundedCornerShape(22.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(vertical = 12.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "۞ ${content.title} ۞",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = content.honoree,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        Text(
                            text = content.text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = fontSize.sp,
                                lineHeight = (fontSize * 1.9f).sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
}

@Composable
private fun DaySelectorRow(
    selected: DayOfWeek,
    today: DayOfWeek,
    onSelect: (DayOfWeek) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(WeekdayRepository.weekOrder, key = { it.name }) { day ->
            val isToday = day == today
            FilterChip(
                selected = day == selected,
                onClick = { onSelect(day) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = CalendarNames.weekday(day, appLanguage()),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (isToday) {
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.days_today),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (day == selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = day == selected,
                    borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = if (isToday) 0.9f else 0.3f),
                    selectedBorderColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

