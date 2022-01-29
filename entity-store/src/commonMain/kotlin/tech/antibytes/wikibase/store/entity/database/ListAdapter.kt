/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListAdapter(
    private val serializer: Json
) : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> = serializer.decodeFromString(databaseValue)

    override fun encode(value: List<String>): String = serializer.encodeToString(value)
}
