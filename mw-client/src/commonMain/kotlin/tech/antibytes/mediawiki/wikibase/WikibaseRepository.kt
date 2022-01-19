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

    private fun mapAlias(
        aliases: List<String>,
        language: LanguageTag
    ): List<LanguageValuePair> {
        return aliases.map { alias ->
            LanguageValuePair(language = language, value = alias)
        }
    }

    private fun mapAliases(
        aliases: List<String>,
        language: LanguageTag
    ): Map<LanguageTag, List<LanguageValuePair>> {
        return if (aliases.isEmpty()) {
            emptyMap()
        } else {
            mapOf(language to mapAlias(aliases, language))
        }
    }

    private fun mapValue(
        value: String,
        language: LanguageTag
    ): Map<LanguageTag, LanguageValuePair> {
        return if (value.isEmpty()) {
            emptyMap()
        } else {
            mapOf(
                language to LanguageValuePair(
                    language = language,
                    value = value
                )
            )
        }
    }

    private fun mapSearchEntities(
        entities: List<SearchEntity>,
        type: EntityType
    ): List<DataModelContract.Entity> {
        return entities.map { search ->
            Entity(
                id = search.id,
                type = type,
                labels = mapValue(search.label, search.match.language),
                descriptions = mapValue(search.description, search.match.language),
                aliases = mapAliases(
                    language = search.match.language,
                    aliases = search.aliases,
                )
            )
        }
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: EntityType,
        limit: Int,
        page: Int
    ): List<DataModelContract.Entity> {
        val response = apiService.search(term, language, type, limit, page)

        return response.returnListOnSuccess {
            mapSearchEntities(response.search, type)
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
