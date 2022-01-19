/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import tech.antibytes.mock.networking.FeatureStub
import tech.antibytes.mock.networking.PluginConfiguratorStub
import tech.antibytes.util.test.coroutine.runBlockingTestInContext
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.BeforeTest
import kotlin.test.Test

class ClientConfiguratorSpec {
    private val fixture = kotlinFixture()

    @BeforeTest
    fun setUp() {
        Counter.amount = 0
    }

    @Test
    fun `It fulfils ClientConfigurator`() {
        ClientConfigurator() fulfils NetworkingContract.ClientConfigurator::class
    }

    @Test
    fun `Given configure is called with a ClientConfigurator and a Set of Plugin it installs a given Plugin`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val capturedPluginConfig = Channel<Any>()
        val capturedSubConfig = Channel<Any>()

        val subConfig: String = fixture.fixture()
        val pluginConfigurator = PluginConfiguratorStub<Any, Any?>()

        pluginConfigurator.configure = { pluginConfig, subConfiguration ->
            launch {
                capturedPluginConfig.send(pluginConfig)
                capturedSubConfig.send(subConfiguration!!)
            }
        }

        val features = setOf(
            NetworkingContract.Plugin(
                FeatureStub,
                pluginConfigurator,
                subConfig,
            )
        )

        // When
        HttpClient(MockEngine) {
            ClientConfigurator().configure(
                this,
                features
            )

            engine {
                addHandler {
                    respond(fixture.fixture<String>())
                }
            }
        }

        // Then
        withTimeout(2000) {
            capturedPluginConfig.receive() fulfils FeatureStub.Config::class
            capturedSubConfig.receive() mustBe subConfig
        }
    }

    @Test
    fun `Given configure is called with a ClientConfigurator and a List of HttpFeatureInstaller it installs a arbitrary number of Plugins`() = runBlockingTestInContext(GlobalScope.coroutineContext) {
        // Given
        val subConfig = object {}
        val pluginConfigurator = PluginConfiguratorStub<Any, Any?>()

        pluginConfigurator.configure = { _, _ ->
            Counter.amount++
        }

        val features = setOf(
            NetworkingContract.Plugin(
                FeatureStub,
                pluginConfigurator,
                subConfig,
            ),
            NetworkingContract.Plugin(
                FeatureStub,
                pluginConfigurator,
                subConfig,
            ),
            NetworkingContract.Plugin(
                FeatureStub,
                pluginConfigurator,
                subConfig,
            )
        )

        // When
        HttpClient(MockEngine) {
            ClientConfigurator().configure(
                this,
                features
            )

            engine {
                addHandler {
                    respond(fixture.fixture<String>())
                }
            }
        }

        // Then
        Counter.amount mustBe features.size
    }

    @ThreadLocal
    private object Counter {
        var amount = 0
    }
}
