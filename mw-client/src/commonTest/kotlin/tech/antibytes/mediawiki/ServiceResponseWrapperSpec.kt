/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.error.MwClientError
import tech.antibytes.mock.ConnectivityManagerStub
import tech.antibytes.mock.SuspendingFunctionWrapperFactoryStub
import tech.antibytes.mock.SuspendingFunctionWrapperStub
import tech.antibytes.util.coroutine.wrapper.CoroutineWrapperContract
import tech.antibytes.util.test.coroutine.runBlockingTest
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ServiceResponseWrapperSpec {
    private val fixture = kotlinFixture()
    private val connectivityManager = ConnectivityManagerStub()
    private val functionWrapperFactory = SuspendingFunctionWrapperFactoryStub()

    @BeforeTest
    fun setUp() {
        connectivityManager.clear()
        functionWrapperFactory.clear()
    }

    @Test
    fun `It fulfils ServiceResponseWrapper`() {
        ServiceResponseWrapper(
            connectivityManager,
            { CoroutineScope(Dispatchers.Default) },
            functionWrapperFactory
        ) fulfils MwClientContract.ServiceResponseWrapper::class
    }

    @Test
    fun `Given wrap is called it returns a SuspendingFunctionWrapper`() {
        // Given
        val function = suspend {}
        val dispatcher = CoroutineWrapperContract.CoroutineScopeDispatcher { CoroutineScope(Dispatchers.Default) }

        var capturedDispatcher: CoroutineWrapperContract.CoroutineScopeDispatcher? = null

        functionWrapperFactory.getInstance = { givenScope, givenFunction ->
            capturedDispatcher = givenScope

            SuspendingFunctionWrapperStub(givenFunction)
        }

        // When
        val wrapped = ServiceResponseWrapper(
            connectivityManager,
            dispatcher,
            functionWrapperFactory
        ).warp(function)

        // Then
        wrapped fulfils CoroutineWrapperContract.SuspendingFunctionWrapper::class
        capturedDispatcher sameAs dispatcher
    }

    @Test
    fun `Given wrap is called it holds a executable Suspending Function if there is a conncection`() {
        // Given
        val expected = fixture.fixture<String>()
        val function = suspend { expected }
        val dispatcher = { CoroutineScope(Dispatchers.Default) }

        connectivityManager.hasConnection = { true }
        functionWrapperFactory.getInstance = { _, givenFunction ->
            SuspendingFunctionWrapperStub(givenFunction)
        }

        // When
        val wrapped = ServiceResponseWrapper(
            connectivityManager,
            dispatcher,
            functionWrapperFactory
        ).warp(function)

        // Then
        runBlockingTest {
            wrapped.wrappedFunction.invoke() mustBe expected
        }
    }

    @Test
    fun `Given wrap is called it holds a fails if there is no conncection`() {
        // Given
        val expected = fixture.fixture<String>()
        val function = suspend { expected }
        val dispatcher = { CoroutineScope(Dispatchers.Default) }

        connectivityManager.hasConnection = { false }
        functionWrapperFactory.getInstance = { _, givenFunction ->
            SuspendingFunctionWrapperStub(givenFunction)
        }

        // When
        val wrapped = ServiceResponseWrapper(
            connectivityManager,
            dispatcher,
            functionWrapperFactory
        ).warp(function)

        // Then
        runBlockingTest {
            assertFailsWith<MwClientError.NoConnection> {
                wrapped.wrappedFunction.invoke()
            }
        }
    }
}
