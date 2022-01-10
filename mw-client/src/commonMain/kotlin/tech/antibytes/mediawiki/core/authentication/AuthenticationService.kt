/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract

internal class AuthenticationService(
    private val authRepository: AuthenticationContract.Repository,
    private val tokenRepository: MetaTokenServiceContract.Repository
) : AuthenticationContract.Service {
    override suspend fun login(username: String, password: String): Boolean {
        val token = tokenRepository.fetchToken(MetaTokenServiceContract.TokenTypes.LOGIN)

        return authRepository.login(username, password, token)
    }
}