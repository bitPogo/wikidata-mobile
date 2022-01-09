/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import tech.antibytes.mediawiki.core.authentication.model.LoginStatus
import tech.antibytes.mediawiki.core.token.MetaToken

internal class AuthenticationRepository(
    private val apiService: AuthenticationContract.ApiService
) : AuthenticationContract.Repository {
    override suspend fun login(username: String, password: String, token: MetaToken): Boolean {
        val response = apiService.login(username, password, token)

        return response.clientlogin.status == LoginStatus.PASS
    }
}
