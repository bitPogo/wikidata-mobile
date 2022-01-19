/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.transfer

import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import tech.antibytes.wikibase.store.user.domain.DomainContract
import kotlin.test.BeforeTest
import kotlin.test.Test

class RemoteRepositorySpec {
    private val fixture = kotlinFixture()
    private val client = MwClientStub()

    @BeforeTest
    fun setUp() {
        client.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        RemoteRepository(client) fulfils DomainContract.Repository::class
    }

    @Test
    fun `Given login is called with a Username and Password, it delegates the call to the Client and returns its result`() = runBlockingTest {
        // Given
        val expected: Boolean = fixture.fixture()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        client.authentication.login = { givenUsername, givenPassword ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword

            SuspendingFunctionWrapperStub {
                expected
            }
        }

        // When
        val actual = RemoteRepository(client).login(username, password)

        // Then
        actual mustBe expected
        capturedUsername mustBe username
        capturedPassword mustBe password
    }

    @Test
    fun `Given logout is called, it returns true`() = runBlockingTest {
        // When
        val actual = RemoteRepository(client).logout()

        // Then
        actual mustBe true
    }
}
