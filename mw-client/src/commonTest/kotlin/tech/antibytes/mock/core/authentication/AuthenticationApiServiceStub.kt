/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.core.authentication

import tech.antibytes.mediawiki.core.authentication.AuthenticationContract
import tech.antibytes.mediawiki.core.authentication.model.LoginResponse
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class AuthenticationApiServiceStub(
    var login: ((String, String, MetaToken) -> LoginResponse)? = null
) : AuthenticationContract.ApiService, MockContract.Mock {
    override suspend fun login(username: String, password: String, token: MetaToken): LoginResponse {
        return login?.invoke(username, password, token)
            ?: throw MockError.MissingStub("Missing Sideeffect login")
    }

    override fun clear() {
        login = null
    }
}
