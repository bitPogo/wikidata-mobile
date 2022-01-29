/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.mock

import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.MockError
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

class EntityStoreStub(
    override val entity: CoroutineWrapperContract.SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception>,
    var fetchEntity: ((String, String) -> Unit)? = null,
    var refresh: (() -> Unit)? = null,
    var setLabel: ((String) -> Unit)? = null,
    var setDescription: ((String) -> Unit)? = null,
    var setAlias: ((Int, String) -> Unit)? = null,
    var setAliases: ((List<String>) -> Unit)? = null,
    var create: ((String, EntityModelContract.EntityType) -> Unit)? = null,
    var save: (() -> Unit)? = null,
    var rollback: (() -> Unit)? = null
) : EntityStoreContract.EntityStore {
    override fun fetchEntity(id: EntityId, language: LanguageTag) {
        return fetchEntity?.invoke(id, language)
            ?: throw MockError.MissingStub("Missing Sideeffect fetchEntity")
    }

    override fun refresh() {
        return refresh?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect refresh")
    }

    override fun setLabel(label: String) {
        return setLabel?.invoke(label)
            ?: throw MockError.MissingStub("Missing Sideeffect setLabel")
    }

    override fun setDescription(description: String) {
        return setDescription?.invoke(description)
            ?: throw MockError.MissingStub("Missing Sideeffect setDescription")
    }

    override fun setAlias(index: Int, alias: String) {
        return setAlias?.invoke(index, alias)
            ?: throw MockError.MissingStub("Missing Sideeffect setAlias")
    }

    override fun setAliases(aliases: List<String>) {
        return setAliases?.invoke(aliases)
            ?: throw MockError.MissingStub("Missing Sideeffect setAliases")
    }

    override fun create(language: LanguageTag, type: EntityModelContract.EntityType) {
        return create?.invoke(language, type)
            ?: throw MockError.MissingStub("Missing Sideeffect create")
    }

    override fun save() {
        return save?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect save")
    }

    override fun rollback() {
        return rollback?.invoke()
            ?: throw MockError.MissingStub("Missing Sideeffect rollback")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}
