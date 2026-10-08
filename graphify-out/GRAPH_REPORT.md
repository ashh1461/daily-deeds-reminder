# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 110 files · ~309,448 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 21 file(s) not represented in the graph (top: .xml 7, .ttf 5, (none) 3)

## Summary
- 1007 nodes · 2613 edges · 78 communities (30 shown, 48 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 84 edges (avg confidence: 0.9)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6e28ec0d`
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
- MainViewModel
- QuranRepository
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- ReminderType
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- localdate
- linear_tool.py
- Repository Soul & Core Identity
- AppNavigation
- SensorEventListener
- SearchRepositoryTest
- IslamicDecor.kt
- MafatihRepositoryTest
- SearchResultType
- QuranRepositoryTest
- ToolsViewModel
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- RowsAndTimeTest.kt
- ReligiousAlarms.kt
- Validation
- QuranViewModel
- Theme.kt
- MainActivity
- .scheduleReminder
- cardBorder
- THIRD_PARTY_NOTICES.md
- Place
- MafatihCategoryType
- OccasionAlarmReceiver.kt
- SahifaViewModel.kt
- .item
- SearchViewModel
- Daily Deeds Reminder 1.3.0
- DailyReminderReceiver.kt
- Daily Deeds Reminder 1.5.0
- SettingsScreen

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 64 edges
2. `ToolsViewModel` - 41 edges
3. `MainViewModel` - 29 edges
4. `MafatihRepository` - 26 edges
5. `MafatihItem` - 26 edges
6. `AppNavigation()` - 25 edges
7. `Deed` - 25 edges
8. `DeedCategory` - 23 edges
9. `MafatihCategoryType` - 23 edges
10. `MafatihViewModel` - 23 edges

## Surprising Connections (you probably didn't know these)
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt
- `Ongoing log` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt
- `Completed` --references--> `MafatihRepository`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihRepository.kt

## Import Cycles
- None detected.

## Communities (78 total, 48 thin omitted)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.10
Nodes (5): PrayerTimes, Raw, Qibla, PrayerTimesTest, Ref

### Community 6 - "FavoriteKey"
Cohesion: 0.14
Nodes (8): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoriteRow, FavoritesTest

### Community 8 - "MainViewModel"
Cohesion: 0.05
Nodes (26): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+18 more)

### Community 9 - "QuranRepository"
Cohesion: 0.08
Nodes (14): QuranDataProvider, QuranRepository, TafsirAlMizanProvider, Ayah, RevelationType, MADANI, MAKKI, Surah (+6 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.16
Nodes (4): MafatihDataProvider, MafatihRepository, MafatihItem, ArabicNormalizer

### Community 13 - "ReminderType"
Cohesion: 0.19
Nodes (7): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 16 - "WeekdayContentScreen.kt"
Cohesion: 0.06
Nodes (5): DeedCard(), FilterChipRow(), canScheduleExactAlarms(), PermissionHealthCard(), HubEntry

### Community 18 - "localdate"
Cohesion: 0.13
Nodes (11): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+3 more)

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "AppNavigation"
Cohesion: 0.16
Nodes (9): AppNavigation(), CalendarScreen(), FavoritesScreen(), GlobalSearchScreen(), SearchResultCard(), PrayerTimesScreen(), QiblaScreen(), rememberTrueHeading() (+1 more)

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.10
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 43 - "SearchResultType"
Cohesion: 0.09
Nodes (19): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+11 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.15
Nodes (7): PrayerAlarmReceiver, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerInstant

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "RowsAndTimeTest.kt"
Cohesion: 0.24
Nodes (6): buildRows(), Entry, Header, ListRow, TimeFormat, RowsAndTimeTest

### Community 58 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 59 - "QuranViewModel"
Cohesion: 0.12
Nodes (7): MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), TafsirBottomSheet(), QuranViewModel, Completed, Ongoing log

### Community 63 - "cardBorder"
Cohesion: 0.35
Nodes (8): cardBorder(), rememberNow(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), DailyProgressCard(), TodayScreen()

### Community 65 - "Place"
Cohesion: 0.15
Nodes (6): Place, PlacePresets, lastKnownPlace(), PrayerSchedule, PrayerTimesResult, PrayerScheduleTest

### Community 66 - "MafatihCategoryType"
Cohesion: 0.18
Nodes (9): MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT, SAHIFA, TAQIBAT (+1 more)

### Community 69 - ".item"
Cohesion: 0.25
Nodes (6): ItemRow(), mafatihRows(), SearchField(), MafatihScreen(), SahifaScreen(), WorshipHubScreen()

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 74 - "Daily Deeds Reminder 1.5.0"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.5.0, New structure, Search, settings and fixes, Verification, What changed in the Mafatih tab

### Community 75 - "SettingsScreen"
Cohesion: 0.80
Nodes (4): ReminderSettingCard(), SectionTitle(), SettingsCard(), SettingsScreen()

## Knowledge Gaps
- **86 isolated node(s):** `EID`, `BIRTH`, `MARTYRDOM`, `NIGHT`, `EVENT` (+81 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 299 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **48 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `Place`, `MainActivity.kt`, `OccasionAlarmReceiver.kt`, `FavoriteKey`, `ToolsViewModel.kt`, `MainViewModel`, `DailyReminderReceiver.kt`, `ReminderType`, `ToolsViewModel`, `Prayer`, `ReligiousAlarms.kt`, `MainActivity`, `.scheduleReminder`?**
  _High betweenness centrality (0.142) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `Place`, `PreferencesManager`, `ToolsViewModel.kt`, `FavoriteKey`, `QuranViewModel`, `SettingsScreen`, `SearchResultType`, `WeekdayContentScreen.kt`, `Prayer`, `AppNavigation`, `MainActivity`, `cardBorder`?**
  _High betweenness centrality (0.074) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `MainViewModel` to `MainActivity.kt`, `OccasionAlarmReceiver.kt`, `ReminderType`, `WeekdayContentScreen.kt`, `ReligiousAlarms.kt`?**
  _High betweenness centrality (0.051) - this node is a cross-community bridge._
- **What connects `EID`, `BIRTH`, `MARTYRDOM` to the rest of the system?**
  _86 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07407407407407407 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.05668016194331984 - nodes in this community are weakly interconnected._