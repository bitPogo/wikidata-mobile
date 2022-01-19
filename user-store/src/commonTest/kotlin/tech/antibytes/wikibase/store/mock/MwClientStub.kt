/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock

import tech.antibytes.mediawiki.DataModelContract
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
        authentication.clear()
    }
}

class AuthenticationStub : PublicApi.AuthenticationService, MockContract.Mock {
    var login: ((String, String) -> SuspendingFunctionWrapper<Boolean>)? = null

    override suspend fun login(username: String, password: String): SuspendingFunctionWrapper<Boolean> {
        return login?.invoke(username, password)
            ?: throw MockError.MissingStub("Missing Sideeffect login")
    }

    override fun clear() {
        login = null
    }
}

class PageStub : PublicApi.PageService {
    override fun randomPage(
        limit: Int,
        namespace: Int?
    ): SuspendingFunctionWrapper<List<DataModelContract.RevisionedPagePointer>> {
        TODO("Not yet implemented")
    }

    override fun fetchRestrictions(pageTitle: String): SuspendingFunctionWrapper<List<String>> {
        TODO("Not yet implemented")
    }
}

class WikibaseStub : PublicApi.WikibaseService {
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
        limit: Int
    ): SuspendingFunctionWrapper<List<DataModelContract.Entity>> {
        TODO("Not yet implemented")
    }

    override fun updateEntity(entity: RevisionedEntity): SuspendingFunctionWrapper<RevisionedEntity?> {
        TODO("Not yet implemented")
    }

    override fun createEntity(type: EntityType, entity: BoxedTerms): SuspendingFunctionWrapper<RevisionedEntity?> {
        TODO("Not yet implemented")
    }
}
