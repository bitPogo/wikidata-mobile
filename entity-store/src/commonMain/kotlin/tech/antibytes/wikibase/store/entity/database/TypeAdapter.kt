/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.database

import com.squareup.sqldelight.ColumnAdapter
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract

class TypeAdapter : ColumnAdapter<EntityModelContract.EntityType, String> {
    override fun decode(
        databaseValue: String
    ): EntityModelContract.EntityType = EntityModelContract.EntityType.valueOf(databaseValue)

    override fun encode(
        value: EntityModelContract.EntityType
    ): String = value.name
}
