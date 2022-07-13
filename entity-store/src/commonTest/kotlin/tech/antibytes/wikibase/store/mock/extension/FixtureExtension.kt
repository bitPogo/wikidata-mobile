/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.extension

import kotlinx.datetime.Instant
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.wikibase.store.entity.domain.model.EntityModelContract
import tech.antibytes.wikibase.store.entity.domain.model.MonolingualEntity

internal fun PublicApi.Fixture.monolingualEntityFixture(aliases: Int = 5): MonolingualEntity {
    return MonolingualEntity(
        id = this.fixture(),
        type = EntityModelContract.EntityType.ITEM,
        revision = this.fixture(),
        language = this.fixture(),
        lastModification = Instant.fromEpochMilliseconds(this.fixture()),
        isEditable = this.fixture(),
        label = this.fixture<String>(),
        description = this.fixture<String>(),
        aliases = this.listFixture(size = aliases),
    )
}
