# Graph Report - Daily Reminder  (2026-10-08)

## Corpus Check
- 77 files · ~271,512 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 15 file(s) not represented in the graph (top: .xml 7, (none) 2, .bat 2)

## Summary
- 687 nodes · 1607 edges · 60 communities (16 shown, 44 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 59 edges (avg confidence: 0.92)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `994a6578`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MafatihViewModel
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- ReminderType
- Deed
- NotificationHelper.kt
- MainViewModel
- QuranViewModel
- Daily Deeds Reminder 1.2.2
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- DayContentKind
- linear_tool.py
- Repository Soul & Core Identity
- SearchRepositoryTest
- MafatihRepositoryTest
- QuranRepositoryTest
- SettingsScreen.kt
- ArabicNormalizerTest
- Daily Deeds Reminder 1.2.1
- SearchRepository
- MafatihFullBookTest
- QuranDataIntegrityTest
- .scheduleReminder
- ColorSchemeContrastTest.kt
- Validation

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 37 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 25 edges
4. `Deed` - 25 edges
5. `QuranViewModel` - 22 edges
6. `MafatihViewModel` - 21 edges
7. `ReminderType` - 20 edges
8. `MafatihCategoryType` - 19 edges
9. `Completed` - 18 edges
10. `Ongoing log` - 18 edges

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

## Communities (60 total, 44 thin omitted)

### Community 0 - "MafatihViewModel"
Cohesion: 0.09
Nodes (13): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, BAQIYAT, FULLBOOK, MUNAJAT (+5 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.05
Nodes (3): BottomTab, MainActivity, DailyReminderTheme()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.07
Nodes (20): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Changes made, Transcription and edition notes, Weekday text review, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+12 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "ReminderType"
Cohesion: 0.24
Nodes (7): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY

### Community 6 - "Deed"
Cohesion: 0.06
Nodes (31): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+23 more)

### Community 7 - "NotificationHelper.kt"
Cohesion: 0.09
Nodes (4): NotificationHelper, ReminderTimeCalculator, DailyReminderReceiver, ReminderTimeCalculatorTest

### Community 9 - "QuranViewModel"
Cohesion: 0.06
Nodes (25): QuranDataProvider, QuranRepository, TafsirAlMizanProvider, AppNavigation(), Ayah, RevelationType, MADANI, MAKKI (+17 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "DayContentKind"
Cohesion: 0.17
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 52 - "SearchRepository"
Cohesion: 0.12
Nodes (10): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, MAFATIH, QURAN, TAFSIR (+2 more)

### Community 59 - "Validation"
Cohesion: 0.40
Nodes (4): Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

## Knowledge Gaps
- **62 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+57 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 240 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **44 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `Deed`, `QuranViewModel`, `SettingsScreen.kt`, `WeekdayContentScreen.kt`, `MainViewModel.kt`, `.scheduleReminder`?**
  _High betweenness centrality (0.087) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `ReminderType`, `Deed`, `NotificationHelper.kt`, `MainViewModel`, `MainViewModel.kt`, `.scheduleReminder`?**
  _High betweenness centrality (0.071) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `Deed` to `MainActivity.kt`, `ReminderType`, `NotificationHelper.kt`, `MainViewModel`, `WeekdayContentScreen.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.068) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `QuranViewModel` (e.g. with `Completed` and `Ongoing log`) actually correct?**
  _`QuranViewModel` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _62 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MafatihViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.0915915915915916 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.05204872646733112 - nodes in this community are weakly interconnected._