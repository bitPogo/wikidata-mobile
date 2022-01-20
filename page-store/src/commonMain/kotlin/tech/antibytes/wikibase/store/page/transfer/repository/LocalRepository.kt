/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer.repository

import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId

internal class LocalRepository(
    val database: PageQueries
) : DomainContract.LocalRepository {
    private fun removeRandomId(id: EntityId?) {
        if (id != null) {
            database.deleteRandomPage(id)
        }
    }

    override fun fetchRandomPageId(): EntityId? {
        val id = database.peekRandomPages().executeAsOneOrNull()

        removeRandomId(id)

        return id
    }

    override fun saveRandomPageIds(ids: List<EntityId>) {
        database.transaction {
            ids.forEach { id ->
                database.insertRandomPage(id)
            }
        }
    }
}
