/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import kotlinx.coroutines.Job
import tech.antibytes.mediawiki.PublicApi

class SuspendingFunctionWrapperStub<T>(
    var function: suspend () -> T
) : PublicApi.SuspendingFunctionWrapper<T> {
    override val wrappedFunction: suspend () -> T
        get() = function

    override fun subscribe(onSuccess: (item: T) -> Unit, onError: (error: Throwable) -> Unit): Job {
        TODO("Not yet implemented")
    }
}
