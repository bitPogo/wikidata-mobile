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
}

kotlin {
    android()

    jvm()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.common)
                implementation(Dependency.multiplatform.koin.core) {
                    exclude(
                        "org.jetbrains.kotlin",
                        "kotlin-stdlib-jdk8"
                    )
                }

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.stately.freeze)

                implementation(LocalDependency.sqldelight.coroutines)

                implementation(project(":mw-client"))
                implementation(project(":utils-coroutine"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)

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
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest)

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
                dependsOn(commonTest)

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
        packageName = "tech.antibytes.wikibase.store.database.page"
        sourceFolders = listOf("database")
        verifyMigrations = true
    }
}
