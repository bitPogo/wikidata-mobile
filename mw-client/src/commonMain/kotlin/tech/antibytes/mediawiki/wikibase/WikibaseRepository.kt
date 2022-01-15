/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.EntityType
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair
import tech.antibytes.mediawiki.wikibase.model.SearchEntity

internal class WikibaseRepository(
    private val apiService: WikibaseContract.ApiService,
    private val serializer: Json,
    private val boxedTermSerializer: KSerializer<BoxedTerms>,
    private val clock: Clock
) : WikibaseContract.Repository {
    private fun <T : DataModelContract.Entity> WikibaseContract.Response.returnListOnSuccess(
        onSuccess: () -> List<T>
    ): List<T> {
        return if (this.success == 1) {
            onSuccess()
        } else {
            emptyList()
        }
    }

    private fun <T : DataModelContract.Entity> WikibaseContract.Response.returnObjectOnSuccess(
        onSuccess: () -> T
    ): T? {
        return if (this.success == 1) {
            onSuccess()
        } else {
            null
        }
    }

    override suspend fun fetch(ids: Set<EntityId>, language: LanguageTag?): List<RevisionedEntity> {
        val response = apiService.fetch(ids, language)

        return response.returnListOnSuccess {
            response.entities.values.toList()
        }
    }

    private fun mapAliases(
        searchAliases: List<String>,
        language: LanguageTag
    ): List<LanguageValuePair> {
        return searchAliases.map { searchAlias ->
            LanguageValuePair(language = language, value = searchAlias)
        }
    }

    private fun mapSearchEntities(
        entities: List<SearchEntity>,
        language: LanguageTag,
        type: EntityType
    ): List<DataModelContract.Entity> {
        return entities.map { search ->
            Entity(
                id = search.id,
                type = type,
                labels = mapOf(
                    language to LanguageValuePair(language = language, value = search.label)
                ),
                descriptions = mapOf(
                    language to LanguageValuePair(language = language, value = search.description)
                ),
                aliases = mapOf(
                    language to mapAliases(search.aliases, language)
                )
            )
        }
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: EntityType,
        limit: Int
    ): List<DataModelContract.Entity> {
        val response = apiService.search(term, language, type, limit)

        return response.returnListOnSuccess {
            mapSearchEntities(response.search, language, type)
        }
    }

    private fun extractEntityAfterEditing(response: EntityResponse): RevisionedEntity? {
        return response.returnObjectOnSuccess {
            response.entity!!.copy(lastModification = clock.now())
        }
    }

    override suspend fun update(entity: RevisionedEntity, token: MetaToken): RevisionedEntity? {
        val id = entity.id
        val revision = entity.revision
        val serializedEntity = serializer.encodeToString(
            boxedTermSerializer,
            entity
        )

        val response = apiService.update(id, revision, serializedEntity, token)

        return extractEntityAfterEditing(response)
    }

    override suspend fun create(type: EntityType, entity: BoxedTerms, token: MetaToken): RevisionedEntity? {
        val serializedEntity = serializer.encodeToString(
            boxedTermSerializer,
            entity
        )

        val response = apiService.create(type, serializedEntity, token)

        return extractEntityAfterEditing(response)
    }
}
