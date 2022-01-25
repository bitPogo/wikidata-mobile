/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.atom.Logo
import tech.antibytes.wikidata.app.ui.atom.PasswordField
import tech.antibytes.wikidata.app.ui.atom.SimpleButton
import tech.antibytes.wikidata.app.ui.atom.SingleLineEditableText

@Composable
fun LoginScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = 5.dp,
            horizontal = 20.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(
                PaddingValues(
                    top = 50.dp,
                    bottom = 25.dp
                )
            )
        ) {
            Logo()
        }
        SingleLineEditableText(
            label = stringResource(R.string.login_username),
            value = "",
            onChange = {}
        )
        PasswordField(
            label = stringResource(R.string.login_password),
            value = "",
            onChange = {}
        )
        Row(
            modifier = Modifier.padding(
                PaddingValues(
                    top = 25.dp
                )
            )
        ) {
            SimpleButton(label = stringResource(R.string.login_login)) {
            }
        }
    }
}
