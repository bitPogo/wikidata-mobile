/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.DomainContract.Companion.ITEM_PREFIX
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.LanguageTag
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

internal class RemoteRepository(
    private val client: PublicApi.Client,
    private val mapper: DataTransferContract.SearchEntityMapper
) : DomainContract.RemoteRepository {
    private fun mapPageIds(
        pages: List<DataModelContract.RevisionedPagePointer>
    ): List<EntityId> {
        return pages
            .map { pointer -> pointer.title }
            .filter { id -> id.startsWith(ITEM_PREFIX) }
    }

    override suspend fun fetchRandomPageIds(): List<EntityId> {
        val response = client.page.randomPage(42).wrappedFunction.invoke()

        return mapPageIds(response)
    }

    override suspend fun searchForItem(term: String, language: LanguageTag): List<PageModelContract.SearchEntry> {
        val response = client.wikibase.searchForEntities(
            term = term,
            language = language,
            type = DataModelContract.EntityType.ITEM,
            limit = 50,
            page = 0
        ).wrappedFunction.invoke()

        return response
            .mapNotNull { entity -> mapper.map(entity) }
    }
}
