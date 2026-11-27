# Graph Report - Daily Reminder  (2026-11-27)

## Corpus Check
- 237 files · ~3,075,979 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 31 file(s) not represented in the graph (top: .xml 17, .ttf 5, (none) 3)

## Summary
- 2514 nodes · 6738 edges · 137 communities (87 shown, 50 thin omitted)
- Extraction: 95% EXTRACTED · 5% INFERRED · 0% AMBIGUOUS · INFERRED: 320 edges (avg confidence: 0.9)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e05caa25`
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
- MizanViewModel
- Daily Deeds Reminder 1.2.2
- steps
- MafatihRepository
- DeviceLocation.kt
- Agent Directives & Operational Rules
- BlessedEvesTest
- ShiaCalendar
- build_mizan.py
- Repository Soul & Core Identity
- assertequals
- steps
- DirectBoot
- FavoritesScreen
- PrefsStorageInstrumentedTest
- Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)
- PrayerAlarmConfig
- .nearest
- CoordinateInput
- SearchRepositoryTest
- IslamicDecor.kt
- Astro
- PrefsStorage
- TasbihState
- BackupCodec
- updates
- QuranPages
- QiblaScreen
- Wird 1.8.1.5: the Quran page by page, and deleting favourites from the list (pre-release)
- AppNavigation
- localdate
- VoiceViewModel
- QuranViewModel
- Distribution: signing, verification, Google Play and Play Protect
- Daily Deeds Reminder 1.2.1
- Khums.kt
- Test
- WeekdayContentScreen.kt
- ReaderFonts
- Daily Deeds Reminder 1.4.0
- AdhanService
- QuranRepository
- Google Play listing: draft answers (not submitted)
- QuranMushafScreen.kt
- PrayerContext
- .handle
- Wird 1.8.0 (formerly Daily Deeds Reminder)
- Test
- ToolsViewModel
- THIRD_PARTY_NOTICES.md
- ReligiousAlarms.kt
- Wird 1.8.3: the mushaf with TalkBack, a faster start, safer tasbih and khums (pre-release)
- WorshipViewModel
- KhumsDraft
- PrayerTimesScreen
- WidgetUpdater.kt
- MizanViewModel
- Daily Deeds Reminder 1.3.0
- Wird 1.8.1: the complete Tafsir al-Mizan
- Daily Deeds Reminder 1.5.0
- Wird «ورد»: review findings and improvement plan
- text
- VoiceAndPolicyTest
- Wird 1.8.4: the features still missing — resume tile, a full day of qada, blessed-night eves, adhan fade-in (pre-release)
- DataReloadsTest
- WorshipViewModel.kt
- Test
- Test
- KhumsScreen
- DayContentKind
- Daily Deeds Reminder 1.6.0
- VoiceStore
- AdhanActionReceiver.kt
- .isBootKey
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
- TodayScreen.kt
- .search
- .parse
- KhumsYear
- MainActivity
- QuranRepositoryTest.kt
- ReminderType
- Licence of the bundled text datasets
- ArabicDigitsTest
- .contains
- MushafFit.kt
- AdhanGlobalSettings
- NightOccasionsTest
- QadaFullDayTest
- Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)
- AppNavigation
- Wird 1.8.1.1: reliability fixes (pre-release)
- ArabicNormalizerTest
- CoordinateRoundingTest.kt
- PlacePickerCard
- SearchViewModel
- AppNavigation
- Test
- VoicePackDownloader.kt
- MushafFitCacheTest
- merge_stack.sh
- MizanFormat
- QuranDataIntegrityTest
- Wird 1.8.1.2: two bundled adhan recordings (pre-release)
- NoEmptySourceFilesTest.kt
- Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)
- Wird 1.8.2: Android 16 target, alarms that survive a restart, high-latitude prayer times (pre-release)
- SahifaViewModel.kt
- ZahraStages
- 7. Status after v1.8.1.5, and review round 2
- AdhanMode
- PrayerTimesScreen
- merge_stack.sh

## God Nodes (most connected - your core abstractions)
1. `PreferencesManager` - 130 edges
2. `Ongoing log` - 73 edges
3. `ToolsViewModel` - 68 edges
4. `Prayer` - 47 edges
5. `WorshipViewModel` - 46 edges
6. `Place` - 40 edges
7. `FavoriteKey` - 39 edges
8. `AppNavigation()` - 38 edges
9. `AdhanService` - 37 edges
10. `PrayerContext` - 35 edges

## Surprising Connections (you probably didn't know these)
- `Verification` --references--> `PrefsStorageInstrumentedTest`  [INFERRED]
  docs/releases/v1.8.2.md → app/src/androidTest/java/com/dailydeeds/reminder/data/PrefsStorageInstrumentedTest.kt
- `7.1 Done` --references--> `DataReloads`  [INFERRED]
  docs/IMPROVEMENT_PLAN.md → app/src/main/java/com/dailydeeds/reminder/data/DataReloads.kt
- `Permissions (checked by `VoiceAndPolicyTest`)` --references--> `VoiceAndPolicyTest`  [INFERRED]
  docs/SECURITY.md → app/src/test/java/com/dailydeeds/reminder/adhan/VoiceAndPolicyTest.kt
- `F-1 — Whole settings file placed in device-protected (direct-boot) storage — **LOW** (privacy / data-minimization)` --references--> `PrefsStorageInstrumentedTest`  [INFERRED]
  docs/security/2026-11-27-apk-audit.md → app/src/androidTest/java/com/dailydeeds/reminder/data/PrefsStorageInstrumentedTest.kt
- `Ongoing log` --references--> `PrefsStorageInstrumentedTest`  [INFERRED]
  progress.md → app/src/androidTest/java/com/dailydeeds/reminder/data/PrefsStorageInstrumentedTest.kt

## Import Cycles
- None detected.

## Communities (137 total, 50 thin omitted)

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
Nodes (8): PrayerParams, PrayerTimes, Raw, Qibla, PrayerMethodsTest, Ref, PrayerTimesTest, Ref

### Community 6 - "MainViewModel"
Cohesion: 0.09
Nodes (15): DeedsRepository, Deed, DeedType, COUNTER, MULTI_STAGE_COUNTER, READING, TasbeehStage, CounterOrb() (+7 more)

### Community 7 - "Prayer"
Cohesion: 0.27
Nodes (3): AlarmText, PrayerAlarmText, PrayerAlarmTextTest

### Community 8 - "FavoriteKey"
Cohesion: 0.13
Nodes (7): Ayah, FavoriteKey, FavoritesList, Mafatih, Sahifa, Weekday, FavoritesTest

### Community 9 - "MizanViewModel"
Cohesion: 0.21
Nodes (6): Failed, Loading, MizanSearchState, MizanState, MizanViewModel, Ready

### Community 10 - "Daily Deeds Reminder 1.2.2"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.2, Known limits, New, Source and licence, Verification

### Community 11 - "steps"
Cohesion: 0.07
Nodes (26): KEYSTORE_B64, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD, RELEASE_KEYSTORE_PASSWORD, jobs, release, name, on (+18 more)

### Community 12 - "MafatihRepository"
Cohesion: 0.06
Nodes (24): MafatihDataProvider, MafatihRepository, MafatihCategoryType, ADIYAH, AMAL, INDEX, JUMUAH, MUNAJAT (+16 more)

### Community 13 - "DeviceLocation.kt"
Cohesion: 0.15
Nodes (9): Found, freshLocation(), hasLocationPermission(), lastKnownLocation(), locateDevice(), LocationOutcome, NoPermission, ServicesOff (+1 more)

### Community 15 - "Agent Directives & Operational Rules"
Cohesion: 0.40
Nodes (4): Agent Directives & Operational Rules, Core Mandatory Workflow, Knowledge graph (graphify): look up first, read second, Quality & Verification Standards

### Community 18 - "ShiaCalendar"
Cohesion: 0.13
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

### Community 24 - "DirectBoot"
Cohesion: 0.24
Nodes (6): BootWork, ALL, NONE, PRAYER_ONLY, DirectBoot, DirectBootTest

### Community 25 - "FavoritesScreen"
Cohesion: 0.22
Nodes (12): FavoriteCard(), FavoriteFilter, ALL, AYAH, MAFATIH, SAHIFA, WEEKDAY, FavoriteRow (+4 more)

### Community 28 - "Wird 1.8.1.4: the adhan can always be stopped, and the place can be changed from the adhan settings (pre-release)"
Cohesion: 0.18
Nodes (10): Blessed nights on the right evening, Changing the place («another area»), Compatibility notes, New section: «أعمال الأشهر القمرية», Not verified (needs a person with a phone), Occasions added (dates from Mafatih al-Jinan), Other adhan fixes, Stopping the adhan (+2 more)

### Community 29 - "PrayerAlarmConfig"
Cohesion: 0.17
Nodes (8): PrayerAlarmConfig, AdhanVoiceChoice, Prayer, DHUHR, FAJR, ISHA, MAGHRIB, AdhanVoiceChoiceTest

### Community 31 - "CoordinateInput"
Cohesion: 0.42
Nodes (5): CoordinateInput, Empty, Invalid, Result, Valid

### Community 33 - "IslamicDecor.kt"
Cohesion: 0.14
Nodes (12): cardBorder(), EmeraldBanner(), IslamicPattern(), khatam(), OrnamentDivider(), OrnateTitle(), rememberNow(), PrayerStrip() (+4 more)

### Community 34 - "Astro"
Cohesion: 0.10
Nodes (20): Astro, Ecliptic, Equatorial, Eclipse, EclipseKind, LUNAR, SOLAR, Eclipses (+12 more)

### Community 36 - "TasbihState"
Cohesion: 0.13
Nodes (3): TasbihState, TasbihSafetyTest, TasbihTest

### Community 37 - "BackupCodec"
Cohesion: 0.18
Nodes (5): BackupCodec, Error, Import, Ok, F-1 — Whole settings file placed in device-protected (direct-boot) storage — **LOW** (privacy / data-minimization)

### Community 38 - "updates"
Cohesion: 0.29
Nodes (6): interval, updates, directory, package-ecosystem, schedule, version

### Community 39 - "QuranPages"
Cohesion: 0.23
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

### Community 44 - "VoiceViewModel"
Cohesion: 0.16
Nodes (7): LazyViewModels, Failed, Idle, Loaded, Loading, RemoteVoicesState, VoiceViewModel

### Community 46 - "Distribution: signing, verification, Google Play and Play Protect"
Cohesion: 0.20
Nodes (9): Distribution: signing, verification, Google Play and Play Protect, Google Play preparation (not published yet), Google's developer verification, Identity, If Play Protect still flags a build, Moving from the old debug build, Releasing, The signing key (+1 more)

### Community 47 - "Daily Deeds Reminder 1.2.1"
Cohesion: 0.33
Nodes (5): Daily Deeds Reminder 1.2.1, Fixed, Improved, Known limits, Verification

### Community 48 - "Khums.kt"
Cohesion: 0.28
Nodes (4): Khums, KhumsInput, KhumsResult, KhumsTest

### Community 53 - "ReaderFonts"
Cohesion: 0.16
Nodes (3): Range, ReaderFonts, ReaderFontsTest

### Community 54 - "Daily Deeds Reminder 1.4.0"
Cohesion: 0.33
Nodes (5): Content, Daily Deeds Reminder 1.4.0, Look and feel, Notes and limits, Verification

### Community 55 - "AdhanService"
Cohesion: 0.08
Nodes (4): AdhanService, Callback, VolumeProvider, Adhan and alarm reliability

### Community 56 - "QuranRepository"
Cohesion: 0.19
Nodes (6): QuranDataProvider, Ayah, RevelationType, MADANI, MAKKI, Surah

### Community 57 - "Google Play listing: draft answers (not submitted)"
Cohesion: 0.18
Nodes (10): Content rating and audience, Content rights (read before submitting), Data safety form, Full description (English), Google Play listing: draft answers (not submitted), Graphics (in `branding/play/`), Permission declarations and justifications, Release checklist (+2 more)

### Community 59 - "PrayerContext"
Cohesion: 0.11
Nodes (14): AdhanPlanner, AlarmKind, IMSAK, MAIN, PRE, PlannedAlarm, Place, PrayerContext (+6 more)

### Community 61 - "Wird 1.8.0 (formerly Daily Deeds Reminder)"
Cohesion: 0.22
Nodes (8): Application id changed: move your data, Google Play preparation (nothing published), Known limits, Name and logo, Release signing and verification, Verification, Why Google warned on every install, Wird 1.8.0 (formerly Daily Deeds Reminder)

### Community 62 - "Test"
Cohesion: 0.13
Nodes (12): AddTo, Bulk, QadaDialog, QadaEntry, QadaKind, ASR, DHUHR, FAJR (+4 more)

### Community 65 - "ReligiousAlarms.kt"
Cohesion: 0.14
Nodes (6): scheduleAll(), BlessedEveReceiver, BootReceiver, OccasionAlarmReceiver, BroadcastReceiver, Confirmed good (reviewed, no issue)

### Community 66 - "Wird 1.8.3: the mushaf with TalkBack, a faster start, safer tasbih and khums (pre-release)"
Cohesion: 0.17
Nodes (11): A faster cold start, Backup import, Compatibility, Khums, Mafatih and Sahifa reader, Not verified (needs a person with a phone), Qibla, Small fixes (+3 more)

### Community 69 - "PrayerTimesScreen"
Cohesion: 0.20
Nodes (10): buildRows(), Entry, Header, ItemRow(), ListRow, mafatihRows(), SearchField(), MafatihScreen() (+2 more)

### Community 70 - "WidgetUpdater.kt"
Cohesion: 0.06
Nodes (11): PrayerWidgetProvider, WidgetUpdater, TimetablePdf, Backup import, Components, Dependencies, Network, Permissions (checked by `VoiceAndPolicyTest`) (+3 more)

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
Cohesion: 0.17
Nodes (8): 2. Other findings, 3. Feature improvements found in the code, 4. Proposed release order, 5. Checked and found clean, 6. Open questions for you, Platform and tests, Rest of the app, Wird «ورد»: review findings and improvement plan

### Community 76 - "text"
Cohesion: 0.08
Nodes (5): FilterChipRow(), canScheduleExactAlarms(), PermissionHealthCard(), HubEntry, WorshipHubScreen()

### Community 77 - "VoiceAndPolicyTest"
Cohesion: 0.06
Nodes (18): BundledVoice, BundledVoices, ParseResult, RemoteVoice, VoiceManifest, VoiceUrlPolicy, voiceLabel(), BundledVoicesTest (+10 more)

### Community 78 - "Wird 1.8.4: the features still missing — resume tile, a full day of qada, blessed-night eves, adhan fade-in (pre-release)"
Cohesion: 0.18
Nodes (10): Adhan, Backup import, Blessed nights, Compatibility, Make-up prayers (qada), Not verified (needs a person with a phone), The Quran, Today (+2 more)

### Community 79 - "DataReloadsTest"
Cohesion: 0.27
Nodes (3): DataReloadsTest, Verification, Verification

### Community 82 - "Test"
Cohesion: 0.18
Nodes (4): AlarmGuard, PendingSnoozes, AlarmGuardTest, PendingSnoozesTest

### Community 83 - "KhumsScreen"
Cohesion: 0.14
Nodes (20): AyatScreen(), EclipseCard(), AmountField(), KhumsScreen(), money(), NumberDialog(), QadaScreen(), Cell() (+12 more)

### Community 84 - "DayContentKind"
Cohesion: 0.07
Nodes (20): Entry, SearchRepository, WeekdayRepository, DayContent, DayContentKind, DUA, ZIYARAT, SearchResultItem (+12 more)

### Community 85 - "Daily Deeds Reminder 1.6.0"
Cohesion: 0.25
Nodes (7): Adhan, Daily Deeds Reminder 1.6.0, Known limits, Prayer times, Security, Verification, Voices

### Community 86 - "VoiceStore"
Cohesion: 0.08
Nodes (6): Installed, VoiceStore, rememberStrongBuzz(), ScreenOnHolds, vibratorOf(), DailyReminderTheme()

### Community 87 - "AdhanActionReceiver.kt"
Cohesion: 0.24
Nodes (4): AdhanActionReceiver, 1.1 The adhan does not stop when you pull down the notification panel, 1.2 You can't set the prayer times to another area, 1. Your two notes

### Community 92 - "Validation"
Cohesion: 0.21
Nodes (5): ColorSchemeContrastTest, Daily Deeds Reminder 1.2.0, Distribution, Key Features, Validation

### Community 94 - "DeedCategory"
Cohesion: 0.11
Nodes (12): App, DeedCategory, AFTER_PRAYER, ALL, ANYTIME, BEDTIME, MORNING_EVENING, NIGHT (+4 more)

### Community 95 - "Daily Deeds Reminder 1.7.0"
Cohesion: 0.17
Nodes (11): Backup, Daily Deeds Reminder 1.7.0, Khums calculator (حاسبة الخمس), Known limits, Make-up prayers and fasts (القضاء), Ramadan and Imsak timetable (رمضان والإمساك), Salat al-Ayat (صلاة الآيات), Tasbih (المسبحة) (+3 more)

### Community 96 - "Privacy policy / سياسة الخصوصية: Wird / ورد"
Cohesion: 0.50
Nodes (3): English, Privacy policy / سياسة الخصوصية: Wird / ورد, العربية

### Community 97 - "QuranMushafScreen"
Cohesion: 0.23
Nodes (8): AyahActionsDialog(), AyahRun(), ayahShareText(), ayahText(), GoToPageDialog(), MushafPage(), QuranMushafScreen(), SurahTitle()

### Community 98 - "ReligiousAlarms.kt"
Cohesion: 0.15
Nodes (7): CalcMethod, CUSTOM, LEVA, TEHRAN, PrayerSettings, VoiceIds, AdhanModelsTest

### Community 99 - "TodayScreen.kt"
Cohesion: 0.09
Nodes (3): CircularProgressBar(), DeedCard(), DailyProgressCard()

### Community 100 - ".search"
Cohesion: 0.24
Nodes (3): City, PlaceSearch, PlaceSearchTest

### Community 102 - "KhumsYear"
Cohesion: 0.18
Nodes (5): DailyNote, DailyNotes, DailyNotesConfig, KhumsYear, DailyNotesTest

### Community 103 - "MainActivity"
Cohesion: 0.17
Nodes (8): MainActivity, Executive summary, F-3 — `MainActivity` is exported and accepts intent extras from any app — **INFO** (by design; no data exposure), F-4 — `androidx.profileinstaller.ProfileInstallReceiver` exported — **INFO** (library component, permission-protected), Findings, Prioritized summary, Target artifact, Wird — APK Static Security Audit

### Community 105 - "ReminderType"
Cohesion: 0.12
Nodes (13): ReminderSettings, ReminderType, BEDTIME, EVENING, MORNING, NIGHT, THURSDAY, AlarmScheduler (+5 more)

### Community 106 - "Licence of the bundled text datasets"
Cohesion: 0.40
Nodes (4): Copyright of the original works, Licence of the bundled text datasets, Other texts, Texts from the OpenITI corpus (CC BY-NC-SA 4.0)

### Community 109 - "MushafFit.kt"
Cohesion: 0.20
Nodes (4): fitMushafFontSize(), MushafFitCache, MushafFitKey, MushafLayout

### Community 113 - "Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)"
Cohesion: 0.29
Nodes (6): Install, Not verified (needs a person with a phone), Rights of the adhan recordings, Switching between adhans, Verification, Wird 1.8.1.3: switch the adhan for all prayers at once (pre-release)

### Community 114 - "AppNavigation"
Cohesion: 0.18
Nodes (8): AdhanPlayingBanner(), AppNavigation(), AppTopBar(), CalendarScreen(), GlobalSearchScreen(), SearchResultCard(), QuranModeBar(), QuranScreen()

### Community 115 - "Wird 1.8.1.1: reliability fixes (pre-release)"
Cohesion: 0.22
Nodes (8): App, Install, Known and unchanged, Not verified on a device (no physical phone available), Prayer alarms, Security and hardening, Verification, Wird 1.8.1.1: reliability fixes (pre-release)

### Community 118 - "PlacePickerCard"
Cohesion: 0.32
Nodes (4): PlacePickerCard(), CitySearchDialog(), CoordinateField(), CoordinatesDialog()

### Community 120 - "AppNavigation"
Cohesion: 0.83
Nodes (3): Box2(), Title(), VoicePacksScreen()

### Community 122 - "VoicePackDownloader.kt"
Cohesion: 0.17
Nodes (4): AdhanFade, AdhanGlobalSettings, AdhanFadeTest, AdhanGlobalSettingsTest

### Community 124 - "merge_stack.sh"
Cohesion: 0.52
Nodes (6): Heading(), PrayerSettingsScreen(), SettingsBox(), Stepper(), SwitchRow(), VoiceMenu()

### Community 126 - "QuranDataIntegrityTest"
Cohesion: 0.15
Nodes (10): KeepScreenOn(), MafatihReaderScreen(), QuranReaderScreen(), QuranDataIntegrityTest, Completed, Ongoing log, Progress, Remaining (+2 more)

### Community 127 - "Wird 1.8.1.2: two bundled adhan recordings (pre-release)"
Cohesion: 0.25
Nodes (7): Also in this release (from 1.8.1.1), Install, Not verified (needs a person with a phone), The adhan, Verification, Where the recordings come from (read this), Wird 1.8.1.2: two bundled adhan recordings (pre-release)

### Community 129 - "Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)"
Cohesion: 0.33
Nodes (5): Compatibility, Fixes, Not verified (needs a person with a phone), Verification, Wird 1.8.1.6: small fixes, including a backup regression from 1.8.1.5 (pre-release)

### Community 130 - "Wird 1.8.2: Android 16 target, alarms that survive a restart, high-latitude prayer times (pre-release)"
Cohesion: 0.25
Nodes (7): Alarms, Compatibility, Not verified (needs a person with a phone), Platform, Prayer times, Verification, Wird 1.8.2: Android 16 target, alarms that survive a restart, high-latitude prayer times (pre-release)

### Community 133 - "7. Status after v1.8.1.5, and review round 2"
Cohesion: 0.29
Nodes (7): 7.1 Done, 7.2 Still open from sections 2 and 3, 7.3 New findings (round 2), 7.4 Small features worth adding, 7.5 Proposed release order (replaces section 4), 7.6 Checked and found clean (round 2), 7. Status after v1.8.1.5, and review round 2

### Community 134 - "AdhanMode"
Cohesion: 0.33
Nodes (5): AdhanMode, ADHAN, NOTIFICATION, OFF, SILENT

## Knowledge Gaps
- **93 isolated node(s):** `OFF`, `SILENT`, `NOTIFICATION`, `ADHAN`, `LEVA` (+88 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 642 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **50 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Ongoing log` connect `QuranDataIntegrityTest` to `PreferencesManager`, `Prayer`, `FavoriteKey`, `MizanViewModel`, `MafatihRepository`, `BlessedEvesTest`, `assertequals`, `DirectBoot`, `PrefsStorageInstrumentedTest`, `PrayerAlarmConfig`, `CoordinateInput`, `PrefsStorage`, `TasbihState`, `BackupCodec`, `QuranPages`, `AppNavigation`, `VoiceViewModel`, `QuranViewModel`, `Test`, `AdhanService`, `QuranRepository`, `Test`, `ToolsViewModel`, `ReligiousAlarms.kt`, `KhumsDraft`, `PrayerTimesScreen`, `MizanViewModel`, `VoiceAndPolicyTest`, `DataReloadsTest`, `WorshipViewModel.kt`, `DayContentKind`, `VoiceStore`, `.isBootKey`, `QuranMushafScreen`, `MainActivity`, `ArabicDigitsTest`, `MushafFit.kt`, `AdhanGlobalSettings`, `QadaFullDayTest`, `AppNavigation`, `PlacePickerCard`, `SearchViewModel`, `VoicePackDownloader.kt`, `MushafFitCacheTest`?**
  _High betweenness centrality (0.138) - this node is a cross-community bridge._
- **Are the 6 inferred relationships involving `PreferencesManager` (e.g. with `.aFreshInstallRoutesKeysToTheRightStore()` and `.backupRoundTripsAcrossBothStores()`) actually correct?**
  _`PreferencesManager` has 6 INFERRED edges - model-reasoned connections that need verification._
- **What connects `OFF`, `SILENT`, `NOTIFICATION` to the rest of the system?**
  _93 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `MizanDataTest` be split into smaller, more focused modules?**
  _Cohesion score 0.056314699792960665 - nodes in this community are weakly interconnected._
- **Why does `PreferencesManager` connect `PreferencesManager` to `MainActivity.kt`, `MainViewModel`, `FavoriteKey`, `MafatihRepository`, `WeekdayContentScreen.kt`, `PrefsStorageInstrumentedTest`, `PrayerAlarmConfig`, `PrefsStorage`, `TasbihState`, `BackupCodec`, `localdate`, `QuranViewModel`, `ReaderFonts`, `AdhanService`, `PrayerContext`, `.handle`, `Test`, `ToolsViewModel`, `ReligiousAlarms.kt`, `WorshipViewModel`, `KhumsDraft`, `WidgetUpdater.kt`, `MizanViewModel`, `Wird «ورد»: review findings and improvement plan`, `WorshipViewModel.kt`, `Test`, `KhumsScreen`, `AdhanActionReceiver.kt`, `ReligiousAlarms.kt`, `KhumsYear`, `MainActivity`, `ReminderType`, `VoicePackDownloader.kt`, `QuranDataIntegrityTest`?**
  _High betweenness centrality (0.129) - this node is a cross-community bridge._
- **Are the 72 inferred relationships involving `Ongoing log` (e.g. with `PrefsStorageInstrumentedTest` and `AdhanVoiceChoice`) actually correct?**
  _`Ongoing log` has 72 INFERRED edges - model-reasoned connections that need verification._
- **Should `MainActivity.kt` be split into smaller, more focused modules?**
  _Cohesion score 0.06451612903225806 - nodes in this community are weakly interconnected._