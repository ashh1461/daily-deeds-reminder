# Graph Report - Daily Reminder  (2026-10-07)

## Corpus Check
- 75 files · ~113,381 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 15 file(s) not represented in the graph (top: .xml 7, (none) 2, .bat 2)

## Summary
- 670 nodes · 1576 edges · 57 communities (16 shown, 41 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 57 edges (avg confidence: 0.93)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5b4e78c6`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MafatihCategoryType
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- ReminderType
- Deed
- .nextOccurrence
- MainViewModel
- Completed
- Ayah
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- DayContentKind
- linear_tool.py
- Repository Soul & Core Identity
- SearchRepositoryTest
- MafatihViewModel
- .scheduleReminder
- DailyReminderReceiver.kt
- Daily Deeds Reminder 1.2.1
- SearchRepository
- QuranRepository
- QuranViewModel
- BootReceiver.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 37 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 25 edges
4. `Deed` - 25 edges
5. `QuranViewModel` - 22 edges
6. `MafatihViewModel` - 21 edges
7. `ReminderType` - 20 edges
8. `Completed` - 18 edges
9. `Ongoing log` - 18 edges
10. `MafatihCategoryType` - 17 edges

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

## Communities (57 total, 41 thin omitted)

### Community 0 - "MafatihCategoryType"
Cohesion: 0.13
Nodes (11): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, BAQIYAT, MUNAJAT, TAQIBAT (+3 more)

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
Cohesion: 0.14
Nodes (9): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+1 more)

### Community 6 - "Deed"
Cohesion: 0.05
Nodes (32): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+24 more)

### Community 9 - "Completed"
Cohesion: 0.12
Nodes (15): AppNavigation(), GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), MafatihScreen(), WeekdayShortcuts(), QuranReaderScreen(), QuranScreen() (+7 more)

### Community 10 - "Ayah"
Cohesion: 0.33
Nodes (6): QuranDataProvider, Ayah, RevelationType, MADANI, MAKKI, Surah

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

### Community 32 - "SearchRepositoryTest"
Cohesion: 0.05
Nodes (10): MafatihRepositoryTest, QuranDataIntegrityTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, ArabicNormalizerTest, Daily Deeds Reminder 1.2.0, Distribution (+2 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 52 - "SearchRepository"
Cohesion: 0.14
Nodes (10): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, MAFATIH, QURAN, TAFSIR (+2 more)

### Community 53 - "QuranRepository"
Cohesion: 0.22
Nodes (3): QuranRepository, TafsirAlMizanProvider, TafsirAlMizan

## Knowledge Gaps
- **57 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+52 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 235 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **41 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `Deed`, `Completed`, `.scheduleReminder`, `WeekdayContentScreen.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `ReminderType`, `Deed`, `MainViewModel`, `.scheduleReminder`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.073) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `Deed` to `SearchRepositoryTest`, `MainActivity.kt`, `ReminderType`, `MainViewModel`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `WeekdayContentScreen.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.071) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `QuranViewModel` (e.g. with `Completed` and `Ongoing log`) actually correct?**
  _`QuranViewModel` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _57 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MafatihCategoryType` be split into smaller, more focused modules?**
  _Cohesion score 0.13333333333333333 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.0507399577167019 - nodes in this community are weakly interconnected._