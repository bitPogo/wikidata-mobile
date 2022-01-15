/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.data.mapper

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.entity.data.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

class RemoteEntityMapperStub(
    var toMonolingualEntity: ((LanguageTag, DataModelContract.RevisionedEntity, List<String>) -> EntityModelContract.MonolingualEntity)? = null,
    var toRevisionedEntity: ((EntityModelContract.MonolingualEntity) -> DataModelContract.RevisionedEntity)? = null
) : MapperContract.RemoteEntityMapper, MockContract.Mock {
    override fun toMonolingualEntity(
        language: LanguageTag,
        revisionedEntity: DataModelContract.RevisionedEntity,
        restrictions: List<String>
    ): EntityModelContract.MonolingualEntity {
        return toMonolingualEntity?.invoke(language, revisionedEntity, restrictions)
            ?: throw MockError.MissingStub("Missing Sideeffect toMonolingualEntity")
    }

    override fun toRevisionedEntity(
        monolingualEntity: EntityModelContract.MonolingualEntity
    ): DataModelContract.RevisionedEntity {
        return toRevisionedEntity?.invoke(monolingualEntity)
            ?: throw MockError.MissingStub("Missing Sideeffect toRevisionedEntity")
    }

    override fun clear() {
        toMonolingualEntity = null
        toRevisionedEntity = null
    }
}
