/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import kotlinx.coroutines.flow.StateFlow

interface LoginContract {
    interface LoginViewModel {
        val isLoggedIn: StateFlow<LoginState>
        val username: StateFlow<String>
        val password: StateFlow<String>

        fun setUsername(username: String)
        fun setPassword(password: String)
        fun login()
    }

    enum class Reason {
        WRONG_USERNAME_OR_PASSWORD
    }

    sealed class LoginState {
        object LoggedIn : LoginState()
        object LoggedOut : LoginState()
        class AuthenticationError(val reason: Reason) : LoginState()
    }

    fun interface Navigator {
        fun goToTermbox()
    }
}
