/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import ClientConfiguratorStub
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mock.networking.PluginConfiguratorStub
import tech.antibytes.mock.networking.FeatureStub
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class NetworkingKoinSpec {
    private val fixture = kotlinFixture()
    
    @Test
    fun `Given resolveHttpClientModule is called it creates a Module, which contains a plain HttpClient`() {
        // When
        val koin = koinApplication {
            modules(
                resolveHttpClientModule()
            )
        }

        val client: HttpClient = koin.koin.get(named(NetworkingContract.KoinIdentifier.PLAIN_CLIENT))

        // Then
        client isNot null
    }

    @Test
    fun `Given resolveHttpClientModule is called it creates a Module, which contains a ClientConfigurator`() {
        // When
        val koin = koinApplication {
            modules(
                resolveHttpClientModule()
            )
        }

        val client: NetworkingContract.ClientConfigurator = koin.koin.get()

        // Then
        client isNot null
    }

    @Test
    fun `Given resolveHttpClientModule is called, it creates a Module, which assembles a configured HttpClient, while no Plugins are given`() {
        // Given
        val clientConfigurator = ClientConfiguratorStub()
        var capturedHttpConfig: HttpClientConfig<*>? = null
        var capturedPlugins: Set<NetworkingContract.Plugin<in Any, in Any>>? = null

        clientConfigurator.configure = { delegatedHttpConfig, delegatedPlugins ->
            capturedHttpConfig = delegatedHttpConfig
            capturedPlugins = delegatedPlugins
        }

        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveHttpClientModule(),
                module {
                    single<NetworkingContract.ClientConfigurator> {
                        clientConfigurator
                    }
                }
            )
        }

        val client: HttpClient = koin.koin.get(named(NetworkingContract.KoinIdentifier.CONFIGURED_CLIENT))

        // Then
        client isNot null
        capturedHttpConfig!! fulfils HttpClientConfig::class

        capturedPlugins mustBe null
    }

    @Test
    fun `Given resolveHttpClientModule is called, it creates a Module, which assembles a configured HttpClient, while Plugins are given`() {
        // Given
        val clientConfigurator = ClientConfiguratorStub()
        val features = setOf(
            NetworkingContract.Plugin(
                FeatureStub,
                PluginConfiguratorStub<Any, Any>(),
                fixture.fixture<String>()
            )
        )
        var capturedHttpConfig: HttpClientConfig<*>? = null
        var capturedPlugins: Set<NetworkingContract.Plugin<in Any, in Any>>? = null

        clientConfigurator.configure = { delegatedHttpConfig, delegatedPlugins ->
            capturedHttpConfig = delegatedHttpConfig
            capturedPlugins = delegatedPlugins
        }

        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveHttpClientModule(),
                module {
                    single<NetworkingContract.ClientConfigurator> {
                        clientConfigurator
                    }
                    single<Set<NetworkingContract.Plugin<out Any, out Any?>>> {
                        features
                    }
                }
            )
        }
        // Then
        val client: HttpClient = koin.koin.get(named(NetworkingContract.KoinIdentifier.CONFIGURED_CLIENT))
        client isNot null

        capturedHttpConfig!! fulfils HttpClientConfig::class
        capturedPlugins sameAs features
    }

    @Test
    fun `Given resolveHttpClientModule is called it creates a Module, which contains a RequestBuilder`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveHttpClientModule(),
                module {
                    single(named(NetworkingContract.KoinIdentifier.CONFIGURED_CLIENT)) {
                        HttpClient(MockEngine) {
                            engine {
                                addHandler {
                                    respond(fixture.fixture<String>())
                                }
                            }
                        }
                    }

                    single(named(NetworkingContract.KoinIdentifier.HOST)) {
                        fixture.fixture<String>()
                    }
                }
            )
        }

        // Then
        val builder: NetworkingContract.RequestBuilder = koin.koin.get()
        builder isNot null
    }
}
