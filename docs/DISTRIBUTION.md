# Distribution: signing, verification, Google Play and Play Protect

## Identity
| | |
|---|---|
| App name | «ورد» (Wird) |
| Application id | `io.github.ashh1461.wird` (debug builds: `io.github.ashh1461.wird.debug`) |
| Kotlin namespace | `com.dailydeeds.reminder` (unchanged; the application id is independent of it) |
| Release signing certificate (SHA-256) | `9B:0C:29:2B:CC:9E:1F:97:30:32:FC:B2:DF:B4:AA:9E:79:A4:16:49:86:03:88:56:C1:09:2A:9B:5C:F1:FF:36` |

Every published APK must show this certificate (`python scripts/verify_release.py <apk>` prints it).

## The signing key
- Keystore: `%USERPROFILE%\.android\wird-release.jks` (PKCS12, RSA 4096, valid until 2056, alias `wird`). Passwords are in `%USERPROFILE%\.android\wird-keystore.properties` (`storeFile`, `storePassword`, `keyAlias`, `keyPassword`; for a PKCS12 keystore the key password is the store password). Gradle reads that file, or the `RELEASE_KEYSTORE_PATH`, `RELEASE_KEYSTORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD` environment variables (CI). Nothing secret is in the repository.
- **Back it up offline** (password manager or an encrypted drive) together with the properties file. A second copy was written to `E:\Antigravity\backups\wird-release\` on the build machine, which only protects against accidental deletion. If the key is lost or leaked, users cannot update in place and Play needs an upload-key reset.
- GitHub Actions: add repository secrets `RELEASE_KEYSTORE_B64` (`[Convert]::ToBase64String([IO.File]::ReadAllBytes("wird-release.jks"))`), `RELEASE_KEYSTORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD`. Pushing a `v*` tag then builds, tests, signs and verifies the release and uploads it as a workflow artifact (`.github/workflows/release.yml`). Publishing the GitHub release stays manual.
- When publishing on Google Play, enrol in Play App Signing and use this key as the upload key.

## Releasing
```
gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleRelease :app:bundleRelease
python scripts/verify_release.py app/build/outputs/apk/release/app-release.apk --version-name X.Y.Z --previous-sha256 <hash of the previous release>
copy the APK to build/distributions/Wird-vX.Y.Z.apk, write its .sha256, then gh release create vX.Y.Z ...
```
The verify script fails on a missing v2/v3 signature, the debug certificate, a debuggable APK, a wrong application id or permission, or an unchanged hash.

## Why Google warned on every install of the 1.x builds
The GitHub releases up to 1.7.0 were the **debug** build: debuggable, application id `com.dailydeeds.reminder.debug`, signed with a machine-generated debug key. Play Protect treats a debuggable, debug-signed APK from an unknown source as unsafe. The release build removes that cause. A brand-new, browser-downloaded APK with no reputation can still show Play Protect's "unrecognized app" prompt once; that goes away when the app is installed through Google Play or the developer is verified (below). `targetSdk` 34 is also older than Google likes; v1.8.1 raises it.

## Moving from the old debug build
The new application id installs next to the old app and cannot read its data. In the old app: Settings → «البيانات والنسخ الاحتياطي» → export; in Wird: import the file (it restarts), then uninstall the old app. Voice files are not part of a backup.

## Google Play preparation (not published yet)
Ready in the repo: signed AAB (`bundleRelease`), listing text and answers in `docs/PLAY_LISTING.md`, privacy policy text in `docs/PRIVACY.md`, 512 px icon and 1024×500 feature graphic in `branding/play/`.
Needs the account owner:
1. A Google Play developer account with identity verification (a one-time registration fee applies; check the current amount in Play Console).
2. A public URL for the privacy policy (for example GitHub Pages serving `docs/PRIVACY.md`). Publishing it is outward-facing, so it was not done.
3. Phone screenshots (needs a device or emulator), the content-rating questionnaire, the Data-safety form and the permission declarations (answers drafted in `docs/PLAY_LISTING.md`).
4. Raising `targetSdk` to the level Play currently requires (planned for v1.8.1).

## Google's developer verification
Google started requiring apps on certified Android devices to come from verified developers on 2026-09-30 in Brazil, Indonesia, Singapore and Thailand, with a global rollout planned for 2027. Unregistered apps can still be installed through `adb` or an "advanced flow" (a developer-options switch, a one-time 24-hour wait, and a warning on each install). Register the app in the Android Developer Console with the signing certificate above; a *limited distribution* account (up to 20 devices, no government ID or fee) exists for hobby use, and a full account is needed for unlimited distribution. Check Google's "Android developer verification" page for the current rules.

## If Play Protect still flags a build
Google's appeal form for Play Protect warnings asks for the APK's SHA-256 (it is in the `.sha256` file of each release); decisions are final and unanswered, so check that the APK is the signed release build first. Re-scanning from Play Store → profile → Play Protect often clears stale verdicts.
