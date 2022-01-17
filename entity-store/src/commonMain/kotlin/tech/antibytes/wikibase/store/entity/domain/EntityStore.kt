/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.di.initKoin
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper as FlowWrapperFactory

class EntityStore internal constructor(
    private val koin: KoinApplication,
): EntityStoreContract.EntityStore {
    override val entity: SharedFlowWrapper<MonolingualEntity, Exception> = koin.koin.get()

    companion object : EntityStoreContract.EntityStoreFactory {
        override fun getInstance(
            client: PublicApi.Client,
            database: EntityQueries,
            producerScope: CoroutineScopeDispatcher,
            consumerScope: CoroutineScopeDispatcher
        ): EntityStoreContract.EntityStore {
            val koin = initKoin(
                client,
                database,
                producerScope,
                consumerScope
            )

            return EntityStore(koin)
        }
    }

    override fun setLabel(label: String?) {
        TODO("Not yet implemented")
    }

    override fun setDescription(label: String?) {
        TODO("Not yet implemented")
    }

    override fun setAliases(aliases: List<String>) {
        TODO("Not yet implemented")
    }

    override fun fetchEntity(id: String) {
        TODO("Not yet implemented")
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun rollback() {
        TODO("Not yet implemented")
    }
}
