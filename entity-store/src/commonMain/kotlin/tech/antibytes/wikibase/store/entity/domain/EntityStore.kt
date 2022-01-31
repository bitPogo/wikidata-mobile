/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain

import co.touchlab.stately.concurrency.AtomicReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.koin.core.KoinApplication
import org.koin.core.qualifier.named
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.CoroutineScopeDispatcher
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract.SharedFlowWrapper
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.entity.di.initKoin
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity
import tech.antibytes.wikibase.store.entity.lang.EntityStoreError

class EntityStore internal constructor(koin: KoinApplication) : EntityStoreContract.EntityStore {
    override val entity: SharedFlowWrapper<EntityModelContract.MonolingualEntity, Exception> = koin.koin.get()
    private val flow: MutableStateFlow<ResultContract<EntityModelContract.MonolingualEntity, Exception>> by koin.koin.inject()

    private val dispatcher: CoroutineScopeDispatcher by koin.koin.inject(
        named(DomainContract.DomainKoinIds.PRODUCER_SCOPE)
    )

    private val localRepository: DomainContract.Repository by koin.koin.inject(
        named(DomainContract.DomainKoinIds.LOCAL)
    )

    private val remoteRepository: DomainContract.Repository by koin.koin.inject(
        named(DomainContract.DomainKoinIds.REMOTE)
    )

    private val rollbackReference = AtomicReference<ResultContract<Pair<EntityId, LanguageTag>, Exception>>(
        Failure(EntityStoreError.InvalidRollbackState())
    )

    private suspend fun wrapResult(
        action: suspend () -> EntityModelContract.MonolingualEntity
    ): ResultContract<EntityModelContract.MonolingualEntity, Exception> {
        return try {
            Success(action.invoke())
        } catch (error: Exception) {
            Failure(error)
        }
    }

    private fun executeEvent(event: suspend () -> EntityModelContract.MonolingualEntity) {
        dispatcher.dispatch().launch {
            flow.update {
                wrapResult(event)
            }
        }
    }

    private fun setRollbackPoint(entity: EntityModelContract.MonolingualEntity) {
        rollbackReference.set(
            Success(Pair(entity.id, entity.language))
        )
    }

    private suspend fun fetchRemoteEntity(id: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity {
        return remoteRepository.fetchEntity(id, language)
            ?: throw EntityStoreError.MissingEntity(id, language)
    }

    private suspend fun fetchAndUpdate(id: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity {
        val remoteEntity = fetchRemoteEntity(id, language)

        return localRepository.updateEntity(remoteEntity)!!
    }

    private suspend fun fetchLocallyOrRemotely(
        id: EntityId,
        language: LanguageTag
    ): EntityModelContract.MonolingualEntity {
        val stored = localRepository.fetchEntity(id, language)

        return stored ?: fetchAndUpdate(id, language)
    }

    override fun fetchEntity(id: EntityId, language: LanguageTag) {
        executeEvent {
            fetchLocallyOrRemotely(id, language).also { entity ->
                setRollbackPoint(entity)
            }
        }
    }

    private suspend fun guardValue(
        mutation: suspend (EntityModelContract.MonolingualEntity) -> EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        val currentState = flow.value

        return if (currentState.isError()) {
            throw EntityStoreError.MutationFailure()
        } else {
            mutation.invoke(currentState.unwrap())
        }
    }

    private fun unwrapRollbackOnRefresh(): Pair<EntityId, LanguageTag> {
        val state = rollbackReference.get()

        return if (state.isError()) {
            throw EntityStoreError.InvalidRefreshState()
        } else {
            state.unwrap()
        }
    }

    override fun refresh() {
        executeEvent {
            val (entityId, language) = unwrapRollbackOnRefresh()

            fetchAndUpdate(entityId, language)
        }
    }

    private fun setLabel(
        label: String,
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        return (entity as MonolingualEntity).copy(label = label)
    }

    override fun setLabel(label: String) {
        executeEvent {
            guardValue { entity ->
                setLabel(label, entity)
            }
        }
    }

    private fun setDescription(
        description: String,
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        return (entity as MonolingualEntity).copy(description = description)
    }

    override fun setDescription(description: String) {
        executeEvent {
            guardValue { entity ->
                setDescription(description, entity)
            }
        }
    }

    private fun setAlias(
        index: Int,
        alias: String,
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        return (entity as MonolingualEntity).copy(
            aliases = entity.aliases
                .toMutableList()
                .also { aliases ->
                    if (alias.isNotEmpty()) {
                        aliases[index] = alias
                    } else {
                        aliases.removeAt(index)
                    }
                }
        )
    }

    override fun setAlias(index: Int, alias: String) {
        executeEvent {
            guardValue { entity ->
                setAlias(index, alias, entity)
            }
        }
    }

    private fun setAliases(
        aliases: List<String>,
        entity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        return (entity as MonolingualEntity).copy(aliases = aliases)
    }

    override fun setAliases(aliases: List<String>) {
        executeEvent {
            guardValue { entity ->
                setAliases(aliases, entity)
            }
        }
    }

    override fun create(
        language: LanguageTag,
        type: EntityModelContract.EntityType
    ) {
        executeEvent {
            MonolingualEntity(
                id = "",
                type = type,
                revision = 0,
                language = language,
                lastModification = Instant.DISTANT_PAST,
                isEditable = true,
                label = null,
                description = null,
                aliases = emptyList()
            )
        }
    }

    override fun reset() {
        executeEvent {
            rollbackReference.set(Failure(EntityStoreError.InvalidRollbackState()))
            throw EntityStoreError.InitialState()
        }
    }

    private fun getState(): EntityModelContract.MonolingualEntity {
        return if (flow.value.isError()) {
            throw EntityStoreError.InvalidCreationState()
        } else {
            flow.value.unwrap()
        }
    }

    private fun cleanValue(value: String?): String? {
        return if (value.isNullOrBlank()) {
            null
        } else {
            value.trim()
        }
    }

    private fun cleanAliases(aliases: List<String>): List<String> {
        return aliases
            .filter { alias -> alias.isNotBlank() }
            .map { alias -> alias.trim() }
    }

    private fun cleanEntity(entity: MonolingualEntity): EntityModelContract.MonolingualEntity {
        return entity.copy(
            label = cleanValue(entity.label),
            description = cleanValue(entity.description),
            aliases = cleanAliases(entity.aliases)
        )
    }

    private suspend fun store(
        onCreate: suspend (EntityModelContract.MonolingualEntity) -> EntityModelContract.MonolingualEntity,
        onUpdate: suspend (EntityModelContract.MonolingualEntity) -> EntityModelContract.MonolingualEntity,
    ): EntityModelContract.MonolingualEntity {
        val value = cleanEntity(
            getState() as MonolingualEntity
        )

        return if (value.id.isEmpty()) {
            onCreate(value)
        } else {
            onUpdate(value)
        }
    }

    private suspend fun rollback(entityId: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity {
        return localRepository.fetchEntity(entityId, language)
            ?: throw EntityStoreError.MissingEntity(entityId, language)
    }

    override fun rollback() {
        executeEvent {
            val (entityId, language) = rollbackReference.get().unwrap()

            rollback(entityId, language)
        }
    }

    private suspend fun saveOnCreate(
        localEntity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        val entity = remoteRepository.createEntity(localEntity)
            ?: throw EntityStoreError.CreationRemoteFailure(localEntity.language)

        return localRepository.createEntity(entity)
            ?: throw EntityStoreError.CreationLocalFailure(entity.id, entity.language)
    }

    private suspend fun saveOnUpdate(
        localEntity: EntityModelContract.MonolingualEntity
    ): EntityModelContract.MonolingualEntity {
        val entity = remoteRepository.updateEntity(localEntity)
            ?: throw EntityStoreError.UpdateRemoteFailure(localEntity.id, localEntity.language)

        return localRepository.updateEntity(entity)
            ?: throw EntityStoreError.UpdateLocalFailure(entity.id, entity.language)
    }

    override fun save() {
        executeEvent {
            store(
                { entity -> saveOnCreate(entity) },
                { entity -> saveOnUpdate(entity) }
            ).also { entity ->
                setRollbackPoint(entity)
            }
        }
    }

    companion object : EntityStoreContract.EntityStoreFactory {
        override fun getInstance(
            client: PublicApi.Client,
            database: EntityQueries,
            producerScope: CoroutineScopeDispatcher,
            consumerScope: CoroutineScopeDispatcher
        ): EntityStoreContract.EntityStore {
            val koin = initKoin(
                client,
                database,
                producerScope,
                consumerScope
            )

            return EntityStore(koin)
        }
    }
}
