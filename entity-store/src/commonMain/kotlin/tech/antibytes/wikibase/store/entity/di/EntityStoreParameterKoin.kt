/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.di

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.domain.DomainContract

internal fun resolveEntityStoreParameterModule(
    client: PublicApi.Client,
    database: EntityQueries,
    producerScope: CoroutineScopeDispatcher,
    consumerScope: CoroutineScopeDispatcher
): Module {
    return module {
        factory { client }
        factory { database }
        factory(named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)) { producerScope }
        factory(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE)) { consumerScope }
    }
}
