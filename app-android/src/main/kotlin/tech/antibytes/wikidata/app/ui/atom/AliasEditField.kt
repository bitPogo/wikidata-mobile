/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.ui.theme.DarkWhite
import tech.antibytes.wikidata.app.ui.theme.DeepBlack
import tech.antibytes.wikidata.app.ui.theme.DeepRed
import tech.antibytes.wikidata.app.ui.theme.LightDarkGray

// TODO: Get rid of this snowflake
@Composable
fun AliasEditField(
    label: String?,
    value: String,
    onChange: (String) -> Unit,
) {
    val focusOuterState = remember { mutableStateOf(false) }
    val background = if (focusOuterState.value) {
        DarkWhite
    } else {
        Color.Transparent
    }

    val colours = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DeepBlack,
        disabledTextColor = LightDarkGray,
        cursorColor = DeepBlack,
        placeholderColor = LightDarkGray,
        backgroundColor = background,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        errorBorderColor = DeepRed,
        focusedLabelColor = DeepBlack,
        unfocusedLabelColor = DeepBlack,
        disabledLabelColor = DeepBlack,
        errorLabelColor = DeepBlack
    )
    TextField(
        value = value,
        onValueChange = onChange,
        label = if (label != null) {
            @Composable {
                Text(text = label)
            }
        } else {
            null
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 3.dp,
                bottom = 3.dp
            )
            .onFocusChanged { focusState ->
                focusOuterState.value = focusState.hasFocus
            },
        colors = colours,
        shape = RoundedCornerShape(0.dp),
        isError = false,
    )
}
