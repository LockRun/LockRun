plugins {
    id("com.tteoli.android.library")
//    id("com.tteoli.android.compose")

    // hilt
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tteoli.core"

}

dependencies {
    api(project(":ui-components"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}