/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import kotlinx.coroutines.Job
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.MockContract

internal class ServiceResponseWrapperStub: MwClientContract.ServiceResponseWrapper, MockContract.Mock {
    var lastFunction: (suspend () -> Any?)? = null

    override fun <T> warp(
        function: suspend () -> T
    ): PublicApi.SuspendingFunctionWrapper<T> = SuspendingFunctionWrapperStub(function).also { lastFunction = function }

    override fun clear() {
        lastFunction = null
    }
}

internal class SuspendingFunctionWrapperStub<T>(
    override val wrappedFunction: suspend () -> T
) : PublicApi.SuspendingFunctionWrapper<T> {
    override fun subscribe(onSuccess: (item: T) -> Unit, onError: (error: Throwable) -> Unit): Job {
        TODO("Not yet implemented")
    }
}
