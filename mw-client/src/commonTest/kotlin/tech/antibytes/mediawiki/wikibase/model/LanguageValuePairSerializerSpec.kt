/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LanguageValuePairSerializerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils KSerializer`() {
        LanguageValuePairSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has an descriptor`() {
        val descriptor = LanguageValuePairSerializer().descriptor

        descriptor.serialName mustBe "LanguageValuePair"
        descriptor.kind mustBe StructureKind.CLASS
        descriptor.elementsCount mustBe 2
    }

    @Test
    fun `Given serialize is called with a LanguageValuePair, it serializes it`() {
        // Given
        val serializer = Json {
            this.serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val pair = LanguageValuePair(
            language = fixture.fixture(),
            value = fixture.fixture()
        )

        val expected = "{\"language\":\"${pair.language}\",\"value\":\"${pair.value}\"}"

        // When
        val result = serializer.encodeToString(
            LanguageValuePairSerializer(),
            pair
        )

        // Them
        result mustBe expected
    }

    @Test
    fun `Given deserialize is called with a String, it fails due to unknown keys`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val expected = LanguageValuePair(
            language = fixture.fixture(),
            value = fixture.fixture()
        )

        val serialized = "{\"test\":\"23\",\"language\":\"${expected.language}\",\"value\":\"${expected.value}\"}"

        // Then
        assertFailsWith<Exception> {
            // When
            serializer.decodeFromString(
                LanguageValuePairSerializer(),
                serialized
            )
        }
    }

    @Test
    fun `Given deserialize is called with a String, it fails due to missing keys`() {
        // Given
        val serializer = Json {
            serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val expected = LanguageValuePair(
            language = fixture.fixture(),
            value = fixture.fixture()
        )

        val serialized = "{\"value\":\"${expected.value}\"}"

        // Then
        assertFailsWith<Exception> {
            // When
            serializer.decodeFromString(
                LanguageValuePairSerializer(),
                serialized
            )
        }
    }

    @Test
    fun `Given deserialize is called with a String, it deserialize it to a LanguageValuePair`() {
        // Given
        val serializer = Json {
            this.serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val expected = LanguageValuePair(
            language = fixture.fixture(),
            value = fixture.fixture()
        )

        val serialized = "{\"language\":\"${expected.language}\",\"value\":\"${expected.value}\"}"

        // When
        val result = serializer.decodeFromString(
            LanguageValuePairSerializer(),
            serialized
        )

        // Them
        result mustBe expected
    }
}
