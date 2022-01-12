/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.services

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.SelectMonoligualEntityById
import tech.antibytes.wikibase.store.database.entity.Term
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

internal interface ServiceContract {


    interface DatabaseService {
        suspend fun setEntity(entity: Entity): Entity
        suspend fun setTerm(term: Term): Term

        suspend fun updateEntity(entity: Entity): Entity
        suspend fun updateTerm(term: Term): Term

        suspend fun fetchMonolingualEntity(id: EntityId): SelectMonoligualEntityById
    }

    interface ApiService {
        suspend fun fetchEntity(id: EntityId): RevisionedEntity
        suspend fun updateEntity(entity: RevisionedEntity): RevisionedEntity?
        suspend fun createEntity(entity: BoxedTerms): RevisionedEntity?
    }
}
