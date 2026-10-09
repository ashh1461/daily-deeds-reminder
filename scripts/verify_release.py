"""Checks a release APK before it is published.

    python scripts/verify_release.py app/build/outputs/apk/release/app-release.apk [--previous-sha256 HEX]

Fails (exit code 1) if the APK is not signed with APK signature scheme v2/v3, is debuggable, has an unexpected
application id or permission, or has the same SHA-256 as the previous release (a stale build). Prints the
signing certificate SHA-256 to compare with docs/DISTRIBUTION.md. Needs the Android SDK build-tools
(ANDROID_HOME, default e:/Antigravity/tools/android-sdk).
"""
import argparse
import glob
import hashlib
import os
import re
import subprocess
import sys

EXPECTED_ID = "io.github.ashh1461.wird"
EXPECTED_PERMISSIONS = {
    "android.permission.POST_NOTIFICATIONS", "android.permission.SCHEDULE_EXACT_ALARM",
    "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.VIBRATE",
    "android.permission.ACCESS_COARSE_LOCATION", "android.permission.FOREGROUND_SERVICE",
    "android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK", "android.permission.WAKE_LOCK",
    "android.permission.INTERNET",
    EXPECTED_ID + ".DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",   # added automatically by AndroidX
}


def build_tools():
    sdk = os.environ.get("ANDROID_HOME") or "e:/Antigravity/tools/android-sdk"
    dirs = sorted(glob.glob(os.path.join(sdk, "build-tools", "*")))
    if not dirs:
        sys.exit("no build-tools found under " + sdk)
    return dirs[-1]


def run(exe, *args):
    ext = ".bat" if exe == "apksigner" else ".exe"
    path = os.path.join(build_tools(), exe + (ext if os.name == "nt" else ""))
    env = dict(os.environ)
    env.setdefault("JAVA_HOME", "e:/Antigravity/tools/jdk-17")   # apksigner is a Java tool
    return subprocess.run([path, *args], capture_output=True, text=True, env=env)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("apk")
    parser.add_argument("--previous-sha256")
    parser.add_argument("--version-name")
    args = parser.parse_args()
    problems = []

    data = open(args.apk, "rb").read()
    sha = hashlib.sha256(data).hexdigest()
    print("file      :", args.apk)
    print("size      :", len(data))
    print("sha256    :", sha)
    if args.previous_sha256 and sha == args.previous_sha256.lower():
        problems.append("same hash as the previous release (stale build?)")

    signer = run("apksigner", "verify", "--print-certs", "-v", args.apk)
    text = signer.stdout + signer.stderr
    if signer.returncode != 0:
        problems.append("apksigner verify failed: " + text.strip().splitlines()[0] if text.strip() else "apksigner failed")
    else:
        for scheme in ("v2", "v3"):
            ok = re.search(r"Verified using %s scheme[^:]*: (true|false)" % scheme, text)
            print("signature : %s scheme verified = %s" % (scheme, ok.group(1) if ok else "?"))
            if not ok or ok.group(1) != "true":
                problems.append("not verified with %s scheme" % scheme)
        cert = re.search(r"certificate SHA-256 digest: ([0-9a-f]+)", text)
        print("cert sha256:", cert.group(1) if cert else "?")
        if "CN=Android Debug" in text:
            problems.append("signed with the Android debug certificate")

    badging = run("aapt2", "dump", "badging", args.apk).stdout
    package = re.search(r"package: name='([^']+)' versionCode='(\d+)' versionName='([^']+)'", badging)
    if not package:
        problems.append("cannot read the manifest")
    else:
        print("package   : %s  versionCode=%s  versionName=%s" % package.groups())
        if package.group(1) != EXPECTED_ID:
            problems.append("application id is %s, expected %s" % (package.group(1), EXPECTED_ID))
        if args.version_name and package.group(3) != args.version_name:
            problems.append("versionName is %s, expected %s" % (package.group(3), args.version_name))
    if "application-debuggable" in badging:
        problems.append("APK is debuggable")
    target = re.search(r"targetSdkVersion:'(\d+)'", badging)
    print("targetSdk :", target.group(1) if target else "?")
    permissions = set(re.findall(r"uses-permission: name='([^']+)'", badging))
    extra = permissions - EXPECTED_PERMISSIONS
    if extra:
        problems.append("unexpected permissions: " + ", ".join(sorted(extra)))
    print("permissions:", len(permissions))

    if problems:
        print("\nFAILED")
        for p in problems:
            print(" -", p)
        sys.exit(1)
    print("\nOK: release APK checks passed")


if __name__ == "__main__":
    main()
