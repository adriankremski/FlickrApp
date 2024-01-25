plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.github.snuffix.flickrapp.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.hilt.android)
    implementation(libs.retrofit)

    kapt(libs.androidx.room.compiler)
    kapt(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}