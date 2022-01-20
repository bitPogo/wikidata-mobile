/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.transfer.mapper

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.wikibase.store.page.transfer.DataTransferContract

class SearchEntityMapperStub(
    var map: ((DataModelContract.Entity) -> PageModelContract.SearchEntry)? = null
) : DataTransferContract.SearchEntityMapper, MockContract.Mock {
    override fun map(entity: DataModelContract.Entity): PageModelContract.SearchEntry {
        return if (map == null) {
            throw MockError.MissingStub("Missing Sideeffect map")
        } else {
            map!!.invoke(entity)
        }
    }

    override fun clear() {
        map = null
    }
}
