/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.wikidata.dependency.Dependency as LocalDependency
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")
    id("tech.antibytes.gradle.coverage")

    // SqlDelight
    id("com.squareup.sqldelight")

    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    android()

    jvm()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.serialization.InternalSerializationApi")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.common)
                implementation(Dependency.multiplatform.koin.core)

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.serialization.common)
                implementation(Dependency.multiplatform.serialization.json)
                implementation(Dependency.multiplatform.dateTime)

                implementation(Dependency.multiplatform.stately.freeze)
                implementation(Dependency.multiplatform.stately.concurrency)

                implementation(LocalDependency.sqldelight.coroutines)

                implementation(project(":mw-client"))
                implementation(project(":utils-coroutine"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)

                implementation(LocalDependency.antibytes.test.annotations)
                implementation(LocalDependency.antibytes.test.core)
                implementation(LocalDependency.antibytes.test.fixture)
                implementation(LocalDependency.antibytes.test.coroutine)
            }
        }

        val androidMain by getting {
            dependencies {
               implementation(Dependency.multiplatform.kotlin.android)
                implementation(Dependency.multiplatform.coroutines.android)
                implementation(LocalDependency.sqldelight.android)
            }
        }
        val androidAndroidTestRelease by getting
        val androidTestFixtures by getting
        val androidTestFixturesDebug by getting
        val androidTestFixturesRelease by getting
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependsOn(androidTestFixtures)
            dependsOn(androidTestFixturesDebug)
            dependsOn(androidTestFixturesRelease)
            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
                implementation(Dependency.android.test.ktx)
                implementation(Dependency.android.test.robolectric)
                implementation(Dependency.android.test.junit)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.jdk8)
                implementation(Dependency.multiplatform.coroutines.common)
                implementation(LocalDependency.sqldelight.jvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }
    }
}

android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}

sqldelight {
    database("WikibaseDataBase") {
        packageName = "tech.antibytes.wikibase.store.database.entity"
        sourceFolders = listOf("database")
        verifyMigrations = true
    }
}
