/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer.dto

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag

data class RevisionedEntity(
    override val labels: Map<String, DataModelContract.LanguageValuePair>,
    override val descriptions: Map<String, DataModelContract.LanguageValuePair>,
    override val aliases: Map<String, List<DataModelContract.LanguageValuePair>>,
    override val id: EntityId,
    override val type: DataModelContract.EntityType,
    override val revision: Long,
    override val lastModification: Instant
) : DataModelContract.RevisionedEntity

data class LanguageValuePair(
    override val language: LanguageTag,
    override val value: String
) : DataModelContract.LanguageValuePair
