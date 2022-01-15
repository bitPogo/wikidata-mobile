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
    class MissingEntity(id: String, language: String) : EntityStoreError(
        "Missing Entity ($id) in Language ($language)"
    )
    class CreationFailure(language: String) : EntityStoreError(
        "Cannot create Entity in Language ($language)"
    )
    class EditFailure(id: String, language: String) : EntityStoreError(
        "Cannot edit Entity ($id) in Language ($language)"
    )
}
