# Graph Report - Daily Reminder  (2026-10-09)

## Corpus Check
- 162 files · ~386,625 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 26 file(s) not represented in the graph (top: .xml 12, .ttf 5, (none) 3)

## Summary
- 1631 nodes · 4397 edges · 104 communities (50 shown, 54 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 158 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a030bff2`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ToolsViewModel
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- FavoriteKey
- AdhanService
- MainViewModel
- QuranViewModel
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- TodayScreen
- Agent Directives & Operational Rules
- PrayerTimesScreen.kt
- ShiaCalendar
- generate_icons.py
- Repository Soul & Core Identity
- localdate
- AppNavigation
- QiblaScreen
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
- SearchResultType
- VoiceAndPolicyTest
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Place
- Daily Deeds Reminder 1.4.0
- ReligiousAlarms.kt
- Google Play listing: draft answers (not submitted)
- PrayerTimesScreen
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- ReminderType
- PrayerSettings
- THIRD_PARTY_NOTICES.md
- .next
- WidgetUpdater.kt
- WorshipViewModel
- SahifaViewModel.kt
- .item
- AdhanGlobalSettings
- QadaState
- Daily Deeds Reminder 1.3.0
- DeedCategory
- Daily Deeds Reminder 1.5.0
- .search
- GlobalSearchScreen.kt
- VoiceStore
- PrayerAlarmConfig
- VoiceViewModel
- .toHijri
- PrayerAlarmReceiver.kt
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- AdhanMode
- OccasionAlarmReceiver.kt
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- PrayerContext
- Khums.kt
- Application
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- PermissionHealth.kt
- .build
- KhumsYear
- materialtheme
- Test
- AddTo

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 92 edges
2. `ToolsViewModel` - 56 edges
3. `WorshipViewModel` - 43 edges
4. `Place` - 35 edges
5. `AppNavigation()` - 34 edges
6. `Prayer` - 32 edges
7. `PrayerContext` - 32 edges
8. `MainViewModel` - 29 edges
9. `MafatihRepository` - 26 edges
10. `MafatihItem` - 26 edges

## Surprising Connections (you probably didn't know these)
- `Google's developer verification` --references--> `adb()`  [INFERRED]
  docs/DISTRIBUTION.md → scripts/emulator_smoke.py
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt
- `Ongoing log` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt

## Import Cycles
- None detected.

## Communities (104 total, 54 thin omitted)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.09
Nodes (7): PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "FavoriteKey"
Cohesion: 0.15
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 8 - "MainViewModel"
Cohesion: 0.06
Nodes (17): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, DeedCard() (+9 more)

### Community 9 - "QuranViewModel"
Cohesion: 0.05
Nodes (21): QuranDataProvider, QuranRepository, TafsirAlMizanProvider, Ayah, RevelationType, MADANI, MAKKI, Surah (+13 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.12
Nodes (12): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+4 more)

### Community 13 - "TodayScreen"
Cohesion: 0.32
Nodes (6): rememberNow(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen()

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.15
Nodes (10): Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT, ShiaCalendar (+2 more)

### Community 19 - "generate_icons.py"
Cohesion: 0.07
Nodes (24): android_resources(), circle_path(), crescent_path(), draw_mark(), f(), octagram(), play_assets(), star_path() (+16 more)

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "AppNavigation"
Cohesion: 0.11
Nodes (14): AppNavigation(), AppTopBar(), CalendarScreen(), FavoriteRow, FavoritesScreen(), GlobalSearchScreen(), SearchResultCard(), Cell() (+6 more)

### Community 29 - "QiblaScreen"
Cohesion: 0.29
Nodes (3): QiblaScreen(), rememberTrueHeading(), SensorEventListener

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.06
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.09
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 43 - "SearchResultType"
Cohesion: 0.06
Nodes (22): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+14 more)

### Community 45 - "VoiceAndPolicyTest"
Cohesion: 0.05
Nodes (21): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), BackupCodec, Error, Import (+13 more)

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Place"
Cohesion: 0.13
Nodes (16): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, Place, PlacePresets (+8 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.20
Nodes (9): Content rating and audience, Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist, Store listing (+1 more)

### Community 59 - "PrayerTimesScreen"
Cohesion: 0.25
Nodes (4): CitySearchDialog(), CoordinatesDialog(), PrayerTimesScreen(), TimeFormat

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 62 - "ReminderType"
Cohesion: 0.07
Nodes (16): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+8 more)

### Community 63 - "PrayerSettings"
Cohesion: 0.15
Nodes (7): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, AdhanModelsTest

### Community 66 - "WidgetUpdater.kt"
Cohesion: 0.08
Nodes (4): PrayerWidgetProvider, WidgetUpdater, TimetablePdf, Widget

### Community 69 - ".item"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 71 - "QadaState"
Cohesion: 0.19
Nodes (9): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+1 more)

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 73 - "DeedCategory"
Cohesion: 0.18
Nodes (10): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+2 more)

### Community 74 - "Daily Deeds Reminder 1.5.0"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.5.0, New structure, Search, settings and fixes, Verification, What changed in the Mafatih tab

### Community 75 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 76 - "GlobalSearchScreen.kt"
Cohesion: 0.11
Nodes (4): cardBorder(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 80 - "VoiceViewModel"
Cohesion: 0.15
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - ".toHijri"
Cohesion: 0.22
Nodes (5): HijriDay, PrayerTimesResult, Timetable, TimetableRow, TimetableTest

### Community 83 - "KhumsScreen"
Cohesion: 0.17
Nodes (16): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), CounterButton() (+8 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (4): Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "AdhanMode"
Cohesion: 0.33
Nodes (5): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

### Community 91 - "PrayerContext"
Cohesion: 0.29
Nodes (4): PrayerContext, PrayerParams, ImsakPlannerTest, WidgetModelTest

### Community 92 - "Khums.kt"
Cohesion: 0.28
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 98 - ".build"
Cohesion: 0.36
Nodes (4): DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 108 - "AddTo"
Cohesion: 0.67
Nodes (3): AddTo, Bulk, QadaDialog

## Knowledge Gaps
- **164 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+159 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 439 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **54 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `ToolsViewModel`, `MainActivity.kt`, `FavoriteKey`, `AdhanService`, `MainViewModel`, `PrayerTimesScreen.kt`, `VoiceAndPolicyTest`, `Place`, `AdhanService.kt`, `ReligiousAlarms.kt`, `ReminderType`, `PrayerSettings`, `WidgetUpdater.kt`, `WorshipViewModel`, `AdhanGlobalSettings`, `QadaState`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `OccasionAlarmReceiver.kt`, `AdhanActionReceiver.kt`, `KhumsYear`?**
  _High betweenness centrality (0.150) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `FavoriteKey`, `QuranViewModel`, `TodayScreen`, `PrayerTimesScreen.kt`, `AppNavigation`, `QiblaScreen`, `IslamicDecor.kt`, `SearchResultType`, `QuranReaderScreen.kt`, `Place`, `WeekdayContentScreen.kt`, `PrayerTimesScreen`, `ReminderType`, `PrayerSettings`, `AdhanGlobalSettings`, `GlobalSearchScreen.kt`, `PrayerAlarmConfig`, `KhumsScreen`, `PrayerSettingsScreen`, `PrayerContext`?**
  _High betweenness centrality (0.102) - this node is a cross-community bridge._
- **Why does `Prayer` connect `Place` to `ToolsViewModel`, `WidgetUpdater.kt`, `AdhanService`, `PrayerContext`, `QuranReaderScreen.kt`, `PrayerAlarmConfig`, `PrayerTimesScreen.kt`, `PrayerAlarmReceiver.kt`, `localdate`, `AdhanService.kt`, `ReligiousAlarms.kt`, `AdhanActionReceiver.kt`, `PrayerSettings`?**
  _High betweenness centrality (0.062) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _164 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07936507936507936 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.04625346901017576 - nodes in this community are weakly interconnected._