/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.WikibaseContract
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class WikibaseApiServiceStub(
    var fetchEntities: ((Set<EntityId>) -> EntityResponse)? = null
) : WikibaseContract.ApiService, MockContract.Mock {
    override suspend fun fetchEntities(ids: Set<EntityId>): EntityResponse {
        return fetchEntities?.invoke(ids) ?: throw MockError.MissingStub("Missing Sideeffect fetchEntities")
    }

    override fun clear() {
        fetchEntities = null
    }
}
