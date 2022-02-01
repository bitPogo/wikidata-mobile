/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.atom.Logo
import tech.antibytes.wikidata.app.ui.atom.PasswordField
import tech.antibytes.wikidata.app.ui.atom.SimpleButton
import tech.antibytes.wikidata.app.ui.atom.SingleLineEditableText

@Composable
fun LoginScreen(
    navigator: LoginContract.Navigator = LoginContract.Navigator { },
    loginViewModel: LoginContract.LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val username = loginViewModel.username.collectAsState()
    val password = loginViewModel.password.collectAsState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = 5.dp,
            horizontal = 20.dp
        )
    ) {
        item {
            Spacer(modifier = Modifier.height(50.dp))

            Row {
                Logo()
            }
            Spacer(modifier = Modifier.height(25.dp))

            SingleLineEditableText(
                label = stringResource(R.string.login_username),
                value = username.value,
                onChange = loginViewModel::setUsername
            )
            PasswordField(
                label = stringResource(R.string.login_password),
                value = password.value,
                onChange = loginViewModel::setPassword
            )
            Spacer(modifier = Modifier.height(25.dp))
            Row {
                SimpleButton(
                    label = stringResource(R.string.login_login),
                    onClick = loginViewModel::login
                )
            }
        }
    }
}
