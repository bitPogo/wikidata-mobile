/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.transfer

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.user.domain.DomainContract

class RepositoryStub(
    var login: ((String, String) -> Boolean)? = null,
    var logout: (() -> Boolean)? = null
) : DomainContract.Repository, MockContract.Mock {
    override suspend fun login(username: String, password: String): Boolean {
        return login?.invoke(username, password)
            ?: throw MockError.MissingStub("Missing Sideeffect login")
    }

    override suspend fun logout(): Boolean {
        return logout?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect logout")
    }

    override fun clear() {
        login = null
        logout = null
    }
}
