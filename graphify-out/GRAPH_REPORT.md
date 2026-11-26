# Graph Report - Daily Reminder  (2026-11-25)

## Corpus Check
- 197 files · ~3,045,735 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 27 file(s) not represented in the graph (top: .xml 13, .ttf 5, (none) 3)

## Summary
- 2033 nodes · 5492 edges · 111 communities (74 shown, 37 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 209 edges (avg confidence: 0.89)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `bf40776f`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MizanDataTest
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- DayContentKind
- Prayer
- FavoriteKey
- Ongoing log
- Daily Deeds Reminder 1.2.2
- steps
- MafatihRepository
- DeviceLocation.kt
- Agent Directives & Operational Rules
- LunarMonthsScreen
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- assertequals
- steps
- ReaderCounterScreen
- DeedCategory
- SahifaViewModel.kt
- Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)
- PrayerAlarmConfig
- .nearest
- CoordinateInput
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
- PlacePickerCard
- .parse
- Wird «ورد»: review findings and improvement plan
- updates
- QadaScreen
- SensorEventListener
- Bundled adhan recordings
- VoiceAndPolicyTest
- WidgetUpdater.kt
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- MafatihViewModel
- PrayerContext
- Daily Deeds Reminder 1.4.0
- AdhanService
- QuranRepository
- Google Play listing: draft answers (not submitted)
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- AlarmScheduler.kt
- PrayerSettings
- THIRD_PARTY_NOTICES.md
- PrayerContext
- QadaState
- WorshipViewModel
- .parse
- PrayerTimesScreen
- Test
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- MainViewModel
- TodayScreen.kt
- VoiceStore
- .next
- VoiceViewModel
- Test
- PrayerAlarmReceiver.kt
- KhumsScreen
- TasbihState
- Daily Deeds Reminder 1.6.0
- BundledVoicesTest
- BroadcastReceiver
- PrayerSettingsScreen
- manifest.json
- ToolsViewModel
- Test
- Deed
- DeedCategory
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- AdhanService.kt
- .build
- DeedCard.kt
- .search
- PermissionHealth.kt
- ReminderType
- Licence of the bundled text datasets
- ReminderTimeCalculatorTest
- TodayScreen
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- BundledVoices
- Wird 1.8.1.1: reliability fixes (pre-release)
- CoordinateRoundingTest.kt
- AdhanGlobalSettings
- AdhanMode
- Adhan voice packs
- merge_stack.sh
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 93 edges
2. `ToolsViewModel` - 58 edges
3. `Prayer` - 45 edges
4. `WorshipViewModel` - 43 edges
5. `AppNavigation()` - 40 edges
6. `Place` - 40 edges
7. `PrayerContext` - 35 edges
8. `MafatihItem` - 33 edges
9. `AdhanService` - 32 edges
10. `PrayerAlarmConfig` - 31 edges

## Surprising Connections (you probably didn't know these)
- `Result files` --references--> `BundledVoicesTest`  [INFERRED]
  docs/licenses/ADHAN_AUDIO.md → app/src/test/java/com/dailydeeds/reminder/adhan/BundledVoicesTest.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Rights gate` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/voices/README.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `1.1 The adhan does not stop when you pull down the notification panel` --references--> `MainActivity`  [INFERRED]
  docs/IMPROVEMENT_PLAN.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt

## Import Cycles
- None detected.

## Communities (111 total, 37 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.05
Nodes (18): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+10 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.06
Nodes (21): AdhanPlayingBanner(), AppNavigation(), AppTab, AppTopBar(), FavoriteRow, FavoritesScreen(), GlobalSearchScreen(), SearchResultCard() (+13 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.09
Nodes (7): PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "DayContentKind"
Cohesion: 0.06
Nodes (21): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+13 more)

### Community 7 - "Prayer"
Cohesion: 0.19
Nodes (8): AlarmText, PrayerAlarmText, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.16
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 9 - "Ongoing log"
Cohesion: 0.15
Nodes (7): MainActivity, QuranDataIntegrityTest, Ongoing log, Progress, Remaining, v1.2.1 corrective release (2026-10-07), Verification limits

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 11 - "steps"
Cohesion: 0.07
Nodes (26): KEYSTORE_B64, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD, RELEASE_KEYSTORE_PASSWORD, jobs, release, name, on (+18 more)

### Community 12 - "MafatihRepository"
Cohesion: 0.12
Nodes (12): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+4 more)

### Community 13 - "DeviceLocation.kt"
Cohesion: 0.14
Nodes (9): Found, freshLocation(), hasLocationPermission(), lastKnownLocation(), locateDevice(), LocationOutcome, NoPermission, ServicesOff (+1 more)

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.50
Nodes (3): Agent Directives & Operational Rules, Core Mandatory Workflow, Quality & Verification Standards

### Community 17 - "LunarMonthsScreen"
Cohesion: 0.20
Nodes (8): DeedRow(), LunarMonthsData, LunarMonthsScreen(), MonthsList(), section(), LunarMonthDeeds, Section, LunarMonthDeedsTest

### Community 18 - "ShiaCalendar"
Cohesion: 0.11
Nodes (13): HijriDay, Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT (+5 more)

### Community 19 - "build_mizan.py"
Cohesion: 0.06
Nodes (34): android_resources(), circle_path(), crescent_path(), draw_mark(), f(), octagram(), play_assets(), star_path() (+26 more)

### Community 20 - "Repository Soul & Core Identity"
Cohesion: 0.40
Nodes (4): Non-Negotiable Directives, Purpose & Ethos, Repository Soul & Core Identity, Triad of Accountability

### Community 23 - "steps"
Cohesion: 0.10
Nodes (20): runs-on, steps, jobs, build, name, on, pull_request, push (+12 more)

### Community 24 - "ReaderCounterScreen"
Cohesion: 0.13
Nodes (7): CounterOrb(), CounterStageRow(), ReaderCounterContent(), ReaderCounterScreen(), ReaderReadingContent(), ReaderTextCard(), ReaderTopBar()

### Community 25 - "DeedCategory"
Cohesion: 0.14
Nodes (11): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+3 more)

### Community 28 - "Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)"
Cohesion: 0.18
Nodes (10): Blessed nights on the right evening, Changing the place («another area»), Compatibility notes, New section: «أعمال الأشهر القمرية», Not verified (needs a person with a phone), Occasions added (dates from Mafatih al-Jinan), Other adhan fixes, Stopping the adhan (+2 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.22
Nodes (3): PrayerAlarmConfig, AdhanVoiceChoice, AdhanVoiceChoiceTest

### Community 31 - "CoordinateInput"
Cohesion: 0.42
Nodes (5): CoordinateInput, Empty, Invalid, Result, Valid

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.08
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 35 - "PlacePickerCard"
Cohesion: 0.32
Nodes (4): PlacePickerCard(), CitySearchDialog(), CoordinateField(), CoordinatesDialog()

### Community 37 - "Wird «ورد»: review findings and improvement plan"
Cohesion: 0.25
Nodes (7): 2. Other findings, 3. Feature improvements found in the code, 4. Proposed release order, 5. Checked and found clean, 6. Open questions for you, Rest of the app, Wird «ورد»: review findings and improvement plan

### Community 38 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 39 - "QadaScreen"
Cohesion: 0.40
Nodes (5): AddTo, Bulk, NumberDialog(), QadaDialog, QadaScreen()

### Community 41 - "Bundled adhan recordings"
Cohesion: 0.40
Nodes (4): Bundled adhan recordings, Processing, Result files, Where they come from

### Community 45 - "WidgetUpdater.kt"
Cohesion: 0.08
Nodes (4): PrayerWidgetProvider, WidgetUpdater, TimetablePdf, Widget

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "PrayerContext"
Cohesion: 0.14
Nodes (12): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, Place, PlacePresets (+4 more)

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "AdhanService"
Cohesion: 0.17
Nodes (4): AdhanService, Callback, 1.1 The adhan does not stop when you pull down the notification panel, Adhan and alarm reliability

### Community 56 - "QuranRepository"
Cohesion: 0.10
Nodes (11): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah, QuranReaderScreen() (+3 more)

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 63 - "PrayerSettings"
Cohesion: 0.15
Nodes (7): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, AdhanModelsTest

### Community 65 - "PrayerContext"
Cohesion: 0.16
Nodes (6): PrayerContext, PrayerInstant, PrayerParams, ImsakPlannerTest, PrayerScheduleTest, WidgetModelTest

### Community 66 - "QadaState"
Cohesion: 0.19
Nodes (9): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+1 more)

### Community 68 - ".parse"
Cohesion: 0.22
Nodes (5): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest()

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 70 - "Test"
Cohesion: 0.09
Nodes (12): BackupCodec, Error, Import, Ok, BackupCodecTest, Backup import, Components, Dependencies (+4 more)

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
Cohesion: 0.12
Nodes (4): cardBorder(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 80 - "VoiceViewModel"
Cohesion: 0.21
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "Test"
Cohesion: 0.25
Nodes (4): PrayerTimesResult, Timetable, TimetableRow, TimetableTest

### Community 83 - "KhumsScreen"
Cohesion: 0.17
Nodes (16): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), Cell(), clock(), RamadanScreen() (+8 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (5): ZahraPanel(), Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 88 - "BroadcastReceiver"
Cohesion: 0.26
Nodes (3): AdhanActionReceiver, BootReceiver, BroadcastReceiver

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.52
Nodes (6): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu()

### Community 92 - "Test"
Cohesion: 0.11
Nodes (8): Khums, KhumsInput, KhumsResult, KhumsYear, KhumsTest, QadaTest, 1.2 You can't set the prayer times to another area, 1. Your two notes

### Community 93 - "Deed"
Cohesion: 0.19
Nodes (8): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, DeedsRepositoryTest

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 98 - ".build"
Cohesion: 0.23
Nodes (5): OccasionAlarmReceiver, DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 105 - "ReminderType"
Cohesion: 0.17
Nodes (12): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+4 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 109 - "TodayScreen"
Cohesion: 0.24
Nodes (7): rememberNow(), PrayerTimesScreen(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen()

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 114 - "BundledVoices"
Cohesion: 0.60
Nodes (3): BundledVoice, BundledVoices, voiceLabel()

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 122 - "AdhanMode"
Cohesion: 0.33
Nodes (5): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT

### Community 123 - "Adhan voice packs"
Cohesion: 0.50
Nodes (3): Adding a voice, Adhan voice packs, Rights gate

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified (needs a person with a phone), The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

## Knowledge Gaps
- **85 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+80 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 539 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **37 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `Ongoing log`, `WeekdayContentScreen.kt`, `PrayerAlarmConfig`, `WidgetUpdater.kt`, `PrayerContext`, `AdhanService`, `AlarmScheduler.kt`, `PrayerSettings`, `QadaState`, `WorshipViewModel`, `Test`, `MainViewModel`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `BroadcastReceiver`, `ToolsViewModel`, `Test`, `Deed`, `AdhanService.kt`, `.build`, `ReminderType`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.089) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _85 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.05017543859649123 - nodes in this community are weakly interconnected._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `DayContentKind`, `Prayer`, `FavoriteKey`, `Ongoing log`, `WeekdayContentScreen.kt`, `LunarMonthsScreen`, `ShiaCalendar`, `PrayerAlarmConfig`, `IslamicDecor.kt`, `WeekdayContentScreen.kt`, `MafatihViewModel`, `PrayerContext`, `QuranRepository`, `MizanScreens.kt`, `PrayerSettings`, `PrayerContext`, `TodayScreen.kt`, `KhumsScreen`, `PrayerSettingsScreen`, `ReminderType`, `TodayScreen`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.072) - this node is a cross-community bridge._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.062409288824383166 - nodes in this community are weakly interconnected._
- **Why does `Ongoing log` connect `Ongoing log` to `MainActivity.kt`, `PlacePickerCard`, `PrayerTimesScreen`, `DayContentKind`, `Prayer`, `MizanViewModel`, `MafatihRepository`, `LunarMonthsScreen`, `BundledVoices`, `MafatihViewModel`, `assertequals`, `BundledVoicesTest`, `AdhanService`, `QuranRepository`, `BroadcastReceiver`, `PrayerAlarmConfig`, `CoordinateInput`?**
  _High betweenness centrality (0.043) - this node is a cross-community bridge._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._