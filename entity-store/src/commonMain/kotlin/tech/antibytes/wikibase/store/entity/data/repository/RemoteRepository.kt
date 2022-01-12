/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.subscribeOn
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity

internal class RemoteRepository(
    private val client: PublicApi.Client
) : DomainContract.Repository {
    private suspend fun fetchDataTransferEntity(id: EntityId): RevisionedEntity? {
        val entities = client.wikibase
            .fetchEntities(setOf(id))
            .wrappedFunction
            .invoke()

        return if (entities.isEmpty()) {
            null
        } else {
            entities.first()
        }
    }

    private suspend fun fetchEntityEdibility(id: EntityId): Boolean {
        val restrictions = client.page
            .fetchRestrictions(id)
            .wrappedFunction
            .invoke()

        return restrictions.isNotEmpty()
    }

    override suspend fun fetchEntity(
        id: EntityId,
        language: LanguageTag
    ): EntityModelContract.MonolingualEntity {
        TODO()
    }


    override suspend fun createEntity(
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        TODO("Not yet implemented")
    }

    override suspend fun updateEntity(
        entity: EntityModelContract.MonolingualEntity
    ): MonolingualEntity {
        TODO("Not yet implemented")
    }
}
