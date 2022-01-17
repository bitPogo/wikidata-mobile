/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.result

sealed interface ResultContract<Success, Error : Throwable> {
    val value: Success?
    val error: Error?

    fun isSuccess(): Boolean
    fun isError(): Boolean

    @Throws(Throwable::class)
    fun unwrap(): Success
}
