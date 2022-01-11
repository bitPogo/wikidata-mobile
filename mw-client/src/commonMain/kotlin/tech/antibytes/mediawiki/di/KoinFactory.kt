/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.di

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.core.authentication.resolveAuthenticationModule
import tech.antibytes.mediawiki.core.page.resolvePageModule
import tech.antibytes.mediawiki.core.token.resolveMetaTokenModule
import tech.antibytes.mediawiki.networking.plugin.resolveKtorPluginsModule
import tech.antibytes.mediawiki.networking.resolveHttpClientModule
import tech.antibytes.mediawiki.wikibase.resolveWikibaseModule

internal fun initKoin(
    logger: PublicApi.Logger,
    host: String,
    connectivityManager: PublicApi.ConnectivityManager,
    dispatcher: CoroutineDispatcher
): KoinApplication {
    return koinApplication {
        modules(
            resolveKtorPluginsModule(),
            resolveHttpClientModule(),
            resolveMwClientModule(),
            resolveMetaTokenModule(),
            resolveAuthenticationModule(),
            resolvePageModule(),
            resolveWikibaseModule(),
            resolveMwClientParameterModule(logger, host, connectivityManager, dispatcher),
        )
    }
}
