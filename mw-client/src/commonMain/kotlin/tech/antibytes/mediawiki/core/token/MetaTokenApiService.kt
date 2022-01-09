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
) : MetaTokenServiceContract.ApiService {

    override suspend fun fetchToken(type: MetaTokenServiceContract.TokenTypes): MetaTokenResponse {
        val request = requestBuilder.setParameter(
            mapOf(
                "action" to "query",
                "meta" to "tokens",
                "type" to type.value
            )
        ).prepare(
            NetworkingContract.Method.GET,
            ENDPOINT
        )

        return receive(request)
    }
}
