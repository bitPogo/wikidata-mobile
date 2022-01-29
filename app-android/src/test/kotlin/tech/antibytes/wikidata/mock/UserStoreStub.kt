/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.user.UserStoreContract

class UserStoreStub(
    override val isAuthenticated: CoroutineWrapperContract.SharedFlowWrapper<Boolean, Exception>,
    var login: ((String, String) -> Unit)? = null
) : UserStoreContract.UserStore, MockContract.Mock {
    override fun login(username: String, password: String) {
        return login?.invoke(username, password)
            ?: throw MockError.MissingStub("Missing Sideeffect login")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun clear() {
        login = null
    }
}
