/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse

internal typealias MetaToken = String

internal interface MetaTokenServiceContract {
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
        suspend fun fetchToken(type: TokenTypes): MetaTokenResponse
    }

    interface Repository {
        suspend fun fetchToken(type: TokenTypes): MetaToken
    }
}
