# Graph Report - Daily Reminder  (2026-10-14)

## Corpus Check
- 182 files · ~3,041,620 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 27 file(s) not represented in the graph (top: .xml 13, .ttf 5, (none) 3)

## Summary
- 1872 nodes · 5008 edges · 128 communities (64 shown, 64 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 177 edges (avg confidence: 0.88)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `95afc7ce`
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
- MafatihRepository
- generate_icons.py
- Agent Directives & Operational Rules
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- assertequals
- AppNavigation
- PrayerAlarmConfig
- MafatihRepositoryTest
- IslamicDecor.kt
- Astro
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
- MizanScreens.kt
- Theme.kt
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- AlarmScheduler.kt
- PrayerSettings
- THIRD_PARTY_NOTICES.md
- Place
- QadaState
- WorshipViewModel
- .parse
- PrayerTimesScreen
- BackupCodecTest
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
- OccasionAlarmReceiver.kt
- AdhanActionReceiver.kt
- PrayerSettingsScreen
- manifest.json
- ToolsViewModel
- Khums.kt
- Deed
- DeedCategory
- Daily Deeds Reminder 1.7.0
- Privacy policy / سياسة الخصوصية: Wird / ورد
- KhumsYear
- DeedCard.kt
- .search
- BackupCodec
- materialtheme
- TimetablePdf.kt
- PermissionHealth.kt
- ReminderType
- Licence of the bundled text datasets
- ReminderTimeCalculatorTest
- PrayerWidgetProvider
- TodayScreen
- Security notes
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- BundledVoices
- Wird 1.8.1.1: reliability fixes (pre-release)
- lastKnownPlace
- SettingsScreen
- AdhanGlobalSettings
- BootReceiver.kt
- ZahraStages
- AdhanMode
- Adhan voice packs
- merge_stack.sh
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 92 edges
2. `ToolsViewModel` - 56 edges
3. `Prayer` - 45 edges
4. `WorshipViewModel` - 43 edges
5. `AppNavigation()` - 38 edges
6. `Place` - 37 edges
7. `PrayerContext` - 34 edges
8. `PrayerAlarmConfig` - 31 edges
9. `MainViewModel` - 29 edges
10. `cardBorder()` - 28 edges

## Surprising Connections (you probably didn't know these)
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `Completed` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `MainActivity`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/MainActivity.kt
- `Ongoing log` --references--> `BundledVoices`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/adhan/BundledVoices.kt
- `Ongoing log` --references--> `PrayerAlarmText`  [INFERRED]
  progress.md → app/src/main/java/com/dailydeeds/reminder/adhan/PrayerAlarmText.kt

## Import Cycles
- None detected.

## Communities (128 total, 64 thin omitted)

### Community 0 - "MizanDataTest"
Cohesion: 0.05
Nodes (16): MizanData, MizanStore, MizanFormat, MizanIndexBuilder, MizanEntry, MizanHeader, MizanHit, MizanKind (+8 more)

### Community 1 - "MainActivity.kt"
Cohesion: 0.06
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

### Community 6 - "DayContentKind"
Cohesion: 0.05
Nodes (22): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+14 more)

### Community 7 - "Prayer"
Cohesion: 0.20
Nodes (8): AlarmText, PrayerAlarmText, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.15
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 9 - "Ongoing log"
Cohesion: 0.12
Nodes (12): GlobalSearchScreen(), SearchResultCard(), MafatihReaderScreen(), QuranReaderScreen(), QuranScreen(), QuranDataIntegrityTest, Completed, Ongoing log (+4 more)

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 12 - "MafatihRepository"
Cohesion: 0.10
Nodes (13): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+5 more)

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
Cohesion: 0.15
Nodes (18): AppNavigation(), CalendarScreen(), FavoriteRow, FavoritesScreen(), MizanAyahScreen(), MizanCard(), MizanHomeContent(), MizanHomeScreen() (+10 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.22
Nodes (3): PrayerAlarmConfig, AdhanVoiceChoice, AdhanVoiceChoiceTest

### Community 32 - "MafatihRepositoryTest"
Cohesion: 0.07
Nodes (8): MafatihRepositoryTest, QuranRepositoryTest, SearchRepositoryTest, ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.10
Nodes (6): CircularProgressBar(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle()

### Community 34 - "Astro"
Cohesion: 0.09
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 53 - "PrayerContext"
Cohesion: 0.14
Nodes (12): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, scheduleAll(), PrayerContext (+4 more)

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

### Community 63 - "PrayerSettings"
Cohesion: 0.21
Nodes (6): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds

### Community 65 - "Place"
Cohesion: 0.26
Nodes (4): Place, PlacePresets, PrayerInstant, PrayerScheduleTest

### Community 66 - "QadaState"
Cohesion: 0.15
Nodes (12): AddTo, Bulk, QadaDialog, QadaEntry, QadaKind, ASR, DHUHR, FAJR (+4 more)

### Community 68 - ".parse"
Cohesion: 0.24
Nodes (5): ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, fetchManifest()

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.13
Nodes (14): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+6 more)

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
Cohesion: 0.14
Nodes (6): Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 81 - "Test"
Cohesion: 0.21
Nodes (3): Timetable, QadaTest, TimetableTest

### Community 83 - "KhumsScreen"
Cohesion: 0.14
Nodes (19): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+11 more)

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "BundledVoicesTest"
Cohesion: 0.20
Nodes (5): BundledVoicesTest, Bundled adhan recordings, Processing, Result files, Where they come from

### Community 89 - "PrayerSettingsScreen"
Cohesion: 0.52
Nodes (6): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu()

### Community 92 - "Khums.kt"
Cohesion: 0.28
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 93 - "Deed"
Cohesion: 0.09
Nodes (15): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+7 more)

### Community 94 - "DeedCategory"
Cohesion: 0.11
Nodes (12): App, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT (+4 more)

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 98 - "KhumsYear"
Cohesion: 0.15
Nodes (5): DailyNote, DailyNotes, DailyNotesConfig, KhumsYear, DailyNotesTest

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 101 - "BackupCodec"
Cohesion: 0.22
Nodes (4): BackupCodec, Error, Import, Ok

### Community 105 - "ReminderType"
Cohesion: 0.24
Nodes (7): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 109 - "TodayScreen"
Cohesion: 0.32
Nodes (6): rememberNow(), PrayerStrip(), ResumeTiles(), TodayHero(), TodayWeekdayTiles(), TodayScreen()

### Community 112 - "Security notes"
Cohesion: 0.22
Nodes (7): Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`), Security notes, Signing

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 114 - "BundledVoices"
Cohesion: 0.60
Nodes (3): BundledVoice, BundledVoices, voiceLabel()

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 117 - "lastKnownPlace"
Cohesion: 0.32
Nodes (3): lastKnownPlace(), roundCoordinate(), CoordinateRoundingTest

### Community 118 - "SettingsScreen"
Cohesion: 0.48
Nodes (5): ReminderSettingCard(), restartApp(), SectionTitle(), SettingsCard(), SettingsScreen()

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
- **198 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+193 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 502 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **64 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `FavoriteKey`, `WeekdayContentScreen.kt`, `PrayerAlarmConfig`, `WidgetUpdater.kt`, `PrayerContext`, `AdhanService`, `AlarmScheduler.kt`, `PrayerSettings`, `Place`, `QadaState`, `WorshipViewModel`, `BackupCodecTest`, `MainViewModel`, `PrayerAlarmReceiver.kt`, `KhumsScreen`, `TasbihState`, `OccasionAlarmReceiver.kt`, `AdhanActionReceiver.kt`, `ToolsViewModel`, `Deed`, `AdhanService.kt`, `KhumsYear`, `BackupCodec`, `ReminderType`, `Security notes`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.081) - this node is a cross-community bridge._
- **Why does `Prayer` connect `Prayer` to `Place`, `AdhanService.kt`, `WidgetUpdater.kt`, `.next`, `WeekdayContentScreen.kt`, `PrayerAlarmReceiver.kt`, `PrayerContext`, `assertequals`, `AdhanService`, `AdhanActionReceiver.kt`, `MizanScreens.kt`, `ToolsViewModel`, `PrayerAlarmConfig`?**
  _High betweenness centrality (0.065) - this node is a cross-community bridge._
- **Why does `ToolsViewModel` connect `ToolsViewModel` to `MainActivity.kt`, `PreferencesManager`, `DayContentKind`, `Prayer`, `FavoriteKey`, `Ongoing log`, `WeekdayContentScreen.kt`, `AppNavigation`, `PrayerAlarmConfig`, `QuranReaderScreen.kt`, `PrayerContext`, `MizanScreens.kt`, `PrayerSettings`, `Place`, `PrayerTimesScreen`, `TodayScreen.kt`, `KhumsScreen`, `PrayerSettingsScreen`, `TodayScreen`, `SettingsScreen`, `AdhanGlobalSettings`?**
  _High betweenness centrality (0.061) - this node is a cross-community bridge._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _198 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.05438184663536776 - nodes in this community are weakly interconnected._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06456456456456457 - nodes in this community are weakly interconnected._
- **Should `Daily Deeds Reminder v1.1.0 release notes` be split into smaller, more focused modules?**
  _Cohesion score 0.08374384236453201 - nodes in this community are weakly interconnected._