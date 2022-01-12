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
                implementation(Dependency.multiplatform.koin.core) {
                    exclude(
                        "org.jetbrains.kotlin",
                        "kotlin-stdlib-jdk8"
                    )
                }

                implementation(Dependency.multiplatform.coroutines.common)

                implementation(Dependency.multiplatform.serialization.common)
                implementation(Dependency.multiplatform.serialization.json)
                implementation(Dependency.multiplatform.dateTime)

                implementation(Dependency.multiplatform.stately.freeze)

                implementation(LocalDependency.sqldelight.coroutines)

                api(project(":mw-client"))
            }
        }
        val commonTest by getting {
            kotlin.srcDir("src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)

                implementation(LocalDependency.antibytes.test.core)
                implementation(LocalDependency.antibytes.test.fixture)
                implementation(LocalDependency.antibytes.test.coroutine)
                implementation(LocalDependency.antibytes.test.ktor)
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

val templatesPath = "${projectDir}/src/commonTest/resources/template"
val configPath = "${projectDir}/src-gen/commonTest/kotlin/tech/antibytes/wikibase/store/entity/test/config"

val provideTestConfig: Task by tasks.creating {
    doFirst {
        val templates = File(templatesPath)
        val configs = File(configPath)

        val config = File(templates, "TestConfig.tmpl")
            .readText()
            .replace("PROJECT_DIR", projectDir.toPath().toAbsolutePath().toString())

        if (!configs.exists()) {
            if(!configs.mkdir()) {
                System.err.println("The script not able to create the config directory")
            }
        }
        File(configPath, "TestConfig.kt").writeText(config)
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class.java) {
    if (this.name.contains("Test")) {
        this.dependsOn(provideTestConfig)
    }
}

sqldelight {
    database("WikibaseDataBase") {
        packageName = "tech.antibytes.wikibase.store.database.entity"
        sourceFolders = listOf("database")
        verifyMigrations = true
    }
}
