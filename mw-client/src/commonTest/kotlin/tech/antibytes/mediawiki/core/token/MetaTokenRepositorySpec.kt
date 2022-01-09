/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mock.token.MetaTokenApiServiceStub
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MetaTokenRepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Repository`() {
        MetaTokenRepository(MetaTokenApiServiceStub()) fulfils MetaTokenServiceContract.Repository::class
    }

    @Test
    fun `Given fetchToken is called with a TokenType it fails due to the missing field for the MetaToken`() = runBlockingTest {
        // Given
        val type = MetaTokenServiceContract.TokenTypes.CSRF
        val response = MetaTokenResponse(
            query = emptyMap()
        )

        val apiService = MetaTokenApiServiceStub { response }

        // Then
        val error = assertFailsWith<MwClientError.InternalFailure> {
            MetaTokenRepository(apiService).fetchToken(type)
        }

        error.message mustBe "Missing Token (${type.value})"
    }

    @Test
    fun `Given fetchToken is called with a TokenType it returns a MetaToken`() = runBlockingTest {
        // Given
        val type = MetaTokenServiceContract.TokenTypes.CSRF
        val expected: String = fixture.fixture()
        val response = MetaTokenResponse(
            query = mapOf(
                type to expected
            )
        )

        var capturedType: MetaTokenServiceContract.TokenTypes? = null
        val apiService = MetaTokenApiServiceStub { givenType ->
            capturedType = givenType
            response
        }

        // When
        val token = MetaTokenRepository(apiService).fetchToken(type)

        // Then
        token mustBe expected
        capturedType mustBe type
    }
}
