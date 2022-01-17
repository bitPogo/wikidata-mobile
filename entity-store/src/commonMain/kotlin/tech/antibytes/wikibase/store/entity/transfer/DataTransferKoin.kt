/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.entity.WikibaseDataBase
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.transfer.mapper.LocalEntityMapper
import tech.antibytes.wikibase.store.entity.transfer.mapper.MapperContract
import tech.antibytes.wikibase.store.entity.transfer.mapper.RemoteEntityMapper
import tech.antibytes.wikibase.store.entity.transfer.repository.LocalRepository
import tech.antibytes.wikibase.store.entity.transfer.repository.RemoteRepository

internal fun resolveDataTransferModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<MapperContract.LocalEntityMapper> {
            LocalEntityMapper()
        }

        @InternalKoinModuleScope
        factory<MapperContract.RemoteEntityMapper> {
            RemoteEntityMapper()
        }

        factory<DomainContract.Repository>(named(DomainContract.DomainKoinIds.REMOTE)) {
            RemoteRepository(
                get(),
                get()
            )
        }

        factory<DomainContract.Repository>(named(DomainContract.DomainKoinIds.LOCAL)) {
            LocalRepository(
                get(),
                get()
            )
        }
    }
}
