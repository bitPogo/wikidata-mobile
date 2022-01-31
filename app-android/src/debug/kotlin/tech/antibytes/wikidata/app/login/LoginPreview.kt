/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Preview
@Composable
fun DefaultLoginScreen() {
    LoginScreen(LoginViewModelStub())
}

private class LoginViewModelStub : LoginContract.LoginViewModel {
    override val isLoggedIn: StateFlow<LoginContract.LoginState> = MutableStateFlow(
        LoginContract.LoginState.LoggedOut
    )

    private val _username = MutableStateFlow("")
    override val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    override val password: StateFlow<String> = _password

    override fun setUsername(username: String) {
        _username.update { username }
    }

    override fun setPassword(password: String) {
        _password.update { password }
    }

    override fun login() {
        /* do nothing */
    }
}
