/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.transfer

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.user.domain.DomainContract

internal class RemoteRepository(
    private val client: PublicApi.Client
) : DomainContract.Repository {
    override suspend fun login(
        username: String,
        password: String
    ): Boolean {
        return client.authentication
            .login(username, password)
            .wrappedFunction
            .invoke()
    }

    override suspend fun logout() = true
}
