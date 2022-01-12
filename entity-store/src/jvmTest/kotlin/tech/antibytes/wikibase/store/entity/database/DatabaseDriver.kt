/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.serialization.json.Json
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.Term
import tech.antibytes.wikibase.store.database.entity.WikibaseDataBase

actual class DatabaseDriver {
    private var driver: SqlDriver? = null
    actual val dataBase: WikibaseDataBase
        get() = WikibaseDataBase(
            driver!!,
            TermAdapter = Term.Adapter(
                aliasesAdapter = ListAdapter(Json)
            ),
            EntityAdapter = Entity.Adapter(
                lastModifiedAdapter = InstantAdapter()
            )
        )

    actual fun open(schema: SqlDriver.Schema) {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        schema.create(driver!!)
    }

    actual fun close() {
        driver?.close()
        driver = null
    }
}
