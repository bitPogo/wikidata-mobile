/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikidata.lib.qr.data.resolveQrCodeDataServiceModule

internal fun initKoin(
    database: QrCodeQueries,
    producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
    consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
): KoinApplication {
    return koinApplication {
        modules(
            resolveQrCodeStoreParameterModule(
                database,
                producerScope,
                consumerScope
            ),
            resolveQrCodeStoreModule(),
            resolveQrCodeDataServiceModule()
        )
    }
}
