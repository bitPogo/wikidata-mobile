/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import android.content.Context
import com.squareup.sqldelight.db.SqlDriver
import io.mockk.mockk
import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.database.WikibaseDataBase

class DatabaseFactorySpec {
    @Test
    fun `It fulfils DatabaseFactory`() {
        DatabaseFactory fulfils UtilContract.DatabaseFactory::class
    }

    @Test
    fun `Given open is called with a Schema and Context, it creates a Database`() {
        // Given
        val context: Context = mockk(relaxed = true)
        val schema: SqlDriver.Schema = mockk(relaxed = true)

        // When
        val actual = DatabaseFactory.create(schema, context)

        // Then
        actual fulfils WikibaseDataBase::class
    }
}
