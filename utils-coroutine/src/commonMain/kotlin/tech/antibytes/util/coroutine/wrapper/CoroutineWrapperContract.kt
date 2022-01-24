/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.wrapper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import tech.antibytes.util.coroutine.result.ResultContract

interface CoroutineWrapperContract {
    fun interface CoroutineScopeDispatcher {
        fun dispatch(): CoroutineScope
    }

    interface SuspendingFunctionWrapper<T> {
        val wrappedFunction: suspend () -> T

        fun subscribe(
            onSuccess: (item: T) -> Unit,
            onError: (error: Throwable) -> Unit,
        ): Job
    }

    interface SuspendingFunctionWrapperFactory {
        fun <T> getInstance(
            function: suspend () -> T,
            dispatcher: CoroutineScopeDispatcher,
        ): SuspendingFunctionWrapper<T>
    }

    interface SharedFlowWrapper<Success, Error : Throwable> {
        val wrappedFlow: SharedFlow<ResultContract<Success, Error>>
        val replayCache: List<ResultContract<Success, Error>>

        fun subscribe(
            onEach: (item: ResultContract<Success, Error>) -> Unit,
        ): Job

        fun subscribeWithSuspendingFunction(
            onEach: suspend (item: ResultContract<Success, Error>) -> Unit,
        ): Job
    }

    interface SharedFlowWrapperFactory {
        fun <Success, Error : Throwable> getInstance(
            flow: SharedFlow<ResultContract<Success, Error>>,
            dispatcher: CoroutineScopeDispatcher
        ): SharedFlowWrapper<Success, Error>
    }
}
