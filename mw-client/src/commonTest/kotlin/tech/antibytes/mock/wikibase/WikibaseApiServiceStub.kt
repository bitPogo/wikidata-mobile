/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.wikibase

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.wikibase.WikibaseContract
import tech.antibytes.mediawiki.wikibase.model.EntitiesResponse
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseApiServiceStub(
    var fetch: ((Set<EntityId>) -> EntitiesResponse)? = null,
    var search: ((String, LanguageTag, DataModelContract.EntityTypes, Int) -> SearchEntityResponse)? = null,
    var update: ((EntityId, Long, String, MetaToken) -> EntityResponse)? = null,
    var create: ((DataModelContract.EntityTypes, String, MetaToken) -> EntityResponse)? = null
) : WikibaseContract.ApiService, MockContract.Mock {
    override suspend fun fetch(ids: Set<EntityId>): EntitiesResponse {
        return fetch?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetch")
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityTypes,
        limit: Int
    ): SearchEntityResponse {
        return search?.invoke(term, language, type, limit)
            ?: throw MockError.MissingStub("Missing Sideeffect search")
    }

    override suspend fun update(
        id: EntityId,
        revisionId: Long,
        entity: String,
        token: MetaToken
    ): EntityResponse {
        return update?.invoke(id, revisionId, entity, token)
            ?: throw MockError.MissingStub("Missing Sideeffect update")
    }

    override suspend fun create(
        type: DataModelContract.EntityTypes,
        entity: String,
        token: MetaToken
    ): EntityResponse {
        return create?.invoke(type, entity, token)
            ?: throw MockError.MissingStub("Missing Sideeffect create")
    }

    override fun clear() {
        fetch = null
        search = null
        update = null
        create = null
    }
}
