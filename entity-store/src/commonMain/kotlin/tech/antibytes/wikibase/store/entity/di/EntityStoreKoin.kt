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
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper as FlowFactory

internal fun resolveEntityStoreModule(): Module {
    return module {
        single {
            MutableSharedFlow<ResultContract<MonolingualEntity, Exception>>()
        }

        @InternalKoinModuleScope
        factory<SharedFlowWrapperFactory> {
            FlowFactory
        }

        factory<SharedFlowWrapper<MonolingualEntity, Exception>> {
            get<SharedFlowWrapperFactory>().getInstance(
                get<MutableSharedFlow<ResultContract<MonolingualEntity, Exception>>>(),
                get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))
            )
        }
    }
}
