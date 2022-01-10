/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock.core.page

import tech.antibytes.mediawiki.core.page.PageContract
import tech.antibytes.mediawiki.core.page.model.PageResponse
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class PageApiServiceStub(
    var randomPage: ((Int, Int?) -> PageResponse)? = null,
    var fetchRestrictions: ((String) -> PageResponse)? = null
) : PageContract.ApiService, MockContract.Mock {
    override suspend fun randomPage(limit: Int, namespace: Int?): PageResponse {
        return randomPage?.invoke(limit, namespace)
            ?: throw MockError.MissingStub("Missing Sideeffect randomPage")
    }

    override suspend fun fetchRestrictions(pageTitle: String): PageResponse {
        return fetchRestrictions?.invoke(pageTitle)
            ?: throw MockError.MissingStub("Missing Sideeffect fetchRestrictions")
    }

    override fun clear() {
        randomPage = null
        fetchRestrictions = null
    }
}
