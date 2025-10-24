import com.android.build.gradle.BaseExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

extensions.getByType<BaseExtension>().apply {
    @Suppress("UnstableApiUsage")
    buildFeatures.compose = true
    // composeOptions { /* 필요시 설정 */ }
}
