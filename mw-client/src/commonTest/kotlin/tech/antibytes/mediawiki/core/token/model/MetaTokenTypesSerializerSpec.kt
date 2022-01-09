/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json
import tech.antibytes.mediawiki.core.token.MetaTokenServiceContract
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MetaTokenTypesSerializerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils KSerializer`() {
        MetaTokenTypesSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has a descriptor`() {
        val descriptor = MetaTokenTypesSerializer().descriptor

        descriptor.serialName mustBe "TokenTypes"
        descriptor.kind mustBe PrimitiveKind.STRING
    }

    @Test
    fun `Given a Serializer is called with a ConsentSignatureType, it encodes it`() {
        // Given
        val serializer = Json

        for (field in MetaTokenServiceContract.TokenTypes.values()) {
            // When
            val result = serializer.encodeToString(
                MetaTokenTypesSerializer(),
                field
            )

            // Then
            result mustBe "\"${field.value}\""
        }
    }

    @Test
    fun `Given a Serializer is called with a serialized TokenType, it fails with a Internal Failure if type is unknown`() {
        // Given
        val serializer = Json
        val type: String = fixture.fixture()

        // Then
        val error = assertFailsWith<MwClientError.InternalFailure> {
            // When
            serializer.decodeFromString(
                MetaTokenTypesSerializer(),
                "\"$type\""
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Unknown TokenType ($type)."
        )
    }

    @Test
    fun `Given a Serializer is called with a serialized TokenType, it decodes it`() {
        // Given
        val serializer = Json

        for (field in MetaTokenServiceContract.TokenTypes.values()) {
            // When
            val result = serializer.decodeFromString(
                MetaTokenTypesSerializer(),
                "\"${field.value}\""
            )

            // Then
            result sameAs field
        }
    }
}
