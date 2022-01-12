/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.services

import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.entity.data.dto.RevisionedEntity
import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.entity.data.dto.LanguageValuePair
import tech.antibytes.wikibase.store.mock.SuspendingFunctionWrapperStub
import kotlin.test.BeforeTest
import kotlin.test.Test

class ApiServiceSpec {
    private val fixture = kotlinFixture()
    private val client = MwClientStub()

    @BeforeTest
    fun setUp() {
        client.clear()
    }

    @Test
    fun `It fulfils ApiService`() {
        EntityStoreApiService(client) fulfils ServiceContract.ApiService::class
    }

    @Test
    fun `Given fetchEntity is called with a EntityId it delegates the call to the Client and returns its result`() = runBlockingTest {
        // Given
        val id: String = fixture.fixture()
        val expectedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
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
            ),
        )

        var capturedIds: Set<String>? = null
        client.wikibase.fetchEntities = { givenIds, _ ->
            capturedIds = givenIds

            SuspendingFunctionWrapperStub(
                suspend { listOf(expectedEntity) }
            )
        }

        // When
        val result = EntityStoreApiService(client).fetchEntity(id)

        // Then
        result sameAs expectedEntity
        capturedIds mustBe setOf(id)
    }

    @Test
    fun `Given updateEntity is called with a RevisionedEntity it delegates the call to the Client and returns its result`() = runBlockingTest {
        // Given
        val expectedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
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
            ),
        )

        var capturedEntity: DataModelContract.RevisionedEntity? = null
        client.wikibase.updateEntity = { givenEntity ->
            capturedEntity = givenEntity

            SuspendingFunctionWrapperStub(
                suspend { expectedEntity }
            )
        }

        // When
        val result = EntityStoreApiService(client).updateEntity(expectedEntity)

        // Then
        result sameAs expectedEntity
        capturedEntity mustBe expectedEntity
    }

    @Test
    fun `Given createEntity is called with a EntityType and BoxedTerms it delegates the call to the Client and returns its result`() = runBlockingTest {
        // Given
        val type = DataModelContract.EntityType.ITEM
        val expectedEntity = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
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
            ),
        )

        var capturedType: DataModelContract.EntityType? = null
        var capturedEntity: DataModelContract.BoxedTerms? = null
        client.wikibase.createEntity = { givenType, givenEntity ->
            capturedType = givenType
            capturedEntity = givenEntity

            SuspendingFunctionWrapperStub(
                suspend { expectedEntity }
            )
        }

        // When
        val result = EntityStoreApiService(client).createEntity(expectedEntity)

        // Then
        result sameAs expectedEntity
        capturedType mustBe type
        capturedEntity mustBe expectedEntity
    }
}
