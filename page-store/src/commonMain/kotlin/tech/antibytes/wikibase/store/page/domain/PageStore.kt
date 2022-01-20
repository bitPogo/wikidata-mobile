/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.domain

import org.koin.core.KoinApplication
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.page.PageStoreContract

class PageStore internal constructor(
    koin: KoinApplication
) {
    companion object : PageStoreContract.PageStoreFactory {
        override fun getInstance(
            client: PublicApi.Client,
            database: PageQueries,
            producerScope: CoroutineWrapperContract.CoroutineScopeDispatcher,
            consumerScope: CoroutineWrapperContract.CoroutineScopeDispatcher
        ) {
            TODO("Not yet implemented")
        }
    }
}
