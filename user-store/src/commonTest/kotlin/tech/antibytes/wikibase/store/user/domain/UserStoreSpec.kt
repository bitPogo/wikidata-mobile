/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.user.domain

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.test.coroutine.runBlockingTestWithTimeout
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import tech.antibytes.wikibase.store.mock.MwClientStub
import tech.antibytes.wikibase.store.mock.SharedFlowWrapperStub
import tech.antibytes.wikibase.store.mock.transfer.RepositoryStub
import tech.antibytes.wikibase.store.user.UserStoreContract
import tech.antibytes.wikibase.store.user.lang.UserStoreError
import tech.antibytes.wikibase.store.user.testScope1
import tech.antibytes.wikibase.store.user.testScope2
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class UserStoreSpec {
    private val fixture = kotlinFixture()
    private val remoteRepository = RepositoryStub()

    @Test
    fun `It fulfils UserStoreFactory`() {
        UserStore fulfils UserStoreContract.UserStoreFactory::class
    }

    @Test
    fun `Given getInstance is called it returns a UserStore`() {
        // When
        val store = UserStore.getInstance(
            MwClientStub(),
            { testScope1 },
            { testScope2 }
        )

        // Then
        store fulfils UserStoreContract.UserStore::class
    }

    @Test
    fun `Given login is called with a Username and Password it returns a Failure, if the RemoteRepository returns false`() {
        // Given
        val flow = MutableStateFlow<ResultContract<Boolean, Exception>>(
            Failure(UserStoreError.InitialState())
        )
        val result = Channel<ResultContract<Boolean, Exception>>()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        remoteRepository.login = { givenUsername, givenPassword ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword

            false
        }

        // When
        flow.onEach { item ->
            if (item.error !is UserStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        UserStore(
            { testScope1 },
            remoteRepository,
            flow,
            SharedFlowWrapperStub()
        ).login(username, password)

        // Then
        runBlockingTestWithTimeout {
            assertFailsWith<UserStoreError.LoginFailure> {
                result.receive().unwrap()
            }

            capturedUsername mustBe username
            capturedPassword mustBe password
        }
    }

    @Test
    fun `Given login is called it returns with a Username and Password it emits a Failure and counts the Attempts, if the RemoteRepository repeatedly returns false`() {
        // Given
        val flow = MutableStateFlow<ResultContract<Boolean, Exception>>(
            Failure(UserStoreError.InitialState())
        )
        val result = Channel<ResultContract<Boolean, Exception>>()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()

        remoteRepository.login = { _, _ -> false }

        // When
        flow.onEach { item ->
            if (item.error !is UserStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        val store = UserStore(
            { testScope1 },
            remoteRepository,
            flow,
            SharedFlowWrapperStub()
        )

        store.login(username, password)
        runBlockingTestWithTimeout {
            val error = assertFailsWith<UserStoreError.LoginFailure> {
                result.receive().unwrap()
            }

            error.attempts mustBe 0
        }

        // When
        store.login(username, password)

        // Then
        runBlockingTestWithTimeout {
            val error = assertFailsWith<UserStoreError.LoginFailure> {
                result.receive().unwrap()
            }

            error.attempts mustBe 1
        }
    }

    @Test
    fun `Given login is called with a Username and Password it emits a Failure, if the RemoteRepository throws an error`() {
        // Given
        val flow = MutableStateFlow<ResultContract<Boolean, Exception>>(
            Failure(UserStoreError.InitialState())
        )
        val result = Channel<ResultContract<Boolean, Exception>>()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()

        val expected = IllegalStateException()

        remoteRepository.login = { _, _ -> throw expected }

        // When
        flow.onEach { item ->
            if (item.error !is UserStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        val store = UserStore(
            { testScope1 },
            remoteRepository,
            flow,
            SharedFlowWrapperStub()
        )

        store.login(username, password)

        // Then
        runBlockingTestWithTimeout {
            val error = assertFails {
                result.receive().unwrap()
            }

            error sameAs expected
        }
    }

    @Test
    fun `Given login is called  with a Username and Password it emits Succes, if the RemoteRepository returns true`() {
        // Given
        val flow = MutableStateFlow<ResultContract<Boolean, Exception>>(
            Failure(UserStoreError.InitialState())
        )
        val result = Channel<ResultContract<Boolean, Exception>>()

        val username: String = fixture.fixture()
        val password: String = fixture.fixture()

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        remoteRepository.login = { givenUsername, givenPassword ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword

            true
        }

        // When
        flow.onEach { item ->
            if (item.error !is UserStoreError.InitialState) {
                result.send(item)
            }
        }.launchIn(testScope2)

        UserStore(
            { testScope1 },
            remoteRepository,
            flow,
            SharedFlowWrapperStub()
        ).login(username, password)

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() mustBe true

            capturedUsername mustBe username
            capturedPassword mustBe password
        }
    }

    @Test
    fun `Given logout is called it emits Succes`() {
        // Given
        val flow = MutableStateFlow<ResultContract<Boolean, Exception>>(
            Success(true)
        )
        val result = Channel<ResultContract<Boolean, Exception>>()

        // When
        flow.onEach { item -> result.send(item) }.launchIn(testScope2)

        UserStore(
            { testScope1 },
            remoteRepository,
            flow,
            SharedFlowWrapperStub()
        ).logout()

        // Then
        runBlockingTestWithTimeout {
            result.receive().unwrap() // skip initial value
            result.receive().unwrap() mustBe false
        }
    }
}
