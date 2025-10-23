plugins {
    id("com.tteoli.android.application")
    id("com.tteoli.android.compose")
    alias(libs.plugins.kotlin.android)
}


android {
    namespace = "com.tteoli.lockrunapp"
    defaultConfig {
        applicationId = "com.tteoli.lockrunapp" // 앱 고유값은 여기서만
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    // app 특화 설정만 적기 (signing, flavors 등)
}
dependencies {

    implementation(project(":features:home"))
    implementation(project(":libraries:storage-contract"))
    implementation(project(":libraries:storage"))
    implementation(project(":libraries:network-contract"))
    implementation(project(":libraries:network"))

    // 네비게이션
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}