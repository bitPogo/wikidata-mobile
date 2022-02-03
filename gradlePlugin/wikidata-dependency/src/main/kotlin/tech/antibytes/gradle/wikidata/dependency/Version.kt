/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.wikidata.dependency

object Version {

    const val kotlin = "1.6.10"

    val gradle = Gradle
    val antibytes = Antibytes

    object Gradle {
        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "c81afcd"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.2.0"

        /**
         * [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
         */
        const val hilt = Version.hilt
    }

    object Antibytes {
        val test = "c9c2a0c"
    }

    /**
     * [SQLDelight](https://github.com/cashapp/sqldelight/)
     */
    const val sqldelight = "1.5.3"

    /**
     * [Google Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
     */
    const val hilt = "2.38.1"

    /**
     * [Google Hilt Compose](https://developer.android.com/jetpack/androidx/releases/hilt)
     */
    const val hiltCompose = "1.0.0"

    /**
     * [ZXing](https://github.com/zxing/zxing)
     */
    const val zxing = "3.4.1"
}
