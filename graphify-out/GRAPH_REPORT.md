# Graph Report - Daily Reminder  (2026-10-14)

## Corpus Check
- 179 files · ~3,040,049 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 27 file(s) not represented in the graph (top: .xml 13, .ttf 5, (none) 3)

## Summary
- 1848 nodes · 4947 edges · 134 communities (62 shown, 72 thin omitted)
- Extraction: 97% EXTRACTED · 3% INFERRED · 0% AMBIGUOUS · INFERRED: 172 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `3ff02435`
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
- ReaderCounterContent
- Ongoing log
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
- IslamicDecor.kt
- Astro
- SearchResultType
- WidgetUpdater.kt
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- Prayer
- Daily Deeds Reminder 1.4.0
- AdhanService
- QuranRepository
- Google Play listing: draft answers (not submitted)
- MizanScreens.kt
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- ReminderType
- PrayerSettings
- THIRD_PARTY_NOTICES.md
- .next
- QadaState
- WorshipViewModel
- WorshipViewModel.kt
- .item
- Place
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- MainViewModel
- TodayScreen.kt
- VoiceStore
- .next
- VoiceViewModel
- PrayerContext
- PrayerAlarmReceiver.kt
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- VoiceAndPolicyTest
- OccasionAlarmReceiver.kt
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- ToolsViewModel
- Test
- Deed
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- AdhanService.kt
- .build
- DeedCard.kt
- .search
- Test
- materialtheme
- QuranDataIntegrityTest
- PermissionHealth.kt
- SearchRepositoryTest
- Licence of the bundled text datasets
- ReminderTimeCalculatorTest
- Validation
- PrayerTimesScreen
- SearchRepository
- DeedCategory
- .contains
- Wird 1.8.1.1: reliability fixes (pre-release)
- ArabicNormalizerTest
- CoordinateRoundingTest.kt
- SettingsScreen
- AdhanGlobalSettings
- BootReceiver.kt
- AddTo
- AdhanMode
- MainActivity
- merge_stack.sh
- QuranRepositoryTest
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt
- ReaderReadingContent
- CalcMethod
- Application
- ReaderTopBar

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
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `v1.2.1 corrective release (2026-10-07)` --references--> `QuranDataIntegrityTest`  [INFERRED]
  progress.md → app/src/test/java/com/dailydeeds/reminder/data/QuranDataIntegrityTest.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `BundledVoices`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/adhan/BundledVoices.kt

## Import Cycles
- None detected.

## Communities (134 total, 72 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.06
Nodes (14): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+6 more)

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
Cohesion: 0.27
Nodes (3): AlarmText, PrayerAlarmText, PrayerAlarmTextTest

### Community 8 - "ReaderCounterContent"
Cohesion: 0.40
Nodes (3): CounterOrb(), CounterStageRow(), ReaderCounterContent()

### Community 9 - "Ongoing log"
Cohesion: 0.14
Nodes (11): GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), Completed, Ongoing log, Progress (+3 more)

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
Cohesion: 0.15
Nodes (10): Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT, ShiaCalendar (+2 more)

### Community 19 - "build_mizan.py"
Cohesion: 0.06
Nodes (22): body_of(), build(), clean_text(), nrm(), parse_sections(), parse_units(), render(), split_inline_pages() (+14 more)

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 27 - "AppNavigation"
Cohesion: 0.16
Nodes (18): AppNavigation(), CalendarScreen(), FavoriteRow, FavoritesScreen(), entryTitle(), MizanAyahScreen(), MizanCard(), MizanHomeContent() (+10 more)

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.10
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.09
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 43 - "SearchResultType"
Cohesion: 0.18
Nodes (9): SearchResultItem, SearchResultType, ALL, DEEDS, MAFATIH, QURAN, SAHIFA, WEEKDAY (+1 more)

### Community 45 - "WidgetUpdater.kt"
Cohesion: 0.08
Nodes (4): PrayerWidgetProvider, WidgetUpdater, TimetablePdf, Widget

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "Prayer"
Cohesion: 0.13
Nodes (14): PrayerAlarmConfig, AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, Prayer (+6 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 56 - "QuranRepository"
Cohesion: 0.12
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

### Community 66 - "QadaState"
Cohesion: 0.14
Nodes (10): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+2 more)

### Community 69 - ".item"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 70 - "Place"
Cohesion: 0.21
Nodes (5): Place, PlacePresets, scheduleAll(), PrayerSchedule, WidgetModel

### Community 71 - "MizanViewModel"
Cohesion: 0.21
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

### Community 76 - "TodayScreen.kt"
Cohesion: 0.13
Nodes (4): cardBorder(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 80 - "VoiceViewModel"
Cohesion: 0.21
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "PrayerContext"
Cohesion: 0.16
Nodes (7): HijriDay, PrayerContext, PrayerTimesResult, Timetable, TimetableRow, TimetableTest, WidgetModelTest

### Community 83 - "KhumsScreen"
Cohesion: 0.14
Nodes (18): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+10 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (5): ZahraPanel(), Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "VoiceAndPolicyTest"
Cohesion: 0.07
Nodes (16): BundledVoice, BundledVoices, ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), BundledVoicesTest (+8 more)

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.60
Nodes (5): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow()

### Community 91 - "ToolsViewModel"
Cohesion: 0.15
Nodes (3): QiblaScreen(), rememberTrueHeading(), ToolsViewModel

### Community 92 - "Test"
Cohesion: 0.17
Nodes (5): Khums, KhumsInput, KhumsResult, KhumsYear, KhumsTest

### Community 93 - "Deed"
Cohesion: 0.16
Nodes (8): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, DeedsRepositoryTest

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 98 - ".build"
Cohesion: 0.36
Nodes (4): DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 101 - "Test"
Cohesion: 0.09
Nodes (12): BackupCodec, Error, Import, Ok, BackupCodecTest, Backup import, Components, Dependencies (+4 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 108 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 109 - "PrayerTimesScreen"
Cohesion: 0.15
Nodes (10): rememberNow(), CitySearchDialog(), CoordinatesDialog(), PrayerTimesScreen(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles() (+2 more)

### Community 113 - "DeedCategory"
Cohesion: 0.18
Nodes (10): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+2 more)

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 117 - "CoordinateRoundingTest.kt"
Cohesion: 0.33
Nodes (3): lastKnownPlace(), roundCoordinate(), CoordinateRoundingTest

### Community 118 - "SettingsScreen"
Cohesion: 0.48
Nodes (5): ReminderSettingCard(), restartApp(), SectionTitle(), SettingsCard(), SettingsScreen()

### Community 121 - "AddTo"
Cohesion: 0.67
Nodes (3): AddTo, Bulk, QadaDialog

### Community 122 - "AdhanMode"
Cohesion: 0.25
Nodes (6): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, PrayerParams

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified on a device, The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

### Community 130 - "CalcMethod"
Cohesion: 0.50
Nodes (4): CalcMethod, CUSTOM, LEVA, TEHRAN

## Knowledge Gaps
- **193 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+188 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 496 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **72 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `.checkAndResetDaily`, `FavoriteKey`, `WeekdayContentScreen.kt`, `WidgetUpdater.kt`, `Prayer`, `AdhanService`, `ReminderType`, `PrayerSettings`, `QadaState`, `WorshipViewModel`, `WorshipViewModel.kt`, `Place`, `MainViewModel`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `OccasionAlarmReceiver.kt`, `AdhanActionReceiver.kt`, `ToolsViewModel`, `Test`, `Deed`, `AdhanService.kt`, `Test`, `AdhanGlobalSettings`, `MainActivity`?**
  _High betweenness centrality (0.098) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `FavoriteKey`, `Ongoing log`, `WeekdayContentScreen.kt`, `AppNavigation`, `QuranReaderScreen.kt`, `Prayer`, `MizanScreens.kt`, `PrayerSettings`, `Place`, `TodayScreen.kt`, `PrayerContext`, `KhumsScreen`, `PrayerSettingsScreen`, `PrayerTimesScreen`, `SettingsScreen`, `AdhanGlobalSettings`, `MainActivity`?**
  _High betweenness centrality (0.048) - this node is a cross-community bridge._
- **Why does `Place` connect `Place` to `.next`, `.build`, `Astro`, `PreferencesManager`, `Test`, `.search`, `PrayerTimesScreen`, `.next`, `WeekdayContentScreen.kt`, `PrayerContext`, `Prayer`, `CoordinateRoundingTest.kt`, `asserttrue`, `ToolsViewModel`?**
  _High betweenness centrality (0.044) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _193 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.05754475703324808 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.07142857142857142 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._