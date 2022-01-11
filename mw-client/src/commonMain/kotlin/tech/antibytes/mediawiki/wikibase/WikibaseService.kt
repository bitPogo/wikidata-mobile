/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract

internal class WikibaseService(
    private val wikibaseRepository: WikibaseContract.Repository,
    private val tokenRepository: MetaTokenServiceContract.Repository
) : WikibaseContract.Service {
    override suspend fun fetch(
        ids: Set<EntityId>
    ): List<RevisionedEntity> = wikibaseRepository.fetch(ids)

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityType,
        limit: Int
    ): List<DataModelContract.Entity> = wikibaseRepository.search(term, language, type, limit)

    override suspend fun update(entity: RevisionedEntity): RevisionedEntity? {
        val token = tokenRepository.fetchToken(MetaTokenServiceContract.MetaTokenType.CSRF)

        return wikibaseRepository.update(entity, token)
    }

    override suspend fun create(
        type: DataModelContract.EntityType,
        entity: DataModelContract.BoxedTerms
    ): RevisionedEntity? {
        val token = tokenRepository.fetchToken(MetaTokenServiceContract.MetaTokenType.CSRF)

        return wikibaseRepository.create(type, entity, token)
    }
}
