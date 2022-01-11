/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract
import tech.antibytes.mock.core.authentication.AuthenticationRepositoryStub
import tech.antibytes.mock.core.token.MetaTokenRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

internal class AuthenticationServiceSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Service`() {
        AuthenticationService(
            AuthenticationRepositoryStub(),
            MetaTokenRepositoryStub()
        ) fulfils AuthenticationContract.Service::class
    }

    @Test
    fun `Given login with a username and password, it retrieves a Token delegates it to the Repository and returns its result`() = runBlockingTest {
        // Given
        val authRepository = AuthenticationRepositoryStub()
        val tokenRepository = MetaTokenRepositoryStub()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()
        val expected: Boolean = fixture.fixture()

        var capturedMetaTokenType: MetaTokenServiceContract.MetaTokenType? = null

        tokenRepository.fetchToken = { givenType ->
            capturedMetaTokenType = givenType
            token
        }

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        var capturedToken: String? = null

        authRepository.login = { givenUsername, givenPassword, givenToken ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword
            capturedToken = givenToken
            expected
        }

        // When
        val result = AuthenticationService(authRepository, tokenRepository).login(username, password)

        // Then
        result mustBe expected

        capturedMetaTokenType mustBe MetaTokenServiceContract.MetaTokenType.LOGIN
        capturedUsername mustBe username
        capturedPassword mustBe password
        capturedToken mustBe token
    }
}
