# Wird «ورد»: review findings and improvement plan

Review date: 2026-11-25, against `master` at `bf40776` (v1.8.1.3).
Method: OMC review, run as two read-only lanes (the adhan/alarm pipeline, and the rest of the app). I traced both of your notes myself and re-checked the HIGH findings in the code.
Not done: no Gradle build or test run, and nothing tried on a device or emulator. Every item marked "device check" still needs to be tried on a real phone.

All paths are relative to `app/src/main/java/com/dailydeeds/reminder/`.

---

## 1. Your two notes

### 1.1 The adhan does not stop when you pull down the notification panel

**What happens now**

| Path | Code | Result |
|---|---|---|
| Swipe the adhan notification away | `service/AdhanService.kt:70-80` has no `setDeleteIntent` | The notification disappears but the **audio keeps playing** for up to `maxSeconds` (300 s by default, up to 900). The app targets SDK 34, and on Android 14 users can swipe away `setOngoing` foreground-service notifications. |
| Tap the notification itself | `NotificationHelper.baseBuilder` sets `contentIntent` = open `MainActivity` | The app opens and the adhan keeps playing. |
| Look inside the app for a stop control | none; the only caller of `AdhanService.stop` is `receiver/AdhanActionReceiver.kt:14` | There is nothing to press. |
| Power or volume button | no media session, no screen-off listener | Nothing happens. Most adhan apps stop on one of these. |
| Notifications denied, or the «الأذان» channel blocked | `receiver/PrayerAlarmReceiver.kt:71-73` starts the service anyway | The adhan plays with **no visible notification at all**, so there is no Stop. |
| Press «إيقاف» | works (`AdhanActionReceiver` → `stopService`) | This is the only way to stop it today, and it is a small button on an expanded notification. |

**Fix plan (hotfix, highest priority)**
1. Add `.setDeleteIntent(stop)` to the adhan notification, so swiping it away stops the adhan.
2. Make tapping the notification stop the adhan, then open the app. Point the content intent at `AdhanActionReceiver` with a new `ACTION_STOP_AND_OPEN`.
3. Stop on hardware buttons:
   - Register an `ACTION_SCREEN_OFF` receiver inside `AdhanService` while it plays, so the power button stops it.
   - Add a `MediaSessionCompat` with a volume provider, so either volume key stops it.
   - Put both behind a setting, «إيقاف الأذان بزر التشغيل أو الصوت», on by default.
4. Show a "the adhan is playing, stop it" banner at the top of the app while the service runs (a static `StateFlow` in the service companion).
5. Before calling `AdhanService.play`, check `NotificationManagerCompat.areNotificationsEnabled()` and that the `prayer_adhan` channel is not `IMPORTANCE_NONE`. If either fails, show the normal reminder notification instead.
6. Optional, later: a full-screen alarm activity with a large Stop button (`USE_FULL_SCREEN_INTENT`). Check Play policy first: since Android 14 this permission is limited to alarm and calling apps.

**Device check:** on Android 13 and 14, swipe, tap, power button, volume key, and with notifications denied. Each one should silence the adhan within a second.

### 1.2 You can't set the prayer times to another area

A place picker does exist, but it only appears on the «أوقات الصلاة» screen (`ui/screens/PrayerTimesScreen.kt:125-149`). Five problems make it look missing or broken:

| # | Problem | Code |
|---|---|---|
| a | **«إعدادات الأذان وطريقة الحساب» has no place picker.** That settings screen is where people look for it, and it only offers per-prayer ±minute offsets. | `ui/screens/PrayerSettingsScreen.kt` |
| b | **«موقعي» uses only `getLastKnownLocation`.** It never asks for a fresh fix, so on many phones it returns null and shows "تعذّر تحديد الموقع". | `PrayerTimesScreen.kt:58-68` |
| c | **Custom coordinates and «موقعي» use the phone's time zone.** Coordinates for a city in another zone produce times that are off by whole hours. | `PlacePickerDialogs.kt:106`, `PrayerTimesScreen.kt:67` |
| d | **The coordinates dialog rejects Arabic-Indic digits and «٫».** The «اعتماد» button just stays disabled, with no message. | `PlacePickerDialogs.kt:80-81` |
| e | **No high-latitude rule.** In London (a preset) Fajr, Imsak and midnight show "—" roughly late May to mid July, and Isha too from about 53°N. Those alarms are silently skipped. | `util/PrayerTimes.kt:111`, `util/PrayerSchedule.kt:54` |

Smaller issues:
- The three location buttons sit in a Row that doesn't wrap, so they clip at font scale 1.15 or more (`PrayerTimesScreen.kt:130-148`).
- The time rows use the phone's date, while «الصلاة القادمة» uses the city's zone (`PrayerTimesScreen.kt:101`). When the two zones differ, the rows show the wrong day.

**Fix plan**
1. Pull the place card out into a shared `PlacePickerCard` and show it at the top of both `PrayerTimesScreen` and `PrayerSettingsScreen`.
2. «موقعي»:
   - Request a fresh fix: `LocationManager.getCurrentLocation` on API 30+, `requestSingleUpdate` before that, with a timeout of about 10 s.
   - If location services are off, offer to open the system location settings.
3. Add `PlaceSearch.nearest(lat, lon)` over the bundled `cities.txt`. Use it to take the time zone from the nearest city instead of `ZoneId.systemDefault()`, and to label the place «قرب صور» rather than «إحداثيات مخصصة».
4. Parse Arabic-Indic digits and «٫» in the coordinates dialog, the same way `Khums.parseAmount` does, and say why the input is invalid.
5. Add a high-latitude setting (PrayTimes NightMiddle / AngleBased / OneSeventh), defaulting to AngleBased above 48°.
6. Switch the location buttons to `FlowRow`. Compute "today" in `place.zone`.
7. Ask you: does "another area" also mean keeping a few saved places (home, family abroad, a ziyarah trip) and switching quickly? If so, add a small "saved places" list.

**Tests:** unit tests for `nearest()`, zone selection, digit parsing, and the high-latitude rule (London 21 June, Manchester and Oslo all produce a Fajr and an Isha).

---

## 2. Other findings

### Adhan and alarm reliability

| Sev. | Finding | Code | Fix |
|---|---|---|---|
| MED | The result of the audio-focus request is ignored, so during a call the adhan plays over the call on the alarm stream. | `AdhanService.kt:197` | If the request isn't `GRANTED`, call `postReminder()` and then `shutdown()`. |
| MED | After an overnight reboot, alarms come back only once the phone is unlocked (`BOOT_COMPLETED`), so Fajr can be missed. | `AndroidManifest.xml:75-86` | Make the receiver `directBootAware`, handle `LOCKED_BOOT_COMPLETED`, and keep the alarm config in device-protected storage. |
| LOW-MED | When the network moves the clock back a few seconds after an adhan, the same adhan can play again. | `receiver/BootReceiver.kt:13`, `notification/ReligiousAlarms.kt:54` | Save the last alarm that fired and pass it as `after`. |
| LOW | A snooze plays the full adhan even after the prayer was switched to notification-only. | `PrayerAlarmReceiver.kt:71` (`mode == ADHAN \|\| snooze`) | Play only when `mode == ADHAN`. |
| LOW | A pending snooze is lost on reboot. | `ReligiousAlarms.kt:89-97` | Save the snooze time and restore it in `BootReceiver`. |
| LOW | If the bundled file can't be opened, the fallback to the phone's tone is skipped (`bundledAttempt` is set after `openFd`). | `AdhanService.kt:123` vs `:136` | Set `bundledAttempt` before opening the file. |
| LOW | A short interruption (the voice assistant, a navigation prompt) ends the adhan for good. | `AdhanService.kt:190` | On `LOSS_TRANSIENT`, pause, then resume on `GAIN`. |
| LOW | On silent or vibrate, an ongoing "يُشغَّل الأذان الآن" notification with a Snooze button shows for 3 s or more, with no wake lock held. | `AdhanService.kt:97-102` | Decide on the ringer mode in the receiver and skip the service. |
| OPT | `setExactAndAllowWhileIdle` is weaker than `setAlarmClock` against aggressive OEM battery savers (Xiaomi, Huawei, Samsung). | `AlarmScheduler` / `ReligiousAlarms` | Use `setAlarmClock` for MAIN adhan alarms. |

### Rest of the app

| Sev. | Finding | Code | Fix |
|---|---|---|---|
| HIGH | **Night occasions are announced the morning after the night.** Laylat al-Qadr 19/21/23 and the night of mid-Sha'ban are matched on the civil day, but in Islamic reckoning the night comes before the day. | `calendar/ShiaCalendar.kt:95,99-101`; `worship/DailyNotes.kt:43`, `TodayHeader.kt:54`, `WidgetModel.kt:34` (`Timetable.kt` already does day+1) | Match NIGHT occasions on the eve, and send an evening reminder before Maghrib. |
| MED | Opening an ayah from search or favourites lands at the top of the surah. Resume keeps only the surah. | `MainActivity.kt:317`, `FavoritesScreen.kt:87` | Put the ayah in the route, `scrollToItem` to it, and save the last ayah. |
| MED | Qibla is 90° off in landscape. | `QiblaScreen.kt:53-55` | Use `remapCoordinateSystem` for the display rotation, or lock this screen to portrait. |
| MED | Heavy work on every cold start:<br>- every ViewModel is created at start<br>- the full search index is built on each launch<br>- three normalized copies of Mafatih/Sahifa are made<br>- `selectSurah` can block the main thread | `MainActivity.kt:171-176, 278`, `SearchViewModel:44`, `MafatihRepository.kt:12-14` | Share one repository, build the index on the first search, load ayahs in a coroutine. |
| MED | Missing occasions: Imam al-Baqir's martyrdom (7 Dhu al-Hijja), Imam al-Kazim's birth (7 Safar), Imam al-Hadi's birth (15 Dhu al-Hijja or 2 Rajab). | `ShiaCalendar.kt:68-111` | **You confirm the dates**, then I add them. |
| LOW-MED | Importing a backup and then choosing «لاحقاً»: the next tasbih or qada tap writes the old in-memory state over the imported data, and Settings still shows the old city. | `WorshipViewModel.kt:36-40, 53-58` | Reload the ViewModels after an import. |
| LOW | Mafatih sections of up to 21.8k characters are drawn in a single `Text`, so each zoom step lays out the whole section again on the main thread. | `MafatihReaderScreen.kt:165-178` | One paragraph per item in a `LazyColumn`. |
| LOW | RTL and TalkBack:<br>- the back arrows are inconsistent<br>- the Ramadan «‹ ›» buttons have no label | `ReaderTopBar.kt:55`, readers `:92`/`:89`, `RamadanScreen.kt:97,102` | Use `AutoMirrored` icons and add `contentDescription`. |
| LOW | The voice-manifest id rule accepts `own_x`, which can collide with a voice the user imported. | `adhan/VoiceModels.kt` | Reserve the `own_` prefix. |

### Platform and tests
- **targetSdk 34:** Play needs at least 35 now (36 for updates after Aug 2026: confirm before release). Edge-to-edge is enforced at 35, and `contentWindowInsets = WindowInsets(0,0,0,0)` (`MainActivity.kt:237`) would draw under the system bars. This is the planned v1.8.2 toolchain work (AGP 8.6+, Kotlin 2.x, and Dependabot PRs #5-#13).
- **Tests:** there is no `androidTest` folder, though the dependencies are declared. Nothing covers `PreferencesManager` (`pruneOldDeedState`, `checkAndResetDaily`), the ViewModels, navigation, high latitudes or night occasions.

---

## 3. Feature improvements found in the code
- Keep reader font sizes (Quran, Mafatih, Mizan): they're held in memory and reset on every launch.
- Resume at the last ayah or scroll position, not just the surah.
- Evening-before reminders for blessed nights, including ليلة الجمعة.
- Make a notification tap open the Today tab (`onNewIntent` only sets the category).
- Saved places with quick switching (see 1.2, item 7).
- A choice of what stops the adhan, and an option to fade its volume in.

---

## 4. Proposed release order

| Release | Contents |
|---|---|
| **v1.8.1.4 hotfix** (pre-release) | 1.1 items 1-5 (the adhan can always be stopped). 1.2 items 1-4 and 6 (the place picker in settings, a fresh location fix, the right time zone, Arabic digits). The low-risk adhan fixes: snooze mode, the `bundledAttempt` order, the audio-focus result. The night-occasion fix. |
| **v1.8.2** (planned toolchain) | targetSdk 35/36 with edge-to-edge insets, AGP/Kotlin upgrade, androidTest setup. The high-latitude rule. Direct-boot restore. Saving the snooze across reboots. `setAlarmClock`. Pausing on transient focus loss. |
| **v1.8.3** | The Quran ayah deep link and resume, kept font sizes, the Qibla rotation fix, the cold-start performance work, the Mafatih `LazyColumn`, backup-import reload, RTL and TalkBack fixes, the missing occasions (once you confirm the dates). |
| then | The v1.9.0+ roadmap as already planned (strings, translations, audio, and so on). |

Each release follows the AGENTS.md checklist. Its release note must list the device checks, since there is no physical phone here.

## 5. Checked and found clean
- Reschedule triggers: boot, time and zone changes, package replaced, and the exact-alarm permission being granted.
- Exact-alarm fallback.
- Late-alarm wording.
- No PendingIntent or notification id collisions.
- The foreground service starts correctly.
- Wake lock is released on every path.
- The adhan channel is HIGH importance, with public visibility.
- Alarms due at the same moment are handled together.
- Daylight saving in daily reminders.
- Prayer maths matches the PrayTimes Jafari parameters (Leva, Tehran).
- Qibla formula.
- Quran normalizer, with no duplicated bismillah.
- Backup codec uses an allow-list.
- Voice downloader: HTTPS only, allow-list on redirects, size caps, SHA-256 check.
- Manifest: only the launcher activity is exported, `allowBackup=false`, no cleartext traffic.
- No source file over 800 lines.

## 6. Open questions for you
1. "Another area": was it only the missing picker in adhan settings and «موقعي» failing, or do you also want saved places?
2. Which hardware stop do you want: power button, volume keys, or both?
3. The dates for the missing occasions (al-Baqir, al-Kazim, al-Hadi): which references does your marja follow?
