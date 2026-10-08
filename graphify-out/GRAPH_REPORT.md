# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 98 files · ~278,158 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 15 file(s) not represented in the graph (top: .xml 7, (none) 2, .bat 2)

## Summary
- 888 nodes · 2189 edges · 74 communities (29 shown, 45 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 63 edges (avg confidence: 0.92)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `35ff75b7`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MafatihCategoryType
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- Deed
- ReminderType
- MainViewModel
- QuranRepository
- Daily Deeds Reminder 1.2.2
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- ShiaCalendar
- linear_tool.py
- Repository Soul & Core Identity
- Place
- PrayerTimesScreen.kt
- QiblaScreen.kt
- SearchRepositoryTest
- MafatihRepositoryTest
- FavoriteKey
- QuranRepositoryTest
- ToolsViewModel
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- DayContentKind
- MafatihFullBookTest.kt
- QuranDataIntegrityTest
- ReligiousAlarms.kt
- Validation
- AppNavigation
- Theme.kt
- Ongoing log
- DeedCategory
- HomeContent
- QuranViewModel
- .next
- MafatihViewModel
- OccasionAlarmReceiver.kt
- ReaderCounterScreen
- DeedsRepositoryTest
- PrayerAlarmReceiver.kt
- ReaderCounterContent
- Daily Deeds Reminder 1.3.0
- Progress

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 58 edges
2. `ToolsViewModel` - 32 edges
3. `MainViewModel` - 30 edges
4. `DeedCategory` - 25 edges
5. `Deed` - 25 edges
6. `AppNavigation()` - 22 edges
7. `QuranViewModel` - 22 edges
8. `MafatihViewModel` - 21 edges
9. `QuranRepository` - 20 edges
10. `ReminderType` - 20 edges

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

## Communities (74 total, 45 thin omitted)

### Community 0 - "MafatihCategoryType"
Cohesion: 0.14
Nodes (12): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, BAQIYAT, FULLBOOK, MUNAJAT (+4 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.08
Nodes (3): BottomTab, MainActivity, HomeToolsCard()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.07
Nodes (20): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Changes made, Transcription and edition notes, Weekday text review, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+12 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.11
Nodes (4): PrayerTimes, Raw, PrayerTimesTest, Ref

### Community 6 - "Deed"
Cohesion: 0.24
Nodes (7): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage

### Community 7 - "ReminderType"
Cohesion: 0.06
Nodes (14): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+6 more)

### Community 9 - "QuranRepository"
Cohesion: 0.16
Nodes (7): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.13
Nodes (12): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+4 more)

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 21 - "Place"
Cohesion: 0.23
Nodes (3): Place, PlacePresets, PrayerInstant

### Community 29 - "QiblaScreen.kt"
Cohesion: 0.11
Nodes (7): FavoriteRow, FavoritesScreen(), QiblaScreen(), rememberTrueHeading(), SensorEventListener, ToolScaffold(), Qibla

### Community 43 - "FavoriteKey"
Cohesion: 0.18
Nodes (6): Ayah, FavoriteKey, FavoritesList, Mafatih, Weekday, FavoritesTest

### Community 45 - "ToolsViewModel"
Cohesion: 0.15
Nodes (6): Prayer, DHUHR, FAJR, ISHA, MAGHRIB, ToolsViewModel

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 52 - "DayContentKind"
Cohesion: 0.07
Nodes (18): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+10 more)

### Community 58 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 59 - "AppNavigation"
Cohesion: 0.18
Nodes (8): AppNavigation(), GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), MafatihScreen(), WeekdayShortcuts(), QuranScreen(), Completed

### Community 61 - "Ongoing log"
Cohesion: 0.22
Nodes (5): TafsirAlMizanProvider, TafsirAlMizan, QuranReaderScreen(), TafsirBottomSheet(), Ongoing log

### Community 62 - "DeedCategory"
Cohesion: 0.15
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

### Community 71 - "ReaderCounterContent"
Cohesion: 0.40
Nodes (3): CounterOrb(), CounterStageRow(), ReaderCounterContent()

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 73 - "Progress"
Cohesion: 0.40
Nodes (4): Progress, Remaining, v1.2.1 corrective release (2026-10-07), Verification limits

## Knowledge Gaps
- **76 isolated node(s):** `EID`, `BIRTH`, `MARTYRDOM`, `NIGHT`, `EVENT` (+71 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 276 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **45 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `OccasionAlarmReceiver.kt`, `Deed`, `ReminderType`, `PrayerAlarmReceiver.kt`, `MainViewModel`, `FavoriteKey`, `ToolsViewModel`, `Place`, `ReligiousAlarms.kt`?**
  _High betweenness centrality (0.099) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `PrayerTimesScreen.kt`, `FavoriteKey`, `WeekdayContentScreen.kt`, `ShiaCalendar`, `Ongoing log`, `DayContentKind`, `Place`, `AppNavigation`, `QiblaScreen.kt`?**
  _High betweenness centrality (0.086) - this node is a cross-community bridge._
- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `ReaderCounterScreen`, `PreferencesManager`, `Deed`, `ReminderType`, `WeekdayContentScreen.kt`, `AppNavigation`, `DeedCategory`, `HomeContent`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **What connects `EID`, `BIRTH`, `MARTYRDOM` to the rest of the system?**
  _76 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MafatihCategoryType` be split into smaller, more focused modules?**
  _Cohesion score 0.13666666666666666 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07936507936507936 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.07196969696969698 - nodes in this community are weakly interconnected._