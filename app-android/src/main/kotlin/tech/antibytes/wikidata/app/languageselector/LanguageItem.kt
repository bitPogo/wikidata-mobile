/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.languageselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.antibytes.wikidata.app.ui.theme.BrightBlue
import java.util.Locale

@Composable
fun LanguageItem(
    value: Locale,
    selected: Locale,
    onClick: (Locale) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button) {
                onClick.invoke(value)
            }
            .padding(
                vertical = 10.dp,
                horizontal = 10.dp
            )
    ) {
        RadioButton(
            selected = value == selected,
            onClick = { /* Do nothing */ },
            colors = RadioButtonDefaults.colors(
                selectedColor = BrightBlue
            )
        )

        Text(
            value.displayName,
            fontWeight = FontWeight.W500,
            fontSize = 17.sp
        )
    }
}
