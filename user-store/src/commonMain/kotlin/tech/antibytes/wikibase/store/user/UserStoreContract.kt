/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

interface UserStoreContract {
    interface UserStore {
        val isAuthenticated: CoroutineWrapperContract.SharedFlowWrapper<Boolean, Exception>

        fun login(username: String, password: String)
        fun logout()
    }

    interface UserStoreFactory {
        fun getInstance(
            client: PublicApi.Client,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): UserStore
    }
}
