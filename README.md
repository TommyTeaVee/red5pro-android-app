Red5 Pro Android Application
===

> This Android application project shows how to integrate the [Red5 Pro](http://red5pro.com) SDKs.

Quickstart
---

### [&gt; Using Android Studio](SETUP-ANDROID-STUDIO.md)

### [&gt; Using Eclipse](SETUP-ANDROID-ECLIPSE.md)

### [&gt; Using Command Line](#building)

Requirements
---

* [Red5 Pro Server](http://red5pro.com)
* [Android SDK](#android-sdk)

### Red5 Pro Server
The __Red5 Pro__ is build on the Open Source [Red5 Server] and allows to build scalable live streaming and second screen applications.

The example application provided in this project repository integrates the Red5 Pro Native SDKs  and libraries that enable live streaming and second screen experiences. You will need to setup a Red5 Pro server - either on your local machine or remotely - in order to stream video and communicate with a second screen host.

More information about Red5 Pro, its SDKs and setup can be found in the online [Red5 Pro Documentation](http://red5pro.com/docs/).

**To register an account and start using Red5 Pro in production, visit the [Red5 Pro Accounts](https://account.red5pro.com/register)!**

### Android SDK

#### [&gt; Android SDK homepage](http://developer.android.com/sdk/index.html)

From the [Android SDK downloads page](http://developer.android.com/sdk/installing/index.html) you can choose to  install the SDK bundle for:

* [Standalone](http://developer.android.com/sdk/installing/index.html?pkg=tools)
* [Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

Follow the instructions found on those pages for either choice.

**If you choose to install with [Android Studio](http://developer.android.com/sdk/index.html), follow along with the [Setup Android Studio Project](SETUP-ANDROID-STUDIO.md) instructions for importing and building the project.**

Building
---

The [Gradle Wrapper](https://gradle.org/docs/current/userguide/gradle_wrapper.html) is used to build the **Red5 Pro** APK.

After installing the [Standalone Android SDK](http://developer.android.com/sdk/installing/index.html?pkg=tools), you will need to modify the `sdk.dir` path from [local.properties.template](local.properties.template) and save the file as `local.properties` in the project root directory. This file is used by gradle to build the project.

### On &#42;nix

#### change local.properties On &#42;nix
```
$ echo "sdk.dir=/path/to/android/sdk/install" > local.properties
```

_replace `/path/to/android/sdk/install` with the path to where you installed the Android SDK._

Once the `local.properties` file is set, issue the following command to build the project:

#### build
```
$ ./gradlew clean build
```

### On Windows

#### change local.properties on Windows
Open the [local.properties.template](local.properties.template) file, modify the `sdk.dir` value to the path where you installed the Android SDK and save the file as __local.properties__.

Once the `local.properties` file is set, issue the following command to build the project:

#### build
**TBD** (Double click on `gradle.bat`?)

Output
---
The Red5Pro APK will be built and available in `Red5Pro/build/outputs/apk`.

