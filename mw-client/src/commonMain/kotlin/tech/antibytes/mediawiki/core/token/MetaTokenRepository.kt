/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.error.MwClientError

internal class MetaTokenRepository(
    private val apiService: MetaTokenServiceContract.ApiService
) : MetaTokenServiceContract.Repository {
    override suspend fun fetchToken(type: MetaTokenServiceContract.TokenTypes): MetaToken {
        val response = apiService.fetchToken(type)

        return response.query[type]
            ?: throw MwClientError.InternalFailure("Missing Token (${type.value})")
    }
}
