/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

interface MapperContract {
    interface RemoteEntityMapper {
        fun toMonolingualEntity(
            language: LanguageTag,
            revisionedEntity: DataModelContract.RevisionedEntity,
            restrictions: List<String>
        ): EntityModelContract.MonolingualEntity

        fun toRevisionedEntity(
            monolingualEntity: EntityModelContract.MonolingualEntity
        ): DataModelContract.RevisionedEntity
    }

    interface LocalEntityMapper {
        fun toMonolingualEntity(
            id: String,
            type: EntityModelContract.EntityType,
            revision: Long,
            language: LanguageTag,
            lastModified: Instant,
            edibility: Boolean,
            label: String? = null,
            description: String? = null,
            aliases: List<String> = emptyList()
        ): EntityModelContract.MonolingualEntity
    }
}
