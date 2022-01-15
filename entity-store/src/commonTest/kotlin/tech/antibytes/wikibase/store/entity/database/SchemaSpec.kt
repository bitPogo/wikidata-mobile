/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import kotlinx.datetime.Instant
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.entity.SelectMonoligualEntityById
import tech.antibytes.wikibase.store.database.entity.WikibaseDataBase
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RobolectricConfig(manifest = "--none")
@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class SchemaSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()

    @BeforeTest
    fun setUp() {
        db.open(WikibaseDataBase.Schema)
    }

    @AfterTest
    fun tearDown() {
        db.close()
    }

    @Test
    fun `It adds and retrievs Entities`() {
        // Given
        val id: String = fixture.fixture()
        val revision: Long = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM
        val lastModified = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        // When
        val entityQueries: EntityQueries = db.dataBase.entityQueries
        entityQueries.addEntity(id, type, revision, lastModified, edibility)

        val storedValues = entityQueries.selectEntityById(id)
            .executeAsList()

        // Then
        storedValues.first() mustBe Entity(id, type, revision, lastModified, edibility)
    }

    @Test
    fun `It adds and retrievs MonoligualEntities`() {
        // Given
        val id: String = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM
        val revision: Long = fixture.fixture()
        val lastModified = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        val language: String = fixture.fixture()
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()
        val aliases: List<String> = fixture.listFixture()

        // When
        val entityQueries: EntityQueries = db.dataBase.entityQueries
        entityQueries.addEntity(id, type, revision, lastModified, edibility)
        entityQueries.addTerm(id, language, label, description, aliases)

        val storedValues = entityQueries.selectMonoligualEntityById(id, language)
            .executeAsList()

        // Then
        storedValues.first() mustBe SelectMonoligualEntityById(
            id,
            type,
            revision,
            lastModified,
            edibility,
            label,
            description,
            aliases
        )
    }

    @Test
    fun `It adds, updates and retrievs Entities`() {
        // Given
        val id: String = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM
        val revision: Long = fixture.fixture()
        val lastModified = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        // When
        val entityQueries: EntityQueries = db.dataBase.entityQueries
        entityQueries.addEntity(
            id,
            type,
            fixture.fixture(),
            Instant.fromEpochMilliseconds(fixture.fixture()),
            fixture.fixture()
        )
        entityQueries.updateEntity(revision, lastModified, edibility, id)

        val storedValues = entityQueries.selectEntityById(id)
            .executeAsList()

        // Then
        storedValues.first() mustBe Entity(id, type, revision, lastModified, edibility)
    }

    @Test
    fun `It adds, updates Terms and retrievs MonoligualEntities`() {
        // Given
        val id: String = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM
        val revision: Long = fixture.fixture()
        val lastModified = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        val language: String = fixture.fixture()
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()
        val aliases: List<String> = fixture.listFixture()

        // When
        val entityQueries: EntityQueries = db.dataBase.entityQueries
        entityQueries.addEntity(
            id,
            type,
            revision,
            lastModified,
            edibility
        )
        entityQueries.addTerm(id, language, null, null, emptyList())
        entityQueries.updateTerm(label, description, aliases, id, language)

        val storedValues = entityQueries.selectMonoligualEntityById(id, language)
            .executeAsList()

        // Then
        storedValues.first() mustBe SelectMonoligualEntityById(
            id,
            type,
            revision,
            lastModified,
            edibility,
            label,
            description,
            aliases
        )
    }

    @Test
    fun `It adds, deletes Terms and retrievs MonoligualEntities`() {
        // Given
        val id: String = fixture.fixture()
        val type = EntityModelContract.EntityType.ITEM
        val revision: Long = fixture.fixture()
        val lastModified = Instant.fromEpochMilliseconds(fixture.fixture())
        val edibility: Boolean = fixture.fixture()

        val language: String = fixture.fixture()
        val label: String = fixture.fixture()
        val description: String = fixture.fixture()
        val aliases: List<String> = fixture.listFixture()

        // When
        val entityQueries: EntityQueries = db.dataBase.entityQueries
        entityQueries.addEntity(
            id,
            type,
            revision,
            lastModified,
            edibility
        )
        entityQueries.addTerm(id, language, label, description, aliases)
        entityQueries.deleteTerm(id, language)

        val storedValues = entityQueries.selectMonoligualEntityById(id, language)
            .executeAsList()

        // Then
        storedValues.isEmpty() mustBe true
    }
}
