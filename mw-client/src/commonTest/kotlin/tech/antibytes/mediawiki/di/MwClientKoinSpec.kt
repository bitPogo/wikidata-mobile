/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.di

import io.ktor.client.features.cookies.CookiesStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.MwClientContract
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.serialization.JsonConfiguratorContract
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.SuspendingFunctionWrapperFactoryStub
import tech.antibytes.mock.serialization.JsonConfiguratorStub
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.isNot
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class MwClientKoinSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given resolveMwClientModule is called, it contains a Clock`() {
        // When
        val koin = koinApplication {
            modules(
                resolveMwClientModule()
            )
        }

        val clock: Clock = koin.koin.get()

        // Then
        clock isNot null
    }

    @Test
    fun `Given resolveMwClientModule is called, it contains a JsonConfigurator`() {
        // When
        val koin = koinApplication {
            modules(
                resolveMwClientModule()
            )
        }

        val configurator: JsonConfiguratorContract = koin.koin.get()

        // Then
        configurator isNot null
    }

    @Test
    fun `Given resolveMwClientModule is called, it creates a Module, it invokes the JsonConfigurator to build the Serializer`() {
        // Given
        val configurator = JsonConfiguratorStub()

        val indicator: String = fixture.fixture()
        configurator.configure = { json ->
            json.classDiscriminator = indicator
            json
        }

        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveMwClientModule(),
                module {
                    single<JsonConfiguratorContract> { configurator }
                }
            )
        }

        val serializer: Json = koin.koin.get()

        // Then
        serializer isNot null
        serializer.configuration.classDiscriminator mustBe indicator
    }

    @Test
    fun `Given resolveMwClientModule is called, it creates a Module, which contains a CookiesStorage`() {
        // When
        val koin = koinApplication {
            modules(
                resolveMwClientModule(),
            )
        }

        val storage: CookiesStorage = koin.koin.get()

        // Then
        storage isNot null
    }

    @Test
    fun `Given resolveMwClientModule is called, it creates a Module, which contains a SuspendingFunctionWrapperFactory`() {
        // When
        val koin = koinApplication {
            modules(
                resolveMwClientModule(),
            )
        }

        val wrapperFactory: CoroutineWrapperContract.SuspendingFunctionWrapperFactory = koin.koin.get()

        // Then
        wrapperFactory isNot null
    }

    @Test
    fun `Given resolveMwClientModule is called, it creates a Module, which contains a ServiceResponseWrapper`() {
        // When
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveMwClientModule(),
                module {
                    single<PublicApi.ConnectivityManager> {
                        ConnectivityManagerStub()
                    }

                    single<CoroutineWrapperContract.SuspendingFunctionWrapperFactory> {
                        SuspendingFunctionWrapperFactoryStub()
                    }

                    single<CoroutineWrapperContract.CoroutineScopeDispatcher> {
                        CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }
                    }
                }
            )
        }

        val wrapper: MwClientContract.ServiceResponseWrapper = koin.koin.get()

        // Then
        wrapper isNot null
    }
}
