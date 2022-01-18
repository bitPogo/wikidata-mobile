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
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.entity.SelectMonoligualEntityById
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

class EntityQueriesStub(
    var selectEntityById: ((String, (String, EntityModelContract.EntityType, Long, Instant, Boolean) -> EntityModelContract.MonolingualEntity) -> Query<EntityModelContract.MonolingualEntity>)? = null,
    var selectMonoligualEntityById: ((String, String, (String, EntityModelContract.EntityType, Long, Instant, Boolean, String?, String?, List<String>) -> EntityModelContract.MonolingualEntity) -> Query<EntityModelContract.MonolingualEntity>)? = null,
    var hasEntity: ((String) -> Query<Long>)? = null,
    var addEntity: ((String, EntityModelContract.EntityType, Long, Instant, Boolean) -> Unit)? = null,
    var updateEntity: ((Long, Instant, Boolean, String) -> Unit)? = null,
    var hasTerm: ((String, String) -> Query<Long>)? = null,
    var addTerm: ((String, String, String?, String?, List<String>) -> Unit)? = null,
    var updateTerm: ((String?, String?, List<String>, String, String) -> Unit)? = null,
    var deleteTerm: ((String, String) -> Unit)? = null
) : EntityQueries, MockContract.Mock {
    override fun hasEntity(whereId: String): Query<Long> {
        return hasEntity?.invoke(whereId)
            ?: throw MockError.MissingStub("Missing Sideeffect hasEntity")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> selectEntityById(
        whereId: String,
        mapper: (id: String, type: EntityModelContract.EntityType, revision: Long, lastModified: Instant, edibility: Boolean) -> T
    ): Query<T> {
        return if (selectEntityById == null) {
            throw MockError.MissingStub("Missing Sideeffect selectEntityById")
        } else {
            selectEntityById!!.invoke(
                whereId,
                mapper as (String, EntityModelContract.EntityType, Long, Instant, Boolean) -> EntityModelContract.MonolingualEntity
            ) as Query<T>
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> selectMonoligualEntityById(
        whereId: String,
        whereLanguage: String,
        mapper: (id: String, type: EntityModelContract.EntityType, revision: Long, lastModified: Instant, edibility: Boolean, label: String?, description: String?, aliases: List<String>) -> T
    ): Query<T> {
        return if (selectMonoligualEntityById == null) {
            throw MockError.MissingStub("Missing Sideeffect selectEntityById")
        } else {
            selectMonoligualEntityById!!.invoke(
                whereId,
                whereLanguage,
                mapper as (String, EntityModelContract.EntityType, Long, Instant, Boolean, String?, String?, List<String>) -> EntityModelContract.MonolingualEntity
            ) as Query<T>
        }
    }

    override fun addEntity(
        id: String,
        type: EntityModelContract.EntityType,
        revision: Long,
        lastModified: Instant,
        edibility: Boolean
    ) {
        return addEntity?.invoke(id, type, revision, lastModified, edibility)
            ?: throw MockError.MissingStub("Missing Sideeffect addEntity")
    }

    override fun updateEntity(revision: Long, lastModified: Instant, edibility: Boolean, whereId: String) {
        return updateEntity?.invoke(revision, lastModified, edibility, whereId)
            ?: throw MockError.MissingStub("Missing Sideeffect updateEntity")
    }

    override fun hasTerm(whereId: String, whereLanguage: String): Query<Long> {
        return hasTerm?.invoke(whereId, whereLanguage)
            ?: throw MockError.MissingStub("Missing Sideeffect hasTerm")
    }

    override fun addTerm(
        entityId: String,
        language: String,
        label: String?,
        description: String?,
        aliases: List<String>
    ) {
        return addTerm?.invoke(entityId, language, label, description, aliases)
            ?: throw MockError.MissingStub("Missing Sideeffect addTerm")
    }

    override fun updateTerm(
        label: String?,
        description: String?,
        aliases: List<String>,
        whereId: String,
        whereLanguage: String
    ) {
        return updateTerm?.invoke(label, description, aliases, whereId, whereLanguage)
            ?: throw MockError.MissingStub("Missing Sideeffect updateTerm")
    }

    override fun deleteTerm(whereId: String, whereLanguage: String) {
        return deleteTerm?.invoke(whereId, whereLanguage)
            ?: throw MockError.MissingStub("Missing Sideeffect deleteTerm")
    }

    override fun selectEntityById(whereId: String): Query<Entity> {
        TODO("Not yet implemented")
    }

    override fun selectMonoligualEntityById(whereId: String, whereLanguage: String): Query<SelectMonoligualEntityById> {
        TODO("Not yet implemented")
    }

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }

    override fun clear() {
        selectEntityById = null
        selectMonoligualEntityById = null
        addEntity = null
        updateEntity = null
        hasTerm = null
        addTerm = null
        updateTerm = null
        deleteTerm = null
    }
}
