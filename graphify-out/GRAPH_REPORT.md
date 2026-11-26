# Graph Report - wt-master  (2026-11-26)

## Corpus Check
- 224 files · ~3,065,889 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 30 file(s) not represented in the graph (top: .xml 15, .ttf 5, (none) 4)

## Summary
- 2394 nodes · 6433 edges · 142 communities (87 shown, 55 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 271 edges (avg confidence: 0.89)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `26230316`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- MizanDataTest
- MainActivity.kt
- Daily Deeds Reminder v1.1.0 release notes
- Daily Deeds Reminder 1.1.1
- PreferencesManager
- .compute
- Deed
- Prayer
- FavoriteKey
- PermissionHealth.kt
- Daily Deeds Reminder 1.2.2
- steps
- MafatihRepository
- DeviceLocation.kt
- Agent Directives & Operational Rules
- LunarMonthsScreen
- ShiaCalendar
- generate_icons.py
- Repository Soul & Core Identity
- assertequals
- steps
- PrefsStorage
- FavoritesScreen
- WeekdayContentScreen.kt
- Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)
- PrayerAlarmConfig
- .nearest
- CoordinateInput
- SearchRepositoryTest
- TodayHeader.kt
- Astro
- .next
- TasbihState
- BackupCodecTest
- updates
- QuranPages
- QiblaScreen
- Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)
- QuranRepository
- Place
- SearchResultType
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- KhumsTest
- QuranViewModel
- PrayerSettings
- Daily Deeds Reminder 1.4.0
- AdhanService
- Surah
- Google Play listing: draft answers (not submitted)
- PrayerContext
- PrayerAlarmReceiver.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- Test
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- BroadcastReceiver
- QadaKind
- WorshipViewModel
- KhumsDraft
- .item
- TimetablePdf.kt
- MainViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- Wird «ورد»: review findings and improvement plan
- TodayScreen.kt
- .parse
- ReaderHelpers.kt
- .next
- VoiceViewModel
- .toHijri
- Test
- KhumsScreen
- DayContentKind
- Daily Deeds Reminder 1.6.0
- VoiceStore
- MafatihViewModel
- DeedsRepositoryTest
- MafatihRepositoryTest
- manifest.json
- MizanScreens.kt
- Validation
- WorshipViewModel.kt
- NotificationHelper.kt
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- QuranMushafScreen
- ReligiousAlarms.kt
- DeedCard.kt
- PlaceSearch
- WidgetUpdater.kt
- KhumsYear
- MainActivity
- QuranRepositoryTest
- ReminderType
- Licence of the bundled text datasets
- ArabicDigits
- .contains
- Ongoing log
- AdhanGlobalSettings
- AlarmScheduler.kt
- TasbihScreen
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- PrayerWidgetProvider
- Wird 1.8.1.1: reliability fixes (pre-release)
- ArabicNormalizerTest
- CoordinateRoundingTest.kt
- PlacePickerCard
- Security notes
- AppNavigation
- ReaderLinesTest
- BundledVoicesTest
- MushafFitCacheTest
- VoiceAndPolicyTest
- ReminderTimeCalculatorTest
- QuranDataIntegrityTest
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest
- Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)
- PrayerSettingsScreen
- DeedCategory
- ReaderCounterScreen
- OwnVoiceIdTest
- NightOccasionsTest
- SearchViewModel
- TodayScreen
- SahifaViewModel.kt
- BundledVoices
- text
- Bundled adhan recordings
- Adhan voice packs

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
- `Result files` --references--> `BundledVoicesTest`  [INFERRED]
  docs/licenses/ADHAN_AUDIO.md → app/src/test/java/com/dailydeeds/reminder/adhan/BundledVoicesTest.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Rights gate` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/voices/README.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Verification` --references--> `PrefsStorageInstrumentedTest`  [INFERRED]
  docs/releases/v1.8.2.md → app/src/androidTest/java/com/dailydeeds/reminder/data/PrefsStorageInstrumentedTest.kt

## Import Cycles
- None detected.

## Communities (142 total, 55 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.06
Nodes (14): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+6 more)

### Community 2 - "Daily Deeds Reminder v1.1.0 release notes"
Cohesion: 0.08
Nodes (17): Historical completion report: original 11 deeds, successful unit tests and debug assembly, Change-of-state supplication at night: no fixed repetition count, v1.1.0: release/v1.1.0 branch, debug application ID, version code 2, Supplication entrusting one's future to Allah: anytime, three repetitions, Quran 16:6 before sleep: three repetitions, Daily Deeds Reminder v1.1.0 release notes, Short ziyara of Sahib al-Zaman: anytime, hand over heart, Thursday morning readings when leaving home to seek a need (+9 more)

### Community 3 - "Daily Deeds Reminder 1.1.1"
Cohesion: 0.50
Nodes (3): Daily Deeds Reminder 1.1.1, Distribution, Validation

### Community 5 - ".compute"
Cohesion: 0.09
Nodes (8): PrayerParams, PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "Deed"
Cohesion: 0.22
Nodes (10): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+2 more)

### Community 7 - "Prayer"
Cohesion: 0.19
Nodes (8): AlarmText, PrayerAlarmText, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.14
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

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
Cohesion: 0.40
Nodes (4): Agent Directives & Operational Rules, Core Mandatory Workflow, Knowledge graph (graphify): look up first, read second, Quality & Verification Standards

### Community 17 - "LunarMonthsScreen"
Cohesion: 0.20
Nodes (8): DeedRow(), LunarMonthsData, LunarMonthsScreen(), MonthsList(), section(), LunarMonthDeeds, Section, LunarMonthDeedsTest

### Community 18 - "ShiaCalendar"
Cohesion: 0.12
Nodes (11): Occasion, OccasionKind, BIRTH, EID, EVENT, MARTYRDOM, NIGHT, ShiaCalendar (+3 more)

### Community 19 - "generate_icons.py"
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
Nodes (15): PrefsStorageInstrumentedTest, BootWork, ALL, NONE, PRAYER_ONLY, DirectBoot, PrefsStorage, DirectBootTest (+7 more)

### Community 25 - "FavoritesScreen"
Cohesion: 0.22
Nodes (12): FavoriteCard(), FavoriteFilter, ALL, AYAH, MAFATIH, SAHIFA, WEEKDAY, FavoriteRow (+4 more)

### Community 28 - "Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)"
Cohesion: 0.18
Nodes (10): Blessed nights on the right evening, Changing the place («another area»), Compatibility notes, New section: «أعمال الأشهر القمرية», Not verified (needs a person with a phone), Occasions added (dates from Mafatih al-Jinan), Other adhan fixes, Stopping the adhan (+2 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.22
Nodes (3): PrayerAlarmConfig, AdhanVoiceChoice, AdhanVoiceChoiceTest

### Community 31 - "CoordinateInput"
Cohesion: 0.24
Nodes (6): CoordinateInput, Empty, Invalid, Result, Valid, CoordinateInputTest

### Community 33 - "TodayHeader.kt"
Cohesion: 0.15
Nodes (6): EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), QuranReaderScreen()

### Community 34 - "Astro"
Cohesion: 0.09
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 36 - "TasbihState"
Cohesion: 0.12
Nodes (3): TasbihState, TasbihSafetyTest, TasbihTest

### Community 37 - "BackupCodecTest"
Cohesion: 0.07
Nodes (8): BackupCodec, Error, Import, Ok, Range, ReaderFonts, BackupCodecTest, ReaderFontsTest

### Community 38 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 39 - "QuranPages"
Cohesion: 0.22
Nodes (4): Page, QuranPages, Segment, QuranPagesTest

### Community 40 - "QiblaScreen"
Cohesion: 0.29
Nodes (3): QiblaScreen(), rememberTrueHeading(), SensorEventListener

### Community 41 - "Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)"
Cohesion: 0.29
Nodes (6): Compatibility, Deleting favourites from the list, Not verified (needs a person with a phone), The Quran as mushaf pages, Verification, Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)

### Community 42 - "QuranRepository"
Cohesion: 0.14
Nodes (17): QuranRepository, entryTitle(), MizanAyahScreen(), MizanCard(), MizanHomeContent(), MizanHomeScreen(), MizanReaderScreen(), MizanSurahScreen() (+9 more)

### Community 45 - "SearchResultType"
Cohesion: 0.19
Nodes (10): Entry, SearchRepository, SearchResultItem, SearchResultType, ALL, DEEDS, MAFATIH, QURAN (+2 more)

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 48 - "KhumsTest"
Cohesion: 0.42
Nodes (3): Khums, KhumsInput, KhumsTest

### Community 53 - "PrayerSettings"
Cohesion: 0.16
Nodes (6): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, AdhanModelsTest

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "AdhanService"
Cohesion: 0.15
Nodes (5): AdhanService, Callback, VolumeProvider, 1.1 The adhan does not stop when you pull down the notification panel, Adhan and alarm reliability

### Community 56 - "Surah"
Cohesion: 0.19
Nodes (6): QuranDataProvider, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 59 - "PrayerContext"
Cohesion: 0.15
Nodes (11): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, PrayerContext, PrayerSchedule (+3 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 62 - "Test"
Cohesion: 0.26
Nodes (3): QadaEntry, QadaState, QadaTest

### Community 65 - "BroadcastReceiver"
Cohesion: 0.26
Nodes (3): AdhanActionReceiver, BootReceiver, BroadcastReceiver

### Community 66 - "QadaKind"
Cohesion: 0.15
Nodes (10): AddTo, Bulk, QadaDialog, QadaKind, ASR, DHUHR, FAJR, FAST (+2 more)

### Community 68 - "KhumsDraft"
Cohesion: 0.09
Nodes (15): KhumsDraft, DataReloadsTest, KhumsDraftTest, A faster cold start, Backup import, Compatibility, Khums, Mafatih and Sahifa reader (+7 more)

### Community 69 - ".item"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

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
Cohesion: 0.13
Nodes (14): 2. Other findings, 3. Feature improvements found in the code, 4. Proposed release order, 5. Checked and found clean, 6. Open questions for you, 7.1 Done, 7.2 Still open from sections 2 and 3, 7.3 New findings (round 2) (+6 more)

### Community 76 - "TodayScreen.kt"
Cohesion: 0.12
Nodes (4): cardBorder(), DailyProgressCard(), HubEntry, WorshipHubScreen()

### Community 77 - ".parse"
Cohesion: 0.19
Nodes (5): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest()

### Community 78 - "ReaderHelpers.kt"
Cohesion: 0.20
Nodes (4): KeepScreenOn(), rememberStrongBuzz(), ScreenOnHolds, vibratorOf()

### Community 80 - "VoiceViewModel"
Cohesion: 0.16
Nodes (7): LazyViewModels, Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - ".toHijri"
Cohesion: 0.23
Nodes (5): HijriDay, PrayerTimesResult, Timetable, TimetableRow, TimetableTest

### Community 82 - "Test"
Cohesion: 0.18
Nodes (4): AlarmGuard, PendingSnoozes, AlarmGuardTest, PendingSnoozesTest

### Community 83 - "KhumsScreen"
Cohesion: 0.17
Nodes (14): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+6 more)

### Community 84 - "DayContentKind"
Cohesion: 0.10
Nodes (11): WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, ReminderTimeCalculator, DaySelectorRow(), WeekdayContentScreen() (+3 more)

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 87 - "MafatihViewModel"
Cohesion: 0.18
Nodes (3): MafatihReaderScreen(), MafatihViewModel, Completed

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
Cohesion: 0.21
Nodes (10): AyahActionsDialog(), AyahRun(), ayahShareText(), ayahText(), GoToPageDialog(), MushafPage(), QuranMushafScreen(), SurahTitle() (+2 more)

### Community 98 - "ReligiousAlarms.kt"
Cohesion: 0.10
Nodes (12): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT, scheduleAll(), OccasionAlarmReceiver, TimeFormat (+4 more)

### Community 100 - "PlaceSearch"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 105 - "ReminderType"
Cohesion: 0.17
Nodes (12): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, ReminderSettingCard() (+4 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 109 - "Ongoing log"
Cohesion: 0.18
Nodes (5): fitMushafFontSize(), MushafFitCache, MushafFitKey, MushafLayout, Ongoing log

### Community 112 - "TasbihScreen"
Cohesion: 0.26
Nodes (8): ClearCountDialog(), CounterButton(), FreePanel(), TasbihScreen(), ZahraPanel(), ChoiceRow(), Stage, ZahraStages

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 118 - "PlacePickerCard"
Cohesion: 0.24
Nodes (6): PlacePickerCard(), CitySearchDialog(), CoordinateField(), CoordinatesDialog(), 1.2 You can't set the prayer times to another area, 1. Your two notes

### Community 119 - "Security notes"
Cohesion: 0.22
Nodes (7): Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`), Security notes, Signing

### Community 120 - "AppNavigation"
Cohesion: 0.17
Nodes (10): AdhanPlayingBanner(), AppNavigation(), CalendarScreen(), GlobalSearchScreen(), SearchResultCard(), PrayerTimesScreen(), ToolScaffold(), Box2() (+2 more)

### Community 121 - "ReaderLinesTest"
Cohesion: 0.44
Nodes (3): readerBlocks(), readerLines(), ReaderLinesTest

### Community 126 - "QuranDataIntegrityTest"
Cohesion: 0.24
Nodes (5): QuranDataIntegrityTest, Progress, Remaining, v1.2.1 corrective release (2026-10-07), Verification limits

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified (needs a person with a phone), The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

### Community 129 - "Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)"
Cohesion: 0.33
Nodes (5): Compatibility, Fixes, Not verified (needs a person with a phone), Verification, Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)

### Community 130 - "PrayerSettingsScreen"
Cohesion: 0.52
Nodes (6): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu()

### Community 131 - "DeedCategory"
Cohesion: 0.18
Nodes (10): DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT, QURAN (+2 more)

### Community 132 - "ReaderCounterScreen"
Cohesion: 0.20
Nodes (4): ReaderCounterScreen(), ReaderReadingContent(), ReaderTextCard(), ReaderTopBar()

### Community 136 - "TodayScreen"
Cohesion: 0.32
Nodes (6): rememberNow(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen()

### Community 138 - "BundledVoices"
Cohesion: 0.60
Nodes (3): BundledVoice, BundledVoices, voiceLabel()

### Community 140 - "Bundled adhan recordings"
Cohesion: 0.40
Nodes (4): Bundled adhan recordings, Processing, Result files, Where they come from

### Community 141 - "Adhan voice packs"
Cohesion: 0.50
Nodes (3): Adding a voice, Adhan voice packs, Rights gate

## Knowledge Gaps
- **93 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+88 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 615 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **55 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `FavoritesScreen.kt`, `PrefsStorage`, `PrayerAlarmConfig`, `TasbihState`, `BackupCodecTest`, `Place`, `AdhanService.kt`, `QuranViewModel`, `PrayerSettings`, `AdhanService`, `PrayerAlarmReceiver.kt`, `Test`, `ToolsViewModel`, `BroadcastReceiver`, `WorshipViewModel`, `KhumsDraft`, `MainViewModel`, `Test`, `MafatihViewModel`, `DeedsRepositoryTest`, `WorshipViewModel.kt`, `ReligiousAlarms.kt`, `WidgetUpdater.kt`, `KhumsYear`, `MainActivity`, `ReminderType`, `AdhanGlobalSettings`, `AlarmScheduler.kt`, `TasbihScreen`, `Security notes`?**
  _High betweenness centrality (0.121) - this node is a cross-community bridge._
- **Are the 3 inferred relationships involving `PreferencesManager` (e.g. with `.aFreshInstallStartsInDeviceStorage()` and `.settingsSavedBeforeTheUpdateAreMovedAndStillRead()`) actually correct?**
  _`PreferencesManager` has 3 INFERRED edges - model-reasoned connections that need verification._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _93 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.056314699792960665 - nodes in this community are weakly interconnected._
- **Why does `Ongoing log` connect `Ongoing log` to `OwnVoiceIdTest`, `Prayer`, `FavoriteKey`, `SearchViewModel`, `BundledVoices`, `MafatihRepository`, `LunarMonthsScreen`, `assertequals`, `PrefsStorage`, `PrayerAlarmConfig`, `CoordinateInput`, `TodayHeader.kt`, `TasbihState`, `BackupCodecTest`, `QuranPages`, `QuranRepository`, `SearchResultType`, `QuranViewModel`, `AdhanService`, `Surah`, `BroadcastReceiver`, `KhumsDraft`, `.item`, `ReaderHelpers.kt`, `VoiceViewModel`, `DayContentKind`, `MafatihViewModel`, `WorshipViewModel.kt`, `QuranMushafScreen`, `MainActivity`, `ArabicDigits`, `PlacePickerCard`, `AppNavigation`, `ReaderLinesTest`, `BundledVoicesTest`, `MushafFitCacheTest`, `VoiceAndPolicyTest`, `QuranDataIntegrityTest`?**
  _High betweenness centrality (0.084) - this node is a cross-community bridge._
- **Are the 53 inferred relationships involving `Ongoing log` (e.g. with `AdhanVoiceChoice` and `BundledVoices`) actually correct?**
  _`Ongoing log` has 53 INFERRED edges - model-reasoned connections that need verification._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06451612903225806 - nodes in this community are weakly interconnected._