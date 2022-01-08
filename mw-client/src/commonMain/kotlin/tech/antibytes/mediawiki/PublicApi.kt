/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import io.ktor.client.features.logging.Logger

interface PublicApi {
    fun interface Connectivity {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.features.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }
}
