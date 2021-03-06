/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract.RevisionedPagePointer
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

internal class PageService(
    private val repository: PageContract.Repository,
    private val wrapper: MwClientContract.ServiceResponseWrapper
) : PageContract.Service {
    private suspend fun fetchRandomPage(
        limit: Int,
        namespace: Int?
    ): List<RevisionedPagePointer> = repository.randomPage(limit, namespace)

    private suspend fun getRestrictions(
        pageTitle: String
    ): List<String> = repository.fetchRestrictions(pageTitle)

    override fun randomPage(
        limit: Int,
        namespace: Int?
    ): CoroutineWrapperContract.SuspendingFunctionWrapper<List<RevisionedPagePointer>> {
        return wrapper.warp { fetchRandomPage(limit, namespace) }
    }

    override fun fetchRestrictions(
        pageTitle: String
    ): CoroutineWrapperContract.SuspendingFunctionWrapper<List<String>> = wrapper.warp { getRestrictions(pageTitle) }
}
