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
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SuspendingFunctionWrapper

internal class WikibaseService(
    private val wikibaseRepository: WikibaseContract.Repository,
    private val tokenRepository: MetaTokenContract.Repository,
    private val wrapper: MwClientContract.ServiceResponseWrapper
) : WikibaseContract.Service {
    private suspend fun fetch(
        ids: Set<EntityId>,
        language: LanguageTag?
    ): List<RevisionedEntity> = wikibaseRepository.fetch(ids, language)

    override fun fetchEntities(
        ids: Set<EntityId>,
        language: LanguageTag?
    ): SuspendingFunctionWrapper<List<RevisionedEntity>> = wrapper.warp { fetch(ids, language) }

    private suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityType,
        limit: Int,
        page: Int
    ): List<DataModelContract.Entity> = wikibaseRepository.search(term, language, type, limit, page)

    private fun guardPageIndex(index: Int): Int {
        return if (index < 0) {
            0
        } else {
            index
        }
    }

    override fun searchForEntities(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityType,
        limit: Int,
        page: Int
    ): SuspendingFunctionWrapper<List<DataModelContract.Entity>> {
        return wrapper.warp {
            search(
                term,
                language,
                type,
                limit,
                guardPageIndex(page)
            )
        }
    }

    private suspend fun update(entity: RevisionedEntity): RevisionedEntity? {
        val token = tokenRepository.fetchToken(MetaTokenContract.MetaTokenType.CSRF)

        return wikibaseRepository.update(entity, token)
    }

    override fun updateEntity(
        entity: RevisionedEntity
    ): SuspendingFunctionWrapper<RevisionedEntity?> = wrapper.warp { update(entity) }

    private suspend fun create(
        type: DataModelContract.EntityType,
        entity: DataModelContract.BoxedTerms
    ): RevisionedEntity? {
        val token = tokenRepository.fetchToken(MetaTokenContract.MetaTokenType.CSRF)

        return wikibaseRepository.create(type, entity, token)
    }

    override fun createEntity(
        type: DataModelContract.EntityType,
        entity: DataModelContract.BoxedTerms
    ): SuspendingFunctionWrapper<RevisionedEntity?> = wrapper.warp { create(type, entity) }
}
