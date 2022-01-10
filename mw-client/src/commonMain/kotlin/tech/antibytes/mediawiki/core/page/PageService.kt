/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract

internal class PageService(
    private val repository: PageContract.Repository
) : PageContract.Service {
    override suspend fun randomPage(
        limit: Int,
        namespace: Int?
    ): List<DataModelContract.RevisionedPagePointer> = repository.randomPage(limit, namespace)

    override suspend fun fetchRestrictions(
        pageTitle: String
    ): List<String> = repository.fetchRestrictions(pageTitle)
}
