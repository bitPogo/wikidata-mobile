/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.antibytes.wikidata.app.ui.theme.Blue
import tech.antibytes.wikidata.app.ui.theme.DeepBlack
import tech.antibytes.wikidata.app.ui.theme.DeepRed
import tech.antibytes.wikidata.app.ui.theme.LightDarkGray
import tech.antibytes.wikidata.app.ui.theme.LightGray

@Preview
@Composable
fun PlainTopSearchBar() {
    TopSearchBar(
        "test",
        {},
    )
}

@Preview
@Composable
fun TopSearchBarWithIconsAndPlaceholder() {
    TopSearchBar(
        "",
        {},
        placeholder = "Test",
        leadingIcon = @Composable {
            Icon(
                Icons.Filled.Search,
                contentDescription = null
            )
        },
        trailingIcon = @Composable {
            Icon(
                Icons.Filled.Cancel,
                contentDescription = null
            )
        },
    )
}

@Preview
@Composable
fun TopSearchBarWithBackgroundColor() {
    TopSearchBar(
        "test",
        {},
        backgroundColour = Color.Red
    )
}

@Preview
@Composable
fun TopSearchBarWithTextFieldColorAndModifier() {
    val colours = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DeepBlack,
        disabledTextColor = LightDarkGray,
        cursorColor = DeepBlack,
        placeholderColor = LightDarkGray,
        focusedBorderColor = Blue,
        unfocusedBorderColor = LightGray,
        disabledBorderColor = LightGray,
        errorBorderColor = DeepRed,
        focusedLabelColor = DeepBlack,
        unfocusedLabelColor = DeepBlack,
        disabledLabelColor = DeepBlack,
        errorLabelColor = DeepBlack,
        backgroundColor = Color.Green
    )

    TopSearchBar(
        "test",
        {},
        textFieldColours = colours,
        modifier = {
            this.border(
                BorderStroke(5.dp, Color.Black),
            )
        }
    )
}
