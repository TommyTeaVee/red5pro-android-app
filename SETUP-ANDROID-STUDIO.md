Android Studio Project Setup
===
> This Android application project show how to integrate [Red5 Pro](http://red5pro.com) SDKs to enable live streaming and second screen experiences on a mobile device.

Requirements
---

* [Red5 Pro Server](http://red5pro.com)
* [Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

The Red5 Pro SDKs have a minimum support for [Android 4.1 / API Level: 16](http://developer.android.com/about/versions/android-4.1.html).

### Red5 Pro Server
The __Red5 Pro__ is build on the Open Source [Red5 Server](https://github.com/Red5/red5-server) and allows to build scalable live streaming and second screen applications.

The example application provided in this project repository integrates the Red5 Pro Native SDKs  and libraries that enable live streaming and second screen experiences. You will need to setup a Red5 Pro server - either on your local machine or remotely - in order to stream video and communicate with a second screen host.

More information about Red5 Pro, its SDKs and setup can be found in the online [Red5 Pro Documentation](http://red5pro.com/docs/).

**To register an account and start using Red5 Pro in production, visit the [Red5 Pro Accounts](https://account.red5pro.com/register)!**

### Android Studio

#### [&gt; Android Studio Download](http://developer.android.com/sdk/installing/index.html?pkg=studio)

Follow the instructions on the download page to install the **Android Studio** IDE onto you machine.

_The current Android SDK(s) will most likely be downloaded along with the **Android Studio** installation._

## Project Setup

### Clone this repository

Change directory into desired location and issue the following:

```
$ git clone git@github.com:infrared5/red5pro-android-app.git
$ cd red5pro-android-app
```

Once you have cloned this repository onto your local machine and are in the repositories root directory, you can launch **Android Studio** and import the project.

### Import the Red5 Pro Project
The project included in this repository uses [Gradle](http://gradle.org) for its build process. Thankfully, **Android Studio** makes **Gradle** integration pretty seemless :)

Launch **Android Studio** and select `Import project`.

![Import Gradle Project](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-1.png)

In the file browser dialog shown in the **Android Studio** IDE, navigate to your local clone of this repoditory, select the root directory and click `OK`.

![Select Red5 Pro Project](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-2.png)

You may see an alert regarding unconfigured **Gradle** settings. This is fine.

We have taken care including the bare minimum to get up and running in **Android Studio** with **Gradle** integration. The **Android Studio** IDE will handle setting up the project with the proper **Gradle** dependencies that are defined in the build file and wrapper.

Click `OK` to continue.

![Select Red5 Pro Project](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-3.png)

Once the **Android Studio** IDE generates the project, it will present the workspace. _Additionally, if you visit the directory into which you cloned this repository you will see **.iml** files and a new **.gradle/** directory. This is part of the Android Studio workspace setup and expected._

From the **Android Studio** IDE, select to launch the application onto a connected device by clicking either the `green arrow` or `bug` icon to the right of the `Red5 Pro` Configuration in the top bar of the workspace:

![Select To Install App on Device](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-4.png)
![Select To Install App on Device](http://infrared5.github.io/red5pro-android-app/images/gradle-setup-5.png)


#### Note: Due to the Android Emulator not providing a proper Network Resource, the Red5 Pro application requires to be launched onto a physical device in order to test and use.

Once the application is launched on your chosen device, happy streaming and second screening!

