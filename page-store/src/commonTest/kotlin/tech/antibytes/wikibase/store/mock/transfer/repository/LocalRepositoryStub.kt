/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.transfer.repository

import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId

class LocalRepositoryStub(
    var fetchRandomPageId: (() -> EntityId?)? = null,
    var saveRandomPageIds: ((List<EntityId>) -> Unit)? = null,
) : DomainContract.LocalRepository, MockContract.Mock {
    override fun fetchRandomPageId(): EntityId? {
        return if (fetchRandomPageId == null) {
            throw MockError.MissingStub("Missing Sideeffect fetchRandomPageId")
        } else {
            fetchRandomPageId!!.invoke()
        }
    }

    override fun saveRandomPageIds(ids: List<EntityId>) {
        return saveRandomPageIds?.invoke(ids)
            ?: throw MockError.MissingStub("Missing Sideeffect saveRandomPageIds")
    }

    override fun clear() {
        fetchRandomPageId = null
        saveRandomPageIds = null
    }
}
