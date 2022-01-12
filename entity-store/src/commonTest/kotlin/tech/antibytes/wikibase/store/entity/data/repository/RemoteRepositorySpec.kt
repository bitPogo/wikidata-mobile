/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.data.repository

import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.mock.MwClientStub
import kotlin.test.BeforeTest
import kotlin.test.Test

class RemoteRepositorySpec {
    private val fixture = kotlinFixture()
    private val client = MwClientStub()

    @BeforeTest
    fun setUp() {
        client.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        RemoteRepository(client) fulfils DomainContract.Repository::class
    }
}
