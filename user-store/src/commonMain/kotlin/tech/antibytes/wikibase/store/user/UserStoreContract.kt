/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

interface UserStoreContract {
    interface UserStore {
        val isAuthenticated: CoroutineWrapperContract.SharedFlowWrapper<Boolean, Exception>

        fun login(username: String, password: String)
        fun logout()
    }
}
