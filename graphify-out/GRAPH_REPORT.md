# Graph Report - Daily Reminder  (2026-11-26)

## Corpus Check
- 225 files · ~3,065,986 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 29 file(s) not represented in the graph (top: .xml 15, .ttf 5, (none) 3)

## Summary
- 2396 nodes · 6434 edges · 139 communities (80 shown, 59 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 271 edges (avg confidence: 0.89)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `fcd0d8a8`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MizanDataTest
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- PrayerTimes.kt
- MainViewModel
- Prayer
- FavoriteKey
- materialtheme
- Daily Deeds Reminder 1.2.2
- steps
- MafatihRepository
- DeviceLocation.kt
- Agent Directives & Operational Rules
- LunarMonthsScreen
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- steps
- PrefsStorage
- FavoritesScreen
- WeekdayContentScreen.kt
- Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)
- PrayerAlarmConfig
- .nearest
- CoordinateInput
- SearchRepositoryTest
- IslamicDecor.kt
- Astro
- .next
- TasbihState
- BackupCodecTest
- updates
- QuranPages
- QiblaScreen
- Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)
- AppNavigation
- Place
- WeekdayContentScreen.kt
- SearchResultType
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- Khums.kt
- QuranViewModel
- PrayerSettings
- Daily Deeds Reminder 1.4.0
- AdhanService
- QuranRepository
- Google Play listing: draft answers (not submitted)
- PrayerContext
- .handle
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- Test
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- PrayerAlarmReceiver.kt
- Test
- WorshipViewModel
- VoiceAndPolicyTest
- PrayerTimesScreen
- TimetablePdf.kt
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- Wird «ورد»: review findings and improvement plan
- WorshipHubScreen.kt
- MizanHeader
- BackupCodec
- .next
- VoiceViewModel
- Test
- Test
- KhumsScreen
- WeekdayRepositoryTest
- Daily Deeds Reminder 1.6.0
- VoiceStore
- MafatihViewModel
- ReaderFonts
- MafatihRepositoryTest
- manifest.json
- MizanScreens.kt
- Validation
- WorshipViewModel.kt
- DeedCategory
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- QuranMushafScreen
- ReligiousAlarms.kt
- DeedCard.kt
- .search
- WidgetUpdater.kt
- KhumsYear
- MainActivity
- QuranRepositoryTest.kt
- ReminderType
- Licence of the bundled text datasets
- ArabicDigitsTest
- .contains
- MushafFit.kt
- AdhanGlobalSettings
- ReligiousAlarms.kt
- .open
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- PrayerWidgetProvider
- Wird 1.8.1.1: reliability fixes (pre-release)
- ArabicNormalizerTest
- CoordinateRoundingTest.kt
- PlacePickerCard
- Security notes
- AppNavigation
- ReaderLinesTest
- Test
- MushafFitCacheTest
- merge_stack.sh
- MizanFormat
- QuranDataIntegrityTest
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt
- Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)
- PrayerSettingsScreen
- 7. Status after v1.8.1.5, and review round 2
- DataReloadsTest
- AdhanModelsTest
- MizanIndexBuilder
- .checkAndResetDaily
- AddTo
- InputStream

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 119 edges
2. `ToolsViewModel` - 64 edges
3. `Ongoing log` - 54 edges
4. `Prayer` - 47 edges
5. `WorshipViewModel` - 45 edges
6. `Place` - 40 edges
7. `FavoriteKey` - 39 edges
8. `AppNavigation()` - 38 edges
9. `AdhanService` - 35 edges
10. `PrayerContext` - 35 edges

## Surprising Connections (you probably didn't know these)
- `7.1 Done` --references--> `DataReloads`  [INFERRED]
  docs/IMPROVEMENT_PLAN.md → app/src/main/java/com/dailydeeds/reminder/data/DataReloads.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Verification` --references--> `PrefsStorageInstrumentedTest`  [INFERRED]
  docs/releases/v1.8.2.md → app/src/androidTest/java/com/dailydeeds/reminder/data/PrefsStorageInstrumentedTest.kt
- `1.1 The adhan does not stop when you pull down the notification panel` --references--> `MainActivity`  [INFERRED]
  docs/IMPROVEMENT_PLAN.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt

## Import Cycles
- None detected.

## Communities (139 total, 59 thin omitted)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - "PrayerTimes.kt"
Cohesion: 0.08
Nodes (8): PrayerParams, PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "MainViewModel"
Cohesion: 0.05
Nodes (28): DeedsRepository, Deed, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING (+20 more)

### Community 7 - "Prayer"
Cohesion: 0.24
Nodes (3): AlarmText, PrayerAlarmText, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.14
Nodes (8): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest, Ongoing log

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 11 - "steps"
Cohesion: 0.07
Nodes (26): KEYSTORE_B64, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD, RELEASE_KEYSTORE_PASSWORD, jobs, release, name, on (+18 more)

### Community 12 - "MafatihRepository"
Cohesion: 0.11
Nodes (13): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+5 more)

### Community 13 - "DeviceLocation.kt"
Cohesion: 0.14
Nodes (9): Found, freshLocation(), hasLocationPermission(), lastKnownLocation(), locateDevice(), LocationOutcome, NoPermission, ServicesOff (+1 more)

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.40
Nodes (4): Agent Directives & Operational Rules, Core Mandatory Workflow, Knowledge graph (graphify): look up first, read second, Quality & Verification Standards

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

### Community 24 - "PrefsStorage"
Cohesion: 0.07
Nodes (16): PrefsStorageInstrumentedTest, BootWork, ALL, NONE, PRAYER_ONLY, DirectBoot, PrefsStorage, BootReceiver (+8 more)

### Community 25 - "FavoritesScreen"
Cohesion: 0.22
Nodes (12): FavoriteCard(), FavoriteFilter, ALL, AYAH, MAFATIH, SAHIFA, WEEKDAY, FavoriteRow (+4 more)

### Community 28 - "Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)"
Cohesion: 0.18
Nodes (10): Blessed nights on the right evening, Changing the place («another area»), Compatibility notes, New section: «أعمال الأشهر القمرية», Not verified (needs a person with a phone), Occasions added (dates from Mafatih al-Jinan), Other adhan fixes, Stopping the adhan (+2 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.16
Nodes (9): PrayerAlarmConfig, AdhanVoiceChoice, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerInstant (+1 more)

### Community 31 - "CoordinateInput"
Cohesion: 0.24
Nodes (6): CoordinateInput, Empty, Invalid, Result, Valid, CoordinateInputTest

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.08
Nodes (7): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), QuranReaderScreen()

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 38 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 39 - "QuranPages"
Cohesion: 0.21
Nodes (4): Page, QuranPages, Segment, QuranPagesTest

### Community 40 - "QiblaScreen"
Cohesion: 0.29
Nodes (3): QiblaScreen(), rememberTrueHeading(), SensorEventListener

### Community 41 - "Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)"
Cohesion: 0.29
Nodes (6): Compatibility, Deleting favourites from the list, Not verified (needs a person with a phone), The Quran as mushaf pages, Verification, Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)

### Community 42 - "AppNavigation"
Cohesion: 0.22
Nodes (12): QuranRepository, entryTitle(), MizanAyahScreen(), MizanCard(), MizanHomeContent(), MizanHomeScreen(), MizanReaderScreen(), MizanSurahScreen() (+4 more)

### Community 43 - "Place"
Cohesion: 0.29
Nodes (3): Place, PlacePresets, WidgetModel

### Community 45 - "SearchResultType"
Cohesion: 0.14
Nodes (11): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, DEEDS, MAFATIH, QURAN (+3 more)

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 48 - "Khums.kt"
Cohesion: 0.25
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 53 - "PrayerSettings"
Cohesion: 0.22
Nodes (5): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "AdhanService"
Cohesion: 0.19
Nodes (4): AdhanService, Callback, VolumeProvider, Adhan and alarm reliability

### Community 56 - "QuranRepository"
Cohesion: 0.19
Nodes (6): QuranDataProvider, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 59 - "PrayerContext"
Cohesion: 0.23
Nodes (5): AdhanPlanner, PlannedAlarm, PrayerContext, ImsakPlannerTest, WidgetModelTest

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 63 - "ToolsViewModel"
Cohesion: 0.13
Nodes (8): rememberNow(), PrayerTimesScreen(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen(), ToolsViewModel

### Community 66 - "Test"
Cohesion: 0.17
Nodes (9): QadaEntry, QadaKind, ASR, DHUHR, FAJR, FAST, ISHA, MAGHRIB (+1 more)

### Community 68 - "VoiceAndPolicyTest"
Cohesion: 0.05
Nodes (25): VoiceIds, ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest(), KhumsDraft, OwnVoiceIdTest (+17 more)

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.17
Nodes (12): GlobalSearchScreen(), SearchResultCard(), buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows() (+4 more)

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

### Community 75 - "Wird «ورد»: review findings and improvement plan"
Cohesion: 0.20
Nodes (9): 1.2 You can't set the prayer times to another area, 1. Your two notes, 2. Other findings, 3. Feature improvements found in the code, 4. Proposed release order, 5. Checked and found clean, 6. Open questions for you, Rest of the app (+1 more)

### Community 76 - "WorshipHubScreen.kt"
Cohesion: 0.12
Nodes (4): cardBorder(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 77 - "MizanHeader"
Cohesion: 0.21
Nodes (8): MizanEntry, MizanHeader, MizanHit, MizanKind, FRONT, INDEX, SECTION, MizanParagraph

### Community 78 - "BackupCodec"
Cohesion: 0.24
Nodes (4): BackupCodec, Error, Import, Ok

### Community 80 - "VoiceViewModel"
Cohesion: 0.12
Nodes (8): LazyViewModels, SahifaViewModel, Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "Test"
Cohesion: 0.23
Nodes (5): HijriDay, PrayerTimesResult, Timetable, TimetableRow, TimetableTest

### Community 82 - "Test"
Cohesion: 0.18
Nodes (4): AlarmGuard, PendingSnoozes, AlarmGuardTest, PendingSnoozesTest

### Community 83 - "KhumsScreen"
Cohesion: 0.11
Nodes (22): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+14 more)

### Community 84 - "WeekdayRepositoryTest"
Cohesion: 0.10
Nodes (11): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, ReminderTimeCalculator, DaySelectorRow(), WeekdayContentScreen() (+3 more)

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "VoiceStore"
Cohesion: 0.07
Nodes (14): BundledVoice, BundledVoices, Installed, VoiceStore, KeepScreenOn(), rememberStrongBuzz(), ScreenOnHolds, vibratorOf() (+6 more)

### Community 87 - "MafatihViewModel"
Cohesion: 0.13
Nodes (6): MafatihReaderScreen(), MafatihViewModel, Completed, Progress, Remaining, Verification limits

### Community 92 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 97 - "QuranMushafScreen"
Cohesion: 0.23
Nodes (10): AyahActionsDialog(), AyahRun(), ayahShareText(), ayahText(), GoToPageDialog(), MushafPage(), QuranMushafScreen(), SurahTitle() (+2 more)

### Community 98 - "ReligiousAlarms.kt"
Cohesion: 0.18
Nodes (6): OccasionAlarmReceiver, TimeFormat, DailyNote, DailyNotes, DailyNotesConfig, DailyNotesTest

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 105 - "ReminderType"
Cohesion: 0.09
Nodes (15): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+7 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 111 - "ReligiousAlarms.kt"
Cohesion: 0.14
Nodes (10): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, AlarmKind, IMSAK, MAIN (+2 more)

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 118 - "PlacePickerCard"
Cohesion: 0.32
Nodes (4): PlacePickerCard(), CitySearchDialog(), CoordinateField(), CoordinatesDialog()

### Community 119 - "Security notes"
Cohesion: 0.22
Nodes (7): Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`), Security notes, Signing

### Community 120 - "AppNavigation"
Cohesion: 0.21
Nodes (7): AdhanPlayingBanner(), AppNavigation(), AppTopBar(), CalendarScreen(), Box2(), Title(), VoicePacksScreen()

### Community 121 - "ReaderLinesTest"
Cohesion: 0.44
Nodes (3): readerBlocks(), readerLines(), ReaderLinesTest

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified (needs a person with a phone), The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

### Community 129 - "Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)"
Cohesion: 0.33
Nodes (5): Compatibility, Fixes, Not verified (needs a person with a phone), Verification, Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)

### Community 130 - "PrayerSettingsScreen"
Cohesion: 0.52
Nodes (6): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu()

### Community 131 - "7. Status after v1.8.1.5, and review round 2"
Cohesion: 0.29
Nodes (7): 7.1 Done, 7.2 Still open from sections 2 and 3, 7.3 New findings (round 2), 7.4 Small features worth adding, 7.5 Proposed release order (replaces section 4), 7.6 Checked and found clean (round 2), 7. Status after v1.8.1.5, and review round 2

### Community 137 - "AddTo"
Cohesion: 0.67
Nodes (3): AddTo, Bulk, QadaDialog

## Knowledge Gaps
- **93 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+88 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 617 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **59 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `MainViewModel`, `.checkAndResetDaily`, `FavoriteKey`, `WeekdayContentScreen.kt`, `PrefsStorage`, `PrayerAlarmConfig`, `TasbihState`, `BackupCodecTest`, `Place`, `AdhanService.kt`, `QuranViewModel`, `PrayerSettings`, `AdhanService`, `.handle`, `ToolsViewModel`, `PrayerAlarmReceiver.kt`, `Test`, `WorshipViewModel`, `VoiceAndPolicyTest`, `BackupCodec`, `Test`, `KhumsScreen`, `MafatihViewModel`, `ReaderFonts`, `WorshipViewModel.kt`, `ReligiousAlarms.kt`, `WidgetUpdater.kt`, `KhumsYear`, `MainActivity`, `ReminderType`, `AdhanGlobalSettings`, `ReligiousAlarms.kt`, `Security notes`?**
  _High betweenness centrality (0.125) - this node is a cross-community bridge._
- **Are the 3 inferred relationships involving `PreferencesManager` (e.g. with `.aFreshInstallStartsInDeviceStorage()` and `.settingsSavedBeforeTheUpdateAreMovedAndStillRead()`) actually correct?**
  _`PreferencesManager` has 3 INFERRED edges - model-reasoned connections that need verification._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _93 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06451612903225806 - nodes in this community are weakly interconnected._
- **Why does `Ongoing log` connect `FavoriteKey` to `DataReloadsTest`, `MainViewModel`, `Prayer`, `MafatihRepository`, `LunarMonthsScreen`, `PrefsStorage`, `PrayerAlarmConfig`, `CoordinateInput`, `IslamicDecor.kt`, `TasbihState`, `BackupCodecTest`, `QuranPages`, `AppNavigation`, `SearchResultType`, `QuranViewModel`, `AdhanService`, `QuranRepository`, `VoiceAndPolicyTest`, `PrayerTimesScreen`, `MizanViewModel`, `BackupCodec`, `VoiceViewModel`, `WeekdayRepositoryTest`, `VoiceStore`, `MafatihViewModel`, `WorshipViewModel.kt`, `QuranMushafScreen`, `MainActivity`, `ArabicDigitsTest`, `MushafFit.kt`, `PlacePickerCard`, `ReaderLinesTest`, `MushafFitCacheTest`, `QuranDataIntegrityTest`?**
  _High betweenness centrality (0.082) - this node is a cross-community bridge._
- **Are the 53 inferred relationships involving `Ongoing log` (e.g. with `AdhanVoiceChoice` and `BundledVoices`) actually correct?**
  _`Ongoing log` has 53 INFERRED edges - model-reasoned connections that need verification._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._