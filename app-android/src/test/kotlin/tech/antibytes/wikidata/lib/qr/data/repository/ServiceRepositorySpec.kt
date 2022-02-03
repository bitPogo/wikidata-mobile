/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data.repository

import android.graphics.Bitmap
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikidata.lib.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.data.MapperStub
import tech.antibytes.wikidata.mock.qr.data.QrCodeServiceStub

@RunWith(RobolectricTestRunner::class)
class ServiceRepositorySpec {
    private val fixture = kotlinFixture()
    private val service = QrCodeServiceStub()
    private val mapper = MapperStub()

    @Before
    fun setUp() {
        service.clear()
        mapper.clear()
    }

    @Test
    fun `It fulfils Repository`() {
        QrCodeServiceRepository(service, mapper) fulfils DomainContract.ServiceRepository::class
    }

    @Test
    fun `Given createQrCode is called it returns a Bitmap for the given url`() {
        // Given
        val url: String = fixture.fixture()
        val data = byteArrayOf(0, 1, 0, 1)

        val expected = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)

        var capturedUrl: String? = null
        service.create = { givenUrl ->
            capturedUrl = givenUrl
            data
        }

        var capturedByteArray: ByteArray? = null
        mapper.mapToBitmap = { givenBytes ->
            capturedByteArray = givenBytes

            expected
        }

        runBlocking {
            // When
            val actual = QrCodeServiceRepository(service, mapper).createQrCode(url)

            // Then
            capturedUrl mustBe url
            capturedByteArray sameAs data
            actual sameAs expected
        }
    }
}
