/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun TopSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    backgroundColour: Color? = null,
    textFieldColours: TextFieldColors? = null,
    textFieldModifier: Modifier.() -> Modifier = { this },
    textFieldShape: Shape? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1F)
            .let { scopedModifier ->
                val colour = backgroundColour ?: Color.Transparent
                scopedModifier.background(
                    color = colour,
                    shape = RoundedCornerShape(0.dp)
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder != null) {
                @Composable { Text(placeholder) }
            } else {
                null
            },
            singleLine = true,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = textFieldModifier.invoke(
                Modifier.fillMaxWidth()
            ),
            colors = textFieldColours ?: TextFieldDefaults.textFieldColors(),
            shape = textFieldShape ?: MaterialTheme.shapes.small.copy(
                bottomEnd = ZeroCornerSize,
                bottomStart = ZeroCornerSize
            )
        )
    }
}
