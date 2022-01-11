/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class SuspendingFunctionWrapperSpec {
    @Test
    fun `It fulfils SuspendingFunctionWrapperFactory`() {
        SuspendingFunctionWrapper fulfils PublicApi.SuspendingFunctionWrapperFactory::class
    }

    @Test
    fun `Given getInstance is called with a CoroutineScope and a supending Function it returns a SuspendingFunctionWrapper`() {
        SuspendingFunctionWrapper.getInstance(
            CoroutineScope(Dispatchers.Default),
            suspend { /* Do nothing*/ }
        ) fulfils PublicApi.SuspendingFunctionWrapper::class
    }

    @Test
    fun `It exposes its wrapped supending Function`() {
        // Given
        val function = suspend { /* Do nothing*/ }

        // When
        val result = SuspendingFunctionWrapper.getInstance(
            GlobalScope,
            function
        ).wrappedFunction

        // Then
        result sameAs function
    }

    @Test
    fun `Given subscribe is called it returns a Job`() {
        // Given
        val function = suspend { /* Do nothing*/ }

        // When
        val result = SuspendingFunctionWrapper.getInstance(
            GlobalScope,
            function
        ).subscribe({}, {})

        // Then
        result fulfils Job::class
    }

    @Test
    fun `Given subscribe is called with a Closure to receive Item it delegates the emitted Item to there`() {
        // Given
        val item = object {}
        val function = suspend { item }

        val capturedItem = Channel<Any>()

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            GlobalScope,
            function
        ).subscribe(
            { delegatedItem ->
                GlobalScope.launch {
                    capturedItem.send(delegatedItem)
                }
            },
            {},
        )

        // Then
        runBlockingTest {
            withTimeout(2000) {
                job.join()

                capturedItem.receive() sameAs item
            }
        }
    }

    @Test
    fun `Given subscribe is called with a Closure to receive Errors it delegates the emitted Error to there`() {
        // Given
        val exception = RuntimeException()
        val function = suspend { throw exception }

        val capturedError = Channel<Throwable>()

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            GlobalScope,
            function
        ).subscribe(
            {},
            { error ->
                GlobalScope.launch {
                    capturedError.send(error)
                }
            },
        )

        // Then
        runBlockingTest {
            withTimeout(2000) {
                job.join()

                capturedError.receive() sameAs exception
            }
        }
    }

    @Test
    fun `Given subscribe is called in a scope it launches in it`() {
        // Given
        val scope = testScope
        val function = suspend { delay(5000) }

        // When
        val job = SuspendingFunctionWrapper.getInstance(
            scope,
            function
        ).subscribe({}, {},)

        // Then
        job.isActive mustBe true
        scope.cancel()
        job.isActive mustBe false
    }
}

expect val testScope: CoroutineScope
