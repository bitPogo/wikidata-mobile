/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.core.token

import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class MetaTokenApiServiceStub(
    var fetchToken: ((MetaTokenContract.MetaTokenType) -> MetaTokenResponse)? = null
) : MetaTokenContract.ApiService, MockContract.Mock {

    override suspend fun fetchToken(type: MetaTokenContract.MetaTokenType): MetaTokenResponse {
        return fetchToken?.invoke(type) ?: throw MockError.MissingStub("Missing Sideeffect fetchToken")
    }

    override fun clear() {
        fetchToken = null
    }
}
