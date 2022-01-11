/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.EntityType
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mediawiki.wikibase.model.EntitiesResponse
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse

internal interface WikibaseContract {
    interface Response {
        val success: Int
    }

    interface MonolingualEntity {
        val id: EntityId
        val label: String
        val description: String
        val aliases: List<String>
    }

    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetch(ids: Set<EntityId>): EntitiesResponse

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun search(
            term: String,
            language: LanguageTag,
            type: EntityType,
            limit: Int
        ): SearchEntityResponse

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun update(
            id: EntityId,
            revisionId: Long,
            entity: String,
            token: MetaToken
        ): EntityResponse

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun create(
            type: EntityType,
            entity: String,
            token: MetaToken
        ): EntityResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetch(ids: Set<EntityId>): List<RevisionedEntity>

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun search(
            term: String,
            language: LanguageTag,
            type: EntityType,
            limit: Int
        ): List<DataModelContract.Entity>

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun update(entity: RevisionedEntity, token: MetaToken): RevisionedEntity?

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun create(type: EntityType, entity: BoxedTerms, token: MetaToken): RevisionedEntity?
    }

    interface Service : PublicApi.WikibaseService


    enum class KoinKey {
        LANGUAGE_PAIR_SERIALIZER,
        BOXED_TERMS_SERIALIZER,
    }
}
