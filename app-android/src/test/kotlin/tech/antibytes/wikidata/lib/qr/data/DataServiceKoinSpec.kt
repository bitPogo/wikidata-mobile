/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.util.test.isNot
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.QrCodeQueriesStub
import tech.antibytes.wikidata.mock.qr.transfer.Base64Stub
import tech.antibytes.wikidata.mock.qr.transfer.MapperStub
import tech.antibytes.wikidata.mock.qr.transfer.QrCodeServiceStub

class DataServiceKoinSpec {
    @Test
    fun `Given resolveDataService is called it contains Base64`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveQrCodeDataServiceModule()
            )
        }

        // When
        val base64: QrCodeDataContract.Base64 = koin.koin.get()

        // Then
        base64 isNot null
    }

    @Test
    fun `Given resolveDataService is called it contains Mapper`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                module { Base64Stub() },
                resolveQrCodeDataServiceModule()
            )
        }

        // When
        val mapper: QrCodeDataContract.Mapper = koin.koin.get()

        // Then
        mapper isNot null
    }

    @Test
    fun `Given resolveDataService is called it contains QrCodeService`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveQrCodeDataServiceModule()
            )
        }

        // When
        val service: QrCodeDataContract.Service = koin.koin.get()

        // Then
        service isNot null
    }

    @Test
    fun `Given resolveDataService is called it contains a ServiceRepository`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                module {
                    factory<QrCodeDataContract.Mapper> { MapperStub() }
                    factory<QrCodeDataContract.Service> { QrCodeServiceStub() }
                },
                resolveQrCodeDataServiceModule()
            )
        }

        // When
        val serviceRepository: DomainContract.ServiceRepository = koin.koin.get()

        // Then
        serviceRepository isNot null
    }

    @Test
    fun `Given resolveDataService is called it contains a StorageRepository`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                module {
                    factory<QrCodeDataContract.Mapper> { MapperStub() }
                    factory<QrCodeQueries> { QrCodeQueriesStub() }
                },
                resolveQrCodeDataServiceModule()
            )
        }

        // When
        val storageRepository: DomainContract.StorageRepository = koin.koin.get()

        // Then
        storageRepository isNot null
    }
}
