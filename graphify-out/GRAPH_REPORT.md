# Graph Report - Daily Reminder  (2026-10-05)

## Corpus Check
- 75 files · ~29,128 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 13 file(s) not represented in the graph (top: .xml 6, (none) 2, .bat 2)

## Summary
- 615 nodes · 1431 edges · 55 communities (11 shown, 44 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 12 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `4ff0b918`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MafatihViewModel
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- ReminderType
- HomeScreen.kt
- .nextOccurrence
- MainViewModel
- Progress
- QuranViewModel
- WeekdayContentScreen.kt
- DayContentKind
- DeedsRepositoryTest.kt
- Theme.kt
- .scheduleReminder
- DailyReminderReceiver.kt
- AppNavigation
- .onCreate
- ReaderCounterScreen

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 36 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 25 edges
4. `Deed` - 25 edges
5. `ReminderType` - 20 edges
6. `MafatihViewModel` - 19 edges
7. `QuranViewModel` - 19 edges
8. `MafatihCategoryType` - 17 edges
9. `AppNavigation()` - 16 edges
10. `Daily Deeds Reminder v1.1.0 release notes` - 15 edges

## Surprising Connections (you probably didn't know these)
- `Implementation plan selects SharedPreferences for daily state and rollover` --semantically_similar_to--> `Current documented persistence: SharedPreferences`  [INFERRED] [semantically similar]
  docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md → README.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Historical 2026-10-02 implementation plan for original 11 deeds`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Tasbeeh Fatima: 34/33/33 stages, audio and haptic feedback`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → README.md
- `ReaderCounterContent()` --calls--> `CounterStageRow()`  [INFERRED]
  app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterContent.kt → app/src/main/java/com/dailydeeds/reminder/ui/screens/CounterWidgets.kt
- `ReaderCounterContent()` --calls--> `CounterOrb()`  [INFERRED]
  app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterContent.kt → app/src/main/java/com/dailydeeds/reminder/ui/screens/CounterWidgets.kt

## Import Cycles
- None detected.

## Communities (55 total, 44 thin omitted)

### Community 0 - "MafatihViewModel"
Cohesion: 0.06
Nodes (21): MafatihDataProvider, MafatihRepository, SearchRepository, MafatihCategoryType, ADIYAH, AMAL, BAQIYAT, MUNAJAT (+13 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.07
Nodes (20): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Changes made, Transcription and edition notes, Weekday text review, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+12 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "ReminderType"
Cohesion: 0.18
Nodes (9): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+1 more)

### Community 6 - "HomeScreen.kt"
Cohesion: 0.08
Nodes (25): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+17 more)

### Community 9 - "Progress"
Cohesion: 0.33
Nodes (5): Completed, Ongoing log, Progress, Remaining, Verification limits

### Community 10 - "QuranViewModel"
Cohesion: 0.09
Nodes (12): QuranDataProvider, QuranRepository, TafsirAlMizanProvider, Ayah, RevelationType, MADANI, MAKKI, Surah (+4 more)

### Community 16 - "WeekdayContentScreen.kt"
Cohesion: 0.05
Nodes (8): CircularProgressBar(), FilterChipRow(), GlobalSearchScreen(), SearchResultCard(), QuranScreen(), ReaderCounterContent(), ReaderReadingContent(), ReaderTextCard()

### Community 18 - "DayContentKind"
Cohesion: 0.17
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

### Community 32 - "DeedsRepositoryTest.kt"
Cohesion: 0.08
Nodes (4): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest

### Community 47 - "AppNavigation"
Cohesion: 0.29
Nodes (3): AppNavigation(), MafatihReaderScreen(), MafatihScreen()

## Knowledge Gaps
- **47 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+42 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 217 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **44 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `HomeScreen.kt`, `.scheduleReminder`, `AppNavigation`, `.onCreate`, `WeekdayContentScreen.kt`, `ReaderCounterScreen`, `MainViewModel.kt`?**
  _High betweenness centrality (0.095) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `HomeScreen.kt` to `DeedsRepositoryTest.kt`, `MainActivity.kt`, `ReminderType`, `MainViewModel`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `WeekdayContentScreen.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.083) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `ReminderType`, `MainViewModel`, `.scheduleReminder`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.080) - this node is a cross-community bridge._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _47 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MafatihViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.05721153846153846 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.08695652173913043 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.07196969696969698 - nodes in this community are weakly interconnected._