/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SuspendingFunctionWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SuspendingFunctionWrapperFactory

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
    private val dispatcher: CoroutineScopeDispatcher,
    private val factory: SuspendingFunctionWrapperFactory
) : MwClientContract.ServiceResponseWrapper {
    override fun <T> warp(function: suspend () -> T): SuspendingFunctionWrapper<T> {
        val connectionAwareFunction = suspend { connectionAwareExecution(connectivityManager, function) }

        return factory.getInstance(connectionAwareFunction, dispatcher)
    }
}
