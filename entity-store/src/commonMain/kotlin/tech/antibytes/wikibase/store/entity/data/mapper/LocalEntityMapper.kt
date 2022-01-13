/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity

internal class LocalEntityMapper : MapperContract.LocalEntityMapper {
    override fun toMonolingualEntity(
        id: String,
        type: EntityModelContract.EntityType,
        revision: Long,
        language: LanguageTag,
        lastModified: Instant,
        edibility: Boolean
    ): EntityModelContract.MonolingualEntity {
        return MonolingualEntity(
            id = id,
            type = type,
            revision = revision,
            language = language,
            isEditable = edibility
        )
    }

    override fun toMonolingualEntity(
        id: String,
        type: EntityModelContract.EntityType,
        revision: Long,
        language: LanguageTag,
        lastModified: Instant,
        edibility: Boolean,
        label: String?,
        description: String?,
        aliases: List<String>
    ): EntityModelContract.MonolingualEntity {
        return MonolingualEntity(
            id = id,
            type = type,
            revision = revision,
            language = language,
            isEditable = edibility,
            label = label,
            description = description,
            aliases = aliases
        )
    }
}
