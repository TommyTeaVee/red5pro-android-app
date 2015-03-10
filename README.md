RREADME.mded5 Pro Android Application
===

> This Android application project shows how to integrate the [Red5 Pro](http://red5pro.com) SDKs.

Quickstart
---

### [&gt; Using Android Studio](SETUP-ANDROID-STUDIO.md)

### [&gt; Using Eclipse](SETUP-ANDROID-EXLIPSE.md)

### [&gt; Using Command Line](#building)

Requirements
---

* Android SDK

### Android SDK
[Android SDK homepage](http://developer.android.com/sdk/index.html)

From the [Android SDK downloads page](http://developer.android.com/sdk/installing/index.html) you can choose to  install the SDK bundle for:

* [Standalone](http://developer.android.com/sdk/installing/index.html?pkg=tools) 
* [Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

Follow the instructions found on those pages for either choice.

**If you choose to install with [Android Studio](http://developer.android.com/sdk/index.html), follow along with the [SETUP-ANDROID-STUDIO](SETUP-ANDROID-STUDIO.md) instructions for importing and building the project.**

Building
---
After installing the [Standalone Android SDK](http://developer.android.com/sdk/installing/index.html?pkg=tools), you will need to modify the `sdk.dir` path from [local.properties.template](local.properties.template) and save the file as `local.properties` in the project root directory. This file is used by gradle to build the project.

```
$ echo "sdk.dir=/path/to/android/sdk/install" > local.properties
```

_replace `/path/to/android/sdk/install` with the path to where you installed the Android SDK._

Once the `local.properties` file is set, issue the following command to build the project:

### On OSX
```
$ ./gradlew clean build
```

### on Windows
TBD


The Red5Pro APK will be built and available in `Red5Pro/build/outputs/apk`.

