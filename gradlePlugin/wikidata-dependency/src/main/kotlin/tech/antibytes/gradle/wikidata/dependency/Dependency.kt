/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.wikidata.dependency

object Dependency {
    val gradle = GradlePlugin
    val antibytes = AntiBytes

    val sqldelight = SqlDelight

    val hilt = Hilt

    object SqlDelight {
        const val android = "com.squareup.sqldelight:android-driver:${Version.sqldelight}"
        const val jvm = "com.squareup.sqldelight:sqlite-driver:${Version.sqldelight}"
        const val js = "com.squareup.sqldelight:sqljs-driver:${Version.sqldelight}"
        const val native = "com.squareup.sqldelight:native-driver:${Version.sqldelight}"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:${Version.sqldelight}"
    }

    object AntiBytes {
        val test = Test

        object Test {
            const val annotations = "tech.antibytes.test-utils-kmp:test-utils-annotations:${Version.antibytes.test}"
            const val core = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
            const val fixture = "tech.antibytes.kfixture:core:${Version.antibytes.kfixture}"
            const val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine:${Version.antibytes.test}"
            const val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor:${Version.antibytes.test}"
        }
    }

    object Hilt {
        const val core = "com.google.dagger:hilt-android:${Version.hilt}"
        const val compiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
        const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.hiltCompose}"
        const val test = "com.google.dagger:hilt-android-testing:${Version.hilt}"
    }

    const val zing = "com.google.zxing:core:${Version.zxing}"
}
