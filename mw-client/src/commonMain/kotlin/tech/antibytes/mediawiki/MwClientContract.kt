/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.CoroutineScope

internal interface MwClientContract {
    interface SuspendingFunctionWrapperFactory {
        fun <T> getInstance(
            scope: CoroutineScope,
            function: suspend () -> T
        ): PublicApi.SuspendingFunctionWrapper<T>
    }

    interface ServiceResponseWrapper {
        fun <T> warp(function: suspend () -> T): PublicApi.SuspendingFunctionWrapper<T>
    }

    companion object {
        val ENDPOINT = listOf("w", "api.php")
    }
}
