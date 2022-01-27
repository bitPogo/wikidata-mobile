/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import android.content.Context
import com.squareup.sqldelight.db.SqlDriver
import tech.antibytes.wikibase.store.database.WikibaseDataBase

interface UtilContract {
    interface DatabaseFactory {
        fun create(schema: SqlDriver.Schema, context: Context): WikibaseDataBase

        companion object {
            const val DATABASE_NAME = "WikibaseMobile.db"
        }
    }
}
