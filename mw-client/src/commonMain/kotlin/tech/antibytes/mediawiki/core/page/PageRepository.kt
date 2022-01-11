/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.core.page.model.PageResponse

internal class PageRepository(
    private val apiService: PageContract.ApiService
) : PageContract.Repository {
    override suspend fun randomPage(limit: Int, namespace: Int?): List<DataModelContract.RevisionedPagePointer> {
        val response = apiService.randomPage(limit, namespace)

        return response.query.pages.values.toList()
    }

    // TODO This should be smooth with a MwApiExpert
    private fun extractRestrictions(response: PageResponse): List<String> {
        return if (response.query.pages.values.first().protectionLevels.isEmpty()) {
            emptyList()
        } else {
            response.query.pages.values.first().restrictions
        }
    }

    override suspend fun fetchRestrictions(pageTitle: String): List<String> {
        val response = apiService.fetchRestrictions(
            pageTitle.replace("|", "")
        )

        return extractRestrictions(response)
    }
}
