/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain

import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

internal interface DomainContract {
    interface LocalRepository {
        fun fetchRandomPageId(): EntityId
        fun saveRandomPageIds(ids: List<EntityId>)
    }

    interface RemoteRepository {
        suspend fun fetchRandomPageIds(): List<EntityId>
        suspend fun searchForItem(term: String, language: LanguageTag): List<PageModelContract.SearchEntry>
    }

    companion object {
        const val ITEM_PREFIX = "Q"
    }
}
