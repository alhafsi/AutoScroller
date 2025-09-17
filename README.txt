Auto Scroller - Android project (Kotlin)
---------------------------------------
Files are located in app/ folder. To build APK online:
1) Upload the project folder to an online IDE like Gitpod (https://gitpod.io/) or APKOnline (https://www.apkonline.net/online-android-ide).
2) Run Gradle build (e.g. ./gradlew assembleDebug) or use the IDE's build feature.
3) Install the generated APK on your Android device.

Important:
- After installing, enable Accessibility Service for this app:
  Settings -> Accessibility -> Auto Scroller -> turn on.
- This app uses AccessibilityService to perform gestures on other apps.
- Test carefully and avoid misuse. Some Google Play policies may restrict apps using accessibility for non-accessibility purposes.
