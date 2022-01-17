/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.entity.transfer.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.EntityType
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract.MonolingualEntity
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

class LocalEntityMapperStub(
    var toMonolingualEntity: ((String, EntityType, Long, LanguageTag, Instant, Boolean, String?, String?, List<String>) -> MonolingualEntity)? = null,
) : MapperContract.LocalEntityMapper, MockContract.Mock {
    override fun toMonolingualEntity(
        id: String,
        type: EntityType,
        revision: Long,
        language: LanguageTag,
        lastModified: Instant,
        edibility: Boolean,
        label: String?,
        description: String?,
        aliases: List<String>
    ): MonolingualEntity {
        return toMonolingualEntity?.invoke(
            id,
            type,
            revision,
            language,
            lastModified,
            edibility,
            label,
            description,
            aliases
        ) ?: throw MockError.MissingStub("Missing Sideeffect toMonolingualEntity")
    }

    override fun clear() {
        toMonolingualEntity = null
    }
}
