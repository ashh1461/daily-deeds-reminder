@echo off
set "JAVA_HOME=e:\Antigravity\tools\jdk-17"
set "ANDROID_HOME=e:\Antigravity\tools\android-sdk"
set "ANDROID_SDK_ROOT=e:\Antigravity\tools\android-sdk"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "GRADLE_USER_HOME=e:\Antigravity\.gradle"
"%~dp0gradlew.bat" --no-daemon %*
