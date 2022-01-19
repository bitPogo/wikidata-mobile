/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import tech.antibytes.mediawiki.DataModelContract.RevisionedPagePointer
import tech.antibytes.mediawiki.DataModelContract.Entity
import tech.antibytes.mediawiki.DataModelContract.BoxedTerms
import tech.antibytes.mediawiki.DataModelContract.EntityType
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.EntityId
import tech.antibytes.mediawiki.LanguageTag
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SuspendingFunctionWrapper
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
    var randomPage: ((Int, Int?) -> SuspendingFunctionWrapper<List<RevisionedPagePointer>>)? = null

    override fun randomPage(
        limit: Int,
        namespace: Int?
    ): SuspendingFunctionWrapper<List<RevisionedPagePointer>> {
        return randomPage?.invoke(limit, namespace)
            ?: throw MockError.MissingStub("Missing Sideeffect randomPage")
    }

    override fun fetchRestrictions(pageTitle: String): SuspendingFunctionWrapper<List<String>> {
        TODO("Not yet implemented")
    }

    override fun clear() {
        randomPage = null
    }
}

class WikibaseStub : PublicApi.WikibaseService, MockContract.Mock {
    var searchForEntities: ((String, LanguageTag, EntityType, Int, Int) -> SuspendingFunctionWrapper<List<Entity>>)? = null

    override fun fetchEntities(
        ids: Set<EntityId>,
        language: LanguageTag?
    ): SuspendingFunctionWrapper<List<RevisionedEntity>> {
        TODO("Not yet implemented")
    }

    override fun searchForEntities(
        term: String,
        language: LanguageTag,
        type: EntityType,
        limit: Int,
        page: Int
    ): SuspendingFunctionWrapper<List<Entity>> {
        return searchForEntities?.invoke(term, language, type, limit, page)
            ?: throw MockError.MissingStub("Missing Sideeffect searchForEntities")
    }

    override fun updateEntity(entity: RevisionedEntity): SuspendingFunctionWrapper<RevisionedEntity?> {
        TODO("Not yet implemented")
    }

    override fun createEntity(type: EntityType, entity: BoxedTerms): SuspendingFunctionWrapper<RevisionedEntity?> {
        TODO("Not yet implemented")
    }

    override fun clear() {
        searchForEntities = null
    }
}
