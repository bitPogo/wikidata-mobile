/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.Dispatchers
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class MwClientSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ClientFactory`() {
        MwClient fulfils PublicApi.ClientFactory::class
    }

    @Test
    fun `Given getInstance is called with its appropriate parameter it returns a Client`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = Dispatchers.Main

        // When
        val client = MwClient.getInstance(host, logger, connection, dispatcher)

        // Then
        client fulfils PublicApi.Client::class
    }
}
