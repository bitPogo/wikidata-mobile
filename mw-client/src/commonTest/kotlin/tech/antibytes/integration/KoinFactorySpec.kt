/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.integration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mediawiki.di.initKoin
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.isNot
import kotlin.test.Test

class KoinFactorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `Given initKoin with its appropriate Parameter is called it returns a KoinApplication, which contains a AuthenticationService`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val koin = initKoin(
            logger,
            host,
            connection,
            dispatcher
        )

        val sut: PublicApi.AuthenticationService = koin.koin.get()

        sut isNot null
    }

    @Test
    fun `Given initKoin with its appropriate Parameter is called it returns a KoinApplication, which contains a PageService`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val koin = initKoin(
            logger,
            host,
            connection,
            dispatcher
        )

        val sut: PublicApi.PageService = koin.koin.get()

        sut isNot null
    }

    @Test
    fun `Given initKoin with its appropriate Parameter is called it returns a KoinApplication, which contains a WikibaseService`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val koin = initKoin(
            logger,
            host,
            connection,
            dispatcher
        )

        val sut: PublicApi.WikibaseService = koin.koin.get()

        sut isNot null
    }
}
