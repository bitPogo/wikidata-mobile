/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.WikibaseContract
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.EntityTypes
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseApiServiceStub(
    var fetch: ((Set<EntityId>) -> EntityResponse)? = null,
    var search: ((String, LanguageTag, EntityTypes, Int) -> SearchEntityResponse)? = null
) : WikibaseContract.ApiService, MockContract.Mock {
    override suspend fun fetch(ids: Set<EntityId>): EntityResponse {
        return fetch?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetch")
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: EntityTypes,
        limit: Int
    ): SearchEntityResponse {
        return search?.invoke(term, language, type, limit)
            ?: throw MockError.MissingStub("Missing Sideeffect search")
    }

    override fun clear() {
        fetch = null
    }
}
