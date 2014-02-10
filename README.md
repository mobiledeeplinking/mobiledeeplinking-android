# MobileDeepLinking-Android [![Build Status](https://travis-ci.org/mobiledeeplinking/mobiledeeplinking-android.png?branch=master)](https://travis-ci.org/mobiledeeplinking/mobiledeeplinking-android)

This project is the Android component of the MobileDeepLinking specification, the industry standard for mobile application deeplinking. This spec and accompanying libraries simplify and reduce implementation of deep links as well as provide flexible and powerful features for routing to custom behavior.

## Features

Give a configuration file (`MobileDeepLinkingConfig.json`), this library provides the ability to define deeplink routes that map to custom logic and activity creation. This is accomplished through custom callback handlers and by creating Intent objects that are used to start new activities.

### Activity Routing

When a class is defined in the configuration file, the library will create an Intent that will be used to start the appropriate Activity. The route parameters defined in the configuration file (path and query parameters) will be set as extras on the intent. The `class` attribute in the json configuration must be a fully qualified class name with package info included.

### Handlers

There may be cases where you wish to do more than simply route to an Activity. This functionality is supported through the use of custom handlers. In your subclass of the `Application` class, you can implement the `org.mobiledeeplinking.android.Handler` interface in order to register custom logic to be executed when a route matches. This association takes place in the configuration json under the `handlers` attribute.

These handlers are provided with a HashMap<String, String> with all route parameters found in the deeplink. The handlers can modify the contents of this map, which will then be forwarded on to the next handler in the handlers array found in the json. Handlers can be reused across multiple routes and can be used in conjunction with view instantiation or entirely on their own.

## Compatibility

Supports Android 2.2 and above.

## Build

Development uses the Gradle build system introduced with Android Studio. To build for release:

1. `./gradlew clean assembleRelease`
2. The resulting library can be found in `MobileDeepLinking-Android-SDK/build/libs/MobileDeepLinking-Android-SDK.aar`.
3. If you wish to build the library in a jar, execute `./gradlew clean packageReleaseJar`
4. The resulting jar can be found in `MobileDeepLinking-Android-SDK/build/bundles/release/classes.jar`. Rename this file to `MobileDeepLinking-Android-SDK.jar`.

## Installation

### Required

Create a `MobileDeepLinkingConfig.json` file in your project with desired routes and options. See demo app and spec for the schema and examples.

Drop the MobileDeepLinking-Android-SDK `.aar` or `.jar` file into your `libs/` folder.

Insert the following into your `AndroidManifest.xml`:

    <activity
        android:name="org.mobiledeeplinking.android.MobileDeepLinking"
        android:theme="@android:style/Theme.NoDisplay"
        android:noHistory="true">
        <intent-filter>
            <data android:scheme="mdldemo"/>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
        </intent-filter>
    </activity>

### Optional

Register Custom Handlers (if desired) in a subclass of your app's `Application` class:

    public class MDLApplication extends Application
    {
        @Override
        public void onCreate()
        {
            super.onCreate();
            MobileDeepLinking.registerHandler("testHandler", new org.mobiledeeplinking.android.Handler()
            {
                @Override
                public Map<String, String> execute(Map<String, String> routeParameters)
                {
                    // inside handler
                    return routeParameters;
                }
            });
        }
    }
    
## Demo App

Part of the `MobileDeepLinking-Android.iml` project consists of a demo app, `MobileDeepLinking-Android-Demo`. This is a demo application that provides an example implementation of the `MobileDeepLinkingConfig.json` file, along with several custom handlers that demonstrate how to route to client-specified functionality. It also provides example routing to several different activities.

## License

Copyright 2014 MobileDeepLinking.org and other contributors
http://mobiledeeplinking.org

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
