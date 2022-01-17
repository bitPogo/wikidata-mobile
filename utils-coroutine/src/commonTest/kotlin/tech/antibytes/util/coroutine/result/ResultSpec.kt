/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.result

import co.touchlab.stately.isFrozen
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFails

val fixture = kotlinFixture()

class SuccessSpec {
    @Test
    fun `It fulfils ResultContract`() {
        Success<String, Throwable>(fixture.fixture()) fulfils ResultContract::class
    }

    @Test
    @Ignore // NativeOnly
    fun `It is frozen ResultContract`() {
        Success<String, Throwable>(fixture.fixture()).isFrozen mustBe true
    }

    @Test
    fun `Given isSuccess is called it returns true`() {
        // Given
        val succ = Success<String, Throwable>(fixture.fixture())

        // When
        val result = succ.isSuccess()

        // Then
        result mustBe true
    }

    @Test
    fun `Given isError is called it returns false`() {
        // Given
        val succ = Success<String, Throwable>(fixture.fixture())

        // When
        val result = succ.isError()

        // Then
        result mustBe false
    }

    @Test
    fun `Given unwarp is called it returns the wraped value`() {
        // Given
        val value: String = fixture.fixture()

        val succ = Success<String, Throwable>(value)

        // When
        val result = succ.unwrap()

        // Then
        result mustBe value
    }
}

class FailureSpec {
    @Test
    fun `It fulfils ResultContract`() {
        Failure<String, Throwable>(RuntimeException()) fulfils ResultContract::class
    }

    @Test
    @Ignore // NativeOnly
    fun `It is frozen ResultContract`() {
        Failure<String, Throwable>(RuntimeException()).isFrozen mustBe true
    }

    @Test
    fun `Given isSuccess is called it returns false`() {
        // Given
        val err = Failure<String, Throwable>(RuntimeException())

        // When
        val result = err.isSuccess()

        // Then
        result mustBe false
    }

    @Test
    fun `Given isError is called it returns true`() {
        // Given
        val err = Failure<String, Throwable>(RuntimeException())

        // When
        val result = err.isError()

        // Then
        result mustBe true
    }

    @Test
    fun `Given unwarp is called it fails with the wraped value`() {
        // Given
        val expected = RuntimeException()

        val err = Failure<String, Throwable>(expected)

        // Then
        val result = assertFails {
            err.unwrap()
        }

        result sameAs expected
    }
}
