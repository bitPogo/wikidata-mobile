/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.wikibase.model.Alias
import tech.antibytes.mediawiki.wikibase.model.Description
import tech.antibytes.mediawiki.wikibase.model.Entity
import tech.antibytes.mediawiki.wikibase.model.Label
import tech.antibytes.mediawiki.wikibase.model.SearchEntity

internal class WikibaseRepository(
    private val apiService: WikibaseContract.ApiService
) : WikibaseContract.Repository {
    private fun <T : EntityContract.Entity> WikibaseContract.Response.applyOnSuccess(
        onSuccess: () -> List<T>
    ): List<T> {
        return if (this.success == 1) {
            onSuccess()
        } else {
            emptyList()
        }
    }

    override suspend fun fetch(ids: Set<EntityId>): List<EntityContract.RevisionedEntity> {
        val response = apiService.fetch(ids)

        return response.applyOnSuccess {
            response.entities.values.toList()
        }
    }

    private fun mapAliases(
        searchAliases: List<String>,
        language: LanguageTag
    ): List<Alias> {
        return searchAliases.map { searchAlias ->
            Alias(language = language, value = searchAlias)
        }
    }

    private fun mapSearchEntities(
        entities: List<SearchEntity>,
        language: LanguageTag,
        type: EntityContract.EntityTypes
    ): List<EntityContract.Entity> {
        return entities.map { search ->
            Entity(
                id = search.id,
                type = type,
                labels = mapOf(
                    language to Label(language = language, value = search.label)
                ),
                descriptions = mapOf(
                    language to Description(language = language, value = search.description)
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
        type: EntityContract.EntityTypes,
        limit: Int
    ): List<EntityContract.Entity> {
        val response = apiService.search(term, language, type, limit)

        return response.applyOnSuccess {
            mapSearchEntities(response.search, language, type)
        }
    }
}
