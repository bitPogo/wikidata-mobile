/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.wikidata.lib.qr.domain.DomainContract

internal fun resolveQrCodeStoreModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<CoroutineWrapperContract.SharedFlowWrapperFactory> {
            SharedFlowWrapper
        }

        single<MutableSharedFlow<ResultContract<Bitmap, Exception>>> { MutableSharedFlow() }

        factory<CoroutineWrapperContract.SharedFlowWrapper<Bitmap, Exception>> {
            get<CoroutineWrapperContract.SharedFlowWrapperFactory>().getInstance(
                get<MutableSharedFlow<ResultContract<Bitmap, Exception>>>(),
                get(named(DomainContract.DomainKoinIds.CONSUMER_SCOPE))
            )
        }
    }
}
