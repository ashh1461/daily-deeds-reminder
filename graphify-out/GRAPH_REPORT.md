# Graph Report - Daily Reminder  (2026-10-04)

## Corpus Check
- 42 files · ~17,706 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 16 file(s) not represented in the graph (top: .xml 6, (none) 2, .bat 2)

## Summary
- 444 nodes · 912 edges · 41 communities (7 shown, 34 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 11 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6ab46d74`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Theme.kt
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Weekday text review
- PreferencesManager
- ReminderType
- Deed
- .nextOccurrence
- MainViewModel
- Progress
- WeekdayContentScreen.kt
- DayContentKind
- ReaderReadingContent
- ColorSchemeContrastTest.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 36 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 25 edges
4. `Deed` - 25 edges
5. `ReminderType` - 20 edges
6. `Daily Deeds Reminder v1.1.0 release notes` - 15 edges
7. `DeedType` - 13 edges
8. `DayContentKind` - 11 edges
9. `DeedsRepository` - 10 edges
10. `HomeContent()` - 10 edges

## Surprising Connections (you probably didn't know these)
- `Implementation plan selects SharedPreferences for daily state and rollover` --semantically_similar_to--> `Current documented persistence: SharedPreferences`  [INFERRED] [semantically similar]
  docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md → README.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Historical 2026-10-02 implementation plan for original 11 deeds`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Tasbeeh Fatima: 34/33/33 stages, audio and haptic feedback`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → README.md
- `ReaderCounterScreen()` --calls--> `ReaderCounterContent()`  [INFERRED]
  app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterScreen.kt → app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterContent.kt
- `ReaderCounterScreen()` --calls--> `ReaderReadingContent()`  [INFERRED]
  app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterScreen.kt → app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderReadingContent.kt

## Import Cycles
- None detected.

## Communities (41 total, 34 thin omitted)

### Community 1 - "MainActivity.kt"
Cohesion: 0.07
Nodes (5): AppNavigation(), BottomTab, MainActivity, ReaderCounterScreen(), ReaderTopBar()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Weekday text review"
Cohesion: 0.50
Nodes (3): Changes made, Transcription and edition notes, Weekday text review

### Community 5 - "ReminderType"
Cohesion: 0.06
Nodes (13): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+5 more)

### Community 6 - "Deed"
Cohesion: 0.06
Nodes (27): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+19 more)

### Community 9 - "Progress"
Cohesion: 0.33
Nodes (5): Completed, Ongoing log, Progress, Remaining, Verification limits

### Community 18 - "DayContentKind"
Cohesion: 0.15
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

## Knowledge Gaps
- **33 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+28 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 182 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **34 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `Deed`, `WeekdayContentScreen.kt`?**
  _High betweenness centrality (0.124) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `Deed` to `WeekdayContentScreen.kt`, `MainActivity.kt`, `ReminderType`, `MainViewModel`?**
  _High betweenness centrality (0.114) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `MainViewModel`, `ReminderType`, `Deed`?**
  _High betweenness centrality (0.108) - this node is a cross-community bridge._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _33 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06951871657754011 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `PreferencesManager` be split into smaller, more focused modules?**
  _Cohesion score 0.07056451612903226 - nodes in this community are weakly interconnected._