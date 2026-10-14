# Adhan voice packs

The app never ships a copyrighted recording. Voices are optional downloads listed in `voices/manifest.json`
(served over HTTPS from this repository), or files the user picks from their own phone.

## Rights gate
A voice is shown in the app **only if** its manifest entry has a non-empty `licence` and `permission` note,
a valid `sha256`, a size of at most 25 MB and an `https` URL on an allowed GitHub host. The unit tests
(`VoiceAndPolicyTest`) fail the build if the manifest in the repository breaks any of these rules, and the
downloader refuses any file whose SHA-256 does not match.

Famous recordings (for example the Moazenzadeh Ardabili family or Seyyed Javad Zabihi) have **no free licence**.
Do not add them without written permission from the rights holder; record that permission in the entry.

## Adding a voice
1. Upload the audio (ogg/mp3/m4a, 5 s to 15 min, 25 MB max) as an asset of a GitHub release, for example tag `voices`.
2. Compute `sha256sum file.ogg` and note the size in bytes.
3. Add an entry to `voices/manifest.json`:

```json
{
  "id": "example_voice",
  "nameAr": "اسم الصوت",
  "reciter": "اسم المؤذن",
  "includesWilayah": false,
  "sizeBytes": 1234567,
  "sha256": "<64 hex characters>",
  "url": "https://github.com/ashh1461/daily-deeds-reminder/releases/download/voices/example_voice.ogg",
  "durationSec": 150,
  "licence": "CC BY 4.0 (or the reciter's licence)",
  "permission": "Written permission from <rights holder>, <date>, <where it is stored>"
}
```
4. Placeholders that are not cleared yet use `"status": "awaiting-rights"` and no `url`; they are never shown.
5. Run the unit tests, commit, and the voice appears in the app's "أصوات الأذان" screen after the manifest is merged to `master`.
