/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.DarkWhite
import tech.antibytes.wikidata.app.ui.theme.DeepBlack
import tech.antibytes.wikidata.app.ui.theme.DeepRed
import tech.antibytes.wikidata.app.ui.theme.LightDarkGray
import tech.antibytes.wikidata.app.ui.theme.LightGray

@Composable
fun PasswordFieldIcon(
    show: Boolean,
    onClick: () -> Unit
) {
    val (description, icon) = if (show) {
        R.string.hide_password to Icons.Filled.VisibilityOff
    } else {
        R.string.show_password to Icons.Filled.Visibility
    }

    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(description)
        )
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) }
    val labelField = @Composable { Text(text = label) }
    val transformation = if (showPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    val icon = @Composable {
        PasswordFieldIcon(show = showPassword) {
            showPassword = !showPassword
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = labelField,
        placeholder = labelField,
        singleLine = true,
        visualTransformation = transformation,
        trailingIcon = icon,
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = DeepBlack,
            disabledTextColor = LightDarkGray,
            cursorColor = DeepBlack,
            backgroundColor = DarkWhite,
            placeholderColor = LightDarkGray,
            focusedBorderColor = Blue,
            unfocusedBorderColor = LightGray,
            disabledBorderColor = LightGray,
            errorBorderColor = DeepRed,
            focusedLabelColor = DeepBlack,
            unfocusedLabelColor = DeepBlack,
            disabledLabelColor = DeepBlack,
            errorLabelColor = DeepBlack,
            trailingIconColor = LightDarkGray,
            disabledTrailingIconColor = Color.Transparent,
            errorTrailingIconColor = LightDarkGray
        ),
        isError = isError
    )
}
