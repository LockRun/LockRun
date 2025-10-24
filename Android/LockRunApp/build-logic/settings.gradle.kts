// /build-logic/settings.gradle.kts

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "build-logic"
include(":convention")
