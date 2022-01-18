/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer.repository

import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.transfer.mapper.MapperContract

internal class LocalRepository(
    private val database: EntityQueries,
    private val mapper: MapperContract.LocalEntityMapper
) : DomainContract.Repository {
    private fun fetchFullEntity(entityId: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity? {
        return database.selectMonoligualEntityById(
            entityId,
            language
        ) { id, type, revision, lastModified, edibility, label, description, aliases ->
            mapper.toMonolingualEntity(
                id,
                type,
                revision,
                language,
                lastModified,
                edibility,
                label,
                description,
                aliases
            )
        }.executeAsOneOrNull()
    }

    override suspend fun fetchEntity(
        id: EntityId,
        language: LanguageTag
    ): EntityModelContract.MonolingualEntity? {
        return fetchFullEntity(id, language)
    }

    private fun cleanStringValue(value: String?): String? {
        val cleaned = value?.trim()

        return if (cleaned.isNullOrEmpty()) {
            null
        } else {
            cleaned
        }
    }

    private fun filterAliases(aliases: List<String>): List<String> {
        return aliases.filter { value -> value.isNotEmpty() && value.isNotBlank() }
    }

    private fun addTerm(entity: EntityModelContract.MonolingualEntity) {
        val label = cleanStringValue(entity.label)
        val description = cleanStringValue(entity.description)
        val aliases = filterAliases(entity.aliases)

        database.addTerm(
            entityId = entity.id,
            language = entity.language,
            label = label,
            description = description,
            aliases = aliases
        )
    }

    override suspend fun createEntity(entity: EntityModelContract.MonolingualEntity): EntityModelContract.MonolingualEntity {
        database.addEntity(
            id = entity.id,
            type = entity.type,
            revision = entity.revision,
            lastModified = entity.lastModification,
            edibility = entity.isEditable
        )

        addTerm(entity)

        return entity
    }

    private fun hasTerm(entity: EntityModelContract.MonolingualEntity): Boolean {
        return database.hasTerm(
            entity.id,
            entity.language
        ).executeAsOne().toInt() != 0
    }

    private fun updateTerm(entity: EntityModelContract.MonolingualEntity) {
        val label = cleanStringValue(entity.label)
        val description = cleanStringValue(entity.description)
        val aliases = filterAliases(entity.aliases)

        if (hasTerm(entity)) {
            database.updateTerm(
                whereId = entity.id,
                whereLanguage = entity.language,
                label = label,
                description = description,
                aliases = aliases
            )
        } else {
            database.addTerm(
                entityId = entity.id,
                language = entity.language,
                label = label,
                description = description,
                aliases = aliases
            )
        }
    }

    private fun update(entity: EntityModelContract.MonolingualEntity): EntityModelContract.MonolingualEntity {
        database.updateEntity(
            whereId = entity.id,
            revision = entity.revision,
            lastModified = entity.lastModification,
            edibility = entity.isEditable,
        )

        updateTerm(entity)

        return entity
    }

    private fun hasEntity(entity: EntityModelContract.MonolingualEntity): Boolean {
        return database.hasEntity(entity.id).executeAsOne().toInt() != 0
    }

    override suspend fun updateEntity(entity: EntityModelContract.MonolingualEntity): EntityModelContract.MonolingualEntity {
        return if (hasEntity(entity)) {
            update(entity)
        } else {
            createEntity(entity)
        }
    }
}
