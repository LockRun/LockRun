plugins {
    `kotlin-dsl`  // 컨벤션 플러그인을 Kotlin DSL로 작성
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.13.0")
    implementation(kotlin("gradle-plugin", "2.0.20"))
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.0.20") // 선택


}
