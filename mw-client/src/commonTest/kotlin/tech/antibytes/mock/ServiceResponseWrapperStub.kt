/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockContract

internal class ServiceResponseWrapperStub : MwClientContract.ServiceResponseWrapper, MockContract.Mock {
    var lastFunction: (suspend () -> Any?)? = null

    override fun <T> warp(
        function: suspend () -> T
    ): CoroutineWrapperContract.SuspendingFunctionWrapper<T> {
        lastFunction = function
        return SuspendingFunctionWrapperStub(function)
    }

    override fun clear() {
        lastFunction = null
    }
}
