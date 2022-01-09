/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import kotlinx.serialization.Serializable
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.core.token.model.MetaTokenTypesSerializer

internal typealias MetaToken = String

internal interface MetaTokenServiceContract {

    @Serializable(with = MetaTokenTypesSerializer::class)
    enum class TokenTypes(val value: String) {
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
        suspend fun fetchToken(type: TokenTypes): MetaTokenResponse
    }

    interface Repository {
        suspend fun fetchToken(type: TokenTypes): MetaToken
    }
}
