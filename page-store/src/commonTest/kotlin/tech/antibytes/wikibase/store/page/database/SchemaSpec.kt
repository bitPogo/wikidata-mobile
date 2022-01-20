/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.database

import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.database.page.WikibaseDataBase
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
    fun `It stores RandomPageIds and recalls the first Entry`() {
        // Given
        val id1: String = fixture.fixture()
        val id2: String = fixture.fixture()

        // When
        val queries = db.dataBase.pageQueries
        queries.insertRandomPage(id1)
        queries.insertRandomPage(id2)

        val actual = queries.peekRandomPages().executeAsOneOrNull()

        // Then
        actual mustBe id1
    }

    @Test
    fun `It stores RandomPageIds, deletes a slected one recalls the first Entry`() {
        // Given
        val id1: String = fixture.fixture()
        val id2: String = fixture.fixture()

        // When
        val queries = db.dataBase.pageQueries
        queries.insertRandomPage(id1)
        queries.insertRandomPage(id2)

        queries.deleteRandomPage(id1)

        val actual = queries.peekRandomPages().executeAsOneOrNull()

        // Then
        actual mustBe id2
    }
}
