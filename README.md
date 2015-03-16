Red5 Pro Android Application
===

> This Android application project shows how to integrate the [Red5 Pro](http://red5pro.com) SDKs to enable live streaming and second screen experiences on a mobile device.

Interested in the iOS version? [Find it here](http://infrared5.github.io/red5pro-ios-app/)

Quickstart
---

### [&gt; Using Android Studio](SETUP-ANDROID-STUDIO.md)

### [&gt; Using Command Line](#building-on-the-command-line)

```
$ git clone https://github.com/infrared5/red5pro-android-app.git
$ cd red5pro-android-app
$ echo "sdk.dir=/path/to/android/sdk/install" > local.properties
$ ./gradlew clean build
```

[more information](#building-on-the-command-line)

Requirements
---

* [Red5 Pro Server](http://red5pro.com)
* [Android SDK](#android-sdk)

The Red5 Pro SDKs have a minimum support for [Android 4.1 / API Level: 16](http://developer.android.com/about/versions/android-4.1.html).

### Red5 Pro Server
The __Red5 Pro Server__ is build on the Open Source [Red5 Server](https://github.com/Red5/red5-server) and allows to build scalable live streaming and second screen applications.

The example application provided in this project repository integrates the Red5 Pro Native SDKs  and libraries that enable live streaming and second screen experiences. You will need to setup a Red5 Pro server - either on your local machine or remotely - in order to stream video and communicate with a second screen host.

More information about Red5 Pro, its SDKs and setup can be found in the online [Red5 Pro Documentation](http://red5pro.com/docs/).

**To register an account and start using Red5 Pro in production, visit the [Red5 Pro Accounts](https://account.red5pro.com/register)!**

### Android SDK

#### [&gt; Android SDK homepage](http://developer.android.com/sdk/index.html)

From the [Android SDK downloads page](http://developer.android.com/sdk/installing/index.html) you can choose to  install the SDK bundle for:

* [Standalone](http://developer.android.com/sdk/installing/index.html?pkg=tools)
* [Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

Follow the instructions found on those pages for either choice.

**If you choose to install with [Android Studio](http://developer.android.com/sdk/index.html), follow along with the [Android Studio Project Setup](SETUP-ANDROID-STUDIO.md) instructions for importing and building the project.**

Building on the Command Line
---

### Clone this repository

Change directory into desired location and issue the following:

```
$ git clone git@github.com:infrared5/red5pro-android-app.git
$ cd red5pro-android-app
```

Once you have cloned this repository onto your local machine and are in the repositories root directory, you can build the project.

### Gradle Build

The [Gradle Wrapper](https://gradle.org/docs/current/userguide/gradle_wrapper.html) is used to build the **Red5 Pro** APK.

This project includes the *Gradle* libraries required to build the application, so there are no additional dependencies to be installed in order to continue.

After installing the [Standalone Android SDK](http://developer.android.com/sdk/installing/index.html?pkg=tools), you will need to modify the `sdk.dir` path from [local.properties.template](local.properties.template) and save the file as `local.properties` in the project root directory. This file is used by *Gradle* to build the project.

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

License
---
Copyright 2015 Infrared5 Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

