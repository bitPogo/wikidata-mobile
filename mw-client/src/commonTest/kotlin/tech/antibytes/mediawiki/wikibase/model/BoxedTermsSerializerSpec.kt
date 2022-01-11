/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.wikibase.model

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.error.MissingPropertyException
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BoxedTermsSerializerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils KSerlializer`() {
        BoxedTermsSerializer() fulfils KSerializer::class
    }

    @Test
    fun `It has an descriptor`() {
        val descriptor = BoxedTermsSerializer().descriptor

        descriptor.serialName mustBe "BoxedTerms"
        descriptor.kind mustBe StructureKind.CLASS
        descriptor.elementsCount mustBe 3
    }

    @Test
    fun `Given serialize is called with a BoxedTerms, it serializes it`() {
        // Given
        val serializer = Json {
            this.serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val entity = TestEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityTypes.ITEM,
            revisionId = fixture.fixture(),
            lastModification = Instant.DISTANT_FUTURE,
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            )
        )

        val expected = "{" +
            "\"labels\":{\"${entity.labels.keys.first()}\":{\"language\":\"${entity.labels.values.first().language}\",\"value\":\"${entity.labels.values.first().value}\"}}," +
            "\"descriptions\":{\"${entity.descriptions.keys.first()}\":{\"language\":\"${entity.descriptions.values.first().language}\",\"value\":\"${entity.descriptions.values.first().value}\"}}," +
            "\"aliases\":{\"${entity.aliases.keys.first()}\":[{\"language\":\"${entity.aliases.values.first().first().language}\",\"value\":\"${entity.aliases.values.first().first().value}\"}]}" +
        "}"

        // When
        val result = serializer.encodeToString(
            BoxedTermsSerializer(),
            entity
        )

        // Them
        result mustBe expected
    }

    @Test
    fun `Given serialize is called with a BoxedTerms, it allows empty Aliases it`() {
        // Given
        val serializer = Json {
            this.serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val entity = TestEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityTypes.ITEM,
            revisionId = fixture.fixture(),
            lastModification = Instant.DISTANT_FUTURE,
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to emptyList()
            )
        )

        val expected = "{" +
            "\"labels\":{\"${entity.labels.keys.first()}\":{\"language\":\"${entity.labels.values.first().language}\",\"value\":\"${entity.labels.values.first().value}\"}}," +
            "\"descriptions\":{\"${entity.descriptions.keys.first()}\":{\"language\":\"${entity.descriptions.values.first().language}\",\"value\":\"${entity.descriptions.values.first().value}\"}}," +
            "\"aliases\":{\"${entity.aliases.keys.first()}\":[]}" +
            "}"

        // When
        val result = serializer.encodeToString(
            BoxedTermsSerializer(),
            entity
        )

        // Them
        result mustBe expected
    }

    @Test
    fun `Given deserialize is called with a String, it fails, since it is not implemented`() {
        // Given
        val serializer = Json {
            this.serializersModule = SerializersModule {
                this.contextual(
                    kClass = DataModelContract.LanguageValuePair::class,
                    serializer = LanguageValuePairSerializer()
                )
            }
        }

        val entity = TestEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityTypes.ITEM,
            revisionId = fixture.fixture(),
            lastModification = Instant.DISTANT_FUTURE,
            labels = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                fixture.fixture<String>() to emptyList()
            )
        )

        val serializedEntity = "{" +
            "\"labels\":{\"${entity.labels.keys.first()}\":{\"language\":\"${entity.labels.values.first().language}\",\"value\":\"${entity.labels.values.first().value}\"}}," +
            "\"descriptions\":{\"${entity.descriptions.keys.first()}\":{\"language\":\"${entity.descriptions.values.first().language}\",\"value\":\"${entity.descriptions.values.first().value}\"}}," +
            "\"aliases\":{\"${entity.aliases.keys.first()}\":[]}" +
        "}"

        // Then
        assertFailsWith<NotImplementedError> {
            // When
            serializer.decodeFromString(
                BoxedTermsSerializer(),
                serializedEntity
            )
        }
    }
}

private data class TestEntity(
    override val labels: Map<String, DataModelContract.LanguageValuePair>,
    override val descriptions: Map<String, DataModelContract.LanguageValuePair>,
    override val aliases: Map<String, List<DataModelContract.LanguageValuePair>>,
    override val id: EntityId,
    override val type: DataModelContract.EntityTypes,
    override val revisionId: Long,
    override val lastModification: Instant
) : DataModelContract.RevisionedEntity
