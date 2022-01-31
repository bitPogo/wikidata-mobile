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
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Ignore
import kotlin.test.Test

class SharedFlowWrapperSpec {
    private val fixture = kotlinFixture()

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

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 }
        )

        wrapped.subscribe { value: ResultContract<String, RuntimeException> ->
            testScope1.launch {
                channel.send(value)
            }

            Unit
        }

        runBlockingTest {
            testScope2.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTestWithTimeout {
            channel.receive() mustBe expected
        }
    }

    @Test
    fun `Given subscribeWithSuspending is called, with a onEach Parameter, which is supspending, it channels emitted values to the subscriber`() {
        // Given
        val flow = MutableSharedFlow<ResultContract<String, RuntimeException>>()
        val expected = Success<String, RuntimeException>(fixture.fixture())
        val channel = Channel<ResultContract<String, RuntimeException>>()

        // When
        val wrapped = SharedFlowWrapper.getInstance(
            flow,
            { testScope1 }
        )

        wrapped.subscribeWithSuspendingFunction { result ->
            channel.send(result)
        }

        runBlockingTest {
            // FIXME: Note this small difference to subscribe -> The consumer scope must be same as the producer scope, execpt the given flow is a StateFlow
            testScope1.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTestWithTimeout {
            channel.receive() mustBe expected
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

        wrapped.subscribe { }

        runBlockingTest {
            testScope2.launch {
                flow.emit(expected)
            }
        }

        // Then
        runBlockingTest {
            wrapped.replayCache mustBe listOf(expected)
        }
    }
}
