# Google Play listing: draft answers (not submitted)

Fields marked **owner** must be filled by the Play account owner.

## Store listing
| Field | English | Arabic |
|---|---|---|
| App name (max 30) | Wird: Shia daily companion | ورد: رفيقك اليومي |
| Short description (max 80) | Jafari prayer times, adhan, Quran, Mafatih, Sahifa and daily deeds. | أوقات الصلاة الجعفرية والأذان والقرآن والمفاتيح والصحيفة والأعمال اليومية. |
| Category | Lifestyle | |
| Contact email / website | **owner** | |
| Privacy policy URL | **owner** (host `docs/PRIVACY.md`) | |

### Full description (English)
Wird brings the daily worship of a Shia Muslim into one calm, ad-free app.

- Prayer times by the Jafari method (Leva / Qom, University of Tehran or custom), with a full-length adhan, pre-adhan reminder, snooze and manual adjustments. Search over 34,000 cities offline.
- Daily deeds with counters and reminders, tasbih al-Zahra, a make-up (qada) prayers and fasts tracker.
- The complete Quran with Tafsir al-Mizan, the complete Mafatih al-Jinan and Sahifa Sajjadiyya, weekday duas and ziyarat, favorites, resume reading and global search.
- Hijri calendar with the occasions of the Ahl al-Bayt, Ramadan and Imsak timetable with PDF export, Salat al-Ayat guide and eclipse calendar, khums calculator, qibla compass and a home-screen widget.
- Private by design: no accounts, no ads, no tracking; your data stays on your phone, with export and import of a backup file.
The Arabic interface is complete; more languages are planned.

### الوصف الكامل (العربية)
«ورد» يجمع عبادات المؤمن اليومية في تطبيق هادئ بلا إعلانات.

- أوقات الصلاة بالطريقة الجعفرية (ليفا / قم، جامعة طهران، أو مخصصة) مع أذان كامل وتنبيه قبل الصلاة وتأجيل وتعديل يدوي، وبحث بلا إنترنت في 34 ألف مدينة.
- الأعمال اليومية بعدّادات وتذكيرات، وتسبيح الزهراء (ع)، ومتابعة قضاء الصلوات والصيام.
- القرآن الكريم كاملاً مع تفسير الميزان، ومفاتيح الجنان والصحيفة السجادية كاملتين، وأدعية الأيام وزياراتها، والمفضلة ومتابعة القراءة والبحث الشامل.
- التقويم الهجري ومناسبات أهل البيت (ع)، وجدول رمضان والإمساك مع PDF، وصلاة الآيات وتقويم الخسوف والكسوف، وحاسبة الخمس، والقبلة، وودجت للشاشة الرئيسية.
- الخصوصية أولاً: بلا حسابات ولا إعلانات ولا تتبّع، وبياناتك على هاتفك مع تصدير واستيراد نسخة احتياطية.

## Graphics (in `branding/play/`)
Icon `icon-512.png` (512×512), feature graphic `feature-graphic-1024x500.png`. Phone screenshots: **owner / emulator** (Today, prayer times, adhan settings, Quran, Mafatih, Ramadan timetable).

## Data safety form
| Question | Answer |
|---|---|
| Does the app collect or share user data? | No data collected, no data shared |
| Approximate location | Read on the device only when the user taps "my location"; not transmitted; so "not collected" |
| Data encrypted in transit | Yes (the only network use is HTTPS) |
| Can users request deletion? | Not applicable: no data leaves the device; uninstalling removes it |
| Ads / analytics / accounts | None |

## Permission declarations and justifications
| Permission | Justification for the declaration form |
|---|---|
| `SCHEDULE_EXACT_ALARM` | Core function: alarms at the exact prayer time (adhan and reminders). Without it prayer alerts can arrive minutes late. The app opens the system "Alarms & reminders" page and works with inexact alarms if the user declines. |
| `FOREGROUND_SERVICE` + `FOREGROUND_SERVICE_MEDIA_PLAYBACK` | Playing the adhan audio at prayer time, with Stop and Snooze buttons. |
| `POST_NOTIFICATIONS` | Prayer, adhan, occasion and deed reminders. |
| `RECEIVE_BOOT_COMPLETED` | Re-schedule alarms after a reboot, time change or app update. |
| `WAKE_LOCK` | Keep the CPU awake while the adhan plays. |
| `VIBRATE` | Vibration with the adhan and tasbih counters. |
| `ACCESS_COARSE_LOCATION` | Optional "my location" to choose the city for prayer times. |
| `INTERNET` | Optional download of adhan voice packs from GitHub over HTTPS (host allow-list, size and checksum checks). |

If Play rejects `SCHEDULE_EXACT_ALARM` for this category, the alternative is `USE_EXACT_ALARM` (auto-granted, intended for alarm and calendar apps) with the matching declaration.

## Content rating and audience
Religious reference app: no violence, sexual content, gambling, user-generated content, purchases or ads. Target audience 13+ (general audience content; not designed for children).

## Release checklist
Signed AAB from `bundleRelease` with `targetSdk` at the level Play requires (v1.8.1), upload key = the key in `docs/DISTRIBUTION.md`, internal testing track first, then production.
