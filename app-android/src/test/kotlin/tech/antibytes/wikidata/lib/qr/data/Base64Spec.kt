/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.lib.qr.data

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@RunWith(RobolectricTestRunner::class)
class Base64Spec {
    @Test
    fun `It fulfils Base64`() {
        Base64() fulfils QrCodeDataContract.Base64::class
    }

    @Test
    fun `Given encodeToString is called with a ByteArray, it returns an encoded String`() {
        // Given
        val bytes: ByteArray = "Encode this String!".toByteArray()

        // When
        val actual = Base64().encodeToString(bytes)

        // Then
        actual mustBe "RW5jb2RlIHRoaXMgU3RyaW5nIQ=="
    }

    @Test
    fun `Given decode is called with a String, it returns an encoded ByteArray`() {
        // Given
        val string = "RW5jb2RlIHRoaXMgU3RyaW5nIQ=="

        // When
        val actual = Base64().decode(string)

        // Then
        String(actual) mustBe "Encode this String!"
    }
}
