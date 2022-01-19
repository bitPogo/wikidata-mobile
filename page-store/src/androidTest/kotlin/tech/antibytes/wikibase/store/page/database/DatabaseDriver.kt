/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.database

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import tech.antibytes.wikibase.store.database.page.WikibaseDataBase

actual class DatabaseDriver {
    private var driver: SqlDriver? = null
    actual val dataBase: WikibaseDataBase
        get() = WikibaseDataBase(driver!!)

    actual fun open(schema: SqlDriver.Schema) {
        val app = ApplicationProvider.getApplicationContext<Application>()
        driver = AndroidSqliteDriver(schema, app, testDatabase)
    }

    actual fun close() {
        driver?.close()
        driver = null

        val app = ApplicationProvider.getApplicationContext<Application>()
        app.deleteDatabase(testDatabase)
    }
}
