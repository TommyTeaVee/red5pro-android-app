Red5 Pro Android Application
===

> This Android application project shows how to integrate the [Red5 Pro](http://red5pro.com) SDKs.

Quickstart
---

### [&gt; Using Android Studio](SETUP-ANDROID-STUDIO.md)

### [&gt; Using Eclipse](SETUP-ANDROID-EXLIPSE.md)

### [&gt; Using Command Line](#Building)

Requirements
---

* Android SDK

## Android SDK
[Android SDK homepage](http://developer.android.com/sdk/index.html)

From the [Android SDK downloads page](http://developer.android.com/sdk/installing/index.html) you can choose to  install the SDK bundle for:

* [Standalone](http://developer.android.com/sdk/installing/index.html?pkg=tools) 
* [Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

Follow the instructions found on those pages for either choice.

**If you choose to install with [Android Studio](http://developer.android.com/sdk/index.html), follow along with the [SETUP-ANDROID-STUDIO](SETUP-ANDROID-STUDIO.md) instructions for importing and building the project.**

Building
---

```
$ ./gradlew clean build
```

APK output to `/app/build/apk`.

![Import Gradle Project](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-1.png)
