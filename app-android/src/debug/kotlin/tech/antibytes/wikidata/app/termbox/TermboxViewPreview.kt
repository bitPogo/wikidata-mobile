/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.termbox

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.MutableStateFlow
import tech.antibytes.wikidata.app.util.MwLocale
import java.util.Locale.ENGLISH

@Preview
@Composable
fun TermboxViewPreview() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(MwLocale(ENGLISH.toLanguageTag())),
            MutableStateFlow(QrCode.qrCode)
        ),
        navigator = TermboxNavigatorPreviewStub()
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyLabel() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow(""),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(MwLocale(ENGLISH.toLanguageTag())),
            MutableStateFlow(QrCode.qrCode)
        ),
        navigator = TermboxNavigatorPreviewStub()
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyDescription() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow(""),
            MutableStateFlow(listOf("dummy", "preview", "Lorem ipsum")),
            MutableStateFlow(MwLocale(ENGLISH.toLanguageTag())),
            MutableStateFlow(QrCode.qrCode)
        ),
        navigator = TermboxNavigatorPreviewStub()
    )
}

@Preview
@Composable
fun TermboxViewPreviewWithEmptyAliases() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(true),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(MwLocale(ENGLISH.toLanguageTag())),
            MutableStateFlow(QrCode.qrCode)
        ),
        navigator = TermboxNavigatorPreviewStub()
    )
}

@Preview
@Composable
fun TermboxViewPreviewProtected() {
    TermboxView(
        onEditMode = { },
        viewModel = TermboxViewModelPreviewStub(
            MutableStateFlow("QTest"),
            MutableStateFlow(false),
            MutableStateFlow("Test"),
            MutableStateFlow("A Simple Preview"),
            MutableStateFlow(emptyList()),
            MutableStateFlow(MwLocale(ENGLISH.toLanguageTag())),
            MutableStateFlow(QrCode.qrCode)
        ),
        navigator = TermboxNavigatorPreviewStub()
    )
}
