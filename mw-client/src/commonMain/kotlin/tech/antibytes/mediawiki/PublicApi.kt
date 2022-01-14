/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import tech.antibytes.mediawiki.DataModelContract.RevisionedEntity
import tech.antibytes.mediawiki.DataModelContract.RevisionedPagePointer

typealias EntityId = String
typealias LanguageTag = String

interface PublicApi {
    fun interface ConnectivityManager {
        fun hasConnection(): Boolean
    }

    interface Logger : io.ktor.client.features.logging.Logger {
        fun info(message: String)
        fun warn(message: String)
        fun error(exception: Throwable, message: String?)
    }

    interface SuspendingFunctionWrapper<T> {
        val wrappedFunction: suspend () -> T

        fun subscribe(
            onSuccess: (item: T) -> Unit,
            onError: (error: Throwable) -> Unit,
        ): Job
    }

    interface AuthenticationService {
        suspend fun login(username: String, password: String): SuspendingFunctionWrapper<Boolean>
    }

    interface PageService {
        fun randomPage(limit: Int, namespace: Int? = null): SuspendingFunctionWrapper<List<RevisionedPagePointer>>
        fun fetchRestrictions(pageTitle: String): SuspendingFunctionWrapper<List<String>>
    }

    interface WikibaseService {
        fun fetchEntities(
            ids: Set<EntityId>,
            language: LanguageTag? = null
        ): SuspendingFunctionWrapper<List<RevisionedEntity>>

        fun searchForEntities(
            term: String,
            language: LanguageTag,
            type: DataModelContract.EntityType,
            limit: Int
        ): SuspendingFunctionWrapper<List<DataModelContract.Entity>>

        fun updateEntity(entity: RevisionedEntity): SuspendingFunctionWrapper<RevisionedEntity?>

        fun createEntity(
            type: DataModelContract.EntityType,
            entity: DataModelContract.BoxedTerms
        ): SuspendingFunctionWrapper<RevisionedEntity?>
    }

    interface Client {
        val authentication: AuthenticationService
        val page: PageService
        val wikibase: WikibaseService
    }

    interface ClientFactory {
        fun getInstance(
            host: String,
            logger: Logger,
            connection: ConnectivityManager,
            dispatcher: CoroutineDispatcher
        ): Client
    }
}
