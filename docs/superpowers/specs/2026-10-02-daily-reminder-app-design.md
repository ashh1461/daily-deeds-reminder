# Design Spec: Daily Deeds Reminder & Tracker Android App (الأعمال اليومية)

**Date:** 2026-10-02  
**Status:** Approved by User  
**Target:** Android Native Application (.apk)

---

## 1. Overview & Goal

The **Daily Deeds (الأعمال اليومية)** application is a dedicated Islamic spiritual companion app for Android. It assists users in fulfilling a specific recommendation of 11 daily spiritual deeds ("توصية ببعض الأعمال اليومية بخط يد السيد الأسمى رض").

The application combines:
1. An **interactive daily completion checklist** with automatic midnight reset and streak/history tracking.
2. A **digital tasbeeh and Quran reader** with full Arabic diacritics (tashkeel), large tactile tap counters, and haptic feedback.
3. A **reliable local notification system** with customizable morning, evening, and post-prayer reminder alarms that survive device reboots.
4. An **aesthetic spiritual design** inspired by Islamic calligraphy and the source document's deep purple and gold accents, with native RTL support and light/dark themes.

---

## 2. The 11 Daily Deeds Specification & Content

Each deed has full Arabic text, categorization, and tracking behavior:

| # | Deed Name | Type | Target / Frequency | Full Text / Content Details |
|---|---|---|---|---|
| 1 | **سجدة الشكر** (Sajdat Al-Shukr) | Reading & Action | Daily | Virtue and instructions on performing Sajdat Al-Shukr with recommended dhikr ("شكراً شكراً", "عفواً عفواً", "شكراً لله"). |
| 2 | **تسبيح السيدة فاطمة الزهراء (ع)** | Multi-stage Counter | 100 beads (34 + 33 + 33) | 34× "الله أكبر", 33× "الحمد لله", 33× "سبحان الله". Auto-transitions between phrases upon reaching target. |
| 3 | **بسم الله الرحمن الرحيم** | Counter & Daily | 19 times (صباحاً ومساءً) | "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ" with 19-count digital tasbeeh and morning/evening completion indicators. |
| 4 | **سورة الفاتحة** | Quranic Reading | Daily | Full text of Surah Al-Fatiha (Ayahs 1 to 7) with full diacritics. |
| 5 | **آية الكرسي حتى هم فيها خالدون** | Quranic Reading | Daily | Surah Al-Baqarah Ayahs 255, 256, and 257 in full diacritics up to "أُولَٰئِكَ أَصْحَابُ النَّارِ ۖ هُمْ فِيهَا خَالِدُونَ". |
| 6 | **آية شهد الله** | Quranic Reading | Daily | Surah Al-Imran Ayahs 18, 19, and 20 in full diacritics. |
| 7 | **آية قل اللهم مالك الملك** | Quranic Reading | Daily | Surah Al-Imran Ayahs 26 and 27 in full diacritics up to "وَتَرْزُقُ مَن تَشَاءُ بِغَيْرِ حِسَابٍ". |
| 8 | **دعاء الدرع الحصينة** | Reading / Dua | Morning & Evening (صباحاً ومساءً) | "اللَّهُمَّ اجْعَلْنَا فِي دِرْعِكَ الْحَصِينَةِ الَّتِي تَجْعَلُ فِيهَا مَنْ تُرِيدُ" (with singular/plural variation option). |
| 9 | **تسبيح ما بعد الصلاة** | Reading / Dua | After each prayer (عقيب الصلوات) | "سُبْحَانَ مَنْ لَا يَعْتَدِي عَلَى أَهْلِ مَمْلَكَتِهِ، سُبْحَانَ مَنْ لَا يَأْخُذُ أَهْلَ الْأَرْضِ بِأَلْوَانِ الْعَذَابِ، سُبْحَانَ الرَّؤُوفِ الرَّحِيمِ، اللَّهُمَّ اجْعَلْ لِي فِي قَلْبِي نُوراً وَبَصَراً وَفَهْماً وَعِلْماً، إِنَّكَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ." |
| 10 | **الاستغفار** | Counter | 70 times | "أَسْتَغْفِرُ اللَّهَ رَبِّي وَأَتُوبُ إِلَيْهِ" with 70-count digital tasbeeh. |
| 11 | **الصلوات على محمد وآل محمد** | Counter | 100 times | "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَآلِ مُحَمَّدٍ" with 100-count digital tasbeeh. |

---

## 3. Architecture & Tech Stack

- **Target OS:** Android 8.0 (API Level 26) through Android 15 (API Level 35).
- **Language:** Kotlin 1.9+
- **UI Toolkit:** Jetpack Compose + Material 3 (with dynamic theming, dark/light mode, RTL native layout).
- **Architecture Pattern:** MVVM (Model-View-ViewModel) + StateFlow for clean separation of concerns and reactive UI updates.
- **Persistence Layer:**
  - `SharedPreferences` / AndroidX `DataStore` for persisting user settings, reminder preferences, daily completion state, and active counter values.
  - Automatic daily reset logic on midnight date transition while maintaining completed day statistics.
- **Notification & Scheduling:**
  - Android `AlarmManager` with `setExactAndAllowWhileIdle`.
  - `BroadcastReceiver` (`DailyReminderReceiver`) to fire notifications at scheduled times.
  - `BootReceiver` listening to `BOOT_COMPLETED` to reschedule alarms after device reboot.
  - AndroidX NotificationCompat with dedicated notification channels and deep-linking into specific deed screens.
- **Sensory Feedback:**
  - `Vibrator` / `VibratorManager` providing tactile clicks on each tasbeeh count and a distinct triple vibration pattern on goal completion.
  - Audio click sound support (toggleable).

---

## 4. UI / UX Design & Screen Flow

### Visual Identity
- **Primary Color:** Deep Royal Purple (`#4A154B` light mode / `#2E082F` dark mode).
- **Accent Color:** Islamic Gold (`#D4AF37` / `#F3C649`).
- **Surface / Background:** Soft cream/white (`#FAF8F5`) in light mode; Deep charcoal/aubergine (`#171219`) in dark mode.
- **Typography:** Arabic Naskh / Amiri style legible typography with full diacritical support.

### Screen 1: Dashboard (الرئيسية)
- **Top Bar:** Islamic decorative header, Hijri & Gregorian date, total progress wheel (e.g. "8 / 11 أُنجز اليوم").
- **Filter Tabs:**
  - "الكل" (All 11 deeds)
  - "الصباح والمساء" (Deeds 3 & 8)
  - "عقيب الصلوات" (Deeds 1, 2, 9)
  - "الأذكار والتسابيح" (Deeds 2, 3, 10, 11)
  - "الآيات والسور" (Deeds 4, 5, 6, 7)
- **Deed Card:**
  - Icon and deed number.
  - Deed title and category badge.
  - Completion status checkbox.
  - For counters: mini counter indicator (e.g. `45 / 70`).
  - Tapping opens the Reader / Tasbeeh modal or screen.

### Screen 2: Reader & Digital Tasbeeh (شاشة القراءة والمسبحة)
- **Counter Deeds:**
  - Large circular interactive tap area with animated ripple and smooth progress ring.
  - Current count / Target count in large bold numerals.
  - Current dhikr phrase in clear Arabic script.
  - Sub-stage indicator for Tasbeeh Fatima (Stage 1: الله أكبر [34], Stage 2: الحمد لله [33], Stage 3: سبحان الله [33]).
  - Reset, sound, and vibration toggle buttons.
  - Success banner with celebratory animation upon reaching target.
- **Reading Deeds (Quran & Duas):**
  - Elegant Quranic parchment style background.
  - Clear, large Arabic text with diacritical marks.
  - Option to adjust font size (A- / A+).
  - Floating "تمت القراءة" (Mark Completed) button.

### Screen 3: Reminders & Settings (التنبيهات والإعدادات)
- **Morning Reminder:** Time picker (default: 07:00 AM), toggle switch.
- **Evening Reminder:** Time picker (default: 08:00 PM), toggle switch.
- **Prayer Reminder Hints:** Toggle to notify after prayer times.
- **Haptic Feedback:** On / Off switch.
- **Sound Effect:** On / Off switch.
- **Reset Today's Progress:** Confirmation dialog to reset counts.
- **About App:** Information regarding the source ("توصية بخط يد السيد الأسمى").

---

## 5. Daily Rollover & State Persistence

- The app tracks the `lastActiveDate` formatted as `YYYY-MM-DD`.
- On launch or resume, if `currentDate != lastActiveDate`:
  - Current day's completion booleans and counter values are reset to 0.
  - Yesterday's completion score is recorded in historical stats (for streak tracking).
  - `lastActiveDate` is updated to today.
- All actions instantly persist to local storage.

---

## 6. Build & Packaging Pipeline

- Standard Gradle wrapper (`gradlew`) setup.
- App package name: `com.dailydeeds.reminder`
- Application name: `الأعمال اليومية`
- Build output: Standalone debug APK (`app-debug.apk`) placed directly in an easily accessible output folder for user download and installation.

---

## 7. Verification & Quality Gates

1. **Text Verification:** Every Ayah and Dua verified against standard Holy Quran and authentic Mafatih al-Jinan texts.
2. **Counter Logic:** Tasbeeh transitions (34 -> 33 -> 33) and boundary counts verified via unit tests.
3. **Alarm Scheduling:** Alarms successfully set and verified via `adb shell dumpsys alarm` or local trigger.
4. **Compilation Verification:** Gradle successfully builds the APK without errors.
