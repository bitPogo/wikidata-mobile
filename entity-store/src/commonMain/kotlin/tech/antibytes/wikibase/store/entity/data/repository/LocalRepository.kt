/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.data.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.domain.model.EntityId
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag

internal class LocalRepository(
    private val database: EntityQueries,
    private val mapper: MapperContract.LocalEntityMapper
) : DomainContract.Repository {
    private fun fetchPartialEntity(entityId: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity? {
        val entity = database.selectEntityById(
            entityId
        ) { id, type, revision, lastModified, edibility ->
            mapper.toMonolingualEntity(id, type, revision, language, lastModified, edibility)
        }

        return entity.executeAsOneOrNull()
    }

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

    override suspend fun fetchEntity(id: EntityId, language: LanguageTag): EntityModelContract.MonolingualEntity? {
        val entity = fetchFullEntity(id, language)

        return entity ?: fetchPartialEntity(id, language)
    }

    private fun isEmptyTerm(
        label: String?,
        description: String?,
        aliases: List<String>
    ): Boolean {
        return label == null &&
            description == null &&
            aliases.isEmpty()
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

        if (!isEmptyTerm(label, description, aliases)) {
            database.addTerm(
                entityId = entity.id,
                language = entity.language,
                label = label,
                description = description,
                aliases = aliases
            )
        }
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

        val hasTerm = hasTerm(entity)
        val isEmptyTerm = isEmptyTerm(
            label,
            description,
            aliases
        )

        when {
            hasTerm && !isEmptyTerm -> {
                database.updateTerm(
                    whereId = entity.id,
                    whereLanguage = entity.language,
                    label = label,
                    description = description,
                    aliases = aliases
                )
            }
            !hasTerm && !isEmptyTerm -> {
                database.addTerm(
                    entityId = entity.id,
                    language = entity.language,
                    label = label,
                    description = description,
                    aliases = aliases
                )
            }
            hasTerm && isEmptyTerm -> database.deleteTerm(entity.id, entity.language)
        }
    }

    override suspend fun updateEntity(entity: EntityModelContract.MonolingualEntity): EntityModelContract.MonolingualEntity {
        database.updateEntity(
            whereId = entity.id,
            revision = entity.revision,
            lastModified = entity.lastModification,
            edibility = entity.isEditable,
        )

        updateTerm(entity)

        return entity
    }
}
