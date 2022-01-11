/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.error.MwClientError

internal class MetaTokenRepository(
    private val apiService: MetaTokenContract.ApiService
) : MetaTokenContract.Repository {
    override suspend fun fetchToken(type: MetaTokenContract.MetaTokenType): MetaToken {
        val response = apiService.fetchToken(type)

        return response.query.tokens[type]
            ?: throw MwClientError.InternalFailure("Missing Token (${type.value})")
    }
}
