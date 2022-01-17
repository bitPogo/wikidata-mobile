/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

internal class AuthenticationService(
    private val authRepository: AuthenticationContract.Repository,
    private val tokenRepository: MetaTokenContract.Repository,
    private val wrapper: MwClientContract.ServiceResponseWrapper
) : AuthenticationContract.Service {
    private suspend fun doLogin(username: String, password: String): Boolean {
        val token = tokenRepository.fetchToken(MetaTokenContract.MetaTokenType.LOGIN)

        return authRepository.login(username, password, token)
    }

    override suspend fun login(
        username: String,
        password: String
    ): CoroutineWrapperContract.SuspendingFunctionWrapper<Boolean> = wrapper.warp { doLogin(username, password) }
}
