/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.services

import tech.antibytes.mediawiki.DataModelContract.EntityType
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.wikibase.store.entity.domain.model.EntityId

internal class EntityStoreApiService(
    private val client: PublicApi.Client
) : ServiceContract.ApiService {
    override suspend fun fetchEntity(
        id: EntityId
    ): RevisionedEntity = client.wikibase.fetchEntities(setOf(id)).wrappedFunction.invoke().first()

    override suspend fun updateEntity(
        entity: RevisionedEntity
    ): RevisionedEntity? = client.wikibase.updateEntity(entity).wrappedFunction.invoke()

    override suspend fun createEntity(
        entity: BoxedTerms
    ): RevisionedEntity? = client.wikibase.createEntity(EntityType.ITEM, entity).wrappedFunction.invoke()
}
