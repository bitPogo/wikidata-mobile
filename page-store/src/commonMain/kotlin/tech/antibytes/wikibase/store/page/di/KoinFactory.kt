/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.di

import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.page.transfer.resolveDataTransferModule

internal fun initKoin(
    client: PublicApi.Client,
    database: PageQueries,
    producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
    consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
): KoinApplication {
    return koinApplication {
        modules(
            resolveEntityStoreParameterModule(
                client,
                database,
                producerScope,
                consumerScope
            ),
            resolveDataTransferModule(),
            resolvePageStoreModule()
        )
    }
}
