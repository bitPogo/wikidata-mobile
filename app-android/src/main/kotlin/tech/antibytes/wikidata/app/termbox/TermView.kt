/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.antibytes.wikidata.app.R

@Composable
fun TermView(
    label: String,
    description: String,
    aliases: List<String>
) {
    val noEntityImage: Painter = painterResource(id = R.drawable.missing_entity_image)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp,
            )
            .fillMaxSize(1F)
    ) {
        items(1) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = noEntityImage,
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color.Transparent)
                        .width(250.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.FillWidth
                )
            }

            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Text(
                text = description,
                lineHeight = 24.sp
            )

            Text(
                text = stringResource(R.string.termbox_view_aka),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        items(aliases) { alias ->
            Text(
                alias,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}
