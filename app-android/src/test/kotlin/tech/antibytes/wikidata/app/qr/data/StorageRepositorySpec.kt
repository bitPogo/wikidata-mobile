/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.qr.data

import android.graphics.Bitmap
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.qr.domain.DomainContract
import tech.antibytes.wikidata.mock.qr.QrCodeQueriesStub
import tech.antibytes.wikidata.mock.qr.QueryStub
import tech.antibytes.wikidata.mock.qr.SqlCursorStub
import tech.antibytes.wikidata.mock.qr.transfer.MapperStub

@RunWith(RobolectricTestRunner::class)
class StorageRepositorySpec {
    private val fixture = kotlinFixture()
    private val queries = QrCodeQueriesStub()
    private val mapper = MapperStub()

    @Test
    fun `It fulfils StorageRepository`() {
        StorageRepository(queries, mapper) fulfils DomainContract.StorageRepository::class
    }

    @Test
    fun `Given fetchQrCode is called with a Url, it retrieves the encoded QrCode from the Storage`() {
        // Given
        val url: String = fixture.fixture()
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val encoded: String = fixture.fixture()

        val nexts = mutableListOf(true, false)
        val fetchQrCodeCursor = SqlCursorStub { nexts.removeFirst() }
        val fetchQrCodeQuery = QueryStub(
            { encoded },
            { fetchQrCodeCursor }
        )

        var capturedUrl: String? = null
        queries.fetchQrCode = { givenUrl ->
            capturedUrl = givenUrl

            fetchQrCodeQuery
        }

        var capturedEncodedString: String? = null
        mapper.mapFromString = { givenEncoded ->
            capturedEncodedString = givenEncoded

            bitmap
        }

        runBlocking {
            // When
            val actual = StorageRepository(queries, mapper).fetchQrCode(url)

            // Then
            capturedUrl mustBe url
            capturedEncodedString mustBe encoded

            actual mustBe bitmap
        }
    }

    @Test
    fun `Given fetchQrCode is called with a Url, it returns null if no QrCode is stored`() {
        // Given
        val url: String = fixture.fixture()
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)

        val nexts = mutableListOf(false)
        val fetchQrCodeCursor = SqlCursorStub { nexts.removeFirst() }
        val fetchQrCodeQuery = QueryStub(
            { fixture.fixture<String>() },
            { fetchQrCodeCursor }
        )

        var capturedUrl: String? = null
        queries.fetchQrCode = { givenUrl ->
            capturedUrl = givenUrl

            fetchQrCodeQuery
        }

        runBlocking {
            // When
            val actual = StorageRepository(queries, mapper).fetchQrCode(url)

            // Then
            capturedUrl mustBe url

            actual mustBe null
        }
    }

    @Test
    fun `Given storeQrCode is called with a Url and Bitmap it stores the encoded Bitmap`() {
        // Given
        val url: String = fixture.fixture()
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        val encoded: String = fixture.fixture()

        var capturedBitmap: Bitmap? = null
        mapper.mapToString = { givenBitmap ->
            capturedBitmap = givenBitmap

            encoded
        }

        var capturedUrl: String? = null
        var capturedEncodedString: String? = null
        queries.addQrCode = { givenUrl, givenEncodedString ->
            capturedUrl = givenUrl
            capturedEncodedString = givenEncodedString
        }

        // When
        runBlocking {
            // When
            val actual = StorageRepository(queries, mapper).storeQrCode(url, bitmap)

            // Then
            capturedBitmap mustBe bitmap

            capturedUrl mustBe url
            capturedEncodedString mustBe encoded

            actual mustBe Unit
        }
    }
}
