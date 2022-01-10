/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.core.page.model.PageResponse
import tech.antibytes.mediawiki.error.MwClientError

internal interface PageContract {
    interface ApiService {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun randomPage(limit: Int, namespace: Int? = null): PageResponse

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchRestrictions(pageTitle: String): PageResponse
    }

    interface Repository {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun randomPage(limit: Int, namespace: Int? = null): List<DataModelContract.RevisionedPagePointer>

        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun fetchRestrictions(pageTitle: String): List<String>
    }

    interface Service {
        @Throws(
            MwClientError.ResponseTransformFailure::class,
            MwClientError.RequestValidationFailure::class,
            MwClientError.InternalFailure::class
        )
        suspend fun randomPage(limit: Int, namespace: Int? = null): List<DataModelContract.RevisionedPagePointer>
    }
}
