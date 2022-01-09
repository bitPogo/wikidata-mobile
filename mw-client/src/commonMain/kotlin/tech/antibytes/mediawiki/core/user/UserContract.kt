/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.user

import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.core.user.model.LoginResponse
import tech.antibytes.mediawiki.error.MwClientError

internal interface UserContract {
    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun login(
            username: String,
            password: String,
            token: MetaToken
        ): LoginResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun login(
            username: String,
            password: String,
            token: MetaToken
        ): Boolean
    }

    interface Controller {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun login(
            username: String,
            password: String
        ): Boolean
    }
}
