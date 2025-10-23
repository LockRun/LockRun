plugins {
    id("com.tteoli.android.library")
    id("com.tteoli.android.compose")
}

android {
    namespace = "com.tteoli.lockrunapp.features.home"

}

dependencies {

    implementation(project(":core"))

    implementation(platform(libs.androidx.compose.bom)) // BOM 추가
    implementation(libs.androidx.compose.runtime) // 런타임 (오류 직접 해결)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}