/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.core.token

import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract
import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class MetaTokenApiServiceStub(
    var fetchToken: ((MetaTokenServiceContract.TokenTypes) -> MetaTokenResponse)? = null
) : MetaTokenServiceContract.ApiService, MockContract.Mock {

    override suspend fun fetchToken(type: MetaTokenServiceContract.TokenTypes): MetaTokenResponse {
        return fetchToken?.invoke(type) ?: throw MockError.MissingStub("Missing Sideeffect fetchToken")
    }

    override fun clear() {
        fetchToken = null
    }
}
