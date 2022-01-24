/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.wrapper

import co.touchlab.stately.freeze
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.antibytes.util.coroutine.result.ResultContract

class SharedFlowWrapper<Succ, Err : Throwable> private constructor(
    private val flow: SharedFlow<ResultContract<Succ, Err>>,
    private val scope: CoroutineScope
) : CoroutineWrapperContract.SharedFlowWrapper<Succ, Err> {

    override val wrappedFlow: SharedFlow<ResultContract<Succ, Err>>
        get() = flow.freeze()

    init {
        this.freeze()
    }

    override val replayCache: List<ResultContract<Succ, Err>>
        get() = wrappedFlow.replayCache.freeze()

    override fun subscribe(
        onEach: (item: ResultContract<Succ, Err>) -> Unit
    ): Job {
        return wrappedFlow
            .onEach { item -> onEach.invoke(item) }
            .launchIn(scope)
    }

    override fun subscribeWithSuspendingFunction(
        onEach: suspend (item: ResultContract<Succ, Err>) -> Unit
    ): Job {
        return wrappedFlow
            .onEach { item -> onEach.invoke(item) }
            .launchIn(scope)
    }

    companion object : CoroutineWrapperContract.SharedFlowWrapperFactory {
        override fun <Success, Error : Throwable> getInstance(
            flow: SharedFlow<ResultContract<Success, Error>>,
            dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): CoroutineWrapperContract.SharedFlowWrapper<Success, Error> {
            return SharedFlowWrapper(
                flow,
                dispatcher.dispatch()
            )
        }
    }
}
