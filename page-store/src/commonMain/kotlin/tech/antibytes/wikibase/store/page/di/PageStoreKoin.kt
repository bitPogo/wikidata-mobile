/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.di

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.domain.model.EntityId
import tech.antibytes.wikibase.store.page.domain.model.PageModelContract
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper as FlowFactory

internal fun resolvePageStoreModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<SharedFlowWrapperFactory> {
            FlowFactory
        }

        single(
            named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
        ) {
            MutableSharedFlow<ResultContract<EntityId, Exception>>()
        }

        factory<SharedFlowWrapper<EntityId, Exception>>(
            named(DomainContract.DomainKoinIds.EXTERNAL_RANDOM_FLOW)
        ) {
            get<SharedFlowWrapperFactory>().getInstance(
                get<MutableSharedFlow<ResultContract<EntityId, Exception>>>(
                    named(DomainContract.DomainKoinIds.INTERNAL_RANDOM_FLOW)
                ),
                get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))
            )
        }

        single(
            named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
        ) {
            MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>()
        }


        factory<SharedFlowWrapper<List<PageModelContract.SearchEntry>, Exception>>(
            named(DomainContract.DomainKoinIds.EXTERNAL_SEARCH_FLOW)
        ) {
            get<SharedFlowWrapperFactory>().getInstance(
                get<MutableSharedFlow<ResultContract<List<PageModelContract.SearchEntry>, Exception>>>(
                    named(DomainContract.DomainKoinIds.INTERNAL_SEARCH_FLOW)
                ),
                get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))
            )
        }
    }
}
