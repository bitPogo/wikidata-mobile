/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.di

import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapperFactory
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper as FlowFactory

internal fun resolveEntityStoreModule(): Module {
    return module {
        single {
            MutableStateFlow<ResultContract<MonolingualEntity, Exception>>(
                Failure(EntityStoreError.InitialState())
            )
        }

        @InternalKoinModuleScope
        factory<SharedFlowWrapperFactory> {
            FlowFactory
        }

        factory<SharedFlowWrapper<MonolingualEntity, Exception>> {
            get<SharedFlowWrapperFactory>().getInstance(
                get<MutableStateFlow<ResultContract<MonolingualEntity, Exception>>>(),
                get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))
            )
        }
    }
}
