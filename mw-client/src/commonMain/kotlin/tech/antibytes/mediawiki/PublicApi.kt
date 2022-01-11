/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

typealias EntityId = String
typealias LanguageTag = String

interface PublicApi {
    fun interface Connectivity {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.features.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }

    interface SuspendingFunctionWrapper<T> {
        val wrappedFunction: suspend () -> T

        fun subscribe(
            onSuccess: (item: T) -> Unit,
            onError: (error: Throwable) -> Unit,
        ) : Job
    }

    interface SuspendingFunctionWrapperFactory {
        fun <T> getInstance(
            scope: CoroutineScope,
            function: suspend () -> T
        ): SuspendingFunctionWrapper<T>
    }

    interface Client {

    }
}
