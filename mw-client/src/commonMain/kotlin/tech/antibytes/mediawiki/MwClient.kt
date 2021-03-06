/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import org.koin.core.KoinApplication
import tech.antibytes.mediawiki.di.initKoin
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

class MwClient internal constructor(
    koin: KoinApplication
) : PublicApi.Client {
    override val authentication: PublicApi.AuthenticationService by koin.koin.inject()
    override val page: PublicApi.PageService by koin.koin.inject()
    override val wikibase: PublicApi.WikibaseService by koin.koin.inject()

    companion object : PublicApi.ClientFactory {
        override fun getInstance(
            host: String,
            logger: PublicApi.Logger,
            connection: PublicApi.ConnectivityManager,
            dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher
        ): PublicApi.Client {
            return MwClient(
                initKoin(
                    logger,
                    host,
                    connection,
                    dispatcher
                )
            )
        }
    }
}
