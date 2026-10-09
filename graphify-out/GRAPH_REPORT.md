# Graph Report - Daily Reminder  (2026-10-09)

## Corpus Check
- 170 files · ~3,032,055 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 26 file(s) not represented in the graph (top: .xml 12, .ttf 5, (none) 3)

## Summary
- 1758 nodes · 4706 edges · 112 communities (53 shown, 59 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 158 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `64efbe8e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MizanData
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- FavoriteKey
- AdhanService
- MainViewModel
- QuranViewModel
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- generate_icons.py
- Agent Directives & Operational Rules
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- asserttrue
- AppNavigation
- SensorEventListener
- MafatihRepositoryTest
- TodayHeader.kt
- Astro
- DayContentKind
- .refresh
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- QuranRepository
- Google Play listing: draft answers (not submitted)
- MizanScreens.kt
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- ReminderType
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- .next
- TimetablePdf.kt
- WorshipViewModel
- SahifaViewModel.kt
- .item
- Place
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- .search
- PrayerTimesScreen.kt
- Timetable.kt
- PrayerAlarmConfig
- VoiceViewModel
- .toHijri
- PrayerAlarmReceiver.kt
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- AdhanMode
- OccasionAlarmReceiver.kt
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- PrayerContext
- Khums.kt
- Application
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- PermissionHealth.kt
- .build
- DeedCard.kt
- KhumsYear
- OccasionKind
- materialtheme
- QuranDataIntegrityTest
- NoEmptySourceFilesTest
- Test
- Licence of the bundled text datasets
- VoicePacksScreen
- QiblaScreen

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 92 edges
2. `ToolsViewModel` - 56 edges
3. `WorshipViewModel` - 43 edges
4. `AppNavigation()` - 39 edges
5. `Place` - 35 edges
6. `Prayer` - 32 edges
7. `PrayerContext` - 32 edges
8. `MainViewModel` - 29 edges
9. `cardBorder()` - 28 edges
10. `MafatihRepository` - 26 edges

## Surprising Connections (you probably didn't know these)
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `v1.2.1 corrective release (2026-10-07)` --references--> `QuranDataIntegrityTest`  [INFERRED]
  progress.md → app/src/test/java/com/dailydeeds/reminder/data/QuranDataIntegrityTest.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MafatihDataProvider`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/data/MafatihDataProvider.kt

## Import Cycles
- None detected.

## Communities (112 total, 59 thin omitted)

### Community 0 - "MizanData"
Cohesion: 0.06
Nodes (13): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+5 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.07
Nodes (3): AppTab, MainActivity, AppTopBar()

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.09
Nodes (8): PrayerParams, PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "FavoriteKey"
Cohesion: 0.15
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 8 - "MainViewModel"
Cohesion: 0.05
Nodes (26): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+18 more)

### Community 9 - "QuranViewModel"
Cohesion: 0.11
Nodes (10): MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), QuranViewModel, Completed, Ongoing log, Progress, Remaining (+2 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.12
Nodes (12): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+4 more)

### Community 13 - "generate_icons.py"
Cohesion: 0.17
Nodes (12): android_resources(), circle_path(), crescent_path(), draw_mark(), f(), octagram(), play_assets(), star_path() (+4 more)

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.24
Nodes (4): Occasion, ShiaCalendar, Upcoming, ShiaCalendarTest

### Community 19 - "build_mizan.py"
Cohesion: 0.06
Nodes (22): body_of(), build(), clean_text(), nrm(), parse_sections(), parse_units(), render(), split_inline_pages() (+14 more)

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "AppNavigation"
Cohesion: 0.17
Nodes (17): AppNavigation(), CalendarScreen(), FavoriteRow, FavoritesScreen(), GlobalSearchScreen(), SearchResultCard(), entryTitle(), MizanAyahScreen() (+9 more)

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "TodayHeader.kt"
Cohesion: 0.16
Nodes (5): EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 43 - "DayContentKind"
Cohesion: 0.06
Nodes (21): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+13 more)

### Community 45 - ".refresh"
Cohesion: 0.06
Nodes (15): BackupCodec, Error, Import, Ok, PrayerWidgetProvider, WidgetUpdater, BackupCodecTest, Backup import (+7 more)

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.09
Nodes (14): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, scheduleAll(), ReminderTimeCalculator (+6 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 56 - "QuranRepository"
Cohesion: 0.17
Nodes (7): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 62 - "ReminderType"
Cohesion: 0.07
Nodes (16): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+8 more)

### Community 63 - "ToolsViewModel"
Cohesion: 0.05
Nodes (19): AdhanGlobalSettings, CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, rememberNow() (+11 more)

### Community 67 - "WorshipViewModel"
Cohesion: 0.07
Nodes (14): AddTo, Bulk, QadaDialog, BackupStatus, WorshipViewModel, QadaEntry, QadaKind, ASR (+6 more)

### Community 69 - ".item"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 71 - "MizanViewModel"
Cohesion: 0.19
Nodes (6): Failed, Loading, MizanSearchState, MizanState, MizanViewModel, Ready

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 73 - "Wird 1.8.1: the complete Tafsir al-Mizan"
Cohesion: 0.29
Nodes (6): Known limits, Licence correction, Tafsir al-Mizan, complete, Verification, What is still not full, Wird 1.8.1: the complete Tafsir al-Mizan

### Community 74 - "Daily Deeds Reminder 1.5.0"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.5.0, New structure, Search, settings and fixes, Verification, What changed in the Mafatih tab

### Community 75 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 76 - "PrayerTimesScreen.kt"
Cohesion: 0.10
Nodes (5): cardBorder(), lastKnownPlace(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 78 - "Timetable.kt"
Cohesion: 0.47
Nodes (3): HijriDay, PrayerTimesResult, TimetableRow

### Community 80 - "VoiceViewModel"
Cohesion: 0.06
Nodes (17): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), Installed, VoiceStore, Failed (+9 more)

### Community 83 - "KhumsScreen"
Cohesion: 0.14
Nodes (19): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+11 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (4): Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "AdhanMode"
Cohesion: 0.33
Nodes (5): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

### Community 91 - "PrayerContext"
Cohesion: 0.23
Nodes (5): PrayerContext, PrayerSchedule, WidgetModel, ImsakPlannerTest, WidgetModelTest

### Community 92 - "Khums.kt"
Cohesion: 0.28
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 98 - ".build"
Cohesion: 0.36
Nodes (4): DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 101 - "OccasionKind"
Cohesion: 0.33
Nodes (6): OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 108 - "VoicePacksScreen"
Cohesion: 0.83
Nodes (3): Box2(), Title(), VoicePacksScreen()

## Knowledge Gaps
- **177 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+172 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 472 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **59 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `AdhanService`, `MainViewModel`, `PrayerSettingsScreen.kt`, `.refresh`, `Prayer`, `AdhanService.kt`, `ReminderType`, `ToolsViewModel`, `WorshipViewModel`, `Place`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `OccasionAlarmReceiver.kt`, `AdhanActionReceiver.kt`, `KhumsYear`, `.checkAndResetDaily`?**
  _High betweenness centrality (0.115) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `FavoriteKey`, `QuranViewModel`, `PrayerSettingsScreen.kt`, `AppNavigation`, `TodayHeader.kt`, `DayContentKind`, `WeekdayContentScreen.kt`, `Prayer`, `QuranReaderScreen.kt`, `ReminderType`, `Place`, `PrayerTimesScreen.kt`, `PrayerAlarmConfig`, `KhumsScreen`, `PrayerSettingsScreen`, `PrayerContext`, `QiblaScreen`?**
  _High betweenness centrality (0.066) - this node is a cross-community bridge._
- **Why does `Place` connect `Place` to `.next`, `.build`, `Astro`, `PreferencesManager`, `.search`, `PrayerTimesScreen.kt`, `.refresh`, `Timetable.kt`, `PrayerAlarmConfig`, `PrayerSettingsScreen.kt`, `.toHijri`, `Prayer`, `asserttrue`, `PrayerContext`, `ToolsViewModel`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _177 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanData` be split into smaller, more focused modules?**
  _Cohesion score 0.0629800307219662 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06854838709677419 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._