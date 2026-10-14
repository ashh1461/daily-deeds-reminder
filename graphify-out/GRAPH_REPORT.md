# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 132 files · ~365,028 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 22 file(s) not represented in the graph (top: .xml 8, .ttf 5, (none) 3)

## Summary
- 1243 nodes · 3297 edges · 91 communities (38 shown, 53 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 100 edges (avg confidence: 0.9)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `152022d4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .contains
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- FavoriteKey
- ReminderTimeCalculator.kt
- MainViewModel
- QuranRepository
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- PreferencesManager.kt
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- ShiaCalendar
- linear_tool.py
- Repository Soul & Core Identity
- assertequals
- ToolScaffold
- SensorEventListener
- SearchRepositoryTest
- IslamicDecor.kt
- MafatihRepositoryTest
- SearchResultType
- QuranRepositoryTest
- VoiceAndPolicyTest
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- AdhanService.kt
- ReligiousAlarms.kt
- Validation
- Completed
- Theme.kt
- PrayerAlarmConfig
- ReminderType
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- Place
- MafatihCategoryType
- OccasionAlarmReceiver.kt
- SahifaViewModel.kt
- AppNavigation
- QuranViewModel
- Daily Deeds Reminder 1.3.0
- DeedCategory
- Daily Deeds Reminder 1.5.0
- .search
- VoiceStore
- .next
- VoiceViewModel
- .compute
- PrayerAlarmReceiver.kt
- PrayerTimes
- PrayerMethodsTest
- Daily Deeds Reminder 1.6.0
- TafsirAlMizan
- QuranDataIntegrityTest
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 72 edges
2. `ToolsViewModel` - 50 edges
3. `Prayer` - 30 edges
4. `MainViewModel` - 29 edges
5. `AppNavigation()` - 28 edges
6. `MafatihRepository` - 26 edges
7. `MafatihItem` - 26 edges
8. `DeedCategory` - 25 edges
9. `Deed` - 25 edges
10. `cardBorder()` - 24 edges

## Surprising Connections (you probably didn't know these)
- `v1.2.1 corrective release (2026-10-07)` --references--> `QuranDataIntegrityTest`  [INFERRED]
  progress.md → app/src/test/java/com/dailydeeds/reminder/data/QuranDataIntegrityTest.kt
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

## Communities (91 total, 53 thin omitted)

### Community 0 - ".contains"
Cohesion: 0.21
Nodes (4): MafatihFullBookTest, Adding a voice, Adhan voice packs, Rights gate

### Community 1 - "MainActivity.kt"
Cohesion: 0.07
Nodes (3): AppTab, MainActivity, AppTopBar()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 6 - "FavoriteKey"
Cohesion: 0.15
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 8 - "MainViewModel"
Cohesion: 0.07
Nodes (16): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+8 more)

### Community 9 - "QuranRepository"
Cohesion: 0.17
Nodes (7): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.20
Nodes (3): MafatihDataProvider, MafatihRepository, MafatihItem

### Community 13 - "PreferencesManager.kt"
Cohesion: 0.22
Nodes (5): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 16 - "WeekdayContentScreen.kt"
Cohesion: 0.06
Nodes (6): DeedCard(), FilterChipRow(), canScheduleExactAlarms(), PermissionHealthCard(), lastKnownPlace(), HubEntry

### Community 18 - "ShiaCalendar"
Cohesion: 0.13
Nodes (12): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+4 more)

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "ToolScaffold"
Cohesion: 0.19
Nodes (8): FavoriteRow, FavoritesScreen(), QiblaScreen(), rememberTrueHeading(), ToolScaffold(), Box2(), Title(), VoicePacksScreen()

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.10
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 43 - "SearchResultType"
Cohesion: 0.08
Nodes (20): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+12 more)

### Community 45 - "VoiceAndPolicyTest"
Cohesion: 0.11
Nodes (11): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), VoiceAndPolicyTest, Components, Dependencies (+3 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.20
Nodes (12): AdhanPlanner, AlarmKind, MAIN, PRE, PlannedAlarm, Prayer, DHUHR, FAJR (+4 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 58 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 59 - "Completed"
Cohesion: 0.13
Nodes (12): GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), TafsirBottomSheet(), Completed, Ongoing log (+4 more)

### Community 61 - "PrayerAlarmConfig"
Cohesion: 0.13
Nodes (9): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerAlarmConfig, PrayerSettings, VoiceIds, PrayerParams (+1 more)

### Community 62 - "ReminderType"
Cohesion: 0.08
Nodes (15): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+7 more)

### Community 63 - "ToolsViewModel"
Cohesion: 0.07
Nodes (14): AdhanGlobalSettings, cardBorder(), rememberNow(), CitySearchDialog(), CoordinatesDialog(), PrayerTimesScreen(), PrayerStrip(), ResumeTiles() (+6 more)

### Community 65 - "Place"
Cohesion: 0.25
Nodes (3): Place, PrayerSchedule, PrayerScheduleTest

### Community 66 - "MafatihCategoryType"
Cohesion: 0.14
Nodes (10): MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT, SAHIFA, TAQIBAT (+2 more)

### Community 69 - "AppNavigation"
Cohesion: 0.16
Nodes (12): AppNavigation(), buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField() (+4 more)

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 73 - "DeedCategory"
Cohesion: 0.14
Nodes (12): App, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT (+4 more)

### Community 74 - "Daily Deeds Reminder 1.5.0"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.5.0, New structure, Search, settings and fixes, Verification, What changed in the Mafatih tab

### Community 75 - ".search"
Cohesion: 0.20
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 80 - "VoiceViewModel"
Cohesion: 0.18
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - ".compute"
Cohesion: 0.36
Nodes (3): PrayerTimesResult, PrayerTimesTest, Ref

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

## Knowledge Gaps
- **110 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+105 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 339 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **53 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `Place`, `MainActivity.kt`, `OccasionAlarmReceiver.kt`, `FavoriteKey`, `MainViewModel`, `PreferencesManager.kt`, `PrayerAlarmReceiver.kt`, `AdhanService.kt`, `ReligiousAlarms.kt`, `AdhanActionReceiver.kt`, `PrayerAlarmConfig`, `ReminderType`, `ToolsViewModel`?**
  _High betweenness centrality (0.118) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `Place`, `PreferencesManager`, `AppNavigation`, `FavoriteKey`, `Completed`, `SearchResultType`, `WeekdayContentScreen.kt`, `ShiaCalendar`, `Prayer`, `PrayerSettingsScreen`, `ToolScaffold`, `PrayerAlarmConfig`, `ReminderType`?**
  _High betweenness centrality (0.103) - this node is a cross-community bridge._
- **Why does `MafatihRepository` connect `MafatihRepository` to `.contains`, `MafatihCategoryType`, `MafatihRepositoryTest`, `SahifaViewModel.kt`, `VoiceViewModel.kt`, `Completed`, `SearchResultType`, `WeekdayContentScreen.kt`, `MafatihViewModel`, `ToolScaffold`, `ToolsViewModel`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _110 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07096774193548387 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.05668016194331984 - nodes in this community are weakly interconnected._