/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

interface PublicApi {
    fun interface Connectivity {
        fun hasConnection(): Boolean
    }

    interface Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(message: String)
    }
}
