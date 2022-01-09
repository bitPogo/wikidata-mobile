/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.user

import tech.antibytes.mediawiki.core.user.model.ClientLogin
import tech.antibytes.mediawiki.core.user.model.LoginResponse
import tech.antibytes.mediawiki.core.user.model.LoginStatus
import tech.antibytes.mock.user.UserApiServiceStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class UserRepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Repository`() {
        UserRepository(UserApiServiceStub()) fulfils UserContract.Repository::class
    }

    @Test
    fun `Given login is called Username, Password and a Token, it returns true if the ResponseStatus is PASS`() = runBlockingTest {
        // Given
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()

        val response = LoginResponse(
            ClientLogin(
                LoginStatus.PASS
            )
        )

        val apiService = UserApiServiceStub()

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        var capturedToken: String? = null

        apiService.login = { givenUsername, givenPassword, givenToken ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword
            capturedToken = givenToken
            response
        }

        // When
        val result = UserRepository(apiService).login(username, password, token)

        // Then
        result mustBe true
        capturedUsername mustBe username
        capturedPassword mustBe password
        capturedToken mustBe token
    }

    @Test
    fun `Given login is called Username, Password and a Token, it returns false if the ResponseStatus is not PASS`() = runBlockingTest {
        // Given
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()

        val response = LoginResponse(
            ClientLogin(
                LoginStatus.RESTART
            )
        )

        val apiService = UserApiServiceStub()

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        var capturedToken: String? = null

        apiService.login = { givenUsername, givenPassword, givenToken ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword
            capturedToken = givenToken
            response
        }

        // When
        val result = UserRepository(apiService).login(username, password, token)

        // Then
        result mustBe false
        capturedUsername mustBe username
        capturedPassword mustBe password
        capturedToken mustBe token
    }
}
