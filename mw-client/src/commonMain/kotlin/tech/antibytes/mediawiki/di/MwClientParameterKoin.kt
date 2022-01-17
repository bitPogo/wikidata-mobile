/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract

internal fun resolveMwClientParameterModule(
    logger: PublicApi.Logger,
    host: String,
    connectivityManager: PublicApi.ConnectivityManager,
    dispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher
): Module {
    return module {
        single { logger }
        single(named(NetworkingContract.KoinIdentifier.HOST)) { host }
        single { connectivityManager }
        single { dispatcher }
    }
}
