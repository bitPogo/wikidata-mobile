/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.mustBe
import tech.antibytes.wikidata.app.ui.theme.WikidataMobileTheme

class LoginScreenSpec {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val fixture = kotlinFixture()

    private val loginState = MutableStateFlow<LoginContract.LoginState>(LoginContract.LoginState.LoggedOut)
    private val username = MutableStateFlow("")
    private val password = MutableStateFlow("")

    private val viewModel = LoginViewModelStub(loginState, username, password)

    @Before
    fun setUp() {
        viewModel.clear()

        loginState.update { LoginContract.LoginState.LoggedOut }
        username.update { "" }
        password.update { "" }
    }

    @Test
    fun It_renders_a_LoginScreen() {
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Username")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Password")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Log In")
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Username_changes_of_the_Viewmodel() {
        // Given
        val username: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        this.username.update { username }

        // Then
        composeTestRule
            .onNodeWithText(username)
            .assertIsDisplayed()
    }

    @Test
    fun It_propagates_Password_changes_of_the_Viewmodel() {
        // Given
        val password: String = fixture.fixture()

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        this.password.update { password }

        // Then
        composeTestRule
            .onNodeWithContentDescription("show password")
            .performClick()

        composeTestRule
            .onNodeWithText(password)
            .assertIsDisplayed()
    }

    @Test
    fun Given_a_user_enters_a_Username_it_propagates_it_to_the_ViewModel() {
        // Given
        val username: String = fixture.fixture()

        var capturedUsername: String? = null
        viewModel.setUsername = { givenUsername ->
            capturedUsername = givenUsername
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput(username)

        // Then
        capturedUsername mustBe username
    }

    @Test
    fun Given_a_user_enters_a_Password_it_propagates_it_to_the_ViewModel() {
        // Given
        val password: String = fixture.fixture()

        var capturedPassword: String? = null
        viewModel.setPassword = { givenPassword ->
            capturedPassword = givenPassword
        }
        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput(password)

        // Then
        capturedPassword mustBe password
    }

    @Test
    fun Given_a_user_clicks_the_LogIn_button_it_propagates_it_to_the_ViewModel() {
        // Given
        var wasClicked = false
        viewModel.login = { wasClicked = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    {},
                    viewModel,
                )
            }
        }

        composeTestRule
            .onNodeWithText("Log In")
            .performClick()

        // Then
        wasClicked mustBe true
    }

    @Test
    fun Given_a_login_is_not_successful_it_calls_not_the_navigator() {
        // Given
        var wasCalled = false
        val navigator = LoginContract.Navigator { wasCalled = true }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    navigator,
                    viewModel,
                )
            }
        }

        // Then
        wasCalled mustBe false
    }

    @Test
    fun Given_a_login_is_successful_it_calls_the_navigator() {
        // Given
        var wasCalled = false
        val navigator = LoginContract.Navigator { wasCalled = true }

        loginState.update { LoginContract.LoginState.LoggedIn }

        // When
        composeTestRule.setContent {
            WikidataMobileTheme {
                LoginScreen(
                    navigator,
                    viewModel,
                )
            }
        }

        // Then
        wasCalled mustBe true
    }
}

private class LoginViewModelStub(
    override val loginState: StateFlow<LoginContract.LoginState>,
    override val username: StateFlow<String>,
    override val password: StateFlow<String>,
    var setUsername: ((String) -> Unit)? = null,
    var setPassword: ((String) -> Unit)? = null,
    var login: (() -> Unit)? = null
) : LoginContract.LoginViewModel {
    override fun setUsername(username: String) {
        return setUsername?.invoke(username)
            ?: throw RuntimeException("Missing Sideeffect setUsername")
    }

    override fun setPassword(password: String) {
        return setPassword?.invoke(password)
            ?: throw RuntimeException("Missing Sideeffect setPassword")
    }

    override fun login() {
        return login?.invoke()
            ?: throw RuntimeException("Missing Sideeffect login")
    }

    fun clear() {
        setUsername = null
        setPassword = null
        login = null
    }
}
