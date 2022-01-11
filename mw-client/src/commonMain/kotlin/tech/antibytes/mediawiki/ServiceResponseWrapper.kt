/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import tech.antibytes.mediawiki.error.MwClientError

private suspend fun <T> connectionAwareExecution(
    connectivityManager: PublicApi.ConnectivityManager,
    function: suspend () -> T
): T {
    return if (connectivityManager.hasConnection()) {
        function.invoke()
    } else {
        throw MwClientError.NoConnection()
    }
}

internal class ServiceResponseWrapper(
    private val connectivityManager: PublicApi.ConnectivityManager,
    private val dispatcher: CoroutineDispatcher,
    private val factory: MwClientContract.SuspendingFunctionWrapperFactory
) : MwClientContract.ServiceResponseWrapper {
    override fun <T> warp(function: suspend () -> T): PublicApi.SuspendingFunctionWrapper<T> {
        return factory.getInstance(CoroutineScope(dispatcher)) {
            connectionAwareExecution(connectivityManager, function)
        }
    }
}
