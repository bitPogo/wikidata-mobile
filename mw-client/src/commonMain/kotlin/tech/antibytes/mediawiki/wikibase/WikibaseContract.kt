/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntityResponse

internal interface WikibaseContract {
    interface ApiService {
        fun fetchEntity(ids: Set<EntityId>): EntityResponse
    }

    interface Repository {
        fun fetchEntity(ids: Set<EntityId>): Set<Entity>
    }

    interface Service {
        fun fetchEntity(ids: Set<EntityId>): Set<Entity>
    }
}
