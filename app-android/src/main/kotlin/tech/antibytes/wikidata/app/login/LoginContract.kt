/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import kotlinx.coroutines.flow.StateFlow

interface LoginContract {
    interface LoginViewModel {
        val isLoggedIn: StateFlow<Boolean>
        val username: StateFlow<String>
        val password: StateFlow<String>

        fun setUsername(username: String)
        fun setPassword(password: String)
        fun login()
    }
}
