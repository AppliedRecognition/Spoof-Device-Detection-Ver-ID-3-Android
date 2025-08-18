# Spoof Device Detection

Android library that detects devices like screens that may be used to spoof face detection.

The library implements the [`SpoofDetection`](https://github.com/AppliedRecognition/Ver-ID-Common-Types-Android/blob/main/lib/src/main/java/com/appliedrec/verid3/common/SpoofDetection.kt) interface from [VerIDCommonTypes](https://github.com/AppliedRecognition/Ver-ID-Common-Types-Android/tree/main), making it easy to plug in to the Ver-ID face capture SDK.

## Installation

Add the spoof detection library in your module's **build.gradle.kts** file:

```kotlin
dependencies {
    implementation(platform("com.appliedrec:verid-bom:2025-08-00"))
    implementation("com.appliedrec:spoof-device-detection-cloud")
    // For converting Bitmaps to Ver-ID images:
    implementation("com.appliedrec:verid-serialization")
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

## Offline version

The library makes calls to an internet endpoint. If you require offline use please [contact Applied Recognition](mailto:info@appliedrecognition.com).