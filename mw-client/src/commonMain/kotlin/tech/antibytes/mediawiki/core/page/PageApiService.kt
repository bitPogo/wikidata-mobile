/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.core.page.model.PageResponse
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.networking.receive

internal class PageApiService(
    private val requestBuilder: NetworkingContract.RequestBuilderFactory
) : PageContract.ApiService {
    private fun createRandomPageParameter(limit: Int, namespace: Int?): Map<String, Comparable<*>> {
        val baseParameter = mutableMapOf(
            "action" to "query",
            "format" to "json",
            "generator" to "random",
            "prop" to "info",
            "grnlimit" to limit,
        )

        return if (namespace == null) {
            baseParameter
        } else {
            baseParameter.also {
                it["grnnamespace"] = namespace
            }
        }
    }

    override suspend fun randomPage(limit: Int, namespace: Int?): PageResponse {
        val request = requestBuilder
            .create()
            .setParameter(createRandomPageParameter(limit, namespace))
            .prepare(
                NetworkingContract.Method.GET,
                MwClientContract.ENDPOINT
            )

        return receive(request)
    }

    override suspend fun fetchRestrictions(pageTitle: String): PageResponse {
        val request = requestBuilder
            .create()
            .setParameter(
                mapOf(
                    "action" to "query",
                    "format" to "json",
                    "prop" to "info",
                    "inprop" to "protection",
                    "titles" to pageTitle
                )
            ).prepare(
                NetworkingContract.Method.GET,
                MwClientContract.ENDPOINT
            )

        return receive(request)
    }
}
