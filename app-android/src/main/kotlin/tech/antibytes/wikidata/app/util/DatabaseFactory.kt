/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.json.Json
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.database.entity.Entity
import tech.antibytes.wikibase.store.database.entity.Term
import tech.antibytes.wikibase.store.entity.database.InstantAdapter
import tech.antibytes.wikibase.store.entity.database.ListAdapter
import tech.antibytes.wikibase.store.entity.database.TypeAdapter
import tech.antibytes.wikidata.app.util.UtilContract.DatabaseFactory.Companion.DATABASE_NAME

object DatabaseFactory : UtilContract.DatabaseFactory {
    override fun create(schema: SqlDriver.Schema, context: Context): WikibaseDataBase {
        val driver = AndroidSqliteDriver(
            schema,
            context,
            DATABASE_NAME
        )

        return WikibaseDataBase(
            driver,
            TermAdapter = Term.Adapter(
                aliasesAdapter = ListAdapter(Json)
            ),
            EntityAdapter = Entity.Adapter(
                typeAdapter = TypeAdapter(),
                lastModifiedAdapter = InstantAdapter()
            ),
        )
    }
}
