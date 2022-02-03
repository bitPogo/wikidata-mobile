/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikidata.app.di.FlowDispatcher
import tech.antibytes.wikidata.app.di.IODispatcher
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract
import tech.antibytes.wikidata.lib.qr.data.Base64
import tech.antibytes.wikidata.lib.qr.data.BitmapMapper
import tech.antibytes.wikidata.lib.qr.data.QrCodeDataContract
import tech.antibytes.wikidata.lib.qr.data.QrCodeService
import tech.antibytes.wikidata.lib.qr.data.repository.QrCodeServiceRepository
import tech.antibytes.wikidata.lib.qr.data.repository.QrCodeStorageRepository
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.lib.qr.domain.QrCodeStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DependencyProvider {
    @Singleton
    @Provides
    fun provideBase64(): QrCodeDataContract.Base64 = Base64()

    @Singleton
    @Provides
    fun provideMapper(base64: QrCodeDataContract.Base64): QrCodeDataContract.Mapper {
        return BitmapMapper(base64)
    }

    @Singleton
    @Provides
    fun provideQrCodeService(): QrCodeDataContract.Service = QrCodeService()

    @Singleton
    @Provides
    fun provideServiceRepository(
        mapper: QrCodeDataContract.Mapper,
        service: QrCodeDataContract.Service
    ): DomainContract.ServiceRepository = QrCodeServiceRepository(service, mapper)

    @Singleton
    @Provides
    fun provideStorageRepository(
        mapper: QrCodeDataContract.Mapper,
        database: WikibaseDataBase
    ): DomainContract.StorageRepository = QrCodeStorageRepository(database.qrCodeQueries, mapper)

    @Singleton
    @Provides
    fun provideQrCodeStore(
        serviceRepository: DomainContract.ServiceRepository,
        storageRepository: DomainContract.StorageRepository,
        @IODispatcher ioDispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher,
        @FlowDispatcher flowDispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher
    ): QrCodeStoreContract.QrCodeStore = QrCodeStore(serviceRepository, storageRepository, ioDispatcher, flowDispatcher)
}
