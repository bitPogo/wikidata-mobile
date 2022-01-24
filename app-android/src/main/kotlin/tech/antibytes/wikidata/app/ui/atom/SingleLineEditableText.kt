/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.DarkWhite
import tech.antibytes.wikidata.app.ui.theme.DeepBlack
import tech.antibytes.wikidata.app.ui.theme.DeepRed
import tech.antibytes.wikidata.app.ui.theme.LightDarkGray
import tech.antibytes.wikidata.app.ui.theme.LightGray

@Composable
fun SingleLineEditableText(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean = false
) {
    val labelField = @Composable { Text(text = label) }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = labelField,
        placeholder = labelField,
        singleLine = true,
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
            errorLabelColor = DeepBlack
        ),
        isError = isError
    )
}
