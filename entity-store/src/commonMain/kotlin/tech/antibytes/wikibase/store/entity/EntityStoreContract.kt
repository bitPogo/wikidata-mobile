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
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

interface EntityStoreContract {
    interface EntityStore {
        val entity: SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>

        fun fetchEntity(id: EntityId, language: LanguageTag)
        fun refresh()

        fun setLabel(label: String)
        fun setDescription(description: String)
        fun setAlias(index: Int, alias: String)
        fun setAliases(aliases: List<String>)

        fun create(
            language: LanguageTag,
            type: EntityModelContract.EntityType
        )

        fun save()
        fun rollback()
        fun reset()
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
