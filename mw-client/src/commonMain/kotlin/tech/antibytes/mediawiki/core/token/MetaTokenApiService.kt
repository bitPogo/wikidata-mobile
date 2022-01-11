/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.MwClientContract.Companion.ENDPOINT
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.receive

internal class MetaTokenApiService(
    private val requestBuilder: NetworkingContract.RequestBuilder
) : MetaTokenContract.ApiService {

    override suspend fun fetchToken(type: MetaTokenContract.MetaTokenType): MetaTokenResponse {
        val request = requestBuilder.setParameter(
            mapOf(
                "action" to "query",
                "meta" to "tokens",
                "format" to "json",
                "type" to type.value.removeSuffix("token")
            )
        ).prepare(
            NetworkingContract.Method.GET,
            ENDPOINT
        )

        return receive(request)
    }
}
