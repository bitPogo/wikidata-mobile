/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termsearch

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun TermSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
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
        placeholder = stringResource(R.string.termbox_search_placeholder),
        onValueChange = onValueChange,
        backgroundColour = Blue,
        leadingIcon = @Composable {
            IconButton(onClick = onSearch) {
                Icon(
                    Icons.Default.Search,
                    stringResource(R.string.termbox_search_search)
                )
            }
        },
        textFieldColours = colours,
        textFieldShape = RoundedCornerShape(10.dp),
        textFieldModifier = {
            this.padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        }
    )
}
