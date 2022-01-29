/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import kotlinx.datetime.Instant
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

data class MonolingualEntity(
    override val id: EntityId,
    override val type: EntityModelContract.EntityType,
    override val revision: Long,
    override val isEditable: Boolean,
    override val language: LanguageTag,
    override val lastModification: Instant,
    override val label: String?,
    override val description: String?,
    override val aliases: List<String>
) : EntityModelContract.MonolingualEntity
