/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.model.Entity

internal class WikibaseService(
    private val wikibaseRepository: WikibaseContract.Repository
) : WikibaseContract.Service {
    override suspend fun fetch(
        ids: Set<EntityId>
    ): List<Entity> = wikibaseRepository.fetch(ids)
}
