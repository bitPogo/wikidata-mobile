/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.user

import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.core.user.UserContract
import tech.antibytes.mediawiki.core.user.model.LoginResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class UserApiServiceStub(
    var login: ((String, String, MetaToken) -> LoginResponse)? = null
) : UserContract.ApiService, MockContract.Mock {
    override suspend fun login(username: String, password: String, token: MetaToken): LoginResponse {
        return login?.invoke(username, password, token)
            ?: throw MockError.MissingStub("Missing Sideeffect login")
    }

    override fun clear() {
        login = null
    }
}
