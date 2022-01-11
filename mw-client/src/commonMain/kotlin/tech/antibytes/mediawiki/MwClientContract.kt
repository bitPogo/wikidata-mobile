/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

internal interface MwClientContract {
    interface ServiceResponseWrapper {
        fun <T> warp(function: suspend () -> T): PublicApi.SuspendingFunctionWrapper<T>
    }

    companion object {
        val ENDPOINT = listOf("w", "api.php")
    }
}
