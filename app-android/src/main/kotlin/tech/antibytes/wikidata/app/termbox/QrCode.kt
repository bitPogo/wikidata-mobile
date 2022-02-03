/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import tech.antibytes.wikidata.app.R

@Composable
fun QrCode(
    qrCode: Bitmap?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val logo = painterResource(id = R.drawable.wikidata_qr)

    if (qrCode == null) {
        Image(
            painter = logo,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            bitmap = qrCode.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
