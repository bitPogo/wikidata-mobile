/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.di

import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.wikibase.store.database.QrCodeQueries
import tech.antibytes.wikibase.store.database.WikibaseDataBase
import tech.antibytes.wikibase.store.database.entity.EntityQueries
import tech.antibytes.wikibase.store.database.page.PageQueries
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract
import tech.antibytes.wikidata.lib.qr.data.QrCodeDataContract
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.QrCodeQueriesStub
import tech.antibytes.wikidata.mock.qr.domain.ServiceRepositoryStub
import tech.antibytes.wikidata.mock.qr.domain.StorageRepositoryStub
import tech.antibytes.wikidata.mock.qr.transfer.Base64Stub
import tech.antibytes.wikidata.mock.qr.transfer.MapperStub
import tech.antibytes.wikidata.mock.qr.transfer.QrCodeServiceStub

class DependencyProviderSpec {
    @Test
    fun `Given provideBase64 is called it returns a Base64 instance`() {
        // When
        val actual = DependencyProvider.provideBase64()

        // Then
        actual fulfils QrCodeDataContract.Base64::class
    }

    @Test
    fun `Given provideMapper is called it, with a Base64 instance returns a Mapper instance`() {
        // Given
        val base64 = Base64Stub()

        // When
        val actual = DependencyProvider.provideMapper(base64)

        // Then
        actual fulfils QrCodeDataContract.Mapper::class
    }

    @Test
    fun `Given provideService is called it instance returns a QrCodeService instance`() {
        // When
        val actual = DependencyProvider.provideQrCodeService()

        // Then
        actual fulfils QrCodeDataContract.Service::class
    }

    @Test
    fun `Given provideServiceRepository is called, with a Mapper and QrCodeService it instance returns a ServiceRepository instance`() {
        // Given
        val mapper = MapperStub()
        val service = QrCodeServiceStub()

        // When
        val actual = DependencyProvider.provideServiceRepository(mapper, service)

        // Then
        actual fulfils DomainContract.ServiceRepository::class
    }

    @Test
    fun `Given provideStorageRepository is called, with a Mapper and WikibaseDatabase it instance returns a StorageRepository instance`() {
        // Given
        val mapper = MapperStub()
        val database = WikibaseDatabaseStub(QrCodeQueriesStub())

        // When
        val actual = DependencyProvider.provideStorageRepository(mapper, database)

        // Then
        actual fulfils DomainContract.StorageRepository::class
    }

    @Test
    fun `Given provideQrCodeStorage is called, with a Mapper and WikibaseDatabase it instance returns a StorageRepository instance`() {
        // Given
        val mapper = MapperStub()
        val database = WikibaseDatabaseStub(QrCodeQueriesStub())

        // When
        val actual = DependencyProvider.provideStorageRepository(mapper, database)

        // Then
        actual fulfils DomainContract.StorageRepository::class
    }

    @Test
    fun `Given provideQrCodeStore is called, with a ServiceRepository, StorageRepository and Dispatcher it instance returns a QrCodeStore instance`() {
        // Given
        val serviceRepository = ServiceRepositoryStub()
        val storageRepository = StorageRepositoryStub()
        val dispatcher = CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        // When
        val store = DependencyProvider.provideQrCodeStore(
            serviceRepository,
            storageRepository,
            dispatcher,
            dispatcher
        )

        // Then
        store fulfils QrCodeStoreContract.QrCodeStore::class
    }
}

private class WikibaseDatabaseStub(
    override val qrCodeQueries: QrCodeQueries,
) : WikibaseDataBase {
    override val entityQueries: EntityQueries
        get() = TODO()
    override val pageQueries: PageQueries
        get() = TODO()

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }
}

