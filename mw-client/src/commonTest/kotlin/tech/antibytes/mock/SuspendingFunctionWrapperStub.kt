/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import kotlinx.coroutines.Job
import tech.antibytes.mediawiki.PublicApi

internal class SuspendingFunctionWrapperStub<T>(
    override val wrappedFunction: suspend () -> T
) : PublicApi.SuspendingFunctionWrapper<T> {
    override fun subscribe(onSuccess: (item: T) -> Unit, onError: (error: Throwable) -> Unit): Job {
        TODO("Not yet implemented")
    }
}
