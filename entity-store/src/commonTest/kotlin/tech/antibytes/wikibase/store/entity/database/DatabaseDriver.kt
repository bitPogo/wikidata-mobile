/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import com.squareup.sqldelight.db.SqlDriver
import tech.antibytes.wikibase.store.database.entity.WikibaseDataBase

internal val testDatabase = "test"

expect class DatabaseDriver constructor() {
    val dataBase: WikibaseDataBase

    fun open(schema: SqlDriver.Schema)

    fun close()
}
