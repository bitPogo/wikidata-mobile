/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.util.test.MockError

class SharedFlowWrapperStub<Succ, Err : Throwable> : SharedFlowWrapper<Succ, Err> {
    override val wrappedFlow: SharedFlow<ResultContract<Succ, Err>>
        get() = TODO("Not yet implemented")
    override val replayCache: List<ResultContract<Succ, Err>>
        get() = TODO("Not yet implemented")

    override fun subscribe(onEach: (item: ResultContract<Succ, Err>) -> Unit): Job {
        TODO("Not yet implemented")
    }
}

class SharedFlowWrapperFactoryStub<Succ, Err : Throwable>(
    var getInstance: ((SharedFlow<ResultContract<Succ, Err>>, CoroutineScopeDispatcher) -> SharedFlowWrapper<Succ, Err>)? = null
) : SharedFlowWrapperFactory {
    @Suppress("UNCHECKED_CAST")
    override fun <Success, Error : Throwable> getInstance(
        flow: SharedFlow<ResultContract<Success, Error>>,
        dispatcher: CoroutineScopeDispatcher
    ): SharedFlowWrapper<Success, Error> {
        return if (getInstance == null) {
            throw MockError.MissingStub("Missing Sideeffect getInstance")
        } else {
            getInstance!!.invoke(
                flow as SharedFlow<ResultContract<Succ, Err>>,
                dispatcher
            ) as SharedFlowWrapper<Success, Error>
        }
    }
}
