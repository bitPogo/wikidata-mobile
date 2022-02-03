/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.domain

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.lib.qr.QrCodeStoreContract
import tech.antibytes.wikidata.mock.qr.domain.ServiceRepositoryStub
import tech.antibytes.wikidata.mock.qr.domain.StorageRepositoryStub

@RunWith(RobolectricTestRunner::class)
class QrCodeStoreSpec {
    private val fixture = kotlinFixture()
    private val serviceRepository = ServiceRepositoryStub()
    private val storageRepository = StorageRepositoryStub()

    @Before
    fun setUp() {
        serviceRepository.clear()
        storageRepository.clear()
    }

    @Test
    fun `It fulfils QrCodeStorage`() {
        QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        ) fulfils QrCodeStoreContract.QrCodeStore::class
    }

    @Test
    fun `Given fetch is called and a QrCode is store it returns it from the Storage`() {
        // Given
        val url: String = fixture.fixture()
        val expected = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val actual = Channel<ResultContract<Bitmap, Exception>>()

        var capturedUrl: String? = null
        storageRepository.fetchQrCode = { givenUrl ->
            capturedUrl = givenUrl

            expected
        }

        val store = QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        )

        store.qrCode.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        store.fetch(url)

        // Then
        runBlocking {
            withTimeout(2000) {
                actual.receive().unwrap() mustBe expected
                capturedUrl mustBe url
            }
        }
    }

    @Test
    fun `Given fetch is called and a QrCode is not store it returns a QrCode while creating and storing`() {
        // Given
        val url: String = fixture.fixture()
        val expected = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val actual = Channel<ResultContract<Bitmap, Exception>>()

        storageRepository.fetchQrCode = { null }

        var capturedCreationUrl: String? = null
        serviceRepository.createQrCode = { givenUrl ->
            capturedCreationUrl = givenUrl

            expected
        }

        var capturedStoringUrl: String? = null
        var capturedStoringBitmap: Bitmap? = null
        storageRepository.storeQrCode = { givenUrl, givenBitmap ->
            capturedStoringUrl = givenUrl
            capturedStoringBitmap = givenBitmap
        }

        // When
        val store = QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        )

        store.qrCode.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        store.fetch(url)

        // Then
        runBlocking {
            withTimeout(2000) {
                actual.receive().unwrap() mustBe expected

                capturedCreationUrl mustBe url

                capturedStoringUrl mustBe url
                capturedStoringBitmap mustBe expected
            }
        }
    }

    @Test
    fun `Given fetch is called and a QrCode is store it propagates errors from the Storage`() {
        // Given
        val url: String = fixture.fixture()
        val expected = RuntimeException()
        val actual = Channel<ResultContract<Bitmap, Exception>>()

        var capturedUrl: String? = null
        storageRepository.fetchQrCode = { givenUrl ->
            capturedUrl = givenUrl

            throw expected
        }

        val store = QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        )

        store.qrCode.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        store.fetch(url)

        // Then
        runBlocking {
            withTimeout(2000) {
                actual.receive().error mustBe expected
                capturedUrl mustBe url
            }
        }
    }

    @Test
    fun `Given fetch is called and a QrCode is not store it propagates Errors form Creating`() {
        // Given
        val url: String = fixture.fixture()
        val expected = RuntimeException()
        val actual = Channel<ResultContract<Bitmap, Exception>>()

        storageRepository.fetchQrCode = { null }

        var capturedCreationUrl: String? = null
        serviceRepository.createQrCode = { givenUrl ->
            capturedCreationUrl = givenUrl

            throw expected
        }

        // When
        val store = QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        )

        store.qrCode.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        store.fetch(url)

        // Then
        runBlocking {
            withTimeout(2000) {
                actual.receive().error mustBe expected

                capturedCreationUrl mustBe url
            }
        }
    }

    @Test
    fun `Given fetch is called and a QrCode is not store it propagates Errors from storing`() {
        // Given
        val url: String = fixture.fixture()
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val expected = RuntimeException()
        val actual = Channel<ResultContract<Bitmap, Exception>>()

        storageRepository.fetchQrCode = { null }

        var capturedCreationUrl: String? = null
        serviceRepository.createQrCode = { givenUrl ->
            capturedCreationUrl = givenUrl

            bitmap
        }

        var capturedStoringUrl: String? = null
        var capturedStoringBitmap: Bitmap? = null
        storageRepository.storeQrCode = { givenUrl, givenBitmap ->
            capturedStoringUrl = givenUrl
            capturedStoringBitmap = givenBitmap

            throw expected
        }

        // When
        val store = QrCodeStore(
            serviceRepository,
            storageRepository,
            { CoroutineScope(Dispatchers.IO) },
            { CoroutineScope(Dispatchers.Default) }
        )

        store.qrCode.subscribeWithSuspendingFunction { result ->
            actual.send(result)
        }

        store.fetch(url)

        // Then
        runBlocking {
            withTimeout(2000) {
                actual.receive().error mustBe expected

                capturedCreationUrl mustBe url

                capturedStoringUrl mustBe url
                capturedStoringBitmap mustBe bitmap
            }
        }
    }
}
