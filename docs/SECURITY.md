# Security notes

## Permissions (checked by `VoiceAndPolicyTest`)
POST_NOTIFICATIONS, SCHEDULE_EXACT_ALARM, RECEIVE_BOOT_COMPLETED, VIBRATE, ACCESS_COARSE_LOCATION (only when the user taps
"موقعي"), FOREGROUND_SERVICE + FOREGROUND_SERVICE_MEDIA_PLAYBACK + WAKE_LOCK (playing the adhan), INTERNET (voice packs only).
Any other permission makes the test fail.

## Network
- `VoicePackDownloader` is the only class that opens connections (a test scans the sources for network code elsewhere).
- HTTPS only, hosts limited to raw.githubusercontent.com / github.com / objects.githubusercontent.com /
  release-assets.githubusercontent.com, re-checked after every redirect (max 3); cleartext is disabled in
  `network_security_config.xml`.
- Downloads start only when the user taps "تنزيل"; size is capped at 25 MB and the SHA-256 in the manifest must match.
- Nothing is uploaded: requests are plain GETs without identifiers.

## Components
The app's own exported components are the launcher activity and the boot receiver only; the adhan service and all other receivers are not exported.
The merged release manifest also contains AndroidX's `ProfileInstallReceiver` (exported, but protected by the system-only `android.permission.DUMP`).
Backups are disabled (`allowBackup=false`).

## Backup import
`BackupCodec.parse` treats a backup file as untrusted: at most 2 MB, only keys on an allow-list, each value must have the type the key has in the app, and the place (latitude, longitude, time zone, name) is validated as a whole and dropped if any part is invalid. `PreferencesManager.getPlace` validates the stored place again when it is read, so a bad value cannot crash prayer calculation. Tests cover malformed JSON, wrong types, unknown keys and hostile places.

## Widget
`PrayerWidgetProvider` is not exported; the refresh alarm is an explicit broadcast to it, and no new permission is needed.

## Dependencies
Last OSV scan (2026-10-08): 93 resolved packages, 0 advisories.
