/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.wikibase.store.user.UserStoreContract
import tech.antibytes.wikibase.store.user.lang.UserStoreError
import tech.antibytes.wikibase.store.user.transfer.RemoteRepository

class UserStore internal constructor(
    private val dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher,
    private val remoteRepository: DomainContract.Repository,
    private val authenticationFlow: MutableStateFlow<ResultContract<Boolean, Exception>>,
    override val isAuthenticated: CoroutineWrapperContract.SharedFlowWrapper<Boolean, Exception>
) : UserStoreContract.UserStore {
    private suspend fun <T> wrapResult(
        action: suspend () -> T
    ): ResultContract<T, Exception> {
        return try {
            Success(action.invoke())
        } catch (error: Exception) {
            Failure(error)
        }
    }

    private fun executeAuthenticationEvent(event: suspend () -> Boolean) {
        dispatcher.dispatch().launch {
            authenticationFlow.update {
                wrapResult(event)
            }
        }
    }

    private fun mapError(): UserStoreError.LoginFailure {
        val attempts = if (authenticationFlow.value.error is UserStoreError.LoginFailure) {
            val error = authenticationFlow.value.error as UserStoreError.LoginFailure

            error.attempts + 1
        } else {
            0
        }

        return UserStoreError.LoginFailure(attempts)
    }

    override fun login(username: String, password: String) {
        executeAuthenticationEvent {
            val isAuthenticated = remoteRepository.login(username, password)

            if (isAuthenticated) {
                isAuthenticated
            } else {
                throw mapError()
            }
        }
    }

    override fun logout() {
        executeAuthenticationEvent {
            false
        }
    }

    companion object : UserStoreContract.UserStoreFactory {
        override fun getInstance(
            client: PublicApi.Client,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): UserStoreContract.UserStore {
            val repository = RemoteRepository(client)
            val internalFlow = MutableStateFlow<ResultContract<Boolean, Exception>>(
                Failure(UserStoreError.InitialState())
            )
            val externalFlow = SharedFlowWrapper.getInstance(
                internalFlow,
                consumerScope
            )

            return UserStore(
                dispatcher = producerScope,
                remoteRepository = repository,
                authenticationFlow = internalFlow,
                isAuthenticated = externalFlow
            )
        }
    }
}
