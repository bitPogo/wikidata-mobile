/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.mediawiki.annotation.InternalKoinModuleScope
import tech.antibytes.wikibase.store.page.domain.DomainContract
import tech.antibytes.wikibase.store.page.transfer.mapper.SearchEntityMapper
import tech.antibytes.wikibase.store.page.transfer.repository.LocalRepository
import tech.antibytes.wikibase.store.page.transfer.repository.RemoteRepository

internal fun resolveDataTransferModule(): Module {
    return module {
        @InternalKoinModuleScope
        factory<DataTransferContract.SearchEntityMapper> {
            SearchEntityMapper()
        }

        factory<DomainContract.RemoteRepository>(named(DomainContract.DomainKoinIds.REMOTE)) {
            RemoteRepository(
                get(),
                get()
            )
        }

        factory<DomainContract.LocalRepository>(named(DomainContract.DomainKoinIds.LOCAL)) {
            LocalRepository(get())
        }
    }
}
