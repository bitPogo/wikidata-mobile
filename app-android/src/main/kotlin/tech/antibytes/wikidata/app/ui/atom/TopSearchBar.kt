/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.primarySurface
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
    actions: (@Composable () -> Unit)? = null,
    backgroundColour: Color? = null,
    textFieldColours: TextFieldColors? = null,
    textFieldModifier: Modifier.() -> Modifier = { this },
    textFieldShape: Shape? = null,
) {
    val searchWidth = if (actions == null) {
        1F
    } else {
        0.85F
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(1F)
            .fillMaxHeight(1F)
            .let { scopedModifier ->
                val colour = backgroundColour ?: MaterialTheme.colors.primarySurface
                scopedModifier.background(
                    color = colour,
                    shape = RoundedCornerShape(0.dp)
                )
            },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(searchWidth)
                .fillMaxHeight()
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

        if (actions != null) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxHeight()
            ) {
                actions.invoke()
            }
        }
    }
}
