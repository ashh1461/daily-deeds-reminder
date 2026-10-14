# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 155 files · ~377,521 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 25 file(s) not represented in the graph (top: .xml 11, .ttf 5, (none) 3)

## Summary
- 1556 nodes · 4294 edges · 109 communities (49 shown, 60 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 156 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a1299ab6`
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
- ReminderTimeCalculatorTest
- Deed
- QuranRepository
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- MainViewModel
- Agent Directives & Operational Rules
- PrayerTimesScreen.kt
- ShiaCalendar
- linear_tool.py
- Repository Soul & Core Identity
- Place
- AppNavigation
- SensorEventListener
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
- SearchResultType
- VoiceAndPolicyTest
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- AdhanService.kt
- ReligiousAlarms.kt
- TimetablePdf.kt
- Completed
- Theme.kt
- BackupCodecTest
- AlarmScheduler
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- .next
- WidgetUpdater.kt
- WorshipViewModel
- WorshipViewModel.kt
- .item
- QuranViewModel
- QadaState
- Daily Deeds Reminder 1.3.0
- DeedCategory
- Daily Deeds Reminder 1.5.0
- .search
- GlobalSearchScreen.kt
- VoiceStore
- .next
- VoiceViewModel
- .toHijri
- PrayerAlarmReceiver.kt
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- TafsirAlMizan
- QuranDataIntegrityTest
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- PrayerContext
- Khums.kt
- DeedsRepositoryTest
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- ReminderType
- PermissionHealth.kt
- .build
- KhumsYear
- Security notes
- materialtheme
- DailyReminderReceiver.kt
- SettingsScreen
- Test
- QuranDataProvider
- RamadanScreen
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
- `Widget` --references--> `PrayerWidgetProvider`  [INFERRED]
  docs/SECURITY.md → app/src/main/java/com/dailydeeds/reminder/widget/PrayerWidgetProvider.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt

## Import Cycles
- None detected.

## Communities (109 total, 60 thin omitted)

### Community 1 - "MainActivity.kt"
Cohesion: 0.07
Nodes (3): AppTab, MainActivity, AppTopBar()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.09
Nodes (8): PrayerParams, PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "FavoriteKey"
Cohesion: 0.08
Nodes (15): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, WeekdayRepository, DayContent (+7 more)

### Community 8 - "Deed"
Cohesion: 0.13
Nodes (13): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+5 more)

### Community 9 - "QuranRepository"
Cohesion: 0.20
Nodes (6): QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.12
Nodes (12): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+4 more)

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.15
Nodes (10): Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT, ShiaCalendar (+2 more)

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 21 - "Place"
Cohesion: 0.15
Nodes (3): Place, PlacePresets, ArabicNormalizer

### Community 27 - "AppNavigation"
Cohesion: 0.14
Nodes (12): AppNavigation(), CalendarScreen(), FavoriteRow, FavoritesScreen(), GlobalSearchScreen(), SearchResultCard(), QiblaScreen(), rememberTrueHeading() (+4 more)

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.06
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.09
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.09
Nodes (21): EclipseCard(), Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR (+13 more)

### Community 43 - "SearchResultType"
Cohesion: 0.14
Nodes (12): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, DEEDS, MAFATIH, QURAN (+4 more)

### Community 45 - "VoiceAndPolicyTest"
Cohesion: 0.12
Nodes (9): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), VoiceAndPolicyTest, Adding a voice, Adhan voice packs (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.10
Nodes (18): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, PrayerAlarmConfig, AdhanPlanner, AlarmKind (+10 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 59 - "Completed"
Cohesion: 0.21
Nodes (6): MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), TafsirBottomSheet(), Completed, Ongoing log

### Community 61 - "BackupCodecTest"
Cohesion: 0.13
Nodes (5): BackupCodec, Error, Import, Ok, BackupCodecTest

### Community 63 - "ToolsViewModel"
Cohesion: 0.05
Nodes (20): AdhanGlobalSettings, CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, DeedCard() (+12 more)

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
Cohesion: 0.19
Nodes (15): AyatScreen(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), CounterButton(), FreePanel() (+7 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (4): Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 87 - "QuranDataIntegrityTest"
Cohesion: 0.23
Nodes (5): QuranDataIntegrityTest, Progress, Remaining, v1.2.1 corrective release (2026-10-07), Verification limits

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

### Community 91 - "PrayerContext"
Cohesion: 0.25
Nodes (5): PrayerContext, PrayerSchedule, WidgetModel, ImsakPlannerTest, WidgetModelTest

### Community 92 - "Khums.kt"
Cohesion: 0.28
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "ReminderType"
Cohesion: 0.24
Nodes (7): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY

### Community 98 - ".build"
Cohesion: 0.36
Nodes (4): DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 101 - "Security notes"
Cohesion: 0.22
Nodes (7): Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`), Security notes, Widget

### Community 104 - "SettingsScreen"
Cohesion: 0.48
Nodes (5): ReminderSettingCard(), restartApp(), SectionTitle(), SettingsCard(), SettingsScreen()

### Community 107 - "RamadanScreen"
Cohesion: 0.67
Nodes (3): Cell(), clock(), RamadanScreen()

### Community 108 - "AddTo"
Cohesion: 0.67
Nodes (3): AddTo, Bulk, QadaDialog

## Knowledge Gaps
- **141 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+136 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 399 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **60 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `MainViewModel`, `PrayerTimesScreen.kt`, `Place`, `Prayer`, `AdhanService.kt`, `ReligiousAlarms.kt`, `BackupCodecTest`, `AlarmScheduler`, `ToolsViewModel`, `WidgetUpdater.kt`, `WorshipViewModel`, `WorshipViewModel.kt`, `QadaState`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `AdhanActionReceiver.kt`, `DeedsRepositoryTest`, `ReminderType`, `KhumsYear`, `Security notes`, `DailyReminderReceiver.kt`?**
  _High betweenness centrality (0.153) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `IslamicDecor.kt`, `WorshipViewModel.kt`, `PreferencesManager`, `FavoriteKey`, `SettingsScreen`, `Completed`, `PrayerContext`, `RamadanScreen`, `GlobalSearchScreen.kt`, `QuranReaderScreen.kt`, `PrayerTimesScreen.kt`, `KhumsScreen`, `Prayer`, `Place`, `PrayerSettingsScreen`, `WeekdayContentScreen.kt`, `AppNavigation`?**
  _High betweenness centrality (0.087) - this node is a cross-community bridge._
- **Why does `WorshipViewModel` connect `WorshipViewModel` to `MainActivity.kt`, `WorshipViewModel.kt`, `PreferencesManager`, `KhumsYear`, `QadaState`, `SettingsScreen`, `RamadanScreen`, `PrayerTimesScreen.kt`, `KhumsScreen`, `TasbihState`, `AppNavigation`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _141 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07096774193548387 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.04625346901017576 - nodes in this community are weakly interconnected._