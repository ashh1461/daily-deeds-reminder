# Graph Report - Daily Reminder  (2026-10-06)

## Corpus Check
- 73 files · ~31,002 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 13 file(s) not represented in the graph (top: .xml 6, (none) 2, .bat 2)

## Summary
- 634 nodes · 1498 edges · 58 communities (14 shown, 44 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 54 edges (avg confidence: 0.93)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `0a80631b`
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
- .nextOccurrence
- MainViewModel
- Completed
- QuranRepository
- Agent Directives & Operational Rules
- WeekdayContentScreen.kt
- DayContentKind
- linear_tool.py
- Repository Soul & Core Identity
- QuranRepositoryTest
- Theme.kt
- .scheduleReminder
- DailyReminderReceiver.kt
- AppNavigation
- .onCreate
- ReaderCounterScreen
- SearchViewModel
- TafsirAlMizan
- QuranViewModel
- .normalize
- BootReceiver.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 36 edges
2. `MainViewModel` - 30 edges
3. `DeedCategory` - 25 edges
4. `Deed` - 25 edges
5. `MafatihViewModel` - 21 edges
6. `QuranViewModel` - 21 edges
7. `ReminderType` - 20 edges
8. `Completed` - 18 edges
9. `Ongoing log` - 18 edges
10. `MafatihCategoryType` - 17 edges

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

## Communities (58 total, 44 thin omitted)

### Community 0 - "MafatihViewModel"
Cohesion: 0.11
Nodes (11): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, BAQIYAT, MUNAJAT, TAQIBAT (+3 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.07
Nodes (20): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Changes made, Transcription and edition notes, Weekday text review, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+12 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "ReminderType"
Cohesion: 0.15
Nodes (9): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+1 more)

### Community 6 - "Deed"
Cohesion: 0.06
Nodes (29): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+21 more)

### Community 9 - "Completed"
Cohesion: 0.15
Nodes (11): GlobalSearchScreen(), SearchResultCard(), MafatihScreen(), QuranReaderScreen(), QuranScreen(), TafsirBottomSheet(), Completed, Ongoing log (+3 more)

### Community 10 - "QuranRepository"
Cohesion: 0.23
Nodes (7): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "DayContentKind"
Cohesion: 0.16
Nodes (8): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, DaySelectorRow(), WeekdayContentScreen(), WeekdayRepositoryTest

### Community 19 - "linear_tool.py"
Cohesion: 0.25
Nodes (5): comment(), create_issue(), query_linear(), teams(), update_issue()

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 32 - "QuranRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 52 - "SearchViewModel"
Cohesion: 0.16
Nodes (8): SearchRepository, SearchResultItem, SearchResultType, ALL, MAFATIH, QURAN, TAFSIR, SearchViewModel

### Community 53 - "TafsirAlMizan"
Cohesion: 0.27
Nodes (3): TafsirAlMizanProvider, TafsirAlMizan, ArabicNormalizer

## Knowledge Gaps
- **52 isolated node(s):** `DUA`, `ZIYARAT`, `READING`, `COUNTER`, `MULTI_STAGE_COUNTER` (+47 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 224 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **44 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `ReminderType`, `Deed`, `.scheduleReminder`, `AppNavigation`, `.onCreate`, `WeekdayContentScreen.kt`, `ReaderCounterScreen`, `MainViewModel.kt`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `ReminderType`, `Deed`, `MainViewModel`, `.scheduleReminder`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `Deed` to `QuranRepositoryTest`, `MainActivity.kt`, `ReminderType`, `MainViewModel`, `DailyReminderReceiver.kt`, `NotificationHelper.kt`, `WeekdayContentScreen.kt`, `MainViewModel.kt`?**
  _High betweenness centrality (0.076) - this node is a cross-community bridge._
- **Are the 2 inferred relationships involving `MafatihViewModel` (e.g. with `Completed` and `Ongoing log`) actually correct?**
  _`MafatihViewModel` has 2 INFERRED edges - model-reasoned connections that need verification._
- **What connects `DUA`, `ZIYARAT`, `READING` to the rest of the system?**
  _52 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MafatihViewModel` be split into smaller, more focused modules?**
  _Cohesion score 0.10967741935483871 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.08695652173913043 - nodes in this community are weakly interconnected._