/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase

import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.MwClientContract.Companion.ENDPOINT
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.receive
import tech.antibytes.mediawiki.wikibase.model.EntityResponse

internal class WikibaseApiService(
    private val requestBuilder: NetworkingContract.RequestBuilder
) : WikibaseContract.ApiService {
    override suspend fun fetchEntities(ids: Set<EntityId>): EntityResponse {
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
}
