/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.entity.EntityStoreContract
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.MwClientStub
import kotlin.test.Test

class EntityStoreSpec {
    @Test
    fun `It fulfils EntityStoreFactory`() {
        EntityStore fulfils EntityStoreContract.EntityStoreFactory::class
    }

    @Test
    fun `Given getInstance is called with its appropriate Parameter it returns a EntityStore`() {
        // When
        val store = EntityStore.getInstance(
            MwClientStub(),
            EntityQueriesStub(),
            { CoroutineScope(Dispatchers.Default) },
            { CoroutineScope(Dispatchers.Default) }
        )

        // Then
        store fulfils EntityStoreContract.EntityStore::class
    }

    @Test
    fun `Given fetchEntity is called it `() {
        // Given

        // When

        //Then
    }
}
