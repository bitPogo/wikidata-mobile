/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.client

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.EntityId

data class EntityStub(
    override val id: EntityId,
    override val type: DataModelContract.EntityType,
    override val labels: Map<String, DataModelContract.LanguageValuePair>,
    override val descriptions: Map<String, DataModelContract.LanguageValuePair>,
    override val aliases: Map<String, List<DataModelContract.LanguageValuePair>>,
) : DataModelContract.Entity
