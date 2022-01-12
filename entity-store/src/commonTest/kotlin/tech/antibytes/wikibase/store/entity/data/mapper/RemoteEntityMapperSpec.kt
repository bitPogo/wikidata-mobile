/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.entity.data.dto.LanguageValuePair
import tech.antibytes.wikibase.store.entity.data.dto.RevisionedEntity
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import kotlin.test.Test

class RemoteEntityMapperSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils RemoteEntityMapper`() {
        RemoteEntityMapper() fulfils MapperContract.RemoteEntityMapper::class
    }

    @Test
    fun `Given toMonolingualEntity is called with a LanguageTag, RevisionedEntity and a isEditable Flag, it returns a MonolingualEntity, while ignoring missmatches`() {
        // Given
        val language: String = fixture.fixture()
        val revisioned = RevisionedEntity(
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

        val flag: Boolean = fixture.fixture()

        // When
        val monolingual = RemoteEntityMapper().toMonolingualEntity(language, revisioned, flag)

        // Then
        monolingual fulfils EntityModelContract.MonolingualEntity::class
        monolingual.id mustBe revisioned.id
        monolingual.type mustBe EntityModelContract.EntityType.valueOf(revisioned.type.name)
        monolingual.revision mustBe revisioned.revision
        monolingual.isEditable mustBe flag
        monolingual.label mustBe null
        monolingual.description mustBe null
        monolingual.aliases mustBe emptyList()
    }

    @Test
    fun `Given toMonolingualEntity is called with a LanguageTag, RevisionedEntity and a isEditable Flag, it returns a MonolingualEntity`() {
        // Given
        val language: String = fixture.fixture()
        val revisioned = RevisionedEntity(
            id = fixture.fixture(),
            type = DataModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            lastModification = Instant.fromEpochMilliseconds(fixture.fixture()),
            labels = mapOf(
                language to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                ),
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            descriptions = mapOf(
                language to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                ),
                fixture.fixture<String>() to LanguageValuePair(
                    language = fixture.fixture(),
                    value = fixture.fixture()
                )
            ),
            aliases = mapOf(
                language to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                ),
                fixture.fixture<String>() to listOf(
                    LanguageValuePair(
                        language = fixture.fixture(),
                        value = fixture.fixture()
                    )
                )
            ),
        )

        val flag: Boolean = fixture.fixture()

        // When
        val monolingual = RemoteEntityMapper().toMonolingualEntity(language, revisioned, flag)

        // Then
        monolingual fulfils EntityModelContract.MonolingualEntity::class
        monolingual.id mustBe revisioned.id
        monolingual.type mustBe EntityModelContract.EntityType.valueOf(revisioned.type.name)
        monolingual.revision mustBe revisioned.revision
        monolingual.isEditable mustBe flag
        monolingual.label mustBe revisioned.labels[language]?.value
        monolingual.description mustBe revisioned.descriptions[language]?.value
        monolingual.aliases mustBe listOf(revisioned.aliases[language]?.first()?.value)
    }

    @Test
    fun `Given toRevisionedEntity is called with a LanguageTag and a MonolingualEntity and a isEditable Flag, it returns a RevisionedEntity, while ignoring nulled values`() {
        // Given
        val language: String = fixture.fixture()
        val monolingual = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = null,
            description = null,
            aliases = emptyList(),
        )

        // When
        val revisioned = RemoteEntityMapper().toRevisionedEntity(language, monolingual)

        // Then
        revisioned fulfils DataModelContract.RevisionedEntity::class
        revisioned.id mustBe monolingual.id
        revisioned.type mustBe DataModelContract.EntityType.valueOf(monolingual.type.name)
        revisioned.revision mustBe monolingual.revision
        revisioned.labels mustBe mapOf(language to LanguageValuePair(language, ""))
        revisioned.descriptions mustBe mapOf(language to LanguageValuePair(language, ""))
        revisioned.aliases mustBe mapOf(language to emptyList())
        revisioned.lastModification mustBe Instant.DISTANT_PAST
    }

    @Test
    fun `Given toRevisionedEntity is called with a LanguageTag and a MonolingualEntity and a isEditable Flag, it returns a RevisionedEntity`() {
        // Given
        val language: String = fixture.fixture()
        val monolingual = MonolingualEntity(
            id = fixture.fixture(),
            type = EntityModelContract.EntityType.ITEM,
            revision = fixture.fixture(),
            isEditable = fixture.fixture(),
            label = fixture.fixture<String>(),
            description = fixture.fixture<String>(),
            aliases = fixture.listFixture(),
        )

        // When
        val revisioned = RemoteEntityMapper().toRevisionedEntity(language, monolingual)

        // Then
        revisioned fulfils DataModelContract.RevisionedEntity::class
        revisioned.id mustBe monolingual.id
        revisioned.type mustBe DataModelContract.EntityType.valueOf(monolingual.type.name)
        revisioned.revision mustBe monolingual.revision
        revisioned.labels mustBe mapOf(language to LanguageValuePair(language, monolingual.label!!))
        revisioned.descriptions mustBe mapOf(language to LanguageValuePair(language, monolingual.description!!))
        revisioned.lastModification mustBe Instant.DISTANT_PAST

        val aliases = monolingual.aliases.map { alias -> LanguageValuePair(language, alias) }
        revisioned.aliases mustBe mapOf(language to aliases)
    }
}