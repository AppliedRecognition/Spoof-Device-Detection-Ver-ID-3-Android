pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/AppliedRecognition/Ver-ID-Releases-Android")
            credentials {
                username = settings.extra["gpr.user"] as String?
                password = settings.extra["gpr.token"] as String?
            }
        }
    }
}

rootProject.name = "Spoof device detection"
include(":spoof-device-detection")
include(":spoof-detection-core")
