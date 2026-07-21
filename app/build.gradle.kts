import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = 36

    buildFeatures {
        dataBinding = false
        viewBinding = true
        buildConfig = true

    }

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "API_ACCESS_TOKEN", value = "\"${developProperties.apiAccessToken}\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    configurations.all {
        resolutionStrategy {
            force("com.squareup:javapoet:1.13.0")
        }
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.core)
    testImplementation(libs.junit)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.okhttp.logging)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.koin.android)
    implementation (libs.koin.androidx.workmanager)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Glide annotation processor
    kapt(libs.compiler)


    // Явно добавляем JavaPoet (на всякий случай)
    implementation(libs.javapoet)

    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val room_version = "2.8.4"

    implementation("androidx.room:room-runtime:${room_version}")

// Kotlin Symbol Processing (KSP)

    ksp("androidx.room:room-compiler:${room_version}")
}
