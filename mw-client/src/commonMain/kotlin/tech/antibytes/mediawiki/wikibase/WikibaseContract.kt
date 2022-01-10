/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntityResponse

internal interface WikibaseContract {
    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchEntities(ids: Set<EntityId>): EntityResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchEntities(ids: Set<EntityId>): List<Entity>
    }

    interface Service {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchEntities(ids: Set<EntityId>): List<Entity>
    }
}
