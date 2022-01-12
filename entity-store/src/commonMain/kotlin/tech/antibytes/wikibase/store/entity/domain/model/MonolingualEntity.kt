/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain.model

internal data class MonolingualEntity(
    override val id: EntityId,
    override val type: EntityModelContract.EntityType,
    override val revision: Long,
    override val isEditable: Boolean,
    override val label: String? = null,
    override val description: String? = null,
    override val aliases: List<String> = emptyList(),
) : EntityModelContract.MonolingualEntity
