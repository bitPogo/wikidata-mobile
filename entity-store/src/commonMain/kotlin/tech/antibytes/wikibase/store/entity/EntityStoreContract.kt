/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity

import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

interface EntityStoreContract {
    interface EntityStore {
        val entity: SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>

        fun setLabel(label: String?)
        fun setDescription(label: String?)
        fun setAliases(aliases: List<String>)

        fun fetchEntity(id: String)
        fun save()
        fun rollback()
    }

    interface EntityStoreFactory {
        fun getInstance(
            client: PublicApi.Client,
            database: EntityQueries,
            producerScope: CoroutineScopeDispatcher,
            consumerScope: CoroutineScopeDispatcher
        ): EntityStore
    }
}
