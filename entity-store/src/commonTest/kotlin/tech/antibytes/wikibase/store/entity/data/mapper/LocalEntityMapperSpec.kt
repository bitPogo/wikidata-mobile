/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import kotlin.test.Test

class LocalEntityMapperSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils LocalEntityMapper`() {
        LocalEntityMapper() fulfils MapperContract.LocalEntityMapper::class
    }

    @Test
    fun `Given toMonolingualEntity is called without Term parameter, it returns a MonolingualEntity`() {
        // Given
        val id: String = fixture.fixture()
        val type: EntityModelContract.EntityType = EntityModelContract.EntityType.PROPERTY
        val revision: Long = fixture.fixture()
        val language: String = fixture.fixture()
        val lastModified: Instant = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        // When
        val entity = LocalEntityMapper().toMonolingualEntity(id, type, revision, language, lastModified, edibility)

        // Then
        entity fulfils EntityModelContract.MonolingualEntity::class
        entity.id mustBe id
        entity.type mustBe type
        entity.revision mustBe revision
        entity.language mustBe language
        entity.isEditable mustBe edibility
        entity.label mustBe null
        entity.description mustBe null
        entity.aliases mustBe emptyList()
    }

    @Test
    fun `Given toMonolingualEntity is called with Term parameter, it returns a MonolingualEntity`() {
        // Given
        val id: String = fixture.fixture()
        val type: EntityModelContract.EntityType = EntityModelContract.EntityType.PROPERTY
        val revision: Long = fixture.fixture()
        val language: String = fixture.fixture()
        val lastModified: Instant = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()
        val aliases: List<String> = fixture.listFixture()

        // When
        val entity = LocalEntityMapper().toMonolingualEntity(
            id,
            type,
            revision,
            language,
            lastModified,
            edibility,
            label,
            description,
            aliases
        )

        // Then
        entity fulfils EntityModelContract.MonolingualEntity::class
        entity.id mustBe id
        entity.type mustBe type
        entity.revision mustBe revision
        entity.language mustBe language
        entity.isEditable mustBe edibility
        entity.label mustBe label
        entity.description mustBe description
        entity.aliases mustBe aliases
    }
}
