/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

interface EntityStoreContract {
    val entity: Flow<EntityModelContract.MonolingualEntity>

    fun setLabel(label: String?)
    fun setDescription(label: String?)
    fun setAliases(aliases: List<String>)

    fun fetchEntity(id: String)
    fun save()
    fun rollback()
}
