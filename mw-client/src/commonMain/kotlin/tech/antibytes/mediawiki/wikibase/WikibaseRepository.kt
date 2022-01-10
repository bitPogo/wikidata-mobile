/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntityResponse

internal class WikibaseRepository(
    private val apiService: WikibaseContract.ApiService
) : WikibaseContract.Repository {
    private fun extractEntities(response: EntityResponse): List<Entity> {
        return if (response.success == 1) {
            response.entities.values.toList()
        } else {
            emptyList()
        }
    }

    override suspend fun fetch(ids: Set<EntityId>): List<Entity> {
        val response = apiService.fetch(ids)

        return extractEntities(response)
    }
}
