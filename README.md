# Spoof Device Detection

Android library that detects devices like screens that may be used to spoof face detection.

The library implements the [`SpoofDetection`]() interface from [VerIDCommonTypes](), making it easy to plug in to the Ver-ID face capture SDK.

## Installation

1. Add your GitHub credentials in your **gradle.properties** file:

    ```
    gpr.user=<user name>
    gpr.token=<personal access token>
    ```
2. Add the following repository in your **settings.gradle.kts** file's `dependencyManagement/repositories` block:

    ```kotlin
    maven {
        url = uri("https://maven.pkg.github.com/AppliedRecognition/Ver-ID-Releases-Android")
        credentials {
            username = settings.extra["gpr.user"] as String?
            password = settings.extra["gpr.token"] as String?
        }
    }
    ```
3. Add the spoof detection library in your module's **build.gradle.kts** file:

    ```kotlin
    dependencies {
        implementation(platform("com.appliedrec:ver-id-bom:2025-05-04"))
        implementation("com.appliedrec:verid3-serialization")
        implementation("com.appliedrec:spoof-device-detection")
    }
    ```

## Usage

Detecting spoof devices in image

```kotlin
val bitmap: Bitmap // The image in which to detect spoof devices
// Convert bitmap to Ver-ID image
val image = Image.fromBitmap(bitmap)
// Detect spoof devices
val spoofDevices = runBlocking { detectSpoofDevicesInImage(image) }
```