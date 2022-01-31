/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.R
import tech.antibytes.wikidata.app.ui.atom.TopSearchBar
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.DeepBlack
import tech.antibytes.wikidata.app.ui.theme.LightDarkGray
import tech.antibytes.wikidata.app.ui.theme.White

@Composable
fun LanguageSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
) {
    val colours = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DeepBlack,
        disabledTextColor = LightDarkGray,
        cursorColor = DeepBlack,
        placeholderColor = LightDarkGray,
        backgroundColor = White,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
        focusedLabelColor = Color.Transparent,
        unfocusedLabelColor = Color.Transparent,
        disabledLabelColor = Color.Transparent,
        errorLabelColor = Color.Transparent
    )

    TopSearchBar(
        value = value,
        placeholder = stringResource(R.string.language_selector_placeholder),
        onValueChange = onValueChange,
        backgroundColour = Blue,
        textFieldColours = colours,
        textFieldShape = RoundedCornerShape(10.dp),
        textFieldModifier = {
            this.padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        },
    )
}
