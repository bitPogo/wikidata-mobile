/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.page

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.core.page.model.RandomPageResponse

internal interface PageContract {
    interface ApiService {
        suspend fun randomPage(limit: Int, namespace: Int? = null): RandomPageResponse
    }

    interface Repository {
        suspend fun randomPage(limit: Int, namespace: Int? = null): List<DataModelContract.RevisionedPagePointer>
    }

    interface Service {
        suspend fun randomPage(limit: Int, namespace: Int? = null): List<DataModelContract.RevisionedPagePointer>
    }
}
