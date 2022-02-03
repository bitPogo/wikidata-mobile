/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.get
import androidx.core.graphics.set
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikidata.mock.qr.transfer.Base64Stub

@RunWith(RobolectricTestRunner::class)
class BitmapMapperSpec {
    private val base64 = Base64Stub()
    private val fixture = kotlinFixture()

    @Before
    fun setUp() {
        base64.clear()
    }

    @Test
    fun `It fulfils Mapper`() {
        BitmapMapper(base64, 2) fulfils QrCodeDataContract.Mapper::class
    }

    @Test
    fun `Given mapToBitmap is called with a ByteArray it maps it to a Bitmap`() {
        // Given
        val array = byteArrayOf(0, 1, 0, 1)

        // When
        val actual = BitmapMapper(base64, 2).mapToBitmap(array)

        // Then
        actual.width mustBe 2
        actual.height mustBe 2
        actual[0, 0] mustBe Color.WHITE
        actual[0, 1] mustBe Color.BLACK
        actual[1, 0] mustBe Color.WHITE
        actual[1, 1] mustBe Color.BLACK
    }

    @Test
    fun `Given mapToBitmap is called with a String it maps it to a Bitmap`() {
        // Given
        val encoded: String = fixture.fixture()
        val array = byteArrayOf(0, 1, 0, 1)

        var capturedEncodedBitmap: String? = null
        base64.decode = { givenEncodedBitmap ->
            capturedEncodedBitmap = givenEncodedBitmap

            array
        }

        // When
        val actual = BitmapMapper(base64, 2).mapToBitmap(encoded)

        // Then
        capturedEncodedBitmap mustBe encoded

        actual.width mustBe 2
        actual.height mustBe 2
        actual[0, 0] mustBe Color.WHITE
        actual[0, 1] mustBe Color.BLACK
        actual[1, 0] mustBe Color.WHITE
        actual[1, 1] mustBe Color.BLACK
    }

    @Test
    fun `Given mapToString is called with a Bitmap it returns a String`() {
        // Given
        val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.RGB_565)
        bitmap[0, 0] = Color.WHITE
        bitmap[0, 1] = Color.BLACK
        bitmap[1, 0] = Color.WHITE
        bitmap[1, 1] = Color.BLACK

        val encoded: String = fixture.fixture()

        var capturedByteArray: ByteArray = ByteArray(0)
        base64.encodeToString = { givenBytes ->
            capturedByteArray = givenBytes

            encoded
        }

        // When
        val actual = BitmapMapper(base64, 2).mapToString(bitmap)

        // Then
        capturedByteArray.contentEquals(byteArrayOf(0, 1, 0, 1))

        actual sameAs encoded
    }
}
