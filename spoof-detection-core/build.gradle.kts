import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.publish)
    signing
}


version = "1.0.1"

android {
    namespace = "com.appliedrec.verid3.spoofdevicedetection.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}

dependencies {
    api(libs.verid.common)
    implementation(libs.verid.serialization)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.serialization)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.okhttp.bom))
    androidTestImplementation(libs.okhttp.mockwebserver)
}

mavenPublishing {
    coordinates("com.appliedrec", "spoof-device-detection-core")
    pom {
        name.set("Spoof device detection core")
        description.set("Core module for detecting spoof devices in images")
        url.set("https://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android")
        developers {
            developer {
                id.set("appliedrec")
                name.set("Applied Recognition")
                email.set("support@appliedrecognition.com")
            }
        }
        licenses {
            license {
                name.set("Commercial")
                url.set("https://raw.githubusercontent.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android/refs/heads/main/LICENCE.txt")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android.git")
            developerConnection.set("scm:git:ssh://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android.git")
            url.set("https://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android")
        }
    }
    publishToMavenCentral(automaticRelease = true)
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("Spoof device detection core")
    moduleVersion.set(project.version.toString())
}