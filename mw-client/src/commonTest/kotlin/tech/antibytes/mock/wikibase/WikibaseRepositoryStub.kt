/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.WikibaseContract
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseRepositoryStub(
    var fetchEntities: ((Set<EntityId>) -> List<Entity>)? = null
) : WikibaseContract.Repository, MockContract.Mock {
    override suspend fun fetch(ids: Set<EntityId>): List<Entity> {
        return fetchEntities?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetchEntities")
    }

    override fun clear() {
        fetchEntities = null
    }
}
