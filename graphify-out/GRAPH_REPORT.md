# Graph Report - Daily Reminder  (2026-10-04)

## Corpus Check
- 43 files · ~18,343 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 16 file(s) not represented in the graph (top: .xml 6, (none) 2, .bat 2)

## Summary
- 448 nodes · 916 edges · 40 communities (8 shown, 32 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 11 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `a59f8330`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- HomeScreen.kt
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- ReminderType
- Deed
- .nextOccurrence
- MainViewModel
- Progress
- DayContentKind
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
- `Daily Deeds Reminder: current README` --references--> `Daily Deeds Reminder v1.1.0 release notes`  [EXTRACTED]
  README.md → docs/releases/v1.1.0.md
- `Implementation plan: AlarmManager and BroadcastReceivers survive reboot` --semantically_similar_to--> `Category navigation and independent rescheduling after firing, reboot, updates or time changes`  [INFERRED] [semantically similar]
  docs/superpowers/plans/2026-10-02-daily-deeds-reminder-app.md → docs/releases/v1.1.0.md

## Import Cycles
- None detected.

## Communities (40 total, 32 thin omitted)

### Community 0 - "HomeScreen.kt"
Cohesion: 0.13
Nodes (7): CircularProgressBar(), DeedCard(), FilterChipRow(), DailyProgressCard(), HomeContent(), HomePreview(), HomeScreen()

### Community 1 - "MainActivity.kt"
Cohesion: 0.05
Nodes (4): AppNavigation(), BottomTab, MainActivity, DailyReminderTheme()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.07
Nodes (20): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Changes made, Transcription and edition notes, Weekday text review, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+12 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "ReminderType"
Cohesion: 0.06
Nodes (13): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+5 more)

### Community 6 - "Deed"
Cohesion: 0.06
Nodes (25): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+17 more)

### Community 9 - "Progress"
Cohesion: 0.33
Nodes (5): Completed, Ongoing log, Progress, Remaining, Verification limits

### Community 18 - "DayContentKind"
Cohesion: 0.15
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

## Knowledge Gaps
- **35 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+30 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 184 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **32 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `HomeScreen.kt`, `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `Deed`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `Deed` to `HomeScreen.kt`, `MainActivity.kt`, `ReminderType`, `MainViewModel`, `WeekdayContentScreen.kt`?**
  _High betweenness centrality (0.112) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `MainViewModel`, `ReminderType`, `Deed`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _35 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `HomeScreen.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.12681159420289856 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.05426356589147287 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.07196969696969698 - nodes in this community are weakly interconnected._