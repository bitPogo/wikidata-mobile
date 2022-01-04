/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.wikidata.dependency

object Dependency {
    val gradle = GradlePlugin
    val antibytes = AntiBytes

    object AntiBytes {
        val test = Test

        object Test {
            val core = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
            val coroutine = "tech.antibytes.test-utils-kmp:test-utils-coroutine:${Version.antibytes.test}"
            val ktor = "tech.antibytes.test-utils-kmp:test-utils-ktor:${Version.antibytes.test}"
        }
    }
}
