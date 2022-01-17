/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.entity.transfer

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.isNot
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.entity.domain.DomainContract
import tech.antibytes.wikibase.store.entity.transfer.mapper.MapperContract
import tech.antibytes.wikibase.store.mock.EntityQueriesStub
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.data.mapper.LocalEntityMapperStub
import tech.antibytes.wikibase.store.mock.data.mapper.RemoteEntityMapperStub
import kotlin.test.Test

class DataTransferKoinSpec {
    @Test
    fun `Given resolveTransferModule is called it contains a LocalEntityMapper`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveDataTransferModule()
            )
        }

        // When
        val mapper: MapperContract.LocalEntityMapper = koin.koin.get()

        // Then
        mapper isNot null
    }

    @Test
    fun `Given resolveTransferModule is called it contains a RemoteEntityMapper`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveDataTransferModule()
            )
        }

        // When
        val mapper: MapperContract.RemoteEntityMapper = koin.koin.get()

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
                    single<MapperContract.RemoteEntityMapper> {
                        RemoteEntityMapperStub()
                    }

                    single<PublicApi.Client> {
                        MwClientStub()
                    }
                }
            )
        }

        // When
        val repository: DomainContract.Repository = koin.koin.get(named(DomainContract.DomainKoinIds.REMOTE))

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
                    single<MapperContract.LocalEntityMapper> {
                        LocalEntityMapperStub()
                    }

                    single<EntityQueries> {
                        EntityQueriesStub()
                    }
                }
            )
        }

        // When
        val repository: DomainContract.Repository = koin.koin.get(named(DomainContract.DomainKoinIds.LOCAL))

        // Then
        repository isNot null
    }
}
