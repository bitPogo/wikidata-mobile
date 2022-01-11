/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking.plugin

import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.cookies.CookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mediawiki.serialization.JsonConfiguratorContract
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.networking.plugin.CookieStorageStub
import tech.antibytes.mock.networking.plugin.ErrorMapperStub
import tech.antibytes.mock.serialization.JsonConfiguratorStub
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class PluginKoinSpec {
    @Test
    fun `Given resolveKtorPluginsModule is called it creates a Module, which contains a List of Plugin`() {
        // Given
        val logger = LoggerStub()
        val mapper = ErrorMapperStub()
        val serializer = JsonConfiguratorStub()
        val cookieStorage = CookieStorageStub()

        // When
        val koin = koinApplication {
            modules(
                resolveKtorPluginsModule(),
                module {
                    single<PublicApi.Logger> { logger }
                    single<KtorPluginsContract.ErrorMapper> { mapper }
                    single<JsonConfiguratorContract> { serializer }
                    single<CookiesStorage> { cookieStorage }
                }
            )
        }

        val configuration = koin.koin.get<Set<NetworkingContract.Plugin<in Any, in Any?>>>()

        // Then
        configuration.size mustBe 4

        configuration.forEach { plugin ->
            when (plugin.subConfiguration) {
                is PublicApi.Logger -> {
                    plugin.subConfiguration sameAs logger
                    plugin.feature sameAs Logging
                }
                is KtorPluginsContract.ErrorMapper -> {
                    plugin.subConfiguration sameAs mapper
                    plugin.feature sameAs HttpCallValidator
                }
                is JsonConfiguratorContract -> {
                    plugin.subConfiguration sameAs serializer
                    plugin.feature sameAs JsonFeature
                }
                else -> {
                    plugin.subConfiguration!! sameAs cookieStorage
                    plugin.feature sameAs HttpCookies
                }
            }
        }
    }
}
