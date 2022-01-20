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
        fun fetchRandomPageId(): EntityId?
        fun saveRandomPageIds(ids: List<EntityId>)
    }

    interface RemoteRepository {
        suspend fun fetchRandomItemIds(): List<EntityId>
        suspend fun searchForItem(term: String, language: LanguageTag): List<PageModelContract.SearchEntry>
    }

    enum class DomainKoinIds {
        REMOTE,
        LOCAL,
        PRODUCER_SCOPE,
        CONSUMER_SCOPE,
        INTERNAL_RANDOM_FLOW,
        EXTERNAL_RANDOM_FLOW,
        INTERNAL_SEARCH_FLOW,
        EXTERNAL_SEARCH_FLOW,
    }

    companion object {
        const val ITEM_PREFIX = "Q"
    }
}
