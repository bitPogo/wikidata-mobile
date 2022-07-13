/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication

import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.core.token.MetaTokenContract
import tech.antibytes.mock.ServiceResponseWrapperStub
import tech.antibytes.mock.core.authentication.AuthenticationRepositoryStub
import tech.antibytes.mock.core.token.MetaTokenRepositoryStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

internal class AuthenticationServiceSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Service`() {
        AuthenticationService(
            AuthenticationRepositoryStub(),
            MetaTokenRepositoryStub(),
            ServiceResponseWrapperStub()
        ) fulfils AuthenticationContract.Service::class
    }

    @Test
    fun `Given login with a username and password, it retrieves a Token delegates it to the Repository and returns its result`() = runBlockingTest {
        // Given
        val authRepository = AuthenticationRepositoryStub()
        val tokenRepository = MetaTokenRepositoryStub()
        val serviceWrapper = ServiceResponseWrapperStub()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val token: String = fixture.fixture()
        val expected: Boolean = fixture.fixture()

        var capturedMetaTokenType: MetaTokenContract.MetaTokenType? = null

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
        val result = AuthenticationService(
            authRepository,
            tokenRepository,
            serviceWrapper
        ).login(username, password)

        // Then
        result.wrappedFunction.invoke() mustBe expected

        serviceWrapper.lastFunction sameAs result.wrappedFunction
        capturedMetaTokenType mustBe MetaTokenContract.MetaTokenType.LOGIN
        capturedUsername mustBe username
        capturedPassword mustBe password
        capturedToken mustBe token
    }
}
