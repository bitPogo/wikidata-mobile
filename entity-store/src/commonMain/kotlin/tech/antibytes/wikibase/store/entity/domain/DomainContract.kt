/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain

import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

internal interface DomainContract {
    interface Repository {
        suspend fun fetchEntity(id: EntityId, language: LanguageTag): MonolingualEntity?
        suspend fun createEntity(entity: MonolingualEntity): MonolingualEntity?
        suspend fun updateEntity(entity: MonolingualEntity): MonolingualEntity?
    }

    enum class DomainKoinIds {
        REMOTE,
        LOCAL,
        PRODUCER_SCOPE,
        CONSUMER_SCOPE
    }
}
