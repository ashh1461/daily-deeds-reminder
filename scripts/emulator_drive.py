"""Tiny adb driver for checking an installed build by hand, one step at a time.

    python scripts/emulator_drive.py start                      # wake the screen, launch Wird
    python scripts/emulator_drive.py tap 360 900 --shot hub     # tap, wait, save build/emulator/hub.png
    python scripts/emulator_drive.py swipe 360 1200 360 400
    python scripts/emulator_drive.py key KEYCODE_BACK
    python scripts/emulator_drive.py log                        # crashes of the app in the crash buffer

Look at each screenshot before choosing the next tap. This replaces an automated uiautomator walk, because
`uiautomator dump` itself crashes on the emulator image used here. The screen is 720x1560 on the wird_test AVD.
"""
import argparse
import os
import subprocess
import time

APP_ID = "io.github.ashh1461.wird"
MAIN_ACTIVITY = "com.dailydeeds.reminder.MainActivity"
ADB = os.path.join(os.environ.get("ANDROID_HOME", "e:/Antigravity/tools/android-sdk"), "platform-tools", "adb")


def adb(*args):
    return subprocess.run([ADB, *args], capture_output=True).stdout.decode("utf-8", "replace")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("command", choices=["start", "tap", "swipe", "key", "log", "shot"])
    parser.add_argument("args", nargs="*")
    parser.add_argument("--shot", help="name of the screenshot to save after the action")
    parser.add_argument("--wait", type=float, default=4.0, help="seconds to wait after the action")
    parser.add_argument("--out", default="build/emulator")
    a = parser.parse_args()

    if a.command == "start":
        adb("shell", "svc", "power", "stayon", "true")   # the virtual display otherwise sleeps and ignores taps
        adb("shell", "input", "keyevent", "KEYCODE_WAKEUP")
        adb("shell", "am", "start", "-n", APP_ID + "/" + MAIN_ACTIVITY)
    elif a.command == "tap":
        adb("shell", "input", "tap", *a.args[:2])
    elif a.command == "swipe":
        adb("shell", "input", "swipe", *a.args[:4], "400")
    elif a.command == "key":
        adb("shell", "input", "keyevent", a.args[0])
    elif a.command == "log":
        lines = [l for l in adb("logcat", "-d", "-v", "brief", "-b", "crash").splitlines() if APP_ID in l or "dailydeeds" in l]
        print("crash-buffer lines mentioning the app: %d" % len(lines))
        for line in lines[:12]:
            print(line[:220])
    time.sleep(a.wait)
    if a.shot or a.command == "shot":
        os.makedirs(a.out, exist_ok=True)
        name = a.shot or (a.args[0] if a.args else "screen")
        adb("shell", "screencap", "-p", "/sdcard/drive.png")
        adb("pull", "/sdcard/drive.png", os.path.join(a.out, name + ".png"))
        print("saved", os.path.join(a.out, name + ".png"))


if __name__ == "__main__":
    main()
