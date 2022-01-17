/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer.repository

import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.entity.transfer.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

internal class RemoteRepository(
    private val client: PublicApi.Client,
    private val entityMapper: MapperContract.RemoteEntityMapper
) : DomainContract.Repository {
    private suspend fun fetchDataTransferEntity(
        id: EntityId,
        language: LanguageTag
    ): RevisionedEntity? {
        val entities = client.wikibase
            .fetchEntities(setOf(id), language)
            .wrappedFunction
            .invoke()

        return entities.getOrNull(0)
    }

    private suspend fun fetchEntityEdibility(id: EntityId): List<String> {
        return client.page
            .fetchRestrictions(id)
            .wrappedFunction
            .invoke()
    }

    private fun mapEntityResponse(
        language: LanguageTag,
        revisionedEntity: RevisionedEntity?,
        restrictions: List<String>
    ): EntityModelContract.MonolingualEntity? {
        return if (revisionedEntity == null) {
            null
        } else {
            entityMapper.toMonolingualEntity(
                language,
                revisionedEntity,
                restrictions
            )
        }
    }

    private suspend fun fetchRestrictions(entity: RevisionedEntity?): List<String> {
        return if (entity == null) {
            emptyList()
        } else {
            fetchEntityEdibility(entity.id)
        }
    }

    override suspend fun fetchEntity(
        id: EntityId,
        language: LanguageTag
    ): EntityModelContract.MonolingualEntity? {
        val response = fetchDataTransferEntity(id, language)
        val restrictions = fetchRestrictions(response)

        return mapEntityResponse(
            language,
            response,
            restrictions
        )
    }

    private suspend fun createEntity(entity: RevisionedEntity): RevisionedEntity? {
        return client.wikibase
            .createEntity(entity.type, entity)
            .wrappedFunction
            .invoke()
    }

    override suspend fun createEntity(
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity? {
        val revisioned = entityMapper.toRevisionedEntity(entity)
        val response = createEntity(revisioned)

        return mapEntityResponse(
            entity.language,
            response,
            emptyList()
        )
    }

    private suspend fun editEntity(entity: RevisionedEntity): RevisionedEntity? {
        return client.wikibase
            .updateEntity(entity)
            .wrappedFunction
            .invoke()
    }

    override suspend fun updateEntity(entity: EntityModelContract.MonolingualEntity): EntityModelContract.MonolingualEntity? {
        val revisioned = entityMapper.toRevisionedEntity(entity)
        val response = editEntity(revisioned)

        return mapEntityResponse(
            entity.language,
            response,
            emptyList()
        )
    }
}
