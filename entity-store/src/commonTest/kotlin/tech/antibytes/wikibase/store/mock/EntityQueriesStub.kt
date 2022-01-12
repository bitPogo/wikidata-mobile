/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.datetime.Instant
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.entity.SelectMonoligualEntityById
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

class EntityQueriesStub : EntityQueries {
    override fun <T : Any> selectEntityById(
        whereId: String,
        mapper: (id: String, type: EntityModelContract.EntityType, revision: Long, lastModified: Instant, edibility: Boolean) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun selectEntityById(whereId: String): Query<Entity> {
        TODO("Not yet implemented")
    }

    override fun <T : Any> selectMonoligualEntityById(
        whereId: String,
        whereLanguage: String,
        mapper: (id: String, type: EntityModelContract.EntityType, revision: Long, lastModified: Instant, edibility: Boolean, label: String?, description: String?, aliases: List<String>) -> T
    ): Query<T> {
        TODO("Not yet implemented")
    }

    override fun selectMonoligualEntityById(whereId: String, whereLanguage: String): Query<SelectMonoligualEntityById> {
        TODO("Not yet implemented")
    }

    override fun addEntity(
        id: String,
        type: EntityModelContract.EntityType,
        revision: Long,
        lastModified: Instant,
        edibility: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun updateEntity(revision: Long, lastModified: Instant, edibility: Boolean, whereId: String) {
        TODO("Not yet implemented")
    }

    override fun addTerm(
        entityId: String,
        language: String,
        label: String?,
        description: String?,
        aliases: List<String>
    ) {
        TODO("Not yet implemented")
    }

    override fun updateTerm(
        label: String?,
        description: String?,
        aliases: List<String>,
        whereId: String,
        whereLanguage: String
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteTerm(whereId: String, whereLanguage: String) {
        TODO("Not yet implemented")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }
}
