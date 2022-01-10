/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.MwClientContract.Companion.ENDPOINT
import tech.antibytes.mediawiki.core.token.MetaToken
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.receive
import tech.antibytes.mediawiki.wikibase.model.EntitiesResponse
import tech.antibytes.mediawiki.wikibase.model.EntityResponse
import tech.antibytes.mediawiki.wikibase.model.SearchEntityResponse

internal class WikibaseApiService(
    private val requestBuilder: NetworkingContract.RequestBuilder
) : WikibaseContract.ApiService {
    override suspend fun fetch(ids: Set<EntityId>): EntitiesResponse {
        val request = requestBuilder.setParameter(
            mapOf(
                "action" to "wbgetentities",
                "format" to "json",
                "ids" to ids.joinToString("|")
            )
        ).prepare(
            NetworkingContract.Method.GET,
            ENDPOINT
        )

        return receive(request)
    }

    override suspend fun search(
        term: String,
        language: LanguageTag,
        type: DataModelContract.EntityTypes,
        limit: Int
    ): SearchEntityResponse {
        val request = requestBuilder.setParameter(
            mapOf(
                "action" to "wbsearchentities",
                "format" to "json",
                "search" to term,
                "language" to language,
                "type" to type.name.lowercase(),
                "limit" to limit
            )
        ).prepare(
            NetworkingContract.Method.GET,
            ENDPOINT
        )

        return receive(request)
    }

    private fun createEditingPayload(entity: String, token: MetaToken) : FormDataContent {
        return FormDataContent(
            Parameters.build {
                append("token", token)
                append("data", entity)
            }
        )
    }

    override suspend fun update(
        id: EntityId,
        revisionId: Long,
        entity: String,
        token: MetaToken
    ): EntityResponse {
        val request = requestBuilder
            .setParameter(
                mapOf(
                    "action" to "wbeditentity",
                    "baserevid" to revisionId,
                    "format" to "json",
                    "id" to id
                )
            )
            .setBody(createEditingPayload(entity, token))
            .prepare(
                NetworkingContract.Method.POST,
                ENDPOINT
            )

        return receive(request)
    }

    override suspend fun create(
        type: DataModelContract.EntityTypes,
        entity: String,
        token: MetaToken
    ): EntityResponse {
        val request = requestBuilder
            .setParameter(
                mapOf(
                    "action" to "wbeditentity",
                    "format" to "json",
                    "new" to type.name.lowercase()
                )
            )
            .setBody(createEditingPayload(entity, token))
            .prepare(
                NetworkingContract.Method.POST,
                ENDPOINT
            )

        return receive(request)
    }
}
