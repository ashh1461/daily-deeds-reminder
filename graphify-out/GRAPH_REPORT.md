# Graph Report - Daily Reminder  (2026-10-13)

## Corpus Check
- 174 files · ~3,035,626 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 27 file(s) not represented in the graph (top: .xml 13, .ttf 5, (none) 3)

## Summary
- 1806 nodes · 4858 edges · 126 communities (60 shown, 66 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 162 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9d6d7da7`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MizanDataTest
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- FavoriteKey
- AlarmText
- Deed
- Completed
- Daily Deeds Reminder 1.2.2
- MafatihRepository
- generate_icons.py
- Agent Directives & Operational Rules
- PrayerTimesScreen.kt
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- asserttrue
- AppNavigation
- QiblaScreen
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
- SearchResultType
- WidgetUpdater.kt
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- AdhanService.kt
- QuranRepository
- Google Play listing: draft answers (not submitted)
- MizanScreens.kt
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- ReminderType
- PrayerSettings
- THIRD_PARTY_NOTICES.md
- .timesFor
- QadaState
- WorshipViewModel
- SahifaViewModel.kt
- PrayerTimesScreen
- Place
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- MainViewModel
- WorshipHubScreen.kt
- VoiceStore
- .next
- VoiceViewModel
- PrayerContext
- .handle
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- VoiceAndPolicyTest
- OccasionAlarmReceiver.kt
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- ToolsViewModel
- Khums.kt
- DeedsRepositoryTest
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- SearchViewModel
- .build
- DeedCard.kt
- KhumsYear
- BackupCodec
- KhumsScreen.kt
- QuranDataIntegrityTest
- BackupCodecTest
- Test
- Licence of the bundled text datasets
- ReminderTimeCalculatorTest
- VoicePacksScreen
- TodayScreen
- VoiceModels.kt
- DeedCategory
- .contains
- Security notes
- ArabicNormalizerTest
- lastKnownPlace
- SettingsScreen
- AdhanGlobalSettings
- BootReceiver.kt
- QadaScreen
- Adhan voice packs
- AppTopBar
- merge_stack.sh

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 92 edges
2. `ToolsViewModel` - 56 edges
3. `WorshipViewModel` - 43 edges
4. `Prayer` - 40 edges
5. `AppNavigation()` - 38 edges
6. `Place` - 37 edges
7. `PrayerContext` - 34 edges
8. `MainViewModel` - 29 edges
9. `cardBorder()` - 28 edges
10. `MafatihRepository` - 26 edges

## Surprising Connections (you probably didn't know these)
- `Widget` --references--> `PrayerWidgetProvider`  [INFERRED]
  docs/SECURITY.md → app/src/main/java/com/dailydeeds/reminder/widget/PrayerWidgetProvider.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `v1.2.1 corrective release (2026-10-07)` --references--> `QuranDataIntegrityTest`  [INFERRED]
  progress.md → app/src/test/java/com/dailydeeds/reminder/data/QuranDataIntegrityTest.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt

## Import Cycles
- None detected.

## Communities (126 total, 66 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.05
Nodes (16): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+8 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.09
Nodes (7): PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "FavoriteKey"
Cohesion: 0.08
Nodes (15): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, WeekdayRepository, DayContent (+7 more)

### Community 7 - "AlarmText"
Cohesion: 0.24
Nodes (3): AlarmText, PrayerAlarmText, PrayerAlarmTextTest

### Community 8 - "Deed"
Cohesion: 0.13
Nodes (13): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+5 more)

### Community 9 - "Completed"
Cohesion: 0.14
Nodes (11): GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), QuranScreen(), ArabicNormalizer, Completed, Ongoing log, Progress (+3 more)

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
Cohesion: 0.13
Nodes (13): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+5 more)

### Community 19 - "build_mizan.py"
Cohesion: 0.06
Nodes (22): body_of(), build(), clean_text(), nrm(), parse_sections(), parse_units(), render(), split_inline_pages() (+14 more)

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "AppNavigation"
Cohesion: 0.20
Nodes (13): AppNavigation(), CalendarScreen(), FavoriteRow, FavoritesScreen(), MizanAyahScreen(), MizanCard(), MizanHomeContent(), MizanHomeScreen() (+5 more)

### Community 29 - "QiblaScreen"
Cohesion: 0.29
Nodes (3): QiblaScreen(), rememberTrueHeading(), SensorEventListener

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.08
Nodes (7): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), QuranReaderScreen()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 43 - "SearchResultType"
Cohesion: 0.19
Nodes (10): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, DEEDS, MAFATIH, QURAN (+2 more)

### Community 45 - "WidgetUpdater.kt"
Cohesion: 0.08
Nodes (3): PrayerWidgetProvider, WidgetUpdater, TimetablePdf

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.10
Nodes (20): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, PrayerAlarmConfig, AdhanPlanner, AlarmKind (+12 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 56 - "QuranRepository"
Cohesion: 0.11
Nodes (8): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah, QuranViewModel

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 62 - "ReminderType"
Cohesion: 0.11
Nodes (9): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+1 more)

### Community 63 - "PrayerSettings"
Cohesion: 0.14
Nodes (7): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, AdhanModelsTest

### Community 66 - "QadaState"
Cohesion: 0.18
Nodes (9): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+1 more)

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.08
Nodes (17): City, PlaceSearch, buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows() (+9 more)

### Community 70 - "Place"
Cohesion: 0.22
Nodes (4): Place, PlacePresets, ReminderTimeCalculator, WidgetModel

### Community 71 - "MizanViewModel"
Cohesion: 0.23
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

### Community 76 - "WorshipHubScreen.kt"
Cohesion: 0.10
Nodes (4): canScheduleExactAlarms(), PermissionHealthCard(), HubEntry, WorshipHubScreen()

### Community 80 - "VoiceViewModel"
Cohesion: 0.19
Nodes (7): RemoteVoice, Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "PrayerContext"
Cohesion: 0.22
Nodes (5): PrayerContext, PrayerParams, Timetable, TimetableTest, WidgetModelTest

### Community 83 - "KhumsScreen"
Cohesion: 0.16
Nodes (16): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), Cell(), clock(), RamadanScreen() (+8 more)

### Community 84 - "TasbihState"
Cohesion: 0.16
Nodes (4): ZahraPanel(), Stage, TasbihState, ZahraStages

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

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

### Community 101 - "BackupCodec"
Cohesion: 0.22
Nodes (4): BackupCodec, Error, Import, Ok

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 108 - "VoicePacksScreen"
Cohesion: 0.83
Nodes (3): Box2(), Title(), VoicePacksScreen()

### Community 109 - "TodayScreen"
Cohesion: 0.32
Nodes (6): rememberNow(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen()

### Community 112 - "VoiceModels.kt"
Cohesion: 0.22
Nodes (4): ParseResult, VoiceManifest, VoiceUrlPolicy, fetchManifest()

### Community 113 - "DeedCategory"
Cohesion: 0.18
Nodes (10): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+2 more)

### Community 115 - "Security notes"
Cohesion: 0.20
Nodes (8): Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`), Security notes, Signing, Widget

### Community 117 - "lastKnownPlace"
Cohesion: 0.32
Nodes (3): lastKnownPlace(), roundCoordinate(), CoordinateRoundingTest

### Community 118 - "SettingsScreen"
Cohesion: 0.48
Nodes (5): ReminderSettingCard(), restartApp(), SectionTitle(), SettingsCard(), SettingsScreen()

### Community 121 - "QadaScreen"
Cohesion: 0.40
Nodes (5): AddTo, Bulk, NumberDialog(), QadaDialog, QadaScreen()

### Community 122 - "Adhan voice packs"
Cohesion: 0.50
Nodes (3): Adding a voice, Adhan voice packs, Rights gate

## Knowledge Gaps
- **178 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+173 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 479 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **66 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `WidgetUpdater.kt`, `Prayer`, `AdhanService.kt`, `GlobalSearchScreen.kt`, `ReminderType`, `PrayerSettings`, `QadaState`, `WorshipViewModel`, `Place`, `MainViewModel`, `.handle`, `KhumsScreen`, `TasbihState`, `OccasionAlarmReceiver.kt`, `AdhanActionReceiver.kt`, `ToolsViewModel`, `DeedsRepositoryTest`, `KhumsYear`, `BackupCodec`, `BackupCodecTest`, `Security notes`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.094) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `FavoriteKey`, `Completed`, `PrayerTimesScreen.kt`, `AppNavigation`, `QiblaScreen`, `IslamicDecor.kt`, `WeekdayContentScreen.kt`, `Prayer`, `GlobalSearchScreen.kt`, `PrayerSettings`, `PrayerTimesScreen`, `Place`, `PrayerContext`, `KhumsScreen`, `PrayerSettingsScreen`, `KhumsScreen.kt`, `TodayScreen`, `SettingsScreen`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.069) - this node is a cross-community bridge._
- **Why does `Prayer` connect `Prayer` to `PreferencesManager`, `Place`, `AlarmText`, `WidgetUpdater.kt`, `.next`, `PrayerTimesScreen.kt`, `PrayerContext`, `.handle`, `asserttrue`, `AdhanService.kt`, `AdhanActionReceiver.kt`, `ToolsViewModel`, `PrayerSettings`?**
  _High betweenness centrality (0.054) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _178 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.0532724505327245 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07386363636363637 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._