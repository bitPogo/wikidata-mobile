/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

internal class MetaTokenRepository(
    private val apiService: MetaTokenServiceContract.ApiService
) : MetaTokenServiceContract.Repository {
    override suspend fun fetchToken(type: MetaTokenServiceContract.TokenTypes): MetaToken {
        TODO("Not yet implemented")
    }
}
