package com.dailydeeds.reminder.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailydeeds.reminder.data.DeedsRepository
import com.dailydeeds.reminder.ui.components.CircularProgressBar
import com.dailydeeds.reminder.ui.components.DeedCard
import com.dailydeeds.reminder.ui.components.FilterChipRow
import com.dailydeeds.reminder.ui.theme.GoldDark
import com.dailydeeds.reminder.ui.theme.GoldPrimary
import com.dailydeeds.reminder.ui.theme.NavyDark
import com.dailydeeds.reminder.ui.theme.NavyPrimary
import com.dailydeeds.reminder.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToDeed: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val completedMap by viewModel.completedMap.collectAsState()
    val countsMap by viewModel.countsMap.collectAsState()
    val dailyProgress by viewModel.dailyProgress.collectAsState()

    val filteredDeeds = DeedsRepository.getDeedsByCategory(selectedCategory)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "الأعمال اليومية",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = "توصية بخط يد السيد الأسمى (رض)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = GoldPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetToday() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "إعادة ضبط اليوم",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "الإعدادات",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavyPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(NavyPrimary, NavyDark)
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.2.dp,
                                color = GoldPrimary.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(22.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "إنجاز الأوراد اليومية",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (dailyProgress.first == dailyProgress.second && dailyProgress.second > 0) {
                                            "ما شاء الله! أتممت جميع الأوراد المباركة لليوم 🌿"
                                        } else {
                                            "أنجزت ${dailyProgress.first} من أصل ${dailyProgress.second} عملاً"
                                        },
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                CircularProgressBar(
                                    completed = dailyProgress.first,
                                    total = dailyProgress.second,
                                    size = 82.dp,
                                    strokeWidth = 9.dp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldPrimary.copy(alpha = 0.08f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "«أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ» • دُمْ عَلَى وِرْدِكَ تَظْفَرْ بِالْبَرَكَةِ",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldDark
                                    ),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                FilterChipRow(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(filteredDeeds, key = { it.id }) { deed ->
                val isCompleted = completedMap[deed.id] ?: false
                val currentCount = countsMap[deed.id] ?: 0

                DeedCard(
                    deed = deed,
                    isCompleted = isCompleted,
                    currentCount = currentCount,
                    onClick = { onNavigateToDeed(deed.id) },
                    onToggleCompleted = { viewModel.toggleDeedCompleted(deed.id) }
                )
            }
        }
    }
}
