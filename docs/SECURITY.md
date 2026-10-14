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

## Dependencies
Last OSV scan (2026-10-08): 93 resolved packages, 0 advisories.
