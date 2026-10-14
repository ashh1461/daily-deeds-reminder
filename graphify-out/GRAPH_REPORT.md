# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 100 files · ~305,661 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 21 file(s) not represented in the graph (top: .xml 7, .ttf 5, (none) 3)

## Summary
- 924 nodes · 2313 edges · 71 communities (27 shown, 44 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 63 edges (avg confidence: 0.92)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8d9dac20`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- ArabicNormalizer
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- Deed
- ReminderType
- MainViewModel
- QuranViewModel
- Daily Deeds Reminder 1.2.2
- Agent Directives & Operational Rules
- ShiaCalendar
- linear_tool.py
- Repository Soul & Core Identity
- localdate
- FavoritesScreen
- QiblaScreen
- SearchRepositoryTest
- IslamicDecor.kt
- MafatihRepositoryTest
- FavoriteKey
- QuranRepositoryTest
- ToolsViewModel
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- QuranDataIntegrityTest
- ReligiousAlarms.kt
- asserttrue
- AppNavigation
- Theme.kt
- MainActivity
- DeedCategory
- HomeContent
- THIRD_PARTY_NOTICES.md
- .next
- OccasionAlarmReceiver.kt
- ReaderCounterScreen
- .getDeedsByCategory
- PrayerAlarmReceiver.kt
- Daily Deeds Reminder 1.3.0

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 58 edges
2. `ToolsViewModel` - 32 edges
3. `MainViewModel` - 30 edges
4. `DeedCategory` - 25 edges
5. `Deed` - 25 edges
6. `MafatihViewModel` - 23 edges
7. `AppNavigation()` - 22 edges
8. `QuranViewModel` - 22 edges
9. `MafatihRepository` - 20 edges
10. `QuranRepository` - 20 edges

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

## Communities (71 total, 44 thin omitted)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.10
Nodes (5): PrayerTimes, Raw, Qibla, PrayerTimesTest, Ref

### Community 6 - "Deed"
Cohesion: 0.19
Nodes (10): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+2 more)

### Community 7 - "ReminderType"
Cohesion: 0.07
Nodes (14): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+6 more)

### Community 9 - "QuranViewModel"
Cohesion: 0.07
Nodes (19): QuranDataProvider, QuranRepository, TafsirAlMizanProvider, Ayah, RevelationType, MADANI, MAKKI, Surah (+11 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.14
Nodes (12): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+4 more)

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "FavoritesScreen"
Cohesion: 0.25
Nodes (4): FavoriteRow, FavoritesScreen(), PrayerTimesScreen(), ToolScaffold()

### Community 29 - "QiblaScreen"
Cohesion: 0.29
Nodes (3): QiblaScreen(), rememberTrueHeading(), SensorEventListener

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.08
Nodes (10): CircularProgressBar(), cardBorder(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), HomeToolsCard() (+2 more)

### Community 43 - "FavoriteKey"
Cohesion: 0.09
Nodes (14): Ayah, FavoriteKey, FavoritesList, Mafatih, Weekday, WeekdayRepository, DayContent, DayContentKind (+6 more)

### Community 45 - "ToolsViewModel"
Cohesion: 0.16
Nodes (3): Place, lastKnownPlace(), ToolsViewModel

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 52 - "MafatihViewModel"
Cohesion: 0.05
Nodes (22): MafatihDataProvider, MafatihRepository, Entry, SearchRepository, MafatihCategoryType, ADIYAH, AMAL, FULLBOOK (+14 more)

### Community 53 - "Prayer"
Cohesion: 0.25
Nodes (6): Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerInstant

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 58 - "asserttrue"
Cohesion: 0.19
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 59 - "AppNavigation"
Cohesion: 0.29
Nodes (4): AppNavigation(), GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen()

### Community 62 - "DeedCategory"
Cohesion: 0.14
Nodes (11): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+3 more)

### Community 63 - "HomeContent"
Cohesion: 0.22
Nodes (6): DeedCard(), DailyProgressCard(), HomeContent(), HomePreview(), HomeScreen(), TodayWeekdayCard()

### Community 65 - ".next"
Cohesion: 0.32
Nodes (3): PrayerSchedule, PrayerTimesResult, PrayerScheduleTest

### Community 68 - "ReaderCounterScreen"
Cohesion: 0.20
Nodes (4): ReaderCounterScreen(), ReaderReadingContent(), ReaderTextCard(), ReaderTopBar()

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

## Knowledge Gaps
- **79 isolated node(s):** `EID`, `BIRTH`, `MARTYRDOM`, `NIGHT`, `EVENT` (+74 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 283 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **44 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `OccasionAlarmReceiver.kt`, `Deed`, `ReminderType`, `PrayerAlarmReceiver.kt`, `MainViewModel`, `FavoriteKey`, `ToolsViewModel`, `Prayer`, `localdate`, `ReligiousAlarms.kt`?**
  _High betweenness centrality (0.092) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `IslamicDecor.kt`, `PreferencesManager`, `FavoritesScreen`, `QuranViewModel`, `FavoriteKey`, `WeekdayContentScreen.kt`, `ShiaCalendar`, `Prayer`, `AppNavigation`, `QiblaScreen`?**
  _High betweenness centrality (0.080) - this node is a cross-community bridge._
- **Why does `QuranRepository` connect `QuranViewModel` to `IslamicDecor.kt`, `QuranRepositoryTest`, `WeekdayContentScreen.kt`, `MafatihViewModel`, `QuranDataIntegrityTest`, `FavoritesScreen`?**
  _High betweenness centrality (0.054) - this node is a cross-community bridge._
- **What connects `EID`, `BIRTH`, `MARTYRDOM` to the rest of the system?**
  _79 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.10526315789473684 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.06386554621848739 - nodes in this community are weakly interconnected._