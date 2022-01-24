/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Version
import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.wikidata.dependency.Dependency as LocalDependency

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-parcelize")

    id("tech.antibytes.gradle.configuration")
    id("tech.antibytes.gradle.coverage")

    // SqlDelight
    id("com.squareup.sqldelight")

    // Hilt
    // kotlin("kapt")
    // id("dagger.hilt.android.plugin")

}

android {
    defaultConfig {
        applicationId = "tech.antibytes.wikidata.app"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
            matchingFallbacks.add("release")
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Version.android.compose.compiler
    }

    packagingOptions {
        resources {
            // excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

/*
kapt {
    correctErrorTypes = true
}*/

dependencies {
    implementation(Dependency.android.ktx.core)
    implementation(Dependency.android.ktx.lifecycle)
    implementation(Dependency.android.ktx.viewmodel)
    implementation(Dependency.android.ktx.viewmodelSaver)
    implementation(Dependency.android.ktx.workmanager)

    implementation(Dependency.android.appCompact.core)
    implementation(Dependency.android.material)
    implementation(Dependency.android.constraintLayout)

    implementation(Dependency.android.compose.ui)
    implementation(Dependency.android.compose.material)
    implementation(Dependency.android.compose.materialIcons)
    implementation(Dependency.android.compose.materialIconsExtended)
    implementation(Dependency.android.compose.activity)
    implementation(Dependency.android.compose.viewmodel)
    implementation(Dependency.android.compose.foundation)
    implementation(Dependency.android.compose.constrainLayout)

    // implementation(LocalDependency.hilt.core)
    // kapt(LocalDependency.hilt.compiler)

    // Debug
    debugImplementation(Dependency.android.compose.uiTooling)

    // Test
    testImplementation(Dependency.android.test.junit)
    testImplementation(Dependency.android.test.junit4)
    testImplementation(Dependency.android.test.composeJunit4)
    testImplementation(Dependency.android.test.robolectric)

    // testImplementation(LocalDependency.hilt.test)
    // kaptTest(LocalDependency.hilt.compiler)

    // InstrumentedTest
    androidTestImplementation(Dependency.android.test.junit)
    androidTestImplementation(Dependency.android.test.junit4)
    androidTestImplementation(Dependency.android.test.espressoCore)
    androidTestImplementation(Dependency.android.test.uiAutomator)

    // androidTestImplementation(LocalDependency.hilt.test)
    // kaptAndroidTest(LocalDependency.hilt.compiler)
}
