/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.networking.NetworkingContract
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class MwClientParameterKoinSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given resolveMwClientParameterModule is called with its appropriate Parameter it holds a Logger`() {
        // Given
        val logger = LoggerStub()

        // When
        val koin = koinApplication {
            modules(
                resolveMwClientParameterModule(
                    logger,
                    fixture.fixture(),
                    ConnectivityManagerStub(),
                    { CoroutineScope(Dispatchers.Default) }
                ),
            )
        }

        val sut: PublicApi.Logger = koin.koin.get()

        sut isNot null
    }

    @Test
    fun `Given resolveMwClientParameterModule is called with its appropriate Parameter it holds a Host`() {
        // Given
        val host: String = fixture.fixture()

        // When
        val koin = koinApplication {
            modules(
                resolveMwClientParameterModule(
                    LoggerStub(),
                    host,
                    ConnectivityManagerStub(),
                    { CoroutineScope(Dispatchers.Default) }
                ),
            )
        }

        val sut: String = koin.koin.get(named(NetworkingContract.KoinIdentifier.HOST))

        sut isNot null
    }

    @Test
    fun `Given resolveMwClientParameterModule is called with its appropriate Parameter it holds a ConnectityManager`() {
        // Given
        val connection = ConnectivityManagerStub()

        // When
        val koin = koinApplication {
            modules(
                resolveMwClientParameterModule(
                    LoggerStub(),
                    fixture.fixture(),
                    connection,
                    { CoroutineScope(Dispatchers.Default) }
                ),
            )
        }

        val sut: PublicApi.ConnectivityManager = koin.koin.get()

        sut isNot null
    }

    @Test
    fun `Given resolveMwClientParameterModule is called with its appropriate Parameter it holds a CoroutineScopeDispatcher`() {
        // Given
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val koin = koinApplication {
            modules(
                resolveMwClientParameterModule(
                    LoggerStub(),
                    fixture.fixture(),
                    ConnectivityManagerStub(),
                    dispatcher
                ),
            )
        }

        val sut: CoroutineWrapperContract.CoroutineScopeDispatcher = koin.koin.get()

        sut isNot null
    }
}
