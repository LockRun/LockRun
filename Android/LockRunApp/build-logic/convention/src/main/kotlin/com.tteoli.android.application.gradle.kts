import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

extensions.getByType<BaseAppModuleExtension>().apply {
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        // applicationId는 각 app 모듈에서만 지정 (여기 X)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        getByName("release") {
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
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions { jvmTarget = "11" }
}
