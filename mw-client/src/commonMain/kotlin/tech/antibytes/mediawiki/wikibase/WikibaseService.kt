/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag

internal class WikibaseService(
    private val wikibaseRepository: WikibaseContract.Repository
) : WikibaseContract.Service {
    override suspend fun fetch(
        ids: Set<EntityId>
    ): List<DataModelContract.RevisionedEntity> = wikibaseRepository.fetch(ids)

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityType,
        limit: Int
    ): List<DataModelContract.Entity> = wikibaseRepository.search(term, language, type, limit)
}
