/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.mock.EntityStub
import tech.antibytes.wikibase.store.mock.LanguageValuePairStub
import tech.antibytes.wikibase.store.mock.transfer.SearchEntityMapperStub
import tech.antibytes.wikibase.store.page.domain.model.SearchEntry
import kotlin.test.Test

class SearchEntityMapperSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils SearchEntityMapper`() {
        SearchEntityMapper() fulfils DataTransferContract.SearchEntityMapper::class
    }

    @Test
    fun `Given map is called with a Entity it maps it to null, if label and description is empty`() {
        // Given
        val entity = EntityStub(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            labels = emptyMap(),
            descriptions = emptyMap(),
            aliases = emptyMap()
        )

        // When
        val actual = SearchEntityMapper().map(entity)

        // Then
        actual mustBe null
    }

    @Test
    fun `Given map is called ith a Entity it maps it to a SearchEntity, if label is not empty`() {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()
        val label: String = fixture.fixture()

        val entity = EntityStub(
            id = id,
            type = DataModelContract.EntityType.ITEM,
            labels = mapOf(
                language to LanguageValuePairStub(
                    language,
                    label
                )
            ),
            descriptions = emptyMap(),
            aliases = emptyMap()
        )

        // When
        val actual = SearchEntityMapper().map(entity)

        // Then
        actual mustBe SearchEntry(
            id = id,
            language = language,
            label = label,
            description = null
        )
    }

    @Test
    fun `Given map is called ith a Entity it maps it to a SearchEntity, if description is not empty`() {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()
        val description: String = fixture.fixture()

        val entity = EntityStub(
            id = id,
            type = DataModelContract.EntityType.ITEM,
            labels = emptyMap(),
            descriptions = mapOf(
                language to LanguageValuePairStub(
                    language,
                    description
                )
            ),
            aliases = emptyMap()
        )

        // When
        val actual = SearchEntityMapper().map(entity)

        // Then
        actual mustBe SearchEntry(
            id = id,
            language = language,
            label = null,
            description = description
        )
    }

    @Test
    fun `Given map is called ith a Entity it maps it to a SearchEntity, if uses the language of the Label over Description is not empty`() {
        // Given
        val id: String = fixture.fixture()
        val language: String = fixture.fixture()

        val label: String = fixture.fixture()
        val description: String = fixture.fixture()

        val entity = EntityStub(
            id = id,
            type = DataModelContract.EntityType.ITEM,
            labels = mapOf(
                language to LanguageValuePairStub(
                    language,
                    label
                )
            ),
            descriptions = mapOf(
                fixture.fixture<String>() to LanguageValuePairStub(
                    language,
                    description
                )
            ),
            aliases = emptyMap()
        )

        // When
        val actual = SearchEntityMapper().map(entity)

        // Then
        actual mustBe SearchEntry(
            id = id,
            language = language,
            label = label,
            description = null
        )
    }
}
