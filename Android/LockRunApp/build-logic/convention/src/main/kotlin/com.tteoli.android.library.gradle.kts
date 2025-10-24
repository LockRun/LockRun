import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

extensions.getByType<LibraryExtension>().apply {
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xjsr305=strict"
    }
}
