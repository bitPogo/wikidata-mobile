/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope

fun resolveHttpClientModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory(named(NetworkingContract.KoinIdentifier.PLAIN_CLIENT)) { HttpClient() }
        @InternalKoinModuleScope
        factory<NetworkingContract.ClientConfigurator> { ClientConfigurator() }
        @InternalKoinModuleScope
        factory(named(NetworkingContract.KoinIdentifier.CONFIGURED_CLIENT)) {
            get<HttpClient>(named(NetworkingContract.KoinIdentifier.PLAIN_CLIENT)).config {
                get<NetworkingContract.ClientConfigurator>().configure(
                    this,
                    getOrNull()
                )
            }
        }
        factory<NetworkingContract.RequestBuilder> {
            RequestBuilder(
                get(named(NetworkingContract.KoinIdentifier.CONFIGURED_CLIENT)),
                get(named(NetworkingContract.KoinIdentifier.HOST)),
                port = getOrNull(named(NetworkingContract.KoinIdentifier.PORT))
            )
        }
    }
}
