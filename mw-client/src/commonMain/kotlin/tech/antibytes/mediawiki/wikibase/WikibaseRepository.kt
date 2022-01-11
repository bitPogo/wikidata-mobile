/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import kotlinx.serialization.json.Json
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.model.LanguageValuePair
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.SearchEntity
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.core.token.MetaToken

internal class WikibaseRepository(
    private val apiService: WikibaseContract.ApiService,
    private val serializer: Json
) : WikibaseContract.Repository {
    private fun <T : DataModelContract.Entity> WikibaseContract.Response.applyOnSuccess(
        onSuccess: () -> List<T>
    ): List<T> {
        return if (this.success == 1) {
            onSuccess()
        } else {
            emptyList()
        }
    }

    override suspend fun fetch(ids: Set<EntityId>): List<RevisionedEntity> {
        val response = apiService.fetch(ids)

        return response.applyOnSuccess {
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
        type: DataModelContract.EntityTypes
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
        type: DataModelContract.EntityTypes,
        limit: Int
    ): List<DataModelContract.Entity> {
        val response = apiService.search(term, language, type, limit)

        return response.applyOnSuccess {
            mapSearchEntities(response.search, language, type)
        }
    }

    override suspend fun update(entity: RevisionedEntity, token: MetaToken): RevisionedEntity? {
        TODO()
    }
}
