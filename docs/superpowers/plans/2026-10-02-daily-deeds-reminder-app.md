# Daily Deeds Reminder & Tracker (الأعمال اليومية) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a native Android application (.apk) in Kotlin with Jetpack Compose that tracks, reminds, and facilitates the daily recitation of the 11 recommended spiritual deeds from the provided image.

**Architecture:** Clean MVVM architecture with Jetpack Compose for the UI layer, reactive `StateFlow` for state management, `SharedPreferences` for daily completion persistence and midnight rollover, and Android's `AlarmManager` with BroadcastReceivers for reliable local reminder notifications that survive reboots.

**Tech Stack:** Kotlin 1.9+, Android SDK (API 26-35), Jetpack Compose (Material 3), AndroidX Core & Navigation, AlarmManager, Haptic/VibratorManager, Gradle 8.2+.

**Spec:** `docs/superpowers/specs/2026-10-02-daily-reminder-app-design.md`

## Global Constraints

- Full Right-to-Left (RTL) Arabic support with legible Arabic typography and accurate diacritical marks (tashkeel).
- All 11 deeds and Quranic verses/duas from the user image must be included verbatim.
- Reliable offline operation with zero external network dependencies or tracking.
- Output a standalone, ready-to-install Android APK file.

## Review Focus

1. **Tasbeeh Fatima multi-stage transitions:** The counter must seamlessly transition from Allahu Akbar (34) to Alhamdulillah (33) to SubhanAllah (33) and mark completed at 100.
2. **Midnight date rollover:** Opening the app on a new day must automatically reset active completion flags while preserving streak counts.
3. **Android 13+ Notification permissions:** The app must gracefully request `POST_NOTIFICATIONS` and `SCHEDULE_EXACT_ALARM` without crashing if denied.
4. **Boot survival:** `BootReceiver` must re-register alarms after device restart.
5. **Exact Quranic diacritics:** Verses for Al-Fatiha, Ayat Al-Kursi (255-257), Shahid Allah (18-20), and Qul Allahumma (26-27) must match standard Uthmani/canonical text.

---

### Task 1: Environment & Android Project Scaffolding

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `app/build.gradle.kts`
- Create: `gradle/wrapper/gradle-wrapper.properties`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `gradlew.bat`

**Interfaces:**
- Consumes: None
- Produces: Compilable Android project structure with Jetpack Compose and AndroidX dependencies.

- [ ] **Step 1: Check and prepare JDK 17 and Android command-line tools**
Ensure JDK 17 is installed (via winget if needed) and `JAVA_HOME` / `PATH` is configured.

- [ ] **Step 2: Create root Gradle build and wrapper configuration**
Configure `settings.gradle.kts`, `build.gradle.kts`, and `gradle/wrapper/gradle-wrapper.properties` targeting Gradle 8.4 and Android Gradle Plugin 8.2.

- [ ] **Step 3: Create `app/build.gradle.kts`**
Configure dependencies: `androidx.core:core-ktx`, `androidx.activity:activity-compose`, `androidx.compose.material3:material3`, `androidx.compose.ui:ui`, `androidx.lifecycle:lifecycle-viewmodel-compose`, `androidx.navigation:navigation-compose`.

- [ ] **Step 4: Create `AndroidManifest.xml` with permissions and application declarations**
Include `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, `RECEIVE_BOOT_COMPLETED`, `VIBRATE`. Declare `MainActivity`, `DailyReminderReceiver`, and `BootReceiver`.

- [ ] **Step 5: Verify project structure syntax**
Run syntax check and git commit:
```bash
git add settings.gradle.kts build.gradle.kts app/build.gradle.kts gradle/ app/src/main/AndroidManifest.xml
git commit -m "chore: scaffold android project and gradle configuration"
```

---

### Task 2: Data Model, Content Repository & Verifications

**Files:**
- Create: `app/src/main/java/com/dailydeeds/reminder/model/Deed.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/data/DeedsRepository.kt`
- Test: `app/src/test/java/com/dailydeeds/reminder/data/DeedsRepositoryTest.kt`

**Interfaces:**
- Consumes: None
- Produces:
  - `data class Deed(id: Int, title: String, subtitle: String, type: DeedType, targetCount: Int, category: DeedCategory, content: String, instructions: String?)`
  - `enum class DeedType { READING, COUNTER, MULTI_STAGE_COUNTER }`
  - `enum class DeedCategory { ALL, MORNING_EVENING, AFTER_PRAYER, TASBEEH, QURAN }`
  - `fun DeedsRepository.getAllDeeds(): List<Deed>`
  - `fun DeedsRepository.getDeedById(id: Int): Deed?`

- [ ] **Step 1: Write failing unit tests for DeedsRepository**
Test that `getAllDeeds()` returns exactly 11 deeds, every deed has non-empty Arabic title and content, and specific deeds (Ayat Al-Kursi, Tasbeeh Fatima, 70 Astaghfirullah, 100 Salawat) have expected target counts and texts.

- [ ] **Step 2: Run test to verify it fails**
Expected: Compilation fail (models and repository do not exist yet).

- [ ] **Step 3: Implement `Deed.kt` data structures and `DeedsRepository.kt`**
Populate all 11 deeds with exact Arabic diacritical texts:
1. سجدة الشكر
2. تسبيح فاطمة الزهراء (34-33-33)
3. 19 مرة بسم الله الرحمن الرحيم
4. سورة الفاتحة (1-7)
5. آية الكرسي حتى هم فيها خالدون (البقرة 255-257)
6. آية شهد الله (آل عمران 18-20)
7. آية قل اللهم مالك الملك (آل عمران 26-27)
8. دعاء الدرع الحصينة (صباحاً ومساءً)
9. تسبيح ما بعد الصلاة (سبحان من لا يعتدي...)
10. 70 استغفار
11. 100 صلوات

- [ ] **Step 4: Run test to verify it passes**
Verify that all 11 deeds pass validation tests.

- [ ] **Step 5: Commit**
```bash
git add app/src/main/java/com/dailydeeds/reminder/model/ app/src/main/java/com/dailydeeds/reminder/data/ app/src/test/
git commit -m "feat: implement deeds data models and comprehensive Arabic content repository"
```

---

### Task 3: Preferences & Daily Rollover State Engine

**Files:**
- Create: `app/src/main/java/com/dailydeeds/reminder/data/PreferencesManager.kt`
- Test: `app/src/test/java/com/dailydeeds/reminder/data/PreferencesManagerTest.kt`

**Interfaces:**
- Consumes: `Deed`, `DeedType`
- Produces:
  - `PreferencesManager(context: Context)`
  - `fun isDeedCompleted(deedId: Int, date: String): Boolean`
  - `fun setDeedCompleted(deedId: Int, completed: Boolean, date: String)`
  - `fun getDeedCount(deedId: Int, date: String): Int`
  - `fun setDeedCount(deedId: Int, count: Int, date: String)`
  - `fun checkAndResetDaily(today: String)`
  - `fun getMorningReminderTime(): Pair<Int, Int>`
  - `fun getEveningReminderTime(): Pair<Int, Int>`
  - `fun isHapticsEnabled(): Boolean`

- [ ] **Step 1: Write unit tests for date rollover and completion tracking**
Test that setting count/completed on day A persists, querying day B resets active counters for day B, and streak logic calculates correctly.

- [ ] **Step 2: Implement `PreferencesManager.kt`**
Store daily state keyed by `deed_${id}_date_${date}` and user preferences (reminder times, vibration, sound). Implement `checkAndResetDaily` to detect day changes.

- [ ] **Step 3: Run tests and verify**
Verify all tests pass.

- [ ] **Step 4: Commit**
```bash
git add app/src/main/java/com/dailydeeds/reminder/data/PreferencesManager.kt app/src/test/
git commit -m "feat: implement daily completion persistence and midnight rollover engine"
```

---

### Task 4: Notification Engine & Alarm Scheduling Subsystem

**Files:**
- Create: `app/src/main/java/com/dailydeeds/reminder/notification/NotificationHelper.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/notification/AlarmScheduler.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/receiver/DailyReminderReceiver.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/receiver/BootReceiver.kt`

**Interfaces:**
- Consumes: `PreferencesManager`
- Produces:
  - `fun NotificationHelper.createNotificationChannels()`
  - `fun NotificationHelper.showDailyReminderNotification(title: String, message: String)`
  - `fun AlarmScheduler.scheduleDailyReminders()`
  - `fun AlarmScheduler.cancelReminders()`

- [ ] **Step 1: Implement `NotificationHelper.kt`**
Set up high-importance NotificationChannel ("daily_deeds_reminders") with Islamic title, icon, and PendingIntent to launch `MainActivity`.

- [ ] **Step 2: Implement `AlarmScheduler.kt`**
Calculate next trigger timestamps for morning (07:00 AM) and evening (08:00 PM), and call `AlarmManager.setExactAndAllowWhileIdle()` with repeating logic.

- [ ] **Step 3: Implement `DailyReminderReceiver.kt` & `BootReceiver.kt`**
`DailyReminderReceiver` triggers the notification and schedules next day's alarm. `BootReceiver` restores scheduled alarms upon `ACTION_BOOT_COMPLETED`.

- [ ] **Step 4: Commit**
```bash
git add app/src/main/java/com/dailydeeds/reminder/notification/ app/src/main/java/com/dailydeeds/reminder/receiver/
git commit -m "feat: implement reliable alarm scheduling and notification receivers"
```

---

### Task 5: Design Tokens, Theme & Reusable UI Components

**Files:**
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/theme/Color.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/theme/Theme.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/theme/Type.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/components/DeedCard.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/components/CircularProgressBar.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/components/FilterChipRow.kt`

**Interfaces:**
- Consumes: `Deed`, `DeedCategory`
- Produces:
  - `Theme`: Deep purple (`#4A154B`), soft gold (`#D4AF37`), dark mode support.
  - `DeedCard`: Card with deed number, title, badge, completion status, and click callback.
  - `CircularProgressBar`: Animated circular completion indicator.
  - `FilterChipRow`: Category selector for filtering deeds.

- [ ] **Step 1: Define Color palettes and Typography**
Implement Royal Purple and Islamic Gold Color palette, dark theme variants, and RTL typography.

- [ ] **Step 2: Build `CircularProgressBar.kt` & `FilterChipRow.kt`**
Compose components with smooth progress animation and category chips ("الكل", "صباحاً ومساءً", "عقيب الصلوات", etc.).

- [ ] **Step 3: Build `DeedCard.kt`**
Interactive card with checkmark animation, category badge, and count progress indicator.

- [ ] **Step 4: Commit**
```bash
git add app/src/main/java/com/dailydeeds/reminder/ui/
git commit -m "feat: implement theme, colors, and reusable UI components"
```

---

### Task 6: Main Dashboard, Digital Tasbeeh Reader & Settings Screens

**Files:**
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/screens/HomeScreen.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/screens/ReaderCounterScreen.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/ui/screens/SettingsScreen.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/viewmodel/MainViewModel.kt`
- Create: `app/src/main/java/com/dailydeeds/reminder/MainActivity.kt`

**Interfaces:**
- Consumes: All previous tasks.
- Produces: Complete navigable Android application with all screens, haptic feedback, and interaction.

- [ ] **Step 1: Implement `MainViewModel.kt`**
Manages UI state: list of deeds with completion states, current filter category, active deed detail, daily progress percentage, settings state.

- [ ] **Step 2: Implement `HomeScreen.kt`**
Header showing Gregorian & Hijri dates, progress ring, filter chips, and lazy column of deed cards.

- [ ] **Step 3: Implement `ReaderCounterScreen.kt`**
Dual-mode screen:
- Counter mode: Large interactive circle for tapping, counter display, haptic vibration via `Vibrator`, multi-stage progression for Tasbeeh Fatima (34 -> 33 -> 33), goal achieved animation.
- Reader mode: Formatted Quranic text display with font sizing controls, bismillah header, and "تمت القراءة" button.

- [ ] **Step 4: Implement `SettingsScreen.kt`**
Morning & evening reminder time configuration, notification toggles, sound/vibration switches, and info about the recommendation source.

- [ ] **Step 5: Implement `MainActivity.kt` with Navigation & Permissions**
Set up `CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl)`, Compose NavHost, and notification permission requests.

- [ ] **Step 6: Commit**
```bash
git add app/src/main/java/com/dailydeeds/reminder/
git commit -m "feat: implement dashboard, digital tasbeeh reader, and settings screens"
```

---

### Task 7: Build, Compilation & APK Generation

**Files:**
- Output: `app/build/outputs/apk/debug/app-debug.apk`
- Copy to: `DailyDeeds-AlAmalAlYawmiyyah.apk` (in project root)

**Interfaces:**
- Consumes: Completed Android codebase.
- Produces: Valid, standalone `.apk` package file ready for installation on user device.

- [ ] **Step 1: Execute Gradle assembleDebug build**
Run `./gradlew assembleDebug --stacktrace` to compile Kotlin sources, package resources, and generate APK.

- [ ] **Step 2: Verify APK generation**
Check that the APK exists, verify its size, and copy it to the root directory as `DailyDeeds-AlAmalAlYawmiyyah.apk`.

- [ ] **Step 3: Final verification and summary**
Ensure all 11 deeds, reminders, and UI components are packaged properly.

- [ ] **Step 4: Commit**
```bash
git add .
git commit -m "build: compile and package Daily Deeds Reminder Android APK"
```
