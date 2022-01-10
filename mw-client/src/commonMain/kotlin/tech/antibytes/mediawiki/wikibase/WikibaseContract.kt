/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse

internal interface WikibaseContract {
    interface Response {
        val success: Int
    }

    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetch(ids: Set<EntityId>): EntityResponse

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun search(
            term: String,
            language: LanguageTag,
            type: DataModelContract.EntityTypes,
            limit: Int
        ): SearchEntityResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetch(ids: Set<EntityId>): List<DataModelContract.RevisionedEntity>

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun search(
            term: String,
            language: LanguageTag,
            type: DataModelContract.EntityTypes,
            limit: Int
        ): List<DataModelContract.Entity>
    }

    interface Service {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetch(ids: Set<EntityId>): List<DataModelContract.RevisionedEntity>

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun search(
            term: String,
            language: LanguageTag,
            type: DataModelContract.EntityTypes,
            limit: Int
        ): List<DataModelContract.Entity>
    }
}
