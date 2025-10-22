plugins {
    id("com.tteoli.android.library")
    id("com.tteoli.android.compose")
}

android {
    namespace = "com.tteoli.lockrunapp.libraries.storage"

}

dependencies {

    implementation(project(":libraries:storage-contract"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}