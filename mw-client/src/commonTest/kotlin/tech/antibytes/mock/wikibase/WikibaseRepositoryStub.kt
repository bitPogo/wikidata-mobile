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
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseRepositoryStub(
    var fetch: ((Set<EntityId>) -> List<Entity>)? = null,
    var search: ((String, LanguageTag, DataModelContract.EntityTypes, Int) -> List<Entity>)? = null,
    var update: ((RevisionedEntity, MetaToken) -> RevisionedEntity?)? = null
) : WikibaseContract.Repository, MockContract.Mock {
    override suspend fun fetch(ids: Set<EntityId>): List<Entity> {
        return fetch?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetchEntities")
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityTypes,
        limit: Int
    ): List<Entity> {
        return search?.invoke(term, language, type, limit)
            ?: throw MockError.MissingStub("Missing Sideeffect search")
    }

    override suspend fun update(
        entity: RevisionedEntity,
        token: MetaToken
    ): RevisionedEntity? {
        return if (update == null) {
            throw MockError.MissingStub("Missing Sideeffect update")
        } else {
            update!!.invoke(entity, token)
        }
    }

    override fun clear() {
        fetch = null
        search = null
        update = null
    }
}
