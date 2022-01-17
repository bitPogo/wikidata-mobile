/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class SuspendingFunctionWrapperFactoryStub(
    var getInstance: ((CoroutineWrapperContract.CoroutineScopeDispatcher, suspend () -> Any?) -> CoroutineWrapperContract.SuspendingFunctionWrapper<Any?>)? = null
) : CoroutineWrapperContract.SuspendingFunctionWrapperFactory, MockContract.Mock {
    @Suppress("UNCHECKED_CAST")
    override fun <T> getInstance(
        function: suspend () -> T,
        dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher,
    ): CoroutineWrapperContract.SuspendingFunctionWrapper<T> {
        val instance = getInstance?.invoke(
            dispatcher,
            function
        ) ?: throw MockError.MissingStub("Missing Sideefffect getInstance")

        return instance as CoroutineWrapperContract.SuspendingFunctionWrapper<T>
    }

    override fun clear() {
        getInstance = null
    }
}
