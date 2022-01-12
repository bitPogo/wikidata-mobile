/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.EntityType
import tech.antibytes.mediawiki.PublicApi.SuspendingFunctionWrapper
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class MwClientStub : PublicApi.Client, MockContract.Mock {
    override val authentication = AuthenticationStub()
    override val page = PageStub()
    override val wikibase = WikibaseStub()

    override fun clear() {
        page.clear()
        wikibase.clear()
    }
}

class AuthenticationStub : PublicApi.AuthenticationService {
    override suspend fun login(username: String, password: String): SuspendingFunctionWrapper<Boolean> {
        TODO("Not yet implemented")
    }
}

class PageStub : PublicApi.PageService, MockContract.Mock {
    var fetchRestrictions: ((String) -> SuspendingFunctionWrapper<List<String>>)? = null

    override fun randomPage(
        limit: Int,
        namespace: Int?
    ): SuspendingFunctionWrapper<List<DataModelContract.RevisionedPagePointer>> {
        TODO("Not yet implemented")
    }

    override fun fetchRestrictions(pageTitle: String): SuspendingFunctionWrapper<List<String>> {
        return fetchRestrictions?.invoke(pageTitle)
            ?: throw MockError.MissingStub("Missing Sideeffect fetchRestrictions")
    }

    override fun clear() {
        fetchRestrictions = null
    }
}

class WikibaseStub: PublicApi.WikibaseService, MockContract.Mock {
    var fetchEntities: ((Set<EntityId>) -> SuspendingFunctionWrapper<List<RevisionedEntity>>)? = null
    var updateEntity: ((RevisionedEntity) -> SuspendingFunctionWrapper<RevisionedEntity?>)? = null
    var createEntity: ((EntityType, BoxedTerms) -> SuspendingFunctionWrapper<RevisionedEntity?>)? = null

    override fun fetchEntities(ids: Set<EntityId>): SuspendingFunctionWrapper<List<RevisionedEntity>> {
        return fetchEntities?.invoke(ids)
            ?: throw MockError.MissingStub("Missing Sideeffect fetchEntities")
    }

    override fun searchForEntities(
        term: String,
        language: LanguageTag,
        type: EntityType,
        limit: Int
    ): SuspendingFunctionWrapper<List<DataModelContract.Entity>> {
        TODO("Not yet implemented")
    }

    override fun updateEntity(entity: RevisionedEntity): SuspendingFunctionWrapper<RevisionedEntity?> {
        return updateEntity?.invoke(entity)
            ?: throw MockError.MissingStub("Missing Sideeffect updateEntity")
    }

    override fun createEntity(
        type: EntityType,
        entity: BoxedTerms
    ): SuspendingFunctionWrapper<RevisionedEntity?> {
        return createEntity?.invoke(type, entity)
            ?: throw MockError.MissingStub("Missing Sideeffect createEntity")
    }

    override fun clear() {
        fetchEntities = null
        updateEntity = null
        createEntity = null
    }
}
