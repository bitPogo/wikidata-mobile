/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.domain

internal interface DomainContract {
    interface Repository {
        suspend fun login(username: String, password: String): Boolean
        suspend fun logout(): Boolean
    }
}
