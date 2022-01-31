/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.wikibase.store.user.UserStoreContract
import tech.antibytes.wikibase.store.user.lang.UserStoreError

class LoginViewModel(
    private val userStore: UserStoreContract.UserStore,
) : LoginContract.LoginViewModel, ViewModel() {
    private val _isLoggedIn = MutableStateFlow<LoginContract.LoginState>(LoginContract.LoginState.LoggedOut)
    override val isLoggedIn: StateFlow<LoginContract.LoginState> = _isLoggedIn

    private val _username = MutableStateFlow("")
    override val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    override val password: StateFlow<String> = _password

    init {
        userStore.isAuthenticated.subscribeWithSuspendingFunction { result ->
            subscription(result)
        }
    }

    private fun subscription(result: ResultContract<Boolean, Exception>) {
        val newState = if (!result.isError()) {
            mapSuccess(result)
        } else {
            mapError(result)
        }

        _isLoggedIn.update { newState }
    }

    private fun mapSuccess(result: ResultContract<Boolean, Exception>): LoginContract.LoginState {
        return if (result.unwrap()) {
            LoginContract.LoginState.LoggedIn
        } else {
            LoginContract.LoginState.LoggedOut
        }
    }

    private fun mapError(result: ResultContract<Boolean, Exception>): LoginContract.LoginState {
        return if (result.error is UserStoreError.InitialState) {
            LoginContract.LoginState.LoggedOut
        } else {
            LoginContract.LoginState.AuthenticationError(LoginContract.Reason.WRONG_USERNAME_OR_PASSWORD)
        }
    }

    override fun setUsername(username: String) {
        _username.update { username }
    }

    override fun setPassword(password: String) {
        _password.update { password }
    }

    override fun login() {
        userStore.login(
            _username.value,
            _password.value
        )

        setPassword("")
    }
}
