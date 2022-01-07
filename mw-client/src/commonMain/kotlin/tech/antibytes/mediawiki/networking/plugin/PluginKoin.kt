/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.mediawiki.networking.NetworkingContract

internal fun resolveKtorPluginsModule(): Module {
    return module {
        factory {
            listOf(
                NetworkingContract.Plugin(
                    JsonFeature,
                    SerializerConfigurator(),
                    get()
                ),
                NetworkingContract.Plugin(
                    Logging,
                    LoggingConfigurator(),
                    get()
                ),
                NetworkingContract.Plugin(
                    HttpCallValidator,
                    ResponseValidatorConfigurator(),
                    get()
                ),
                NetworkingContract.Plugin(
                    HttpCookies,
                    CookieStorageConfigurator(),
                    get()
                )
            )
        }
    }
}
