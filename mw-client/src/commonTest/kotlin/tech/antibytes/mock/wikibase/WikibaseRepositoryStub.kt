/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.wikibase

import tech.antibytes.mediawiki.EntityContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.WikibaseContract
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseRepositoryStub(
    var fetch: ((Set<EntityId>) -> List<Entity>)? = null,
    var search: ((String, LanguageTag, EntityContract.EntityTypes, Int) -> List<Entity>)? = null
) : WikibaseContract.Repository, MockContract.Mock {
    override suspend fun fetch(ids: Set<EntityId>): List<Entity> {
        return fetch?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetchEntities")
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: EntityContract.EntityTypes,
        limit: Int
    ): List<Entity> {
        return search?.invoke(term, language, type, limit)
            ?: throw MockError.MissingStub("Missing Sideeffect search")
    }

    override fun clear() {
        fetch = null
    }
}
