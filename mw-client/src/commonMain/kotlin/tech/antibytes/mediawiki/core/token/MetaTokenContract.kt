/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.core.token.model.MetaTokenTypeSerializer
import tech.antibytes.mediawiki.error.MwClientError

internal typealias MetaToken = String

internal interface MetaTokenContract {

    @Serializable(with = MetaTokenTypeSerializer::class)
    enum class MetaTokenType(val value: String) {
        CREATE_ACCOUNT("createaccounttoken"),
        CSRF("csrftoken"),
        DELETE_GLOBAL_ACCOUNT("deleteglobalaccounttoken"),
        LOGIN("logintoken"),
        PATROL("patroltoken"),
        ROLLBACK("rollbacktoken"),
        SET_GLOBAL_ACCOUNT_STATUS("setglobalaccountstatustoken"),
        USER_RIGHTS("userrightstoken"),
        WATCH("watchtoken")
    }

    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchToken(type: MetaTokenType): MetaTokenResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchToken(type: MetaTokenType): MetaToken
    }
}
