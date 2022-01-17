/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.wrapper

import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SuspendingFunctionWrapper<T> private constructor(
    override val wrappedFunction: suspend () -> T,
    private val scope: CoroutineScope,
) : CoroutineWrapperContract.SuspendingFunctionWrapper<T> {

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

    companion object Factory : CoroutineWrapperContract.SuspendingFunctionWrapperFactory {
        override fun <T> getInstance(
            function: suspend () -> T,
            dispatcher: CoroutineWrapperContract.ScopeDispatcher,
        ): SuspendingFunctionWrapper<T> {
            return SuspendingFunctionWrapper(
                function,
                dispatcher.dispatch(),
            )
        }
    }
}
