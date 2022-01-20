/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page.transfer

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.isNot
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikibase.store.mock.client.MwClientStub
import tech.antibytes.wikibase.store.mock.database.PageQueriesStub
import tech.antibytes.wikibase.store.mock.transfer.mapper.SearchEntityMapperStub
import tech.antibytes.wikibase.store.page.domain.DomainContract
import kotlin.test.Test

class DataTransferKoinSpec {
    @Test
    fun `Given resolveTransferModule is called it contains a RemoteSearchEntryMapper`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveDataTransferModule()
            )
        }

        // When
        val mapper: DataTransferContract.SearchEntityMapper = koin.koin.get()

        // Then
        mapper isNot null
    }

    @Test
    fun `Given resolveTransferModule is called it contains a RemoteRepository`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveDataTransferModule(),
                module {
                    single<DataTransferContract.SearchEntityMapper> {
                        SearchEntityMapperStub()
                    }

                    single<PublicApi.Client> {
                        MwClientStub()
                    }
                }
            )
        }

        // When
        val repository: DomainContract.RemoteRepository = koin.koin.get(named(DomainContract.DomainKoinIds.REMOTE))

        // Then
        repository isNot null
    }

    @Test
    fun `Given resolveTransferModule is called it contains a LocalRepository`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveDataTransferModule(),
                module {
                    single<PageQueries> {
                        PageQueriesStub()
                    }
                }
            )
        }

        // When
        val repository: DomainContract.LocalRepository = koin.koin.get(named(DomainContract.DomainKoinIds.LOCAL))

        // Then
        repository isNot null
    }
}
