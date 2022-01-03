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
        val test = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
    }
}
