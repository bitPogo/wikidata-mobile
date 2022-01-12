/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.services

import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.SelectMonoligualEntityById
import tech.antibytes.wikibase.store.database.entity.Term
import tech.antibytes.wikibase.store.entity.domain.model.EntityId

internal interface ServiceContract {
    interface DatabaseService {
        suspend fun setEntity(entity: Entity): Entity
        suspend fun setTerm(term: Term): Term

        suspend fun updateEntity(entity: Entity): Entity
        suspend fun updateTerm(term: Term): Term

        suspend fun fetchMonolingualEntity(id: EntityId): SelectMonoligualEntityById
    }
}
