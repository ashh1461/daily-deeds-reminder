# Graph Report - Daily Reminder  (2026-10-04)

## Corpus Check
- 35 files · ~16,270 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 387 nodes · 651 edges · 44 communities (19 shown, 25 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 5 edges (avg confidence: 0.87)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8e5ca15c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- DeedCard.kt
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- WeekdayContentScreen.kt
- PreferencesManager
- NotificationHelper.kt
- DeedCategory
- ReminderTimeCalculatorTest
- MainViewModel
- Theme.kt
- CircularProgressBar.kt
- ReaderCounterScreen.kt
- accesstime
- alertdialog
- FilterChipRow.kt
- HomeScreen.kt
- daycontentkind
- DayContentKind
- ReminderType
- DeedsRepositoryTest
- fontweight
- calendar
- divider
- notifications
- switch
- switchdefaults
- textbutton
- vibration
- volumeup
- ColorSchemeContrastTest.kt
- brush
- chevronleft
- fillmaxheight
- golddark
- goldlight
- goldprimary
- navydark
- navyprimary
- navysecondary
- successgreen
- textoverflow

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 36 edges
2. `MainViewModel` - 29 edges
3. `DeedCategory` - 23 edges
4. `ReminderType` - 19 edges
5. `Daily Deeds Reminder v1.1.0 release notes` - 15 edges
6. `Deed` - 11 edges
7. `DeedsRepository` - 10 edges
8. `DayContentKind` - 10 edges
9. `ReminderTimeCalculatorTest` - 10 edges
10. `DeedType` - 9 edges

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

## Communities (44 total, 25 thin omitted)

### Community 0 - "DeedCard.kt"
Cohesion: 0.20
Nodes (10): animatecolorasstate, DeedCard(), Modifier, borderstroke, card, carddefaults, check, experimentalmaterial3api (+2 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.07
Nodes (31): activityresultcontracts, AppNavigation(), BottomTab, MainActivity, HomeContent(), HomePreview(), HomeScreen(), ReaderCounterScreen() (+23 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (29): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, Date changes refresh progress; counter actions check date before saving, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Release limitation: installation, UI and device notification delivery unverified, Editable defaults: night 21:00, bedtime 22:00, Thursday 07:00 in device time zone, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions (+21 more)

### Community 3 - "WeekdayContentScreen.kt"
Cohesion: 0.11
Nodes (18): annotatedstring, background, border, clip, contentcopy, height, lazyrow, localclipboardmanager (+10 more)

### Community 5 - "NotificationHelper.kt"
Cohesion: 0.10
Nodes (20): alarmmanager, AlarmScheduler, Context, Context, NotificationHelper, BootReceiver, BroadcastReceiver, Context (+12 more)

### Community 6 - "DeedCategory"
Cohesion: 0.11
Nodes (19): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+11 more)

### Community 8 - "MainViewModel"
Cohesion: 0.11
Nodes (13): AndroidViewModel, MainViewModel, application, asstateflow, delay, isactive, launch, mutablestateflow (+5 more)

### Community 9 - "Theme.kt"
Cohesion: 0.15
Nodes (12): activity, compositionlocalprovider, darkcolorscheme, issystemindarktheme, layoutdirection, lightcolorscheme, locallayoutdirection, localview (+4 more)

### Community 10 - "CircularProgressBar.kt"
Cohesion: 0.15
Nodes (15): alignment, animatefloatasstate, CircularProgressBar(), Color, Modifier, box, canvas, column (+7 more)

### Community 11 - "ReaderCounterScreen.kt"
Cohesion: 0.13
Nodes (13): animatedvisibility, arrowforward, button, buttondefaults, circleshape, clickable, info, mutablefloatstateof (+5 more)

### Community 15 - "FilterChipRow.kt"
Cohesion: 0.20
Nodes (10): FilterChipRow(), Modifier, arrangement, fillmaxwidth, filterchip, filterchipdefaults, horizontalscroll, padding (+2 more)

### Community 16 - "HomeScreen.kt"
Cohesion: 0.15
Nodes (13): DailyProgressCard(), Modifier, collectasstate, fillmaxsize, iconbutton, items, lazycolumn, paddingvalues (+5 more)

### Community 18 - "DayContentKind"
Cohesion: 0.10
Nodes (13): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, ReminderTimeCalculator, WeekdayRepositoryTest, assertequals (+5 more)

### Community 19 - "ReminderType"
Cohesion: 0.09
Nodes (23): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+15 more)

### Community 21 - "fontweight"
Cohesion: 0.33
Nodes (5): fontfamily, fontweight, sp, textstyle, typography

### Community 32 - "ColorSchemeContrastTest.kt"
Cohesion: 0.47
Nodes (3): ColorSchemeContrastTest, Color, asserttrue

## Knowledge Gaps
- **28 isolated node(s):** `BottomTab`, `DUA`, `ZIYARAT`, `READING`, `COUNTER` (+23 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **25 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainViewModel` connect `MainViewModel` to `MainActivity.kt`, `PreferencesManager`, `DeedCategory`, `ReaderCounterScreen.kt`, `HomeScreen.kt`, `ReminderType`?**
  _High betweenness centrality (0.176) - this node is a cross-community bridge._
- **Why does `PreferencesManager` connect `PreferencesManager` to `MainViewModel`, `ReminderType`, `NotificationHelper.kt`?**
  _High betweenness centrality (0.139) - this node is a cross-community bridge._
- **Why does `DeedCategory` connect `DeedCategory` to `MainActivity.kt`, `NotificationHelper.kt`, `MainViewModel`, `FilterChipRow.kt`, `HomeScreen.kt`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **What connects `BottomTab`, `DUA`, `ZIYARAT` to the rest of the system?**
  _28 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07308377896613191 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._
- **Should `WeekdayContentScreen.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.10526315789473684 - nodes in this community are weakly interconnected._