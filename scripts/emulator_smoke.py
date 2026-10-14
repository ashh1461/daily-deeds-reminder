"""Smoke test of an installed APK on a running emulator/device: install, launch, visit each tab, take
screenshots and fail on any crash in logcat. This is the check that proves a minified (R8) release build
actually runs.

    python scripts/emulator_smoke.py app/build/outputs/apk/release/app-release.apk --out build/smoke

Needs one emulator or device visible to adb (ANDROID_HOME, default e:/Antigravity/tools/android-sdk).
The bottom bar has five tabs (right to left: today, worship, Quran, Mafatih, Sahifa).
"""
import argparse
import os
import re
import subprocess
import sys
import time

APP_ID = "io.github.ashh1461.wird"
MAIN_ACTIVITY = "com.dailydeeds.reminder.MainActivity"   # class lives in the Kotlin namespace, not the application id
ADB = os.path.join(os.environ.get("ANDROID_HOME", "e:/Antigravity/tools/android-sdk"), "platform-tools", "adb")
TABS = ["today", "worship", "quran", "mafatih", "sahifa"]


def adb(*args, check=True, binary=False):
    result = subprocess.run([ADB, *args], capture_output=True)
    if check and result.returncode != 0:
        sys.exit("adb %s failed: %s" % (" ".join(args), result.stderr.decode("utf-8", "replace")[:300]))
    return result.stdout if binary else result.stdout.decode("utf-8", "replace")


def size():
    m = re.search(r"(\d+)x(\d+)", adb("shell", "wm", "size"))
    return int(m.group(1)), int(m.group(2))


def shot(out, name):
    remote = "/sdcard/smoke_%s.png" % name
    adb("shell", "screencap", "-p", remote)
    adb("pull", remote, os.path.join(out, name + ".png"))
    adb("shell", "rm", remote, check=False)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("apk")
    parser.add_argument("--out", default="build/smoke")
    parser.add_argument("--package", default=APP_ID)
    args = parser.parse_args()
    os.makedirs(args.out, exist_ok=True)

    adb("shell", "pm", "uninstall", args.package, check=False)
    print(adb("install", "-r", args.apk).strip())
    for permission in ("POST_NOTIFICATIONS",):
        adb("shell", "pm", "grant", args.package, "android.permission." + permission, check=False)
    adb("logcat", "-c")
    # The stripped emulator image's own Bluetooth service crashes and throws a dialog over the app.
    adb("shell", "pm", "disable-user", "--user", "0", "com.android.bluetooth", check=False)
    adb("shell", "settings", "put", "global", "hide_error_dialogs", "1", check=False)
    adb("shell", "input", "keyevent", "KEYCODE_WAKEUP")
    adb("shell", "wm", "dismiss-keyguard", check=False)
    adb("shell", "am", "start", "-n", args.package + "/" + MAIN_ACTIVITY, check=False)
    time.sleep(12)
    width, height = size()
    shot(args.out, "01-launch")

    bar_y = int(height * 0.955)
    for index, tab in enumerate(TABS):
        x = int(width * (len(TABS) - index - 0.5) / len(TABS))
        adb("shell", "input", "tap", str(x), str(bar_y))
        time.sleep(3)
        shot(args.out, "%02d-%s" % (index + 2, tab))

    pid = adb("shell", "pidof", args.package, check=False).strip()
    top = adb("shell", "dumpsys", "activity", "activities", check=False)
    # Launcher icon: go home and open the app drawer (swipe up) for a picture of the icon.
    adb("shell", "input", "keyevent", "KEYCODE_HOME")
    time.sleep(3)
    shot(args.out, "08-home")
    adb("shell", "input", "swipe", str(width // 2), str(int(height * 0.8)), str(width // 2), str(int(height * 0.2)), "400")
    time.sleep(3)
    shot(args.out, "09-drawer")
    log = adb("logcat", "-d", "-v", "brief", "-b", "crash,main", check=False)
    if args.package + "/" + MAIN_ACTIVITY not in top:
        log += "\nMISSING: the app is not the foreground activity"
    open(os.path.join(args.out, "logcat.txt"), "w", encoding="utf-8").write(log)
    problems = []
    if not pid:
        problems.append("the app process is not running after the tour")
    if "FATAL EXCEPTION" in log:
        problems.append("FATAL EXCEPTION in logcat")
    if "MISSING: the app is not the foreground activity" in log:
        problems.append("the app is not the foreground activity at the end of the tour")
    if re.search(r"ANR in " + re.escape(args.package), log):
        problems.append("ANR in logcat")
    if problems:
        print("FAILED:", "; ".join(problems), "(see %s)" % os.path.join(args.out, "logcat.txt"))
        sys.exit(1)
    print("OK: launched, visited %d tabs, no crash; screenshots in %s" % (len(TABS), args.out))


if __name__ == "__main__":
    main()
