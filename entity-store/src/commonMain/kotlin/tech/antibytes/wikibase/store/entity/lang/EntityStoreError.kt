/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.lang

sealed class EntityStoreError(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    class InitialState : EntityStoreError()

    class MissingEntity(id: String, language: String) : EntityStoreError(
        "Entity ($id) in Language ($language) not found."
    )

    class MutationFailure : EntityStoreError(
        "Cannot mutate Entity, since last event resulted in an error."
    )

    // TODO Remove if the StateFlow allows a repeating the last state
    class InvalidCreationState : EntityStoreError()
    class InvalidRollbackState : EntityStoreError()

    class CreationRemoteFailure(language: String) : EntityStoreError(
        "Cannot create Entity in Language ($language)"
    )

    class CreationLocalFailure(id: String, language: String) : EntityStoreError(
        "Cannot store created Entity ($id) in Language ($language)"
    )

    class UpdateRemoteFailure(id: String, language: String) : EntityStoreError(
        "Cannot edit Entity ($id) in Language ($language)"
    )

    class UpdateLocalFailure(id: String, language: String) : EntityStoreError(
        "Cannot store edited Entity ($id) in Language ($language)"
    )
}
