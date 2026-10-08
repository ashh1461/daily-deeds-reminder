package com.dailydeeds.reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.ui.components.AppTopBar
import com.dailydeeds.reminder.ui.screens.CalendarScreen
import com.dailydeeds.reminder.ui.screens.FavoritesScreen
import com.dailydeeds.reminder.ui.screens.GlobalSearchScreen
import com.dailydeeds.reminder.ui.screens.MafatihReaderScreen
import com.dailydeeds.reminder.ui.screens.MafatihScreen
import com.dailydeeds.reminder.ui.screens.PrayerTimesScreen
import com.dailydeeds.reminder.ui.screens.QiblaScreen
import com.dailydeeds.reminder.ui.screens.QuranReaderScreen
import com.dailydeeds.reminder.ui.screens.QuranScreen
import com.dailydeeds.reminder.ui.screens.ReaderCounterScreen
import com.dailydeeds.reminder.ui.screens.SahifaScreen
import com.dailydeeds.reminder.ui.screens.SettingsScreen
import com.dailydeeds.reminder.ui.screens.TodayScreen
import com.dailydeeds.reminder.ui.screens.WeekdayContentScreen
import com.dailydeeds.reminder.ui.screens.WorshipHubScreen
import com.dailydeeds.reminder.ui.theme.DailyReminderTheme
import com.dailydeeds.reminder.viewmodel.MafatihViewModel
import com.dailydeeds.reminder.viewmodel.MainViewModel
import com.dailydeeds.reminder.viewmodel.QuranViewModel
import com.dailydeeds.reminder.viewmodel.SahifaViewModel
import com.dailydeeds.reminder.viewmodel.SearchViewModel
import com.dailydeeds.reminder.viewmodel.ToolsViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val toolsViewModel: ToolsViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            AlarmScheduler.scheduleAllReminders(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)
        intent.getStringExtra(NotificationHelper.EXTRA_CATEGORY)?.let { name ->
            DeedCategory.values().find { it.name == name }?.let(viewModel::selectCategory)
        }

        // Alarms are always scheduled; the permission prompt is shown once, not on every launch. If it was
        // declined, the permission-health card in Settings and the worship hub explains how to enable it.
        AlarmScheduler.scheduleAllReminders(this)
        val prefs = PreferencesManager(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
            !prefs.hasAskedNotificationPermission()
        ) {
            prefs.setAskedNotificationPermission()
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            val themeMode by toolsViewModel.themeMode.collectAsState()
            val dark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }
            DailyReminderTheme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel, toolsViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDailyState()
    }
}

private data class AppTab(val route: String, val label: String, val title: String, val icon: ImageVector)

/** The five bottom tabs. Search, Favorites and Settings live in the shared top bar. */
private val appTabs = listOf(
    AppTab("home", "اليوم", "اليوم", Icons.Default.Today),
    AppTab("worship", "العبادات", "العبادات", Icons.Default.Mosque),
    AppTab("quran", "القرآن", "القرآن الكريم", Icons.Default.AutoStories),
    AppTab("mafatih", "المفاتيح", "مفاتيح الجنان", Icons.Default.MenuBook),
    AppTab("sahifa", "الصحيفة", "الصحيفة السجادية", Icons.Default.Book)
)

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    toolsViewModel: ToolsViewModel,
    quranViewModel: QuranViewModel = viewModel(),
    mafatihViewModel: MafatihViewModel = viewModel(),
    sahifaViewModel: SahifaViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentTab = appTabs.firstOrNull { it.route == currentRoute }

    fun go(route: String) = navController.navigate(route)
    fun back() { navController.popBackStack() }
    fun openReader(itemId: String) {
        mafatihViewModel.selectItem(itemId)
        go("mafatih/reader/$itemId")
    }
    fun openWeekday(kind: DayContentKind) = go(if (kind == DayContentKind.DUA) "duas" else "ziyarat")

    Scaffold(
        topBar = {
            if (currentTab != null) {
                AppTopBar(
                    title = currentTab.title,
                    onSearch = { go("search") },
                    onFavorites = { go("favorites") },
                    onSettings = { go("settings") }
                )
            }
        },
        bottomBar = {
            if (currentTab != null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    appTabs.forEach { tab ->
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
            // ---- tabs
            composable("home") {
                TodayScreen(
                    viewModel = mainViewModel,
                    tools = toolsViewModel,
                    onOpenDeed = { go("deed/$it") },
                    onOpenRoute = { go(it) },
                    onResumeSurah = { go("quran/reader/$it") },
                    onResumeMafatih = { openReader(it) },
                    onResumeSahifa = { openReader(it) }
                )
            }
            composable("worship") { WorshipHubScreen(onOpenRoute = { go(it) }) }
            composable("quran") {
                QuranScreen(viewModel = quranViewModel, onNavigateToSurah = { go("quran/reader/$it") })
            }
            composable("mafatih") {
                MafatihScreen(
                    viewModel = mafatihViewModel,
                    onNavigateToItem = { go("mafatih/reader/$it") },
                    onNavigateToDuas = { go("duas") },
                    onNavigateToZiyarat = { go("ziyarat") }
                )
            }
            composable("sahifa") {
                SahifaScreen(viewModel = sahifaViewModel, onOpenItem = { openReader(it) })
            }

            // ---- readers
            composable(
                route = "quran/reader/{surahNumber}",
                arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
            ) { entry ->
                val surahNumber = entry.arguments?.getInt("surahNumber") ?: 1
                quranViewModel.selectSurah(surahNumber)
                QuranReaderScreen(
                    surahNumber = surahNumber,
                    viewModel = quranViewModel,
                    tools = toolsViewModel,
                    onNavigateBack = { back() }
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
                    tools = toolsViewModel,
                    onNavigateBack = { back() }
                )
            }
            composable(
                route = "deed/{deedId}",
                arguments = listOf(navArgument("deedId") { type = NavType.IntType })
            ) { entry ->
                ReaderCounterScreen(
                    deedId = entry.arguments?.getInt("deedId") ?: 1,
                    viewModel = mainViewModel,
                    onNavigateBack = { back() }
                )
            }

            // ---- pushed screens
            composable("search") {
                GlobalSearchScreen(
                    viewModel = searchViewModel,
                    onNavigateToQuran = { surahNum, _ ->
                        quranViewModel.selectSurah(surahNum)
                        go("quran/reader/$surahNum")
                    },
                    onNavigateToMafatih = { openReader(it) },
                    onNavigateToWeekday = { openWeekday(it) },
                    onNavigateToDeed = { go("deed/$it") },
                    onNavigateBack = { back() }
                )
            }
            composable("duas") { WeekdayContentScreen(DayContentKind.DUA, toolsViewModel, onNavigateBack = { back() }) }
            composable("ziyarat") { WeekdayContentScreen(DayContentKind.ZIYARAT, toolsViewModel, onNavigateBack = { back() }) }
            composable("calendar") { CalendarScreen(toolsViewModel, onNavigateBack = { back() }) }
            composable("prayer") {
                PrayerTimesScreen(toolsViewModel, onNavigateBack = { back() }, onOpenQibla = { go("qibla") })
            }
            composable("qibla") { QiblaScreen(toolsViewModel, onNavigateBack = { back() }) }
            composable("favorites") {
                FavoritesScreen(
                    viewModel = toolsViewModel,
                    onNavigateBack = { back() },
                    onOpenSurah = { go("quran/reader/$it") },
                    onOpenMafatih = { openReader(it) },
                    onOpenWeekday = { openWeekday(it) }
                )
            }
            composable("settings") {
                SettingsScreen(
                    viewModel = mainViewModel,
                    tools = toolsViewModel,
                    onNavigateBack = { back() },
                    onOpenRoute = { go(it) }
                )
            }
        }
    }
}
