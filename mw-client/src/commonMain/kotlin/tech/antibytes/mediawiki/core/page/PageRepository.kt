/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract

internal class PageRepository(
    private val apiService: PageContract.ApiService
) : PageContract.Repository {
    override suspend fun randomPage(limit: Int, namespace: Int?): List<DataModelContract.RevisionedPagePointer> {
        val response = apiService.randomPage(limit, namespace)

        return response.query.random.values.toList()
    }
}
