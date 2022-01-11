/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import kotlinx.coroutines.CoroutineScope
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class SuspendingFunctionWrapperFactoryStub(
    var getInstance: ((CoroutineScope, suspend () -> Any?) -> PublicApi.SuspendingFunctionWrapper<Any?>)? = null
) : MwClientContract.SuspendingFunctionWrapperFactory, MockContract.Mock {
    @Suppress("UNCHECKED_CAST")
    override fun <T> getInstance(
        scope: CoroutineScope,
        function: suspend () -> T
    ): PublicApi.SuspendingFunctionWrapper<T> {
        val instance = getInstance?.invoke(
            scope,
            function
        ) ?: throw MockError.MissingStub("Missing Sideefffect getInstance")

        return instance as PublicApi.SuspendingFunctionWrapper<T>
    }

    override fun clear() {
        getInstance = null
    }
}
