/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

internal class InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun encode(value: Instant): Long = value.toEpochMilliseconds()

    override fun decode(databaseValue: Long): Instant = Instant.fromEpochMilliseconds(databaseValue)
}
