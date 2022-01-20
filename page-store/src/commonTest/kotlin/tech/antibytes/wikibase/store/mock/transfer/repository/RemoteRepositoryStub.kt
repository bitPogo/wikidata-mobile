/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.transfer.repository

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

class RemoteRepositoryStub(
    var fetchRandomPageIds: (() -> List<EntityId>)? = null,
    var searchForItem: ((String, LanguageTag) -> List<PageModelContract.SearchEntry>)? = null
) : DomainContract.RemoteRepository, MockContract.Mock {
    override suspend fun fetchRandomItemIds(): List<EntityId> {
        return fetchRandomPageIds?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect fetchRandomPageIds")
    }

    override suspend fun searchForItem(term: String, language: LanguageTag): List<PageModelContract.SearchEntry> {
        return searchForItem?.invoke(term, language)
            ?: throw MockError.MissingStub("Missing Sideeffect searchForItem")
    }

    override fun clear() {
        fetchRandomPageIds = null
        searchForItem = null
    }
}
