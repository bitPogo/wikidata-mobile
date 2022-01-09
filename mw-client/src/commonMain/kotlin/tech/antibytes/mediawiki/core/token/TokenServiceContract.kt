/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.core.token.model.TokenResponse

internal typealias Token = String

internal interface TokenServiceContract {
    enum class TokenTypes(val value: String) {
        CREATE_ACCOUNT("createaccount"),
        CSRF("csrf"),
        DELETE_GLOBAL_ACCOUNT("deleteglobalaccount"),
        LOGIN("login"),
        PATROL("patrol"),
        ROLLBACK("rollback"),
        SET_GLOBAL_ACCOUNT_STATUS("setglobalaccountstatus"),
        USER_RIGHTS("userrights"),
        WATCH("watch")
    }

    interface ApiService {
        fun fetchToken(type: TokenTypes): TokenResponse
    }

    interface Repository {
        fun fetchToken(type: TokenTypes): Token
    }
}
