# Graph Report - Daily Reminder  (2026-10-04)

## Corpus Check
- 34 files · ~16,426 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 13 file(s) not represented in the graph (top: .xml 6, (none) 2, .bat 2)

## Summary
- 395 nodes · 790 edges · 29 communities (6 shown, 23 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 5 edges (avg confidence: 0.87)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f8ba4365`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- DeedCard.kt
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- PreferencesManager
- NotificationHelper.kt
- DeedCategory
- .nextOccurrence
- MainViewModel
- Theme.kt
- CircularProgressBar.kt
- FilterChipRow.kt
- DayContentKind

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 36 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 24 edges
4. `ReminderType` - 20 edges
5. `Daily Deeds Reminder v1.1.0 release notes` - 15 edges
6. `Deed` - 12 edges
7. `DayContentKind` - 11 edges
8. `DeedsRepository` - 10 edges
9. `DeedType` - 10 edges
10. `HomeScreen()` - 10 edges

## Surprising Connections (you probably didn't know these)
- `Implementation plan selects SharedPreferences for daily state and rollover` --semantically_similar_to--> `Current documented persistence: SharedPreferences`  [INFERRED] [semantically similar]
  docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md → README.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Historical 2026-10-02 implementation plan for original 11 deeds`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md
- `Historical completion report: original 11 deeds, successful unit tests and debug assembly` --conceptually_related_to--> `Tasbeeh Fatima: 34/33/33 stages, audio and haptic feedback`  [INFERRED]
  .superpowers/sdd/2026-10-02-daily-deeds-reminder-app/progress.md → README.md
- `Daily Deeds Reminder: current README` --references--> `Daily Deeds Reminder v1.1.0 release notes`  [EXTRACTED]
  README.md → docs/releases/v1.1.0.md
- `Implementation plan: AlarmManager and BroadcastReceivers survive reboot` --semantically_similar_to--> `Category navigation and independent rescheduling after firing, reboot, updates or time changes`  [INFERRED] [semantically similar]
  docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md → docs/releases/v1.1.0.md

## Import Cycles
- None detected.

## Communities (29 total, 23 thin omitted)

### Community 1 - "MainActivity.kt"
Cohesion: 0.06
Nodes (5): AppNavigation(), BottomTab, MainActivity, HomeScreen(), ReaderCounterScreen()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 4 - "PreferencesManager"
Cohesion: 0.05
Nodes (10): PreferencesManager, ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY (+2 more)

### Community 5 - "NotificationHelper.kt"
Cohesion: 0.10
Nodes (4): AlarmScheduler, NotificationHelper, BootReceiver, DailyReminderReceiver

### Community 6 - "DeedCategory"
Cohesion: 0.10
Nodes (18): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+10 more)

### Community 18 - "DayContentKind"
Cohesion: 0.15
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

## Knowledge Gaps
- **27 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+22 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 159 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **23 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `WeekdayContentScreen.kt`, `PreferencesManager`, `NotificationHelper.kt`, `DeedCategory`, `ReaderCounterScreen.kt`, `HomeScreen.kt`?**
  _High betweenness centrality (0.183) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `MainViewModel`, `NotificationHelper.kt`, `DeedCategory`?**
  _High betweenness centrality (0.136) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `DeedCategory` to `DeedCard.kt`, `MainActivity.kt`, `PreferencesManager`, `NotificationHelper.kt`, `MainViewModel`, `FilterChipRow.kt`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _27 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `DeedCard.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.11695906432748537 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06258890469416785 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._