package com.dailydeeds.reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.ui.screens.GlobalSearchScreen
import com.dailydeeds.reminder.ui.screens.HomeScreen
import com.dailydeeds.reminder.ui.screens.MafatihReaderScreen
import com.dailydeeds.reminder.ui.screens.MafatihScreen
import com.dailydeeds.reminder.ui.screens.QuranReaderScreen
import com.dailydeeds.reminder.ui.screens.QuranScreen
import com.dailydeeds.reminder.ui.screens.ReaderCounterScreen
import com.dailydeeds.reminder.ui.screens.SettingsScreen
import com.dailydeeds.reminder.ui.screens.WeekdayContentScreen
import com.dailydeeds.reminder.ui.theme.DailyReminderTheme
import com.dailydeeds.reminder.viewmodel.MafatihViewModel
import com.dailydeeds.reminder.viewmodel.MainViewModel
import com.dailydeeds.reminder.viewmodel.QuranViewModel
import com.dailydeeds.reminder.viewmodel.SearchViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                AlarmScheduler.scheduleAllReminders(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)
        intent.getStringExtra(NotificationHelper.EXTRA_CATEGORY)?.let { name ->
            DeedCategory.values().find { it.name == name }?.let(viewModel::selectCategory)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                AlarmScheduler.scheduleAllReminders(this)
            }
        } else {
            AlarmScheduler.scheduleAllReminders(this)
        }

        setContent {
            DailyReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDailyState()
    }
}

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomTabs = listOf(
    BottomTab("home", "الأعمال", Icons.Default.Checklist),
    BottomTab("quran", "القرآن", Icons.Default.AutoStories),
    BottomTab("mafatih", "المفاتيح", Icons.Default.Mosque),
    BottomTab("search", "البحث", Icons.Default.Search),
    BottomTab("settings", "الإعدادات", Icons.Default.Settings)
)

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    quranViewModel: QuranViewModel = viewModel(),
    mafatihViewModel: MafatihViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = bottomTabs.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = mainViewModel,
                    onNavigateToDeed = { deedId ->
                        navController.navigate("deed/$deedId")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }

            composable("quran") {
                QuranScreen(
                    viewModel = quranViewModel,
                    onNavigateToSurah = { surahNumber ->
                        navController.navigate("quran/reader/$surahNumber")
                    }
                )
            }

            composable(
                route = "quran/reader/{surahNumber}",
                arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
            ) { entry ->
                val surahNumber = entry.arguments?.getInt("surahNumber") ?: 1
                quranViewModel.selectSurah(surahNumber)
                QuranReaderScreen(
                    surahNumber = surahNumber,
                    viewModel = quranViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("mafatih") {
                MafatihScreen(
                    viewModel = mafatihViewModel,
                    onNavigateToItem = { itemId ->
                        navController.navigate("mafatih/reader/$itemId")
                    }
                )
            }

            composable(
                route = "mafatih/reader/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { entry ->
                val itemId = entry.arguments?.getString("itemId") ?: ""
                mafatihViewModel.selectItem(itemId)
                MafatihReaderScreen(
                    itemId = itemId,
                    viewModel = mafatihViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("search") {
                GlobalSearchScreen(
                    viewModel = searchViewModel,
                    onNavigateToQuran = { surahNum, _ ->
                        quranViewModel.selectSurah(surahNum)
                        navController.navigate("quran/reader/$surahNum")
                    },
                    onNavigateToMafatih = { itemId ->
                        mafatihViewModel.selectItem(itemId)
                        navController.navigate("mafatih/reader/$itemId")
                    }
                )
            }

            composable("duas") { WeekdayContentScreen(DayContentKind.DUA) }

            composable("ziyarat") { WeekdayContentScreen(DayContentKind.ZIYARAT) }

            composable(
                route = "deed/{deedId}",
                arguments = listOf(navArgument("deedId") { type = NavType.IntType })
            ) { entry ->
                val deedId = entry.arguments?.getInt("deedId") ?: 1
                ReaderCounterScreen(
                    deedId = deedId,
                    viewModel = mainViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("settings") {
                SettingsScreen(
                    viewModel = mainViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
