import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

version = "1.0.0"

android {
    namespace = "com.appliedrec.verid3.spoofdevicedetection.core"
    compileSdk = 35

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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(libs.verid3.common)
    api(libs.verid3.serialization)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.serialization)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.okhttp.mockwebserver)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.appliedrec"
            artifactId = "spoof-device-detection-core"
            afterEvaluate {
                from(components["release"])
            }

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
                scm {
                    connection.set("scm:git:git://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android.git")
                    developerConnection.set("scm:git:ssh://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android.git")
                    url.set("https://github.com/AppliedRecognition/Spoof-Device-Detection-Ver-ID-3-Android")
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/AppliedRecognition/Ver-ID-Releases-Android")
            credentials {
                username = project.findProperty("gpr.user") as String?
                password = project.findProperty("gpr.token") as String?
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["release"])
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set("Spoof device detection core")
    moduleVersion.set(project.version.toString())
//    outputDirectory.set(file("../docs"))
}