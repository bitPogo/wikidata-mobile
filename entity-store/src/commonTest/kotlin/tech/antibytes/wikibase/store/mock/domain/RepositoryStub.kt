/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.domain

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

class RepositoryStub(
    var fetchEntity: ((EntityId, LanguageTag) -> MonolingualEntity?)? = null,
    var createEntity: ((MonolingualEntity) -> MonolingualEntity?)? = null,
    var updateEntity: ((MonolingualEntity) -> MonolingualEntity?)? = null,
) : DomainContract.Repository, MockContract.Mock {
    override suspend fun fetchEntity(
        id: EntityId,
        language: LanguageTag
    ): MonolingualEntity? {
        return if (fetchEntity == null) {
            throw MockError.MissingStub("Missing Sideeffect fetchEntity")
        } else {
            fetchEntity!!.invoke(id, language)
        }
    }

    override suspend fun createEntity(entity: MonolingualEntity): MonolingualEntity? {
        return if (createEntity == null) {
            throw MockError.MissingStub("Missing Sideeffect createEntity")
        } else {
            createEntity!!.invoke(entity)
        }
    }

    override suspend fun updateEntity(entity: MonolingualEntity): MonolingualEntity? {
        return if (updateEntity == null) {
            throw MockError.MissingStub("Missing Sideeffect updateEntity")
        } else {
            updateEntity!!.invoke(entity)
        }
    }

    override fun clear() {
        fetchEntity = null
        createEntity = null
        updateEntity = null
    }
}
