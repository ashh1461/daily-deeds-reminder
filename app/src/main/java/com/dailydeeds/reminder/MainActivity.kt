package com.dailydeeds.reminder

import com.dailydeeds.reminder.ui.screens.PrayerSettingsScreen
import com.dailydeeds.reminder.ui.screens.VoicePacksScreen
import com.dailydeeds.reminder.viewmodel.VoiceViewModel
import com.dailydeeds.reminder.viewmodel.WorshipViewModel
import com.dailydeeds.reminder.viewmodel.MizanViewModel
import com.dailydeeds.reminder.ui.screens.MizanAyahScreen
import com.dailydeeds.reminder.ui.screens.MizanHomeScreen
import com.dailydeeds.reminder.ui.screens.MizanReaderScreen
import com.dailydeeds.reminder.ui.screens.MizanSurahScreen
import com.dailydeeds.reminder.ui.screens.AyatScreen
import com.dailydeeds.reminder.ui.screens.KhumsScreen
import com.dailydeeds.reminder.ui.screens.QadaScreen
import com.dailydeeds.reminder.ui.screens.RamadanScreen
import com.dailydeeds.reminder.ui.screens.TasbihScreen
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.dailydeeds.reminder.service.AdhanService
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.PreferencesManager
import com.dailydeeds.reminder.data.QuranPages
import com.dailydeeds.reminder.data.QuranResume
import com.dailydeeds.reminder.model.DayContentKind
import com.dailydeeds.reminder.model.DeedCategory
import com.dailydeeds.reminder.notification.AlarmScheduler
import com.dailydeeds.reminder.notification.NotificationHelper
import com.dailydeeds.reminder.ui.components.AppTopBar
import com.dailydeeds.reminder.ui.screens.CalendarScreen
import com.dailydeeds.reminder.ui.screens.LunarMonthsScreen
import com.dailydeeds.reminder.ui.screens.FavoritesScreen
import com.dailydeeds.reminder.ui.screens.GlobalSearchScreen
import com.dailydeeds.reminder.ui.screens.MafatihReaderScreen
import com.dailydeeds.reminder.ui.screens.MafatihScreen
import com.dailydeeds.reminder.ui.screens.PrayerTimesScreen
import com.dailydeeds.reminder.ui.screens.QiblaScreen
import com.dailydeeds.reminder.ui.screens.QuranMushafScreen
import com.dailydeeds.reminder.ui.screens.QuranReaderScreen
import com.dailydeeds.reminder.ui.screens.QuranScreen
import com.dailydeeds.reminder.ui.screens.ReaderCounterScreen
import com.dailydeeds.reminder.ui.screens.SahifaScreen
import com.dailydeeds.reminder.ui.screens.SettingsScreen
import com.dailydeeds.reminder.ui.screens.TodayScreen
import com.dailydeeds.reminder.ui.screens.WeekdayContentScreen
import com.dailydeeds.reminder.ui.screens.WeekdayRoute
import java.time.DayOfWeek
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
        // Android 15+ draws every app edge to edge when it targets SDK 35+; doing it on all versions keeps one layout.
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)
        // Only on a fresh start: a recreated activity (rotation, theme change) keeps the category the user picked.
        if (savedInstanceState == null) {
            applyReminderCategory(intent)
            stopAdhanIfAsked(intent)
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

    // Reminder notifications open the activity with SINGLE_TOP, so a tap while the app is alive lands here.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        applyReminderCategory(intent)
        stopAdhanIfAsked(intent)
        // On a fresh start the app opens on Today anyway; while it is open, a tap leaves any pushed screen or tab.
        if (intent.getBooleanExtra(NotificationHelper.EXTRA_OPEN_TODAY, false)) viewModel.requestToday()
    }

    /** A tap on the playing-adhan notification opens the app with this extra: the tap also silences the adhan. */
    private fun stopAdhanIfAsked(intent: Intent) {
        if (intent.getBooleanExtra(AdhanService.EXTRA_STOP_ADHAN, false)) {
            AdhanService.stop(this)
            // Handled once: a later recreation from this intent must not stop a newer adhan.
            intent.removeExtra(AdhanService.EXTRA_STOP_ADHAN)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDailyState()
    }

    private fun applyReminderCategory(intent: Intent) {
        intent.getStringExtra(NotificationHelper.EXTRA_CATEGORY)?.let { name ->
            DeedCategory.values().find { it.name == name }?.let(viewModel::selectCategory)
        }
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
    toolsViewModel: ToolsViewModel
) {
    // MizanViewModel copies the 24 MB book the first time it is created, so it is fetched only when a Mizan screen
    // or a Mizan search needs it, not at cold start. It stays activity-scoped so all Mizan screens share it.
    val activityOwner = checkNotNull(LocalViewModelStoreOwner.current)
    fun mizan(): MizanViewModel = ViewModelProvider(activityOwner)[MizanViewModel::class.java]
    // The other screen view models are also created on first use (the Today tab needs none of them), so a cold start
    // neither loads the Quran, Mafatih and Sahifa data nor builds the search index.
    val vms = remember(activityOwner) { LazyViewModels(activityOwner) }

    val navController = rememberNavController()
    LaunchedEffect(navController) {
        mainViewModel.openToday.collect {
            navController.navigate("home") {
                popUpTo("home")
                launchSingleTop = true
            }
        }
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentTab = appTabs.firstOrNull { it.route == currentRoute }

    fun go(route: String) = navController.navigate(route)
    fun back() { navController.popBackStack() }
    fun openReader(itemId: String) {
        vms.mafatih.selectItem(itemId)
        go("mafatih/reader/$itemId")
    }
    fun openWeekday(kind: DayContentKind, day: DayOfWeek? = null) = go(WeekdayRoute.of(kind, day))

    // The Quran opens as mushaf pages or as a scrolling list of ayahs, as chosen on the Quran tab.
    val pagesMode by toolsViewModel.quranPagesMode.collectAsState()
    val lastPage by toolsViewModel.lastQuranPage.collectAsState()
    fun openSurah(surah: Int) {
        if (pagesMode) go("quran/page/${QuranPages.firstPageOfSurah(surah)}")
        else { vms.quran.selectSurah(surah); go("quran/reader/$surah") }
    }
    fun openAyah(surah: Int, ayah: Int) {
        if (pagesMode) go("quran/page/${QuranPages.pageOf(surah, ayah)}?s=$surah&a=$ayah")
        else { vms.quran.selectSurah(surah); go("quran/reader/$surah?a=$ayah") }
    }
    fun resumeSurah(surah: Int) {
        val page = toolsViewModel.lastQuranPage.value
        val ayah = QuranResume.resumeAyah(surah, toolsViewModel.lastSurah.value, toolsViewModel.lastAyah.value)
        when {
            pagesMode && page > 0 && QuranPages.page(page).showsSurah(surah) -> go("quran/page/$page")
            !pagesMode && ayah > 0 -> openAyah(surah, ayah)
            else -> openSurah(surah)
        }
    }

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
        // Edge to edge: on a tab, the top and bottom bars take the status and navigation bars, so the content only
        // keeps clear of side cutouts. A pushed screen brings its own Scaffold (which pads the navigation bar), so here
        // only the status bar is taken, which keeps the adhan banner below it. Consuming the padding stops the nested
        // Scaffolds and top bars from adding the same insets twice.
        contentWindowInsets = if (currentTab != null) {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        } else {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
        }
    ) { innerPadding ->
      // The window no longer shrinks for the keyboard when drawing edge to edge, so the content takes its height. The
      // navigation bar part is left out: a tab's bottom bar or a pushed screen's own Scaffold already pads it.
      Column(
          Modifier
              .padding(innerPadding)
              .consumeWindowInsets(innerPadding)
              .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars))
      ) {
        AdhanPlayingBanner()
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.weight(1f)
        ) {
            // ---- tabs
            composable("home") {
                TodayScreen(
                    viewModel = mainViewModel,
                    tools = toolsViewModel,
                    onOpenDeed = { go("deed/$it") },
                    onOpenRoute = { go(it) },
                    onResumeSurah = { resumeSurah(it) },
                    onResumeMafatih = { openReader(it) },
                    onResumeSahifa = { openReader(it) }
                )
            }
            composable("worship") { WorshipHubScreen(onOpenRoute = { go(it) }) }
            composable("quran") {
                QuranScreen(
                    viewModel = vms.quran,
                    pagesMode = pagesMode,
                    lastPage = lastPage,
                    onPagesModeChange = { toolsViewModel.setQuranPagesMode(it) },
                    onNavigateToSurah = { openSurah(it) },
                    onOpenPage = { go("quran/page/$it") }
                )
            }
            composable("mafatih") {
                MafatihScreen(
                    viewModel = vms.mafatih,
                    onNavigateToItem = { go("mafatih/reader/$it") },
                    onNavigateToDuas = { go("duas") },
                    onNavigateToZiyarat = { go("ziyarat") }
                )
            }
            composable("sahifa") {
                SahifaScreen(viewModel = vms.sahifa, onOpenItem = { openReader(it) })
            }

            // ---- readers
            composable(
                route = "quran/reader/{surahNumber}?a={a}",
                arguments = listOf(
                    navArgument("surahNumber") { type = NavType.IntType },
                    navArgument("a") { type = NavType.IntType; defaultValue = 0 }
                )
            ) { entry ->
                val surahNumber = entry.arguments?.getInt("surahNumber") ?: 1
                vms.quran.selectSurah(surahNumber)
                QuranReaderScreen(
                    surahNumber = surahNumber,
                    viewModel = vms.quran,
                    tools = toolsViewModel,
                    onOpenMizan = { s, a -> go("mizan/ayah/$s/$a") },
                    onNavigateBack = { back() },
                    initialAyah = entry.arguments?.getInt("a") ?: 0
                )
            }
            composable(
                route = "quran/page/{page}?s={s}&a={a}",
                arguments = listOf(
                    navArgument("page") { type = NavType.IntType },
                    navArgument("s") { type = NavType.IntType; defaultValue = 0 },
                    navArgument("a") { type = NavType.IntType; defaultValue = 0 }
                )
            ) { entry ->
                val s = entry.arguments?.getInt("s") ?: 0
                val a = entry.arguments?.getInt("a") ?: 0
                QuranMushafScreen(
                    initialPage = entry.arguments?.getInt("page") ?: 1,
                    highlight = if (s > 0 && a > 0) FavoriteKey.Ayah(s, a) else null,
                    viewModel = vms.quran,
                    tools = toolsViewModel,
                    onOpenMizan = { surah, ayah -> go("mizan/ayah/$surah/$ayah") },
                    onNavigateBack = { back() }
                )
            }
            composable(
                route = "mafatih/reader/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { entry ->
                val itemId = entry.arguments?.getString("itemId") ?: ""
                // Selected while composing so that the first frame already shows this item (the reader screens do not
                // check the id). A no-op when the item is already selected, so a rotation keeps the counter.
                vms.mafatih.selectItem(itemId)
                MafatihReaderScreen(
                    itemId = itemId,
                    viewModel = vms.mafatih,
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
                    viewModel = vms.search,
                    onNavigateToQuran = { surahNum, ayahNum ->
                        if (ayahNum != null && ayahNum > 0) openAyah(surahNum, ayahNum) else openSurah(surahNum)
                    },
                    onNavigateToMafatih = { openReader(it) },
                    onNavigateToWeekday = { kind, day -> openWeekday(kind, day) },
                    onNavigateToDeed = { go("deed/$it") },
                    onSearchMizan = { query ->
                        mizan().search(query)
                        go("mizan")
                    },
                    onNavigateBack = { back() }
                )
            }
            composable("mizan") {
                MizanHomeScreen(
                    viewModel = mizan(),
                    onOpenSurah = { go("mizan/surah/$it") },
                    onOpenEntry = { id, paragraph -> go("mizan/read/$id?p=$paragraph") },
                    onNavigateBack = { back() }
                )
            }
            composable(
                route = "mizan/surah/{surah}",
                arguments = listOf(navArgument("surah") { type = NavType.IntType })
            ) { entry ->
                MizanSurahScreen(
                    surah = entry.arguments?.getInt("surah") ?: 1,
                    viewModel = mizan(),
                    onOpenEntry = { id, paragraph -> go("mizan/read/$id?p=$paragraph") },
                    onNavigateBack = { back() }
                )
            }
            composable(
                route = "mizan/ayah/{surah}/{ayah}",
                arguments = listOf(
                    navArgument("surah") { type = NavType.IntType },
                    navArgument("ayah") { type = NavType.IntType }
                )
            ) { entry ->
                val surah = entry.arguments?.getInt("surah") ?: 1
                val ayah = entry.arguments?.getInt("ayah") ?: 1
                MizanAyahScreen(
                    surah = surah,
                    ayah = ayah,
                    viewModel = mizan(),
                    onResolved = { id ->
                        navController.navigate("mizan/read/$id?p=0") {
                            popUpTo("mizan/ayah/{surah}/{ayah}") { inclusive = true }
                        }
                    },
                    onNavigateBack = { back() }
                )
            }
            composable(
                route = "mizan/read/{id}?p={p}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("p") { type = NavType.IntType; defaultValue = 0 }
                )
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                MizanReaderScreen(
                    entryId = id,
                    paragraph = entry.arguments?.getInt("p") ?: 0,
                    viewModel = mizan(),
                    onOpenEntry = { target ->
                        navController.navigate("mizan/read/$target?p=0") {
                            popUpTo("mizan/read/{id}?p={p}") { inclusive = true }
                        }
                    },
                    onNavigateBack = { back() }
                )
            }
            DayContentKind.values().forEach { kind ->
                composable(
                    route = WeekdayRoute.pattern(kind),
                    arguments = listOf(
                        navArgument(WeekdayRoute.DAY_ARG) { type = NavType.StringType; nullable = true; defaultValue = null }
                    )
                ) { entry ->
                    WeekdayContentScreen(
                        kind,
                        toolsViewModel,
                        onNavigateBack = { back() },
                        initialDay = WeekdayRoute.parseDay(entry.arguments?.getString(WeekdayRoute.DAY_ARG))
                    )
                }
            }
            composable("calendar") { CalendarScreen(toolsViewModel, onNavigateBack = { back() }) }
            composable("lunar_months") {
                LunarMonthsScreen(toolsViewModel, onNavigateBack = { back() }, onOpenItem = { openReader(it) })
            }
            composable("prayer") {
                PrayerTimesScreen(
                    toolsViewModel,
                    onNavigateBack = { back() },
                    onOpenQibla = { go("qibla") },
                    onOpenSettings = { go("prayer/settings") }
                )
            }
            composable("prayer/settings") {
                PrayerSettingsScreen(toolsViewModel, vms.voice, onNavigateBack = { back() }, onOpenVoices = { go("prayer/voices") })
            }
            composable("prayer/voices") { VoicePacksScreen(vms.voice, onNavigateBack = { back() }) }
            composable("tasbih") { TasbihScreen(vms.worship, onNavigateBack = { back() }) }
            composable("qada") { QadaScreen(vms.worship, onNavigateBack = { back() }) }
            composable("ramadan") { RamadanScreen(toolsViewModel, vms.worship, onNavigateBack = { back() }) }
            composable("ayat") { AyatScreen(toolsViewModel, vms.worship, onNavigateBack = { back() }) }
            composable("khums") { KhumsScreen(toolsViewModel, vms.worship, onNavigateBack = { back() }) }
            composable("qibla") { QiblaScreen(toolsViewModel, onNavigateBack = { back() }) }
            composable("favorites") {
                FavoritesScreen(
                    viewModel = toolsViewModel,
                    onNavigateBack = { back() },
                    onOpenAyah = { s, a -> openAyah(s, a) },
                    onOpenMafatih = { openReader(it) },
                    onOpenWeekday = { kind, day -> openWeekday(kind, day) }
                )
            }
            composable("settings") {
                SettingsScreen(
                    viewModel = mainViewModel,
                    tools = toolsViewModel,
                    worship = vms.worship,
                    onNavigateBack = { back() },
                    onOpenRoute = { go(it) }
                )
            }
        }
      }
    }
}

/** Activity-scoped view models, each created the first time a screen asks for it. */
private class LazyViewModels(owner: ViewModelStoreOwner) {
    private val provider = ViewModelProvider(owner)
    val quran: QuranViewModel by lazy { provider[QuranViewModel::class.java] }
    val mafatih: MafatihViewModel by lazy { provider[MafatihViewModel::class.java] }
    val sahifa: SahifaViewModel by lazy { provider[SahifaViewModel::class.java] }
    val search: SearchViewModel by lazy { provider[SearchViewModel::class.java] }
    val voice: VoiceViewModel by lazy { provider[VoiceViewModel::class.java] }
    val worship: WorshipViewModel by lazy { provider[WorshipViewModel::class.java] }
}

/** Shown on every screen while an adhan plays, so it can always be stopped from inside the app. */
@Composable
private fun AdhanPlayingBanner() {
    val playing by AdhanService.playing.collectAsState()
    if (!playing) return
    val context = LocalContext.current
    Surface(color = MaterialTheme.colorScheme.errorContainer, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "يُشغَّل الأذان الآن",
                Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.SemiBold
            )
            Button(onClick = { AdhanService.stop(context) }) { Text("إيقاف الأذان") }
        }
    }
}
