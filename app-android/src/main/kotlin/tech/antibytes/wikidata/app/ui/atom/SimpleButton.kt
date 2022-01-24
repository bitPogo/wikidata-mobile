/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import tech.antibytes.wikidata.app.ui.theme.DarkBlue
import tech.antibytes.wikidata.app.ui.theme.LightBrightWhite
import tech.antibytes.wikidata.app.ui.theme.LightGray
import tech.antibytes.wikidata.app.ui.theme.Shapes

@Composable
fun SimpleButton(
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = DarkBlue,
            contentColor = LightBrightWhite,
            disabledContentColor = LightGray
        ),
        shape = Shapes.medium
    ) { Text(label) }
}
