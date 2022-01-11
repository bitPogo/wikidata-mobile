/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.coroutine

import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi

internal class SuspendingFunctionWrapper<T> private constructor(
    private val scope: CoroutineScope,
    override val wrappedFunction: suspend () -> T
): PublicApi.SuspendingFunctionWrapper<T> {

    init {
        freeze()
    }

    override fun subscribe(
        onSuccess: (item: T) -> Unit,
        onError: (error: Throwable) -> Unit
    ): Job {
        return scope.launch {
            try {
                val result = wrappedFunction.invoke()
                onSuccess(result.freeze())
            } catch (error: Throwable) {
                onError(error.freeze())
            }
        }
    }

    companion object Factory : MwClientContract.SuspendingFunctionWrapperFactory {
        override fun <T> getInstance(
            scope: CoroutineScope,
            function: suspend () -> T
        ): SuspendingFunctionWrapper<T> {
            return SuspendingFunctionWrapper(scope, function)
        }
    }
}
