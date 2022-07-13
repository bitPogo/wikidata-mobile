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
import tech.antibytes.mediawiki.MwClient
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class MwClientIntegrationSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It has a AuthentificationSerivce`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val service = MwClient.getInstance(host, logger, connection, dispatcher).authentication

        // Then
        service fulfils PublicApi.AuthenticationService::class
    }

    @Test
    fun `It has a PageSerivce`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val service = MwClient.getInstance(host, logger, connection, dispatcher).page

        // Then
        service fulfils PublicApi.PageService::class
    }

    @Test
    fun `It has a WikibaseSerivce`() {
        // Given
        val logger = LoggerStub()
        val host: String = fixture.fixture()
        val connection = ConnectivityManagerStub()
        val dispatcher = { CoroutineScope(Dispatchers.Main) }

        // When
        val service = MwClient.getInstance(host, logger, connection, dispatcher).wikibase

        // Then
        service fulfils PublicApi.WikibaseService::class
    }
}
