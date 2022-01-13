/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.entity.data.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError

internal class RemoteRepository(
    private val client: PublicApi.Client,
    private val entityMapper: MapperContract.RemoteEntityMapper
) : DomainContract.Repository {
    private suspend fun fetchDataTransferEntity(
        id: EntityId,
        language: LanguageTag
    ): RevisionedEntity {
        val entities = client.wikibase
            .fetchEntities(setOf(id), language)
            .wrappedFunction
            .invoke()

        return if (entities.isEmpty()) {
            throw EntityStoreError.MissingEntity(id, language)
        } else {
            entities.first()
        }
    }

    private suspend fun fetchEntityEdibility(id: EntityId): List<String> {
        return client.page
            .fetchRestrictions(id)
            .wrappedFunction
            .invoke()
    }

    override suspend fun fetchEntity(
        id: EntityId,
        language: LanguageTag
    ): EntityModelContract.MonolingualEntity {
        val revisionedEntity = fetchDataTransferEntity(id, language)
        val restrictions = fetchEntityEdibility(id)

        return entityMapper.toMonolingualEntity(language, revisionedEntity, restrictions)
    }

    private suspend fun createEntity(language: LanguageTag, entity: RevisionedEntity): RevisionedEntity {
        val response = client.wikibase
            .createEntity(entity.type, entity)
            .wrappedFunction
            .invoke()

        return response ?: throw EntityStoreError.CreationFailure(language)
    }

    override suspend fun createEntity(
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        val revisioned = entityMapper.toRevisionedEntity(entity)
        val response = createEntity(entity.language, revisioned)

        return entityMapper.toMonolingualEntity(
            entity.language,
            response,
            emptyList()
        )
    }

    private suspend fun editEntity(language: LanguageTag, entity: RevisionedEntity): RevisionedEntity {
        val response = client.wikibase
            .updateEntity(entity)
            .wrappedFunction
            .invoke()

        return response ?: throw EntityStoreError.EditFailure(entity.id, language)
    }

    override suspend fun updateEntity(
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        val revisioned = entityMapper.toRevisionedEntity(entity)
        val response = editEntity(entity.language, revisioned)

        return entityMapper.toMonolingualEntity(
            entity.language,
            response,
            emptyList()
        )
    }
}
