# Graph Report - Daily Reminder  (2026-11-25)

## Corpus Check
- 204 files · ~3,051,201 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 27 file(s) not represented in the graph (top: .xml 13, .ttf 5, (none) 3)

## Summary
- 2139 nodes · 5812 edges · 104 communities (73 shown, 31 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 219 edges (avg confidence: 0.89)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `94a421d4`
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
- WeekdayContentScreen.kt
- LunarMonthsScreen
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- assertequals
- steps
- ReaderCounterScreen
- FavoritesScreen
- SahifaViewModel.kt
- Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)
- PrayerAlarmConfig
- .nearest
- CoordinateInput
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
- .next
- .parse
- Wird «ورد»: review findings and improvement plan
- updates
- AddTo
- SensorEventListener
- Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)
- VoiceAndPolicyTest
- WidgetUpdater.kt
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- PrayerContext
- Daily Deeds Reminder 1.4.0
- AdhanService
- QuranRepository
- Google Play listing: draft answers (not submitted)
- QuranMushafScreen.kt
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- AlarmScheduler.kt
- ToolsViewModel
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
- manifest.json
- Test
- Deed
- DeedCategory
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- .build
- DeedCard.kt
- .search
- text
- FavoritesScreen.kt
- ReminderType
- Licence of the bundled text datasets
- ReminderTimeCalculatorTest
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- BundledVoices
- Wird 1.8.1.1: reliability fixes (pre-release)
- CoordinateRoundingTest.kt
- Adhan voice packs
- merge_stack.sh
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 99 edges
2. `ToolsViewModel` - 64 edges
3. `Prayer` - 45 edges
4. `WorshipViewModel` - 43 edges
5. `AppNavigation()` - 41 edges
6. `Place` - 40 edges
7. `Ongoing log` - 36 edges
8. `PrayerContext` - 35 edges
9. `FavoriteKey` - 34 edges
10. `MafatihItem` - 33 edges

## Surprising Connections (you probably didn't know these)
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Rights gate` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/voices/README.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `1.1 The adhan does not stop when you pull down the notification panel` --references--> `MainActivity`  [INFERRED]
  docs/IMPROVEMENT_PLAN.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `AdhanVoiceChoice`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/adhan/AdhanVoiceChoice.kt
- `Ongoing log` --references--> `BundledVoices`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/adhan/BundledVoices.kt

## Import Cycles
- None detected.

## Communities (104 total, 31 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.06
Nodes (13): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanKind, FRONT (+5 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.06
Nodes (22): AdhanPlayingBanner(), AppNavigation(), AppTab, AppTopBar(), CalendarScreen(), entryTitle(), MizanAyahScreen(), MizanCard() (+14 more)

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
Cohesion: 0.05
Nodes (22): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+14 more)

### Community 7 - "Prayer"
Cohesion: 0.19
Nodes (8): AlarmText, PrayerAlarmText, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.05
Nodes (20): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, Page, QuranPages (+12 more)

### Community 9 - "Ongoing log"
Cohesion: 0.10
Nodes (12): MainActivity, GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), QuranReaderScreen(), QuranDataIntegrityTest, Completed, Ongoing log (+4 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 11 - "steps"
Cohesion: 0.07
Nodes (26): KEYSTORE_B64, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD, RELEASE_KEYSTORE_PASSWORD, jobs, release, name, on (+18 more)

### Community 12 - "MafatihRepository"
Cohesion: 0.09
Nodes (13): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+5 more)

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
Cohesion: 0.12
Nodes (11): Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT, ShiaCalendar (+3 more)

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

### Community 25 - "FavoritesScreen"
Cohesion: 0.22
Nodes (12): FavoriteCard(), FavoriteFilter, ALL, AYAH, MAFATIH, SAHIFA, WEEKDAY, FavoriteRow (+4 more)

### Community 28 - "Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)"
Cohesion: 0.18
Nodes (10): Blessed nights on the right evening, Changing the place («another area»), Compatibility notes, New section: «أعمال الأشهر القمرية», Not verified (needs a person with a phone), Occasions added (dates from Mafatih al-Jinan), Other adhan fixes, Stopping the adhan (+2 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.15
Nodes (9): PrayerAlarmConfig, AdhanVoiceChoice, Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu() (+1 more)

### Community 31 - "CoordinateInput"
Cohesion: 0.32
Nodes (7): CoordinateField(), CoordinatesDialog(), CoordinateInput, Empty, Invalid, Result, Valid

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.19
Nodes (6): EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), SurahTitle()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 37 - "Wird «ورد»: review findings and improvement plan"
Cohesion: 0.13
Nodes (11): PlacePickerCard(), CitySearchDialog(), 1.2 You can't set the prayer times to another area, 1. Your two notes, 2. Other findings, 3. Feature improvements found in the code, 4. Proposed release order, 5. Checked and found clean (+3 more)

### Community 38 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 39 - "AddTo"
Cohesion: 0.67
Nodes (3): AddTo, Bulk, QadaDialog

### Community 41 - "Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)"
Cohesion: 0.29
Nodes (6): Compatibility, Deleting favourites from the list, Not verified (needs a person with a phone), The Quran as mushaf pages, Verification, Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)

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
Cohesion: 0.19
Nodes (5): Place, PlacePresets, scheduleAll(), TimeFormat, WidgetModel

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "AdhanService"
Cohesion: 0.09
Nodes (5): AdhanService, Callback, VolumeProvider, 1.1 The adhan does not stop when you pull down the notification panel, Adhan and alarm reliability

### Community 56 - "QuranRepository"
Cohesion: 0.09
Nodes (10): QuranDataProvider, QuranRepository, Ayah, RevelationType, MADANI, MAKKI, Surah, QuranModeBar() (+2 more)

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 63 - "ToolsViewModel"
Cohesion: 0.05
Nodes (17): AdhanGlobalSettings, CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, rememberNow() (+9 more)

### Community 65 - "PrayerContext"
Cohesion: 0.16
Nodes (11): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, PrayerContext, PrayerSchedule (+3 more)

### Community 66 - "QadaState"
Cohesion: 0.14
Nodes (10): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+2 more)

### Community 68 - ".parse"
Cohesion: 0.24
Nodes (5): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest()

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 70 - "Test"
Cohesion: 0.08
Nodes (12): BackupCodec, Error, Import, Ok, BackupCodecTest, Backup import, Components, Dependencies (+4 more)

### Community 71 - "MizanViewModel"
Cohesion: 0.20
Nodes (7): MizanHit, Failed, Loading, MizanSearchState, MizanState, MizanViewModel, Ready

### Community 72 - "Daily Deeds Reminder 1.3.0"
Cohesion: 0.33
Nodes (5): Accuracy, Daily Deeds Reminder 1.3.0, Known limits, New, Verification

### Community 73 - "Wird 1.8.1: the complete Tafsir al-Mizan"
Cohesion: 0.29
Nodes (6): Known limits, Licence correction, Tafsir al-Mizan, complete, Verification, What is still not full, Wird 1.8.1: the complete Tafsir al-Mizan

### Community 74 - "Daily Deeds Reminder 1.5.0"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.5.0, New structure, Search, settings and fixes, Verification, What changed in the Mafatih tab

### Community 75 - "MainViewModel"
Cohesion: 0.11
Nodes (11): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+3 more)

### Community 80 - "VoiceViewModel"
Cohesion: 0.14
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "Test"
Cohesion: 0.23
Nodes (5): HijriDay, PrayerTimesResult, Timetable, TimetableRow, TimetableTest

### Community 82 - "PrayerAlarmReceiver.kt"
Cohesion: 0.17
Nodes (6): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, PrayerAlarmReceiver

### Community 83 - "KhumsScreen"
Cohesion: 0.15
Nodes (18): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+10 more)

### Community 84 - "TasbihState"
Cohesion: 0.13
Nodes (5): ZahraPanel(), Stage, TasbihState, ZahraStages, TasbihTest

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "BundledVoicesTest"
Cohesion: 0.21
Nodes (5): BundledVoicesTest, Bundled adhan recordings, Processing, Result files, Where they come from

### Community 88 - "BroadcastReceiver"
Cohesion: 0.18
Nodes (4): AdhanActionReceiver, BootReceiver, OccasionAlarmReceiver, BroadcastReceiver

### Community 92 - "Test"
Cohesion: 0.17
Nodes (5): Khums, KhumsInput, KhumsResult, KhumsYear, KhumsTest

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
Cohesion: 0.36
Nodes (4): DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 104 - "FavoritesScreen.kt"
Cohesion: 0.07
Nodes (3): canScheduleExactAlarms(), PermissionHealthCard(), DailyProgressCard()

### Community 105 - "ReminderType"
Cohesion: 0.17
Nodes (12): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+4 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 114 - "BundledVoices"
Cohesion: 0.60
Nodes (3): BundledVoice, BundledVoices, voiceLabel()

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 123 - "Adhan voice packs"
Cohesion: 0.50
Nodes (3): Adding a voice, Adhan voice packs, Rights gate

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified (needs a person with a phone), The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

## Knowledge Gaps
- **90 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+85 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 570 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **31 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `Ongoing log`, `WeekdayContentScreen.kt`, `PrayerAlarmConfig`, `WidgetUpdater.kt`, `PrayerContext`, `AdhanService`, `AlarmScheduler.kt`, `ToolsViewModel`, `QadaState`, `WorshipViewModel`, `Test`, `MainViewModel`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `BroadcastReceiver`, `Test`, `Deed`, `ReminderType`?**
  _High betweenness centrality (0.091) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _90 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.05548654244306418 - nodes in this community are weakly interconnected._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PrayerContext`, `PreferencesManager`, `DayContentKind`, `Prayer`, `FavoritesScreen.kt`, `Ongoing log`, `FavoriteKey`, `ReminderType`, `WeekdayContentScreen.kt`, `WeekdayContentScreen.kt`, `LunarMonthsScreen`, `KhumsScreen`, `PrayerContext`, `FavoritesScreen`, `QuranMushafScreen.kt`, `PrayerAlarmConfig`?**
  _High betweenness centrality (0.077) - this node is a cross-community bridge._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.062409288824383166 - nodes in this community are weakly interconnected._
- **Why does `Ongoing log` connect `Ongoing log` to `PrayerTimesScreen`, `DayContentKind`, `Prayer`, `FavoriteKey`, `Wird «ورد»: review findings and improvement plan`, `MizanViewModel`, `Test`, `MafatihRepository`, `LunarMonthsScreen`, `BundledVoices`, `assertequals`, `BundledVoicesTest`, `AdhanService`, `QuranRepository`, `BroadcastReceiver`, `PrayerAlarmConfig`, `CoordinateInput`?**
  _High betweenness centrality (0.052) - this node is a cross-community bridge._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._