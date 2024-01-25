plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.kotlinx.coroutines.core)
}