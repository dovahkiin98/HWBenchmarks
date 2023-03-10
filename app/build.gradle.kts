@file:SuppressWarnings("INCUBATING")

import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android {
    namespace = "net.inferno.hwbenchmarks"

    signingConfigs {
        maybeCreate("testKey").apply {
            keyAlias = "key0"
            keyPassword = "123456"
            storeFile = file("../testkey.jks")
            storePassword = "123456"
        }
    }

    buildToolsVersion = "34.0.0-rc01"
    compileSdkPreview = "UpsideDownCake"
//    compileSdk = 33

    defaultConfig {
        applicationId = "net.inferno.hwbenchmarks"

        minSdk = 24
        targetSdk = 33

        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        buildFeatures {
            compose = true
            buildConfig = true
        }
    }

    flavorDimensions += arrayOf("dev")

    productFlavors {
        maybeCreate("dev").apply {
            dimension = "dev"

            minSdk = 31

            versionNameSuffix =
                "-dev-${DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now())}"

            buildConfigField("boolean", "DEV", "true")
            resourceConfigurations += arrayOf("en", "xxhdpi")
        }

        maybeCreate("deploy").apply {
            dimension = "dev"

            minSdk = 24

            buildConfigField("boolean", "DEV", "false")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        maybeCreate("preRelease").apply {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = true

            versionNameSuffix =
                "-${DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now())}"

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs["testKey"]
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs["testKey"]
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + arrayOf(
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check",
            "-Xopt-in=kotlin.RequiresOptIn",
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    //region Kotlin
    implementation(libs.kotlin.std)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    //endregion

    //region AndroidX
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.datastore.preferences)
    //endregion

    //region Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    //endregion

    //region Google
    implementation(libs.google.material)
    //endregion

    //region Network
    implementation(libs.jsoup)
    //endregion

    //region Compose
    implementation(libs.bundles.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.compose.material3)
    //endregion

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    debugImplementation(libs.androidx.compose.ui.tooling)
}