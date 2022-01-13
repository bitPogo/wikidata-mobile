/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.mapper

import kotlinx.datetime.Instant
import tech.antibytes.mediawiki.DataModelContract
import tech.antibytes.wikibase.store.entity.data.dto.LanguageValuePair
import tech.antibytes.wikibase.store.entity.data.dto.RevisionedEntity
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.LanguageTag
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity

internal class RemoteEntityMapper : MapperContract.RemoteEntityMapper {
    private fun mapAliases(
        aliases: List<DataModelContract.LanguageValuePair>?
    ): List<String> = aliases?.map { alias -> alias.value } ?: emptyList()

    override fun toMonolingualEntity(
        language: LanguageTag,
        revisionedEntity: DataModelContract.RevisionedEntity,
        restrictions: List<String>
    ): EntityModelContract.MonolingualEntity {
        return MonolingualEntity(
            id = revisionedEntity.id,
            type = EntityModelContract.EntityType.valueOf(revisionedEntity.type.name),
            revision = revisionedEntity.revision,
            language = language,
            isEditable = restrictions.isEmpty(),
            label = revisionedEntity.labels[language]?.value,
            description = revisionedEntity.descriptions[language]?.value,
            aliases = mapAliases(revisionedEntity.aliases[language])
        )
    }

    private fun mapValue(
        language: LanguageTag,
        value: String?
    ): DataModelContract.LanguageValuePair {
        return LanguageValuePair(
            language,
            value ?: ""
        )
    }

    private fun mapAliases(language: LanguageTag, aliases: List<String>): List<DataModelContract.LanguageValuePair> {
        return aliases.map { alias -> LanguageValuePair(language, alias) }
    }

    override fun toRevisionedEntity(
        monolingualEntity: EntityModelContract.MonolingualEntity
    ): DataModelContract.RevisionedEntity {
        return RevisionedEntity(
            id = monolingualEntity.id,
            revision = monolingualEntity.revision,
            type = DataModelContract.EntityType.valueOf(monolingualEntity.type.name),
            lastModification = Instant.DISTANT_PAST, // TODO Use Clock.now, once a proper RequestQueue is in place
            labels = mapOf(
                monolingualEntity.language to mapValue(
                    monolingualEntity.language, monolingualEntity.label
                )
            ),
            descriptions = mapOf(
                monolingualEntity.language to mapValue(
                    monolingualEntity.language,
                    monolingualEntity.description
                )
            ),
            aliases = mapOf(
                monolingualEntity.language to mapAliases(
                    monolingualEntity.language,
                    monolingualEntity.aliases
                )
            ),
        )
    }
}
