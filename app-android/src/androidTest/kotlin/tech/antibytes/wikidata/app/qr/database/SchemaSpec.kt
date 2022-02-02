/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.database

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikibase.store.database.WikibaseDataBase

class SchemaSpec {
    private val fixture = kotlinFixture()
    private val db = DatabaseDriver()

    @Before
    fun setUp() {
        db.open(WikibaseDataBase.Schema)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun It_adds_and_fetches_QrCodes() {
        // Given
        val id: String = fixture.fixture()
        val qrCode: String = fixture.fixture()

        // When
        val qrCodeQueries: QrCodeQueries = db.dataBase.qrCodeQueries
        qrCodeQueries.addQrCode(id, qrCode)

        val actual = qrCodeQueries.fetchQrCode(id).executeAsOneOrNull()

        // Then
        assertEquals(
            qrCode,
            actual
        )
    }
}
