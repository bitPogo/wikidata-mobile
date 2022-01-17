/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.util.coroutine.wrapper

import co.touchlab.stately.isFrozen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Ignore
import kotlin.test.Test

class SharedFlowWrapperSpec {
    val fixture = kotlinFixture()

    @Test
    fun `It fulfils SharedFlowWrapperFactory`() {
        SharedFlowWrapper fulfils CoroutineWrapperContract.SharedFlowWrapperFactory::class
    }

    @Test
    fun `It fulfils SharedFlowWrapper`() {
        SharedFlowWrapper.getInstance<Any, RuntimeException>(
            MutableSharedFlow(),
            { GlobalScope }
        ) fulfils CoroutineWrapperContract.SharedFlowWrapper::class
    }

    @Test
    @Ignore // Native only
    fun `It is frozen`() {
        SharedFlowWrapper.getInstance<Any, RuntimeException>(
            MutableSharedFlow(),
            { GlobalScope }
        ).isFrozen mustBe true
    }

    @Test
    fun `Given subscribe is called, with a onEach parameter, it channels emitted values to the subscriber`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>()
        val expected = Success<String, RuntimeException>(fixture.fixture())
        val channel = Channel<ResultContract<String, RuntimeException>>()

        val subscription = { value: ResultContract<String, RuntimeException> ->
            println("abc")
            testScope1.launch {
                channel.send(value)
            }

            Unit
        }

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 }
        )

        runBlockingTest {
            wrapped.subscribe(subscription)
        }

        testScope2.launch {
            flow.emit(expected)
        }

        // Then
        runBlockingTest {
            withTimeout(2000) {
                channel.receive() mustBe expected
            }
        }
    }

    @Test
    fun `It exposes its Cache`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>(replay = 1)
        val expected = Success<String, RuntimeException>(fixture.fixture())

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 }
        )

        testScope1.launch {
            wrapped.subscribe { }
        }

        testScope2.launch {
            flow.emit(expected)
        }

        // Then
        runBlockingTest {
            wrapped.replayCache mustBe listOf(expected)
        }
    }
}
