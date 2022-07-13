/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.util.coroutine.result.Failure
import tech.antibytes.util.coroutine.result.ResultContract
import tech.antibytes.util.coroutine.result.Success
import tech.antibytes.util.coroutine.wrapper.SharedFlowWrapper
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikibase.store.user.lang.UserStoreError
import tech.antibytes.wikidata.mock.UserStoreStub

class LoginViewModelSpec {
    private val fixture = kotlinFixture()
    private val flow: MutableStateFlow<ResultContract<Boolean, Exception>> = MutableStateFlow(
        Failure(UserStoreError.InitialState())
    )

    private val flowSurface = SharedFlowWrapper.getInstance(
        flow
    ) { CoroutineScope(Dispatchers.IO) }

    private val store = UserStoreStub(flowSurface)

    @Before
    fun setUp() {
        flow.value = Failure(UserStoreError.InitialState())
        store.clear()
    }

    @Test
    fun `It fulfils LoginViewModel`() {
        val viewModel = LoginViewModel(store)

        viewModel fulfils LoginContract.LoginViewModel::class
        viewModel fulfils ViewModel::class
    }

    @Test
    fun `Its default state is false`() {
        LoginViewModel(store).loginState.value mustBe LoginContract.LoginState.LoggedOut
    }

    @Test
    fun `Given a username is updated, it emits the changes`() {
        // Given
        val username: String = fixture.fixture()
        val result = Channel<String>()

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.username.collectLatest { givenUsername ->
                result.send(givenUsername)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setUsername(username)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe username
            }
        }
    }

    @Test
    fun `Given a username is updated, it trims the input`() {
        // Given
        val username = " ${fixture.fixture<String>()} "
        val result = Channel<String>()

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.username.collectLatest { givenUsername ->
                result.send(givenUsername)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setUsername(username)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe username.trim()
            }
        }
    }

    @Test
    fun `Given a password is updated, it emits the changes`() {
        // Given
        val password: String = fixture.fixture()
        val result = Channel<String>()

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.password.collectLatest { givenPassword ->
                result.send(givenPassword)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setPassword(password)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe password
            }
        }
    }

    @Test
    fun `Given a password is updated, it trims it`() {
        // Given
        val password = " ${fixture.fixture<String>()} "
        val result = Channel<String>()

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.password.collectLatest { givenPassword ->
                result.send(givenPassword)
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe ""
            }
        }

        // When
        viewModel.setPassword(password)

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe password.trim()
            }
        }
    }

    @Test
    fun `Given a login is called, it delegates the password and username to the store and emits its result and clears the password`() {
        // Given
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val resultLogin = Channel<LoginContract.LoginState>()
        val resultPassword = Channel<String>()
        val expected = true

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.loginState.collectLatest { isLoggedIn ->
                resultLogin.send(isLoggedIn)
            }
        }

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        store.login = { givenUsername, givenPassword ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword

            CoroutineScope(Dispatchers.Default).launch {
                flow.emit(Success(expected))
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                resultLogin.receive() mustBe LoginContract.LoginState.LoggedOut
            }
        }

        // When
        viewModel.setUsername(username)
        viewModel.setPassword(password)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.password.collectLatest { givenPassword ->
                resultPassword.send(givenPassword)
            }
        }

        viewModel.login()

        // Then
        runBlocking {
            withTimeout(2000) {
                resultLogin.receive() mustBe LoginContract.LoginState.LoggedIn
                resultPassword.receive() mustBe ""
            }
        }

        capturedUsername mustBe username
        capturedPassword mustBe password
    }

    @Test
    fun `Given a login is called, it delegates the password and username to the store and emits its result with an ErrorState`() {
        // Given
        val username: String = fixture.fixture()
        val password: String = fixture.fixture()
        val result = Channel<LoginContract.LoginState>()

        // When
        val viewModel = LoginViewModel(store)

        CoroutineScope(Dispatchers.Default).launch {
            viewModel.loginState.collectLatest { isLoggedIn ->
                result.send(isLoggedIn)
            }
        }

        var capturedUsername: String? = null
        var capturedPassword: String? = null
        store.login = { givenUsername, givenPassword ->
            capturedUsername = givenUsername
            capturedPassword = givenPassword

            CoroutineScope(Dispatchers.Default).launch {
                flow.emit(Failure(RuntimeException()))
            }
        }

        // Then
        runBlocking {
            withTimeout(2000) {
                result.receive() mustBe LoginContract.LoginState.LoggedOut
            }
        }

        // When
        viewModel.setUsername(username)
        viewModel.setPassword(password)
        viewModel.login()

        // Then
        runBlocking {
            withTimeout(2000) {
                val error = result.receive()

                error fulfils LoginContract.LoginState.AuthenticationError::class
                (error as LoginContract.LoginState.AuthenticationError).reason mustBe LoginContract.Reason.WRONG_USERNAME_OR_PASSWORD
            }
        }

        capturedUsername mustBe username
        capturedPassword mustBe password
    }
}
