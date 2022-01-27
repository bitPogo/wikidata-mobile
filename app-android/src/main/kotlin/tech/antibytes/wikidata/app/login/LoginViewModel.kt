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
import tech.antibytes.wikibase.store.user.UserStoreContract

class LoginViewModel(
    private val userStore: UserStoreContract.UserStore
) : LoginContract.LoginViewModel, ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)

    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _username = MutableStateFlow("")

    override val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")

    override val password: StateFlow<String> = _password

    init {
        userStore.isAuthenticated.subscribeWithSuspendingFunction { result ->
            if (!result.isError()) {
                _isLoggedIn.update { result.unwrap() }
            }
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
    }
}
