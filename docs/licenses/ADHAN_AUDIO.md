# Bundled adhan recordings

Two adhan recordings ship inside the APK, in `app/src/main/assets/adhan/`, as the app's built-in voices
(`adhan/BundledVoices.kt`). The Karbala recording is the default adhan.

| In the app | App id | Asset |
|---|---|---|
| «أذان الحرم الحسيني – كربلاء المقدسة» (Adhan, Shrine of Imam Hussain (a.s.), Karbala) | `karbala_imam_hussain` (default) | `adhan/adhan_karbala_imam_hussain.ogg` |
| «أذان الحرم الرضوي – مشهد المقدسة» (Adhan, Shrine of Imam al-Ridha (a.s.), Mashhad) | `mashhad_imam_reza` | `adhan/adhan_mashhad_imam_reza.ogg` |

## Where they come from
The recordings were supplied by the project owner, who states that they are free of licence restrictions and
**confirmed this again on 2026-10-14**. No licence text, source URL or reciter's name accompanies the files: they are MP3
files converted from videos published online. The owner's confirmation is therefore the only evidence of the rights; it
was **not independently verified**, and the reciters are not credited because the files do not identify them.

If you hold rights in either recording and object to its use, open an issue at
https://github.com/ashh1461/daily-deeds-reminder/issues and the file will be removed.

Files as supplied (titles as received). The originals are not in this repository.

| Original file | Size | Duration | SHA-256 |
|---|---|---|---|
| `Most Beautiful Azan ever heard Live Azan in Karbala Shrine Hazrat Imam Hussain a.s.mp3` | 7,229,995 bytes | 301.2 s | `fb4435aea5d49d5747f2db305f471c4544935d7c56efca7c7121257c778e0ca0` |
| `Amazing Azan Mashhad Imam Ali-Rida ( أذان مشهد عند الامام علي الرضا (ع.mp3` | 7,601,142 bytes | 316.7 s | `75a9235a74088ee24b6e6e4caa27643a01833e111377e7f1fd126bd430d74c75` |

## Processing
1. Silence trimmed at one end of each recording (the silence map of the originals showed digital silence there): the
   first 1.4 s of the Karbala recording (silent up to 1.6 s); the Mashhad recording is cut at 310.6 s, where its
   trailing silence begins (310.3 s), which removes its last 6.1 s. Nothing else was cut.
2. Static gain to an integrated loudness of -14 LUFS, with a peak limiter (true peak at most -4.6 dBFS).
3. A 0.25 s fade-in and a 1.5 s fade-out.
4. Encoded as Ogg Opus, 80 kbps, stereo, 48 kHz, with the metadata stripped.

No other change was made to the audio.

## Result files
| Asset | Size | Duration | SHA-256 |
|---|---|---|---|
| `adhan/adhan_karbala_imam_hussain.ogg` | 2,857,049 bytes | 299.8 s | `3689a380cb2880806b58ba251d544ae916a874233000a95de5298c2597d7245b` |
| `adhan/adhan_mashhad_imam_reza.ogg` | 2,802,699 bytes | 310.6 s | `e59472508dc5d08e2e97982b83ace228f7ff5db3c5a6cebce78a5a77224da843` |

`BundledVoicesTest` fails the build if a file's size or SHA-256 changes, or if the duration declared in
`BundledVoices.kt` is shorter than the audio (the playback time limit is derived from it). The files are stored
uncompressed in the APK (`noCompress += "ogg"` in `app/build.gradle.kts`) so they play straight from it.

The app stays free and ad-free. The Settings screen («حول التطبيق») carries a short Arabic version of this note.
Before any store publication the owner must confirm the rights in these recordings; see `docs/PLAY_LISTING.md`.
