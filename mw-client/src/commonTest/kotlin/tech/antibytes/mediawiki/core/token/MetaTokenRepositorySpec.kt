/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mediawiki.core.token.model.MetaTokenResponse
import tech.antibytes.mediawiki.core.token.model.Query
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mock.core.token.MetaTokenApiServiceStub
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
        MetaTokenRepository(MetaTokenApiServiceStub()) fulfils MetaTokenContract.Repository::class
    }

    @Test
    fun `Given fetchToken is called with a TokenType it fails due to the missing field for the MetaToken`() = runBlockingTest {
        // Given
        val type = MetaTokenContract.MetaTokenType.CSRF
        val response = MetaTokenResponse(
            query = Query(emptyMap())
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
        val type = MetaTokenContract.MetaTokenType.CSRF
        val expected: String = fixture.fixture()
        val response = MetaTokenResponse(
            query = Query(
                mapOf(
                    type to expected
                )
            )
        )

        var capturedType: MetaTokenContract.MetaTokenType? = null
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
