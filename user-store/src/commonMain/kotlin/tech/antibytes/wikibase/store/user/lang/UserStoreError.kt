/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.lang

sealed class UserStoreError(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    class InitialState : UserStoreError()

    class LoginFailure(val attempts: Int) : UserStoreError()
}
