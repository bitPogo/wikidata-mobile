/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract

internal interface DataTransferContract {
    interface SearchEntityMapper {
        fun map(entity: DataModelContract.Entity): PageModelContract.SearchEntry?
    }
}
